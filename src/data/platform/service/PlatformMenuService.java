package data.platform.service;

import java.util.HashMap ;
import java.util.List ;
import java.util.Map ;

import org.apache.commons.lang.StringUtils ;
import org.springframework.stereotype.Service ;

import data.framework.data.DataTree ;
import data.framework.pagination.model.PagingResult ;
import data.framework.support.AbstractService ;
import data.platform.entity.EntityPlatformMenu ;
/**
 * 平台-菜单服务类。
 * @author wanggq
 *
 */
@Service
public class PlatformMenuService extends AbstractService
{
    /**
     * 保存或更新菜单信息。
     * @param entity 菜单实体
     * @throws Exception
     */
    public void saveOrUpdate( EntityPlatformMenu entity )
    {
        if( StringUtils.isNotBlank( entity.getId() ) )
            update( "platformMenu.updateMenu", entity ) ;
        else
            insert( "platformMenu.insertMenu", entity ) ;
    }
    
    /**
     * 根据菜单编号获取菜单信息。
     * @param id 菜单编号
     * @return 菜单实体
     */
    public EntityPlatformMenu load( String id )
    {
        Map<String,String> param = new HashMap<String,String>() ;
        param.put( "id", id ) ;
        return selectOne( "platformMenu.loadMenu", param ) ;
    }
    
    /**
     * 根据菜单编号删除菜单信息。
     * @param id 菜单编号集合
     * @return 删除记录数
     */
    public int remove( List<String> idAry )
    {
        Map<String,Object> param = new HashMap<String,Object>() ;
        param.put( "idAry", idAry ) ;
        return delete( "platformMenu.deleteMenu", param ) ;
    }
    
    /**
     * 分页查询数据。
     * @param menuName 菜单名称
     * @param menuCode 菜单代码
     * @param status 状态
     * @param sortField 数据库排序字段
     * @param sort 排序方式（ASC|DESC）
     * @param currentPage 当前页数
     * @param pageSize 页大小
     * @return 分页查询集合
     */
    public PagingResult<EntityPlatformMenu> searchMenus( String menuName, String menuCode, Integer status, String sortField, String sort, int currentPage, int pageSize )
    {
        Map<String,Object> param = new HashMap<String,Object>() ;
        param.put( "menuName", menuName ) ;
        param.put( "menuCode", menuCode ) ;
        param.put( "status", status ) ;
        if( StringUtils.isBlank( sortField ) )
            sortField = "serial" ;
        if( StringUtils.isBlank( sort ) )
            sort = "ASC" ;
        return selectPaging( "platformMenu.selectPaging", param, sortField, sort, currentPage, pageSize ) ;
    }
    
    /**
     * 根据菜单状态获取树形结构数据。
     * @param status 菜单状态
     * @return 树形结构数据集合
     */
    public List<DataTree> getMenuTree( Integer status )
    {
        Map<String,Object> param = new HashMap<String,Object>() ;
        param.put( "status", status ) ;
        return selectList( "platformMenu.selectTreeMenu", param ) ;
    }
}