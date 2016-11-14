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

import data.framework.pagination.model.PagingResult ;
import data.framework.support.AbstractBaseController ;
import data.platform.entity.EntityPlatformUserGroup ;
import data.platform.service.PlatformUserGroupService ;

/**
 * 平台-用户组控制器类。
 * @author wanggq
 */
@Controller
@RequestMapping( "platform/userGroup" )
public class PlatformUserGroupController extends AbstractBaseController
{

    @Override
    protected void init( ModelMap model, HttpServletRequest request )
    {
    }
    
    /**
     * 分页查询用户组信息的方法。
     * @param data 输入参数(由 Browser 端 POST 回来的JSON数据)
     * @param out 响应输出对象
     */
    @SuppressWarnings( "unchecked" )
    @RequestMapping( params = "command=search" )
    public void search( @RequestParam("data") String data, java.io.PrintWriter out )
    {
        Map<String,Object> paramMap = getSerializer().parseMap( data ) ;
        boolean isFast = parseBoolean( paramMap.get( "isFast" ) ) ;
        String groupName = "" ;
        Integer status = null ;
        if( isFast )
            groupName = trimString( paramMap.get( "q" ) ) ;
        else
        {
            groupName = trimString( paramMap.get( "groupName" ) ) ;
            Map<String,Object> statusData = (Map<String,Object>)paramMap.get( "status" );
            status = parseInteger( statusData == null ? null : statusData.get( "value" ) ) ;
        }
        int currentPage = parseInteger( paramMap.get( "page" ) ) ;
        int pageSize = parseInteger( paramMap.get( "rows" ) ) ;
        String sortField = trimString( paramMap.get( "sidx" ) ) ;
        String sort = trimString( paramMap.get( "sord" ) ) ;
        PagingResult<EntityPlatformUserGroup> pagingResult = userGroupService.searchUserGroups( groupName, status, sortField, sort, currentPage, pageSize ) ;
        List<EntityPlatformUserGroup> list = pagingResult.getRows() ;
        List<Map<String,String>> newList = new ArrayList<Map<String,String>>() ;
        for( EntityPlatformUserGroup entity : list )
        {
            Map<String,String> map = new HashMap<String,String>() ;
            map.put( "id", formatString( entity.getId() ) ) ;
            map.put( "groupName", formatString( entity.getGroupName() ) ) ;
            map.put( "remark", formatString( entity.getRemark() ) ) ;
            map.put( "createTime", formatDate( entity.getCreateTime(), "yyyy-MM-dd HH:mm:ss" ) ) ;
            map.put( "status", formatBoolean( entity.getStatus() == 0 ? false : true, "启用|停用" ) ) ;
            newList.add( map ) ;
        }
        PagingResult<Map<String,String>> newPagingResult = new PagingResult<Map<String,String>>( newList, pagingResult.getPage(), pagingResult.getPageSize(), pagingResult.getRecords() ) ;
        out.print( getSerializer().formatObject( newPagingResult ) ) ;
    }
    
    /**
     * 根据一个实体 ID 来加载用户组详细信息的 Web 方法。<br /><br />
     * 命令: "load" ；<br/><br/>
     * @param id 输入参数(由 Browser 端 POST 回来的用户组编号)
     * @param out 响应输出对象
     */
    @RequestMapping( params = "command=load" )
    public void load( @RequestParam( "id" ) String id, java.io.PrintWriter out )
    {
        EntityPlatformUserGroup entity = userGroupService.load( id ) ;
        out.print( getSerializer().formatObject( entity ) ) ;
    }
    
    /**
     * 保存用户组信息的Web方法。<br /><br />
     * 命令: "submit" ；<br/><br/>
     * @param data 输入参数(由 Browser 端 POST 回来的JSON数据)
     * @param out 响应输出对象
     */
    @RequestMapping( params = "command=submit" )
    public void submit( @RequestParam( "data" ) String data, java.io.PrintWriter out )
    {
        Map<String,Object> paramMap = getSerializer().parseMap( data ) ;
        EntityPlatformUserGroup entity = null ;
        String id = trimString( paramMap.get( "id" ) ) ;
        if( StringUtils.isNotBlank( id ) )
        {
            entity = userGroupService.load( id ) ;
            entity.setUpdateTime( new Date() );
        }
        else
        {
            entity = new EntityPlatformUserGroup() ;
            entity.setCreateTime( new Date() );
        }
        entity.setGroupName( parseString( paramMap.get( "groupName" ) ) ) ;
        entity.setStatus( parseInteger( paramMap.get( "status" ) ) ) ;
        entity.setRemark( parseString( paramMap.get( "remark" ) ) ) ;
        userGroupService.saveOrUpdate( entity ) ;
        out.print( getSerializer().message( "" ) ) ;
    }
    
    /**
     * 根据一个实体 ID 来删除用户组实体信息的 Web 方法。<br /><br />
     * 命令: "delete" ；<br/><br/>
     * @param data 输入参数(由 Browser 端 POST 回来的JSON数据)
     * @param out 响应输出对象
     */
    @SuppressWarnings( "unchecked" )
    @RequestMapping( params = "command=delete" )
    public void delete( @RequestParam( "data" ) String data, java.io.PrintWriter out )
    {
        Map<String,Object> paramMap = getSerializer().parseMap( data ) ;
        userGroupService.remove( (List<String>)paramMap.get( "id" ) ) ;
        out.print( getSerializer().message( "" ) ) ;
    }

    @Autowired
    private PlatformUserGroupService userGroupService ;
}