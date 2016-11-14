package data.platform.controller ;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractBaseController;
import data.platform.entity.EntityPlatformDataDictionary;
import data.platform.entity.EntityPlatformOrganization;
import data.platform.entity.EntityPlatformRole;
import data.platform.entity.EntityPlatformUser;
import data.platform.service.PlatformDataDictionaryService;
import data.platform.service.PlatformOrganizationService;
import data.platform.service.PlatformRoleService;
import data.platform.service.PlatformUserService;
/**
 * 平台－用户控制器类。
 * @author wanggq
 */
@Controller
@RequestMapping( "platform/user" )
public class PlatformUserController extends AbstractBaseController
{
    @Override
    protected void init( ModelMap model, HttpServletRequest request )
    {
    	 List<EntityPlatformDataDictionary>  positionList=userService.searchPositionNames();
    	 model.addAttribute("positionList", positionList);
    }
    
    /**
     * 根据组织机构名称查询组织机构信息的 Web 方法。<br /><br />
     * 命令: "searchTerm" ；<br/><br/>
     * @param term 输入参数(由 Browser 端 POST 回来的组织机构名称)
     * @param out 响应输出对象
     * @throws UnsupportedEncodingException 
     */
    @RequestMapping( params = "command=searchTerm" )
    public void searchTerm( @RequestParam( "term" ) String term, java.io.PrintWriter out ) throws UnsupportedEncodingException
    {
        term = new String( term.getBytes( "iso-8859-1" ),"UTF-8" ) ;
        PagingResult<Map<String,String>> pagingResult = organizationService.searchTermOrganization( term, null, null, null, 1, 10 ) ;
        List<Map<String,String>> resultList = pagingResult.getRows() ;
        out.print( getSerializer().formatList( resultList ) ) ;
    }
    
    /**
     * 分页查询用户信息的方法。
     * @param data 输入参数(由 Browser 端 POST 回来的JSON数据)
     * @param out 响应输出对象
     */
    @SuppressWarnings( "unchecked" )
    @RequestMapping( params = "command=search" )
    public void search( @RequestParam("data") String data, java.io.PrintWriter out )
    {
        Map<String,Object> paramMap = getSerializer().parseMap( data ) ;
        boolean isFast = parseBoolean( paramMap.get( "isFast" ) ) ;
        String chineseName = "" ;
        String englishName = "" ;
        String loginAccount = "" ;
        String organizationId = "" ;
        String officePhone = "" ;
        String mobile = "" ;
        String officeMail = "" ;
        Integer status = null ;
        if( isFast )
            chineseName = trimString( paramMap.get( "q" ) ) ;
        else
        {
            chineseName = trimString( paramMap.get( "chineseName" ) ) ;
            englishName = trimString( paramMap.get( "englishName" ) ) ;
            loginAccount = trimString( paramMap.get( "loginAccount" ) ) ;
            Map<String,Object> organizationData = (Map<String,Object>)paramMap.get( "parentOrganization" );
            organizationId = parseString( organizationData == null ? null : organizationData.get( "value" ) ) ;
            officePhone = trimString( paramMap.get( "officePhone" ) ) ;
            mobile = trimString( paramMap.get( "mobile" ) ) ;
            officeMail = trimString( paramMap.get( "officeMail" ) ) ;
            Map<String,Object> statusData = (Map<String,Object>)paramMap.get( "status" );
            status = parseInteger( statusData == null ? null : statusData.get( "value" ) ) ;
        }
        int currentPage = parseInteger( paramMap.get( "page" ) ) ;
        int pageSize = parseInteger( paramMap.get( "rows" ) ) ;
        String sortField = trimString( paramMap.get( "sidx" ) ) ;
        String sort = trimString( paramMap.get( "sord" ) ) ;
        PagingResult<EntityPlatformUser> pagingResult = userService.searchUsers( chineseName, englishName, loginAccount, organizationId, officePhone, mobile, officeMail, status, null,sortField, sort, currentPage, pageSize ) ;
        List<EntityPlatformUser> list = pagingResult.getRows() ;
        List<Map<String,String>> newList = new ArrayList<Map<String,String>>() ;
        for( EntityPlatformUser entity : list )
        {
            Map<String,String> map = new HashMap<String,String>() ;
            map.put( "id", formatString( entity.getId() ) ) ;
            map.put( "chineseName", formatString( entity.getChineseName() ) ) ;
            EntityPlatformDataDictionary dataDictEntity=dataDictionaryService.load(entity.getPosition());
            map.put( "position",dataDictEntity==null ? "" : dataDictEntity.getDictionaryName()) ;
            map.put( "loginAccount", formatString( entity.getLoginAccount() ) ) ;
            EntityPlatformOrganization orgEntity = entity.getOrganization() ;
            map.put( "organizationName", formatString( orgEntity == null ? "" : orgEntity.getOrganizationName() ) ) ;
            map.put( "adAccount", formatString( entity.getAdAccount() ) ) ;
            map.put( "officePhone", formatString( entity.getOfficePhone() ) ) ;
            map.put( "mobile", formatString( entity.getMobile() ) ) ;
            map.put( "officeMail", formatString( entity.getOfficeMail() ) ) ;
            map.put( "lastLoginTime", formatDate( entity.getLastLoginTime(), "yyyy-MM-dd HH:mm:ss" ) ) ;
            map.put( "status", formatBoolean( entity.getStatus() == 0 ? false : true, "启用|停用" ) ) ;
            
            List<String> roleIds = userService.selectUserRoles( entity.getId() ) ;
            StringBuilder roleNames = new StringBuilder() ;
            for( String roleId : roleIds )
            {
                EntityPlatformRole roleEntity = roleService.load( roleId ) ;
                if( roleEntity != null )
                {
                    roleNames.append( roleNames.length() == 0 ? "" : "," ).append( roleEntity.getRoleName() ) ;
                }
            }
            map.put( "roles", roleNames.toString() ) ;

            newList.add( map ) ;
        }
        PagingResult<Map<String,String>> newPagingResult = new PagingResult<Map<String,String>>( newList, pagingResult.getPage(), pagingResult.getPageSize(), pagingResult.getRecords() ) ;
        out.print( getSerializer().formatObject( newPagingResult ) ) ;
    }
    
    /**
     * 根据一个实体 ID 来加载用户详细信息的 Web 方法。<br /><br />
     * 命令: "load" ；<br/><br/>
     * @param id 输入参数(由 Browser 端 POST 回来的用户编号)
     * @param out 响应输出对象
     */
    @RequestMapping( params = "command=load" )
    public void load( @RequestParam( "id" ) String id, java.io.PrintWriter out )
    {
        EntityPlatformUser entity = userService.load( id ) ;
        Map<String,Object> map = getSerializer().parseMap( getSerializer().formatObject( entity ) ) ;
        List<Map<String,String>> parentOrgMap = new ArrayList<Map<String,String>>() ;
        EntityPlatformOrganization orgEntity = entity.getOrganization() ;
        if( orgEntity != null )
        {
            Map<String,String> orgMap = new HashMap<String,String>() ;
            orgMap.put( "text", parseString( orgEntity.getOrganizationName() ) ) ;
            orgMap.put( "value", parseString( orgEntity.getId() ) ) ;
            parentOrgMap.add( orgMap ) ;
        }
        map.put( "parentOrganization", parentOrgMap ) ;
        map.remove( "organization" ) ;
        out.print( getSerializer().formatMap( map ) ) ;
    }
    
    /**
     * 保存用户信息的Web方法。<br /><br />
     * 命令: "submit" ；<br/><br/>
     * @param data 输入参数(由 Browser 端 POST 回来的JSON数据)
     * @param out 响应输出对象
     */
    @SuppressWarnings( "unchecked" )
    @RequestMapping( params = "command=submit" )
    public void submit( @RequestParam( "data" ) String data, java.io.PrintWriter out )
    {
        Map<String,Object> paramMap = getSerializer().parseMap( data ) ;
        EntityPlatformUser entity = null ;
        String id = trimString( paramMap.get( "id" ) ) ;
        if( StringUtils.isNotBlank( id ) )
        {
            entity = userService.load( id ) ;
            entity.setUpdateTime( new Date() ) ;
        }
        else
        {
            entity = new EntityPlatformUser() ;
            entity.setCreateTime( new Date() ) ;
            entity.setLoginPassword( parseString( paramMap.get( "loginPassword" ) ) ) ;
        }
        entity.setChineseName( parseString( paramMap.get( "chineseName" ) ) ) ;
        Map<String,String> postionMap=(Map<String,String>)paramMap.get( "position" );
        if(postionMap!=null)
        {
           entity.setPosition(parseString( postionMap.get("value") ) );
        }
        entity.setAdAccount( parseString( paramMap.get( "adAccount" ) ) ) ;
        entity.setLoginAccount( parseString( paramMap.get( "loginAccount" ) ) ) ;
        List<Map<String,Object>> parentList = (List<Map<String,Object>>)paramMap.get( "parentOrganization" );
        String organizationId = (parentList == null || parentList.isEmpty()) ? null : parseString( parentList.get( 0 ).get( "value" ) ) ;
        entity.setOrganizationId( organizationId ) ;
        entity.setOfficePhone( parseString( paramMap.get( "officePhone" ) ) ) ;
        entity.setMobile( parseString( paramMap.get( "mobile" ) ) ) ;
        entity.setOfficeMail( parseString( paramMap.get( "officeMail" ) ) ) ;
        entity.setStatus( parseInteger( paramMap.get( "status" ) ) ) ;
        entity.setRemark( parseString( paramMap.get( "remark" ) ) ) ;
        String seqNum= parseString(paramMap.get( "seqNums" )) ;
        String sex=parseString(paramMap.get( "sex" ) );
        if(StringUtils.isNotBlank(sex))
        {
            entity.setSex(parseInteger(sex));
        }
      
        if(StringUtils.isNotBlank(seqNum))
        {
            entity.setSeqNums( parseInteger(seqNum) ) ;
        }
        userService.saveOrUpdate( entity ) ;
        
        if(!("1".equals(parseString(paramMap.get("status")))))//如果账户停用，删除用户对应的所有角色
        {
        	roleService.deleteUserRole(entity.getId());
        }
        out.print( getSerializer().message( "" ) ) ;
    }
    
    /**
     * 根据一个实体 ID 来删除用户实体信息的 Web 方法。<br /><br />
     * 命令: "delete" ；<br/><br/>
     * @param data 输入参数(由 Browser 端 POST 回来的JSON数据)
     * @param out 响应输出对象
     */
    @SuppressWarnings( "unchecked" )
    @RequestMapping( params = "command=delete" )
    public void delete( @RequestParam( "data" ) String data, java.io.PrintWriter out )
    {
        Map<String,Object> paramMap = getSerializer().parseMap( data ) ;
        List<String> list = (List<String>)paramMap.get( "id" ) ;
        
        userService.remove( list ) ;
        out.print( getSerializer().message( "" ) ) ;
    }
    
    /**
     * 获取所有角色的 Web 方法。<br /><br />
     * 命令: "initRoles" ；<br/><br/>
     * @param out 响应输出对象
     */
    @RequestMapping( params = "command=initRoles" )
    public void initRoles( java.io.PrintWriter out )
    {
        List<Map<String,String>> list = roleService.searchRoles( null, 1 ) ;
        out.print( getSerializer().formatList( list ) ) ;
    }
    
    /**
     * 获取用户包含角色信息的 Web 方法。<br /><br />
     * 命令: "getRole" ；<br/><br/>
     * @param out 响应输出对象
     */
    @RequestMapping( params = "command=getRole" )
    public void getRole( @RequestParam( "id" ) String id, java.io.PrintWriter out )
    {
        List<Map<String,Object>> list = userService.selectUserRolesAndDefault( id ) ;
        Map<String,Object> map = new HashMap<String,Object>() ;
        map.put( "roles", list ) ;
        out.print( getSerializer().formatMap( map ) ) ;
    }
    
    /**
     * 保存用户包含角色信息的 Web 方法。<br /><br />
     * 命令: "saveRoles" ；<br/><br/>
     * @param out 响应输出对象
     */
    @SuppressWarnings( "unchecked" )
    @RequestMapping( params = "command=saveRoles" )
    public void saveRoles( @RequestParam( "data" ) String data, java.io.PrintWriter out )
    {
        Map<String,Object> paramMap = getSerializer().parseMap( data ) ;
        List<String> userIds = (List<String>)paramMap.get( "users" ) ;
        List<String> roleIds = (List<String>)paramMap.get( "roles" ) ;
        userService.addUserRoles( userIds, roleIds ) ;
        out.print( getSerializer().message( "" ) ) ;
    }
    
    /**
     * 批量更改用户密码信息的 Web 方法。<br /><br />
     * 命令: "changePwd" ；<br/><br/>
     * @param data 输入参数(由 Browser 端 POST 回来的JSON数据)
     * @param out 响应输出对象
     */
    @SuppressWarnings( "unchecked" )
    @RequestMapping( params = "command=changePwd" )
    public void changePassword( @RequestParam( "data" ) String data, java.io.PrintWriter out )
    {
        Map<String,Object> paramMap = getSerializer().parseMap( data ) ;
        List<String> userIds = (List<String>)paramMap.get( "users" ) ;
        String newPassword = parseString( paramMap.get( "newPwd" ) ) ;
        userService.changePassword( userIds, newPassword ) ;
        out.print( getSerializer().message( "" ) ) ;
    }
    
    /**
     * 批量重置用户密码信息的 Web 方法。<br /><br />
     * 命令: "changePwd" ；<br/><br/>
     * @param data 输入参数(由 Browser 端 POST 回来的JSON数据)
     * @param out 响应输出对象
     */
    @RequestMapping( params = "command=resetPwd" )
    public void resetPassword( @RequestParam( "data" ) String data, java.io.PrintWriter out )
    {
        Map<String,Object> paramMap = getSerializer().parseMap( data ) ;
        String[] userIds =parseString(paramMap.get( "id" )).split(",") ;
        List<String> userIdList=new ArrayList<String>();
        for(String id : userIds)
        {
        	userIdList.add(id);
        }
        String newPassword = parseString( paramMap.get( "newPwd" ) ) ;
        userService.changePassword( userIdList, newPassword ) ;
        HashMap<String,Object> resultMap=new HashMap<String,Object>();
        resultMap.put("message", "密码重置成功");
        out.print( getSerializer().formatMap(resultMap)) ;
    }
    
    @Autowired
    private PlatformUserService userService ;
    @Autowired
    private PlatformOrganizationService organizationService ;
    @Autowired
    private PlatformRoleService roleService ;
    @Autowired
    private PlatformDataDictionaryService dataDictionaryService ;
}