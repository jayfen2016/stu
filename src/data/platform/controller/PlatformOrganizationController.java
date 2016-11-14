package data.platform.controller;

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

import data.framework.data.DataTree;
import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractBaseController;
import data.platform.authority.security.SecurityContext;
import data.platform.entity.EntityPlatformDataDictionary;
import data.platform.entity.EntityPlatformOrgSignPerson;
import data.platform.entity.EntityPlatformOrganization;
import data.platform.entity.EntityPlatformUser;
import data.platform.service.PlatformOrgSignPersonService;
import data.platform.service.PlatformOrganizationService;
import data.platform.service.PlatformUserService;

/**
 * 平台-组织机构控制器类。
 * @author wanggq
 *
 */
@Controller
@RequestMapping( "platform/organization" )
public class PlatformOrganizationController extends AbstractBaseController
{

    @Override
    protected void init( ModelMap model, HttpServletRequest request )
    {
    	/*List<EntityPlatformDataDictionary> modelTypeList=organizationService.loadModelTypes();
    	model.addAttribute("modelTypeList", modelTypeList); */
    }

    /**
     * 分页查询组织机构信息的方法。
     * @param data 输入参数(由 Browser 端 POST 回来的JSON数据)
     * @param out 响应输出对象
     */
    @RequestMapping( params = "command=search" )
    public void search( @RequestParam("data") String data, java.io.PrintWriter out )
    {
        Map<String,Object> paramMap = getSerializer().parseMap( data ) ;
        boolean isFast = parseBoolean( paramMap.get( "isFast" ) ) ;
        String organizationName = "" ;
        String organizationCode = "" ;
        if( isFast )
            organizationName = trimString( paramMap.get( "q" ) ) ;
        else
        {
            organizationName = trimString( paramMap.get( "organizationName" ) ) ;
            organizationCode = trimString( paramMap.get( "organizationCode" ) ) ;
        }
        int currentPage = parseInteger( paramMap.get( "page" ) ) ;
        int pageSize = parseInteger( paramMap.get( "rows" ) ) ;
        String sortField = trimString( paramMap.get( "sidx" ) ) ;
        String sort = trimString( paramMap.get( "sord" ) ) ;
        PagingResult<EntityPlatformOrganization> pagingResult = organizationService.searchOrganization( organizationName, organizationCode, sortField, sort, currentPage, pageSize ) ;
        List<EntityPlatformOrganization> list = pagingResult.getRows() ;
        List<Map<String,String>> newList = new ArrayList<Map<String,String>>() ;
        for( EntityPlatformOrganization entity : list )
        {
            Map<String,String> map = new HashMap<String,String>() ;
            map.put( "id", formatString( entity.getId() ) ) ;
            map.put( "organizationName", formatString( entity.getOrganizationName() ) ) ;
            map.put( "organizationCode", formatString( entity.getOrganizationCode() ) ) ;
            map.put( "remark", formatString( entity.getRemark() ) ) ;
            map.put( "orgLevel", formatString( entity.getOrgLevel() ) ) ;
            newList.add( map ) ;
        }
        PagingResult<Map<String,String>> newPagingResult = new PagingResult<Map<String,String>>( newList, pagingResult.getPage(), pagingResult.getPageSize(), pagingResult.getRecords() ) ;
        out.print( getSerializer().formatObject( newPagingResult ) ) ;
    }
    
    /**
     * 根据一个实体 ID 来加载组织机构详细信息的 Web 方法。<br /><br />
     * 命令: "load" ；<br/><br/>
     * @param id 输入参数(由 Browser 端 POST 回来的组织机构编号)
     * @param out 响应输出对象
     */
    @RequestMapping( params = "command=load" )
    public void load( @RequestParam( "id" ) String id, java.io.PrintWriter out )
    {
        EntityPlatformOrganization entity = organizationService.load( id ) ;
        Map<String,Object> map = getSerializer().parseMap( getSerializer().formatObject( entity ) ) ;
        String parentId = parseString( map.get( "parentId" ) ) ;
        List<Map<String,String>> parentOrganization = new ArrayList<Map<String,String>>() ;
        if( StringUtils.isNotBlank( parentId ) )
        {
            EntityPlatformOrganization parentEntity = organizationService.load( parentId ) ;
            Map<String,String> parentMap = new HashMap<String,String>() ;
            parentMap.put( "text", parseString( parentEntity.getOrganizationName() ) ) ;
            parentMap.put( "value", parseString( parentEntity.getId() ) ) ;
            parentOrganization.add( parentMap ) ;
        }
        map.put( "parentOrganization", parentOrganization ) ;
        out.print( getSerializer().formatMap( map ) ) ;
    }
    
    /**
     * 保存组织机构信息的Web方法。<br /><br />
     * 命令: "submit" ；<br/><br/>
     * @param data 输入参数(由 Browser 端 POST 回来的JSON数据)
     * @param out 响应输出对象
     */
    @SuppressWarnings( "unchecked" )
    @RequestMapping( params = "command=submit" )
    public void submit( @RequestParam( "data" ) String data, java.io.PrintWriter out )
    {
        Map<String,Object> paramMap = getSerializer().parseMap( data ) ;
        EntityPlatformOrganization entity = null ;
        String id = trimString( paramMap.get( "id" ) ) ;
        if( StringUtils.isNotBlank( id ) )
        {
            entity = organizationService.load( id ) ;
            entity.setUpdateTime( new Date() );
        }
        else
        {
            entity = new EntityPlatformOrganization() ;
            entity.setCreateTime( new Date() );
        }
        entity.setOrganizationName( parseString( paramMap.get( "organizationName" ) ) ) ;
        entity.setOrganizationCode( parseString( paramMap.get( "organizationCode" ) ) ) ;
        List<Map<String,Object>> parentList = (List<Map<String,Object>>)paramMap.get( "parentOrganization" );
        String parentId = (parentList == null || parentList.isEmpty()) ? null : parseString( parentList.get( 0 ).get( "value" ) ) ;
        entity.setParentId( parentId ) ;
        entity.setRemark( parseString( paramMap.get( "remark" ) ) ) ;
        organizationService.saveOrUpdate( entity ) ;
        out.print( getSerializer().message( "" ) );
    }
    
    /**
     * 根据一个实体 ID 来删除组织机构实体信息的 Web 方法。<br /><br />
     * 命令: "delete" ；<br/><br/>
     * @param data 输入参数(由 Browser 端 POST 回来的JSON数据)
     * @param out 响应输出对象
     */
    @SuppressWarnings( "unchecked" )
    @RequestMapping( params = "command=delete" )
    public void delete( @RequestParam( "data" ) String data, java.io.PrintWriter out )
    {
        Map<String,Object> paramMap = getSerializer().parseMap( data ) ;
        organizationService.remove( (List<String>)paramMap.get( "id" ) ) ;
        out.print( getSerializer().message( "" ) ) ;
    }
    
    /**
     * 获取树形组织机构数据的 Web 方法。<br /><br />
     * 命令: "getTree" ；<br/><br/>
     * @param out 响应输出对象
     */
    @RequestMapping( params = "command=getTree" )
    public void getTree( java.io.PrintWriter out )
    {
        //获取启用状态的组织机构
        List<DataTree> orgTree = organizationService.getOrganizationTree() ;
        out.print( getSerializer().formatList( orgTree ) ) ;
    }
    
    /**
     * 保存 关联组织机构的领导人信息的Web方法。<br /><br />
     * 命令: "saveRelatePerson" ；<br/><br/>
     * @param data 输入参数(由 Browser 端 POST 回来的JSON数据)
     * @param out 响应输出对象
     */
    @SuppressWarnings( "unchecked" )
    @RequestMapping( params = "command=saveRelatePerson" )
    public void saveRelatePerson( @RequestParam( "data" ) String data, java.io.PrintWriter out )
    {
        Map<String,Object> paramMap = getSerializer().parseMap( data ) ;
        EntityPlatformOrgSignPerson entity = null ;
        List<Map<String,Object>> paramList=( List<Map<String,Object>>)paramMap.get("parameter");
        String orgId=parseString(paramList.get(0));
        String orgName=parseString(paramList.get(1));
        if(paramList.size()-2>0)
        {
        	for(int i=2;i<paramList.size();i++)
        	{
        		Map<String,Object> map=paramList.get(i);
        		String signId = trimString( map.get( "signId" ) ) ;
        		if( StringUtils.isNotBlank( signId ) )
                {
                    entity = orgSignPersonService.load( signId ) ;
                    entity.setUpdateTime( new Date() );
                    entity.setModifierId(SecurityContext.getPrincipal().getId());
                }
                else
                {
                    entity = new EntityPlatformOrgSignPerson() ;
                    entity.setCreateTime( new Date() );
                    entity.setCreatorId(SecurityContext.getPrincipal().getId());
                }
        		entity.setOrgId(orgId);
    	        entity.setOrgName(orgName) ;
    	        String signPersonId=parseString(map.get("signPersonId"));
    	        entity.setSignPersonId(signPersonId);
    	        entity.setSignPersonName(parseString(map.get("signPersonName")));
	    	    entity.setModuleTypeId(parseString(map.get("moduleId")));
	            entity.setModuleTypeName(parseString(map.get("moduleName")));
    	        if(StringUtils.isNotBlank(signPersonId))
    	        {
    	        	orgSignPersonService.saveOrUpdate( entity ) ;
    	        }
	            
        	}
        }
        	out.print( getSerializer().message( "" ) );
    }
    
    /**
     * 根据一个实体 ID 来加载部门转发人员信息的 Web 方法。<br /><br />
     * 命令: "loadRelatePersonUrl" ；<br/><br/>
     * @param id 输入参数(由 Browser 端 POST 回来的组织机构编号)
     * @param out 响应输出对象
     */
    @RequestMapping( params = "command=loadRelatePersonUrl" )
    public void loadRelatePersonUrl( @RequestParam( "id" ) String id, java.io.PrintWriter out )
    {
    	List<Map<String,Object>> resultList= orgSignPersonService.loadOrgSendPersonByOrgId(id);
    	List<EntityPlatformDataDictionary> modelTypeList=organizationService.loadModelTypes(id);
    	HashMap<String,Object> map=new HashMap<String,Object>();
    	map.put("resultList", resultList);
    	map.put("modelTypeList", modelTypeList);
        out.print( getSerializer().formatMap( map ) ) ;
    }
    
    @Autowired
    private PlatformOrganizationService organizationService ;
    @Autowired
    private PlatformOrgSignPersonService orgSignPersonService ;
    @Autowired
    private PlatformUserService platformUserService ;
}