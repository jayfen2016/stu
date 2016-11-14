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

import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractBaseController;
import data.platform.entity.EntityPlatformMenu;
import data.platform.entity.EntityPlatformRole;
import data.platform.entity.EntityPlatformUser;
import data.platform.service.PlatformMenuService;
import data.platform.service.PlatformRoleService;
/**
 * 平台-角色控制器类。
 * @author wanggq
 */
@Controller
@RequestMapping( "platform/role" )
public class PlatformRoleController extends AbstractBaseController
{
    private final static int MAX_INSERT = 500 ;
    @Override
    protected void init( ModelMap model, HttpServletRequest request )
    {
    }
    
    /**
     * 分页查询角色信息的方法。
     * @param data 输入参数(由 Browser 端 POST 回来的JSON数据)
     * @param out 响应输出对象
     */
    @SuppressWarnings( "unchecked" )
    @RequestMapping( params = "command=search" )
    public void search( @RequestParam("data") String data, java.io.PrintWriter out )
    {
        Map<String,Object> paramMap = getSerializer().parseMap( data ) ;
        boolean isFast = parseBoolean( paramMap.get( "isFast" ) ) ;
        String roleName = "" ;
        Integer status = null ;
        if( isFast )
            roleName = trimString( paramMap.get( "q" ) ) ;
        else
        {
            roleName = trimString( paramMap.get( "roleName" ) ) ;
            Map<String,Object> statusData = (Map<String,Object>)paramMap.get( "status" );
            status = parseInteger( statusData == null ? null : statusData.get( "value" ) ) ;
        }
        int currentPage = parseInteger( paramMap.get( "page" ) ) ;
        int pageSize = parseInteger( paramMap.get( "rows" ) ) ;
        String sortField = trimString( paramMap.get( "sidx" ) ) ;
        String sort = trimString( paramMap.get( "sord" ) ) ;
        PagingResult<EntityPlatformRole> pagingResult = roleService.searchRoles( roleName, status, sortField, sort, currentPage, pageSize ) ;
        List<EntityPlatformRole> list = pagingResult.getRows() ;
        List<Map<String,String>> newList = new ArrayList<Map<String,String>>() ;
        for( EntityPlatformRole entity : list )
        {
            Map<String,String> map = new HashMap<String,String>() ;
            map.put( "id", formatString( entity.getId() ) ) ;
            map.put( "roleName", formatString( entity.getRoleName() ) ) ;
            map.put( "remark", formatString( entity.getRemark() ) ) ;
            map.put( "status", formatBoolean( entity.getStatus() == 0 ? false : true, "启用|停用" ) ) ;
            map.put( "createTime", formatDate( entity.getCreateTime(), "yyyy-MM-dd HH:mm:ss" ) ) ;
            newList.add( map ) ;
        }
        PagingResult<Map<String,String>> newPagingResult = new PagingResult<Map<String,String>>( newList, pagingResult.getPage(), pagingResult.getPageSize(), pagingResult.getRecords() ) ;
        out.print( getSerializer().formatObject( newPagingResult ) ) ;
    }
    
    /**
     * 根据一个实体 ID 来加载角色详细信息的 Web 方法。<br /><br />
     * 命令: "load" ；<br/><br/>
     * @param id 输入参数(由 Browser 端 POST 回来的角色编号)
     * @param out 响应输出对象
     */
    @RequestMapping( params = "command=load" )
    public void load( @RequestParam( "id" ) String id, java.io.PrintWriter out )
    {
        EntityPlatformRole entity = roleService.load( id ) ;
        out.print( getSerializer().formatObject( entity ) ) ;
    }
    
    /**
     * 保存角色信息的Web方法。<br /><br />
     * 命令: "submit" ；<br/><br/>
     * @param data 输入参数(由 Browser 端 POST 回来的JSON数据)
     * @param out 响应输出对象
     */
    @RequestMapping( params = "command=submit" )
    public void submit( @RequestParam( "data" ) String data, java.io.PrintWriter out )
    {
        Map<String,Object> paramMap = getSerializer().parseMap( data ) ;
        EntityPlatformRole entity = null ;
        String id = trimString( paramMap.get( "id" ) ) ;
        if( StringUtils.isNotBlank( id ) )
        {
            entity = roleService.load( id ) ;
            entity.setUpdateTime( new Date() );
        }
        else
        {
            entity = new EntityPlatformRole() ;
            entity.setCreateTime( new Date() );
        }
        entity.setRoleName( parseString( paramMap.get( "roleName" ) ) ) ;
        entity.setApprovalRole( parseString( paramMap.get( "approvalRole" ) ) ) ;
        entity.setStatus( parseInteger( paramMap.get( "status" ) ) ) ;
        entity.setRemark( parseString( paramMap.get( "remark" ) ) ) ;
        roleService.saveOrUpdate( entity ) ;
        out.print( getSerializer().message( "" ) ) ;
    }
    
    /**
     * 根据一个实体 ID 来删除实体信息的 Web 方法。<br /><br />
     * 命令: "delete" ；<br/><br/>
     * @param date 输入参数(由 Browser 端 POST 回来的JSON数据)
     * @param out 响应输出对象
     */
    @SuppressWarnings( "unchecked" )
    @RequestMapping( params = "command=delete" )
    public void delete( @RequestParam( "data" ) String data, java.io.PrintWriter out )
    {
        Map<String,Object> paramMap = getSerializer().parseMap( data ) ;
        roleService.remove( (List<String>)paramMap.get( "id" ) ) ;
        out.print( getSerializer().message( "" ) ) ;
    }
    
    /**
     * 加载系统所有功能权限信息的 Web 方法。<br /><br />
     * 命令: "initFunctions" ；<br/><br/>
     * @param out 响应输出对象
     */
    @RequestMapping( params = "command=initFunctions" )
    public void initFunctions( java.io.PrintWriter out )
    {
        PagingResult<EntityPlatformMenu> menuResult = menuService.searchMenus( null, null, 1, null, null, 1, Integer.MAX_VALUE ) ;
        out.print( getSerializer().formatList( menuResult.getRows() ) ) ;
    }
    
    /**
     * 加载角色对应的功能权限信息的 Web 方法。<br /><br />
     * 命令: "getFunctions" ；<br/><br/>
     * @param roleId 输入参数(角色id)
     * @param out 响应输出对象
     */
    @RequestMapping( params = "command=getFunctions" )
    public void getFunctions( @RequestParam( "roleId" ) String roleId, java.io.PrintWriter out )
    {
        List<Map<String,String>> list = roleService.selectRoleFunctions( roleId ) ;
        out.print( getSerializer().formatList( list ) ) ;
    }
    
    /**
     * 保存角色对应的功能权限信息的 Web 方法。<br /><br />
     * 命令: "saveFunctions" ；<br/><br/>
     * @param date 输入参数(由 Browser 端 POST 回来的JSON数据)
     * @param out 响应输出对象
     */
    @SuppressWarnings( "unchecked" )
    @RequestMapping( params = "command=saveFunctions" )
    public void saveFunctions( @RequestParam( "data" ) String data, java.io.PrintWriter out )
    {
        Map<String,Object> paramMap = getSerializer().parseMap( data ) ;
        String roleId = parseString( paramMap.get( "roleId" ) ) ;
        List<Map<String,String>> functionList = (List<Map<String,String>>)paramMap.get( "functions" ) ;
        roleService.saveRoleFunctions( roleId, functionList, "" ) ;
        out.print( getSerializer().message( "" ) ) ;
    }
    
    
    /**
     * 保存角色对应的数据权限信息的 Web 方法。<br /><br />
     * 命令: "saveDatas" ；<br/><br/>
     * @param date 输入参数(由 Browser 端 POST 回来的JSON数据)
     * @param out 响应输出对象
     */
    @SuppressWarnings( "unchecked" )
    @RequestMapping( params = "command=saveDatas" )
    public void saveDatas( @RequestParam( "data" ) String data, java.io.PrintWriter out )
    {
        Map<String,Object> paramMap = getSerializer().parseMap( data ) ;
        String roleId = parseString( paramMap.get( "roleId" ) ) ;
        List<String> dataAuthorityList = (List<String>)paramMap.get( "datas" ) ;

        int totalCount = dataAuthorityList.size() ;
        int totalPackage = ( totalCount / MAX_INSERT ) + ( ( totalCount % MAX_INSERT == 0 ) ? 0 : 1 ) ;
        List<List<String>> packageList = new ArrayList<List<String>>( totalPackage ) ;
        
        int rowIndex = 0 ;
        int packageIndex = 0 ;
        for( int i = 0; i < dataAuthorityList.size(); ++i )
        {
            if( rowIndex == 0 )
            {
                packageList.add( new ArrayList<String>() ) ;
            }
            packageList.get( packageIndex ).add( dataAuthorityList.get( i ) ) ;
            rowIndex++ ;

            if( rowIndex > MAX_INSERT - 1 )
            {
                rowIndex = 0 ;
                packageIndex++ ;
            }
        }

        roleService.deleteRoleDataAuthoritys( roleId ) ;
        for( List<String> packageItem : packageList )
        {
            roleService.saveRoleDataAuthoritys( roleId, packageItem, "" ) ;
        }

        Map<String,Object> resultMap = new HashMap<String,Object>() ;
        resultMap.put( "message", "保存成功"  ) ;
        out.print( getSerializer().formatMap( resultMap ) ) ;
    }
    
    
    /**
     * 根据角色ID查找用户List
     */
    @RequestMapping( params = "command=selectUsersByRoleId" )
    public void selectUsersByRoleId( @RequestParam( "roleId" ) String roleId, java.io.PrintWriter out )
    {
    	List<EntityPlatformUser> users = roleService.selectUsersByRoleId(roleId);
    	out.print( getSerializer().formatObject(users) ) ;
    }
    
    /**
     * 根据角色ID删除关联的用户映射
     * 添加新的映射
     */
    @RequestMapping( params = "command=makeRoleUserMapping" )
    public void makeRoleUserMapping( @RequestParam( "data" ) String data, java.io.PrintWriter out )
    {
    	Map<String,Object> paramMap = getSerializer().parseMap( data ) ;
        String roleId = parseString( paramMap.get( "roleId" ) ) ;
        String userIds = parseString( paramMap.get( "userIds" ) ) ;
    	String[] s = userIds.split(",");
    	this.roleService.roleUserMapping(roleId,s);
    }
    
    @Autowired
    private PlatformRoleService roleService ;
    @Autowired
    private PlatformMenuService menuService ;
}