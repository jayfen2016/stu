package data.platform.service;

import java.util.HashMap ;
import java.util.List ;
import java.util.Map ;

import org.apache.commons.lang.StringUtils ;
import org.springframework.stereotype.Service ;

import data.framework.pagination.model.PagingResult ;
import data.framework.support.AbstractService ;
import data.platform.entity.EntityPlatformUserGroup ;
/**
 * 平台-用户组服务类。
 * @author wanggq
 */
@Service
public class PlatformUserGroupService extends AbstractService
{
    /**
     * 保存或更新用户组信息。
     * @param entity 用户组实体
     */
    public void saveOrUpdate( EntityPlatformUserGroup entity )
    {
        if( StringUtils.isNotBlank( entity.getId() ) )
            update( "platformUserGroup.updateUserGroup", entity ) ;
        else
            insert( "platformUserGroup.insertUserGroup", entity ) ;
    }
    
    /**
     * 根据用户组编号获取用户组信息。
     * @param id 用户组编号
     * @return 用户组实体
     */
    public EntityPlatformUserGroup load( String id )
    {
        Map<String,String> param = new HashMap<String,String>() ;
        param.put( "id", id ) ;
        return selectOne( "platformUserGroup.loadUserGroup", param ) ;
    }
    
    /**
     * 根据用户组编号删除用户组信息。
     * @param id 用户组编号集合
     * @return 删除记录数
     */
    public int remove( List<String> idAry )
    {
        Map<String,Object> param = new HashMap<String,Object>() ;
        param.put( "idAry", idAry ) ;
        return delete( "platformUserGroup.deleteUserGroup", param ) ;
    }
    
    /**
     * 分页查询数据。
     * @param groupName 用户组名称
     * @param status 状态
     * @param sortField 数据库排序字段
     * @param sort 排序方式（ASC|DESC）
     * @param currentPage 当前页数
     * @param pageSize 页大小
     * @return 分页查询集合
     */
    public PagingResult<EntityPlatformUserGroup> searchUserGroups( String groupName, Integer status, String sortField, String sort, int currentPage, int pageSize )
    {
        Map<String,Object> param = new HashMap<String,Object>() ;
        param.put( "groupName", groupName ) ;
        param.put( "status", status ) ;
        if( StringUtils.isBlank( sortField ) )
            sortField = "CreateTime" ;
        if( StringUtils.isBlank( sort ) )
            sort = "DESC" ;
        return selectPaging( "platformUserGroup.selectPaging", param, sortField, sort, currentPage, pageSize ) ;
    }
}