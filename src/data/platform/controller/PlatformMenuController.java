package data.platform.controller;

import java.util.ArrayList ;
import java.util.Date ;
import java.util.HashMap ;
import java.util.List ;
import java.util.Map ;

import javax.servlet.http.HttpServletRequest ;

import org.apache.commons.lang.StringUtils ;
import org.springframework.beans.factory.annotation.Autowired ;
import org.springframework.stereotype.Controller ;
import org.springframework.ui.ModelMap ;
import org.springframework.web.bind.annotation.RequestMapping ;
import org.springframework.web.bind.annotation.RequestParam ;

import data.framework.data.DataTree ;
import data.framework.pagination.model.PagingResult ;
import data.framework.support.AbstractBaseController ;
import data.platform.entity.EntityPlatformMenu ;
import data.platform.service.PlatformMenuService ;

/**
 * 平台-菜单控制器类。
 * @author wanggq
 */
@Controller
@RequestMapping( "platform/menu" )
public class PlatformMenuController extends AbstractBaseController
{

    @Override
    protected void init( ModelMap model, HttpServletRequest request )
    {
    }

    /**
     * 分页查询菜单信息的方法。
     * @param data 输入参数(由 Browser 端 POST 回来的JSON数据)
     * @param out 响应输出对象
     */
    @SuppressWarnings( "unchecked" )
    @RequestMapping( params = "command=search" )
    public void search( @RequestParam("data") String data, java.io.PrintWriter out )
    {
        Map<String,Object> paramMap = getSerializer().parseMap( data ) ;
        boolean isFast = parseBoolean( paramMap.get( "isFast" ) ) ;
        String menuName = "" ;
        String menuCode = "" ;
        Integer status = null ;
        if( isFast )
            menuName = trimString( paramMap.get( "q" ) ) ;
        else
        {
            menuName = trimString( paramMap.get( "menuName" ) ) ;
            menuCode = trimString( paramMap.get( "menuCode" ) ) ;
            Map<String,Object> statusData = (Map<String,Object>)paramMap.get( "status" );
            status = parseInteger( statusData == null ? null : statusData.get( "value" ) ) ;
        }
        int currentPage = parseInteger( paramMap.get( "page" ) ) ;
        int pageSize = parseInteger( paramMap.get( "rows" ) ) ;
        String sortField = parseString( paramMap.get( "sidx" ) ) ;
        String sort = parseString( paramMap.get( "sord" ) ) ;
        PagingResult<EntityPlatformMenu> pagingResult = menuService.searchMenus( menuName, menuCode, status, sortField, sort, currentPage, pageSize ) ;
        List<EntityPlatformMenu> list = pagingResult.getRows() ;
        List<Map<String,String>> newList = new ArrayList<Map<String,String>>() ;
        for( EntityPlatformMenu entity : list )
        {
            Map<String,String> map = new HashMap<String,String>() ;
            map.put( "id", formatString( entity.getId() ) ) ;
            map.put( "menuName", formatString( entity.getMenuName() ) ) ;
            map.put( "menuCode", formatString( entity.getMenuCode() ) ) ;
            map.put( "menuAddress", formatString( entity.getMenuAddress() ) ) ;
            map.put( "menuLevel", formatString( entity.getMenuLevel() ) ) ;
            map.put( "status", formatBoolean( entity.getStatus() == 0 ? false : true, "启用|停用" ) ) ;
            newList.add( map ) ;
        }
        PagingResult<Map<String,String>> newPagingResult = new PagingResult<Map<String,String>>( newList, pagingResult.getPage(), pagingResult.getPageSize(), pagingResult.getRecords() ) ;
        out.print( getSerializer().formatObject( newPagingResult ) ) ;
    }
    
    /**
     * 根据一个实体 ID 来加载菜单详细信息的 Web 方法。<br /><br />
     * 命令: "load" ；<br/><br/>
     * @param id 输入参数(由 Browser 端 POST 回来的菜单编号)
     * @param out 响应输出对象
     */
    @RequestMapping( params = "command=load" )
    public void load( @RequestParam( "id" ) String id, java.io.PrintWriter out )
    {
        EntityPlatformMenu entity = menuService.load( id ) ;
        Map<String,Object> map = getSerializer().parseMap( getSerializer().formatObject( entity ) ) ;
        String parentId = parseString( map.get( "parentId" ) ) ;
        List<Map<String,String>> parentMenu = new ArrayList<Map<String,String>>() ;
        if( StringUtils.isNotBlank( parentId ) )
        {
            EntityPlatformMenu parentEntity = menuService.load( parentId ) ;
            Map<String,String> parentMap = new HashMap<String,String>() ;
            parentMap.put( "text", parseString( parentEntity.getMenuName() ) ) ;
            parentMap.put( "value", parseString( parentEntity.getId() ) ) ;
            parentMenu.add( parentMap ) ;
        }
        map.put( "parentMenu", parentMenu ) ;
        out.print( getSerializer().formatMap( map ) ) ;
    }
    
    /**
     * 保存菜单信息的Web方法。<br /><br />
     * 命令: "submit" ；<br/><br/>
     * @param data 输入参数(由 Browser 端 POST 回来的JSON数据)
     * @param out 响应输出对象
     */
    @SuppressWarnings( "unchecked" )
    @RequestMapping( params = "command=submit" )
    public void submit( @RequestParam( "data" ) String data, java.io.PrintWriter out )
    {
        Map<String,Object> paramMap = getSerializer().parseMap( data ) ;
        EntityPlatformMenu entity = null ;
        String id = trimString( paramMap.get( "id" ) ) ;
        if( StringUtils.isNotBlank( id ) )
        {
            entity = menuService.load( id ) ;
            entity.setUpdateTime( new Date() );
        }
        else
        {
            entity = new EntityPlatformMenu() ;
            entity.setCreateTime( new Date() );
        }
        entity.setMenuName( parseString( paramMap.get( "menuName" ) ) ) ;
        entity.setMenuCode( parseString( paramMap.get( "menuCode" ) ) ) ;
        entity.setMenuAddress( parseString( paramMap.get( "menuAddress" ) ) ) ;
        entity.setStatus( parseInteger( paramMap.get( "status" ) ) ) ;
        entity.setRemark( parseString( paramMap.get( "remark" ) ) ) ;
        entity.setIconClass( parseString( paramMap.get( "iconClass" ) ) );
        List<Map<String,Object>> parentList = (List<Map<String,Object>>)paramMap.get( "parentMenu" );
        String parentId = (parentList == null || parentList.isEmpty()) ? null : parseString( parentList.get( 0 ).get( "value" ) ) ;
        entity.setParentId( parentId ) ;
        menuService.saveOrUpdate( entity ) ;
        out.print( getSerializer().message( "" ) );
    }
    
    /**
     * 根据一个实体 ID 来删除菜单实体信息的 Web 方法。<br /><br />
     * 命令: "delete" ；<br/><br/>
     * @param data 输入参数(由 Browser 端 POST 回来的JSON数据)
     * @param out 响应输出对象
     */
    @SuppressWarnings( "unchecked" )
    @RequestMapping( params = "command=delete" )
    public void delete( @RequestParam( "data" ) String data, java.io.PrintWriter out )
    {
        Map<String,Object> paramMap = getSerializer().parseMap( data ) ;
        menuService.remove( (List<String>)paramMap.get( "id" ) ) ;
        out.print( getSerializer().message( "" ) ) ;
    }
    
    /**
     * 获取树形菜单数据的 Web 方法。<br /><br />
     * 命令: "getTree" ；<br/><br/>
     * @param out 响应输出对象
     */
    @RequestMapping( params = "command=getTree" )
    public void getTree( java.io.PrintWriter out )
    {
        //获取启用状态的菜单
        List<DataTree> menuTree = menuService.getMenuTree( 1 ) ;
        out.print( getSerializer().formatList( menuTree ) ) ;
    }

    @Autowired
    private PlatformMenuService menuService ;
}