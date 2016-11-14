package data.platform.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractService;
import data.platform.entity.EntityPlatformRole;
import data.platform.entity.EntityPlatformUser;

/**
 *平台-角色服务类。
 * @author wanggq
 *
 */
@Service
public class PlatformRoleService extends AbstractService
{
    /**
     * 保存或更新角色信息。
     * @param entity 角色实体
     */
    public void saveOrUpdate( EntityPlatformRole entity )
    {
        if( StringUtils.isNotBlank( entity.getId() ) )
            update( "platformRole.updateRole", entity ) ;
        else
            insert( "platformRole.insertRole", entity ) ;
    }
    
    /**
     * 根据角色编号获取角色信息。
     * @param id 角色编号
     * @return 角色实体
     */
    public EntityPlatformRole load( String id )
    {
        Map<String,String> param = new HashMap<String,String>() ;
        param.put( "id", id ) ;
        return selectOne( "platformRole.loadRole", param ) ;
    }
    
    /**
     * 根据角色编号数组获取角色信息集合。
     * @param id 角色编号
     * @return 角色实体
     * @author lidong
     */
    public List<EntityPlatformRole> loadRoleByIDs( List<String> ids )
    {
        return selectList( "platformRole.loadRoleByIDs", ids ) ;
    }
    
    /**
     * 根据角色编号删除角色信息。
     * @param id 角色编号集合
     * @return 删除记录数
     */
    public int remove( List<String> idAry )
    {
        Map<String,Object> param = new HashMap<String,Object>() ;
        param.put( "idAry", idAry ) ;
        return delete( "platformRole.deleteRole", param ) ;
    }
    
    /**
     * 分页查询数据。
     * @param roleName 角色名称
     * @param status 角色状态
     * @param sortField 数据库排序字段
     * @param sort 排序方式（ASC|DESC）
     * @param currentPage 当前页数
     * @param pageSize 页大小
     * @return 分页查询集合
     */
    public PagingResult<EntityPlatformRole> searchRoles( String roleName, Integer status, String sortField, String sort, int currentPage, int pageSize )
    {
        Map<String,Object> param = new HashMap<String,Object>() ;
        param.put( "roleName", roleName ) ;
        param.put( "status", status ) ;
        if( StringUtils.isBlank( sortField ) )
            sortField = "RoleName" ;
        if( StringUtils.isBlank( sort ) )
            sort = "ASC" ;
        return selectPaging( "platformRole.selectPaging", param, sortField, sort, currentPage, pageSize ) ;
    }
    
    /**
     * 根据条件查询角色的ID和名称信息。
     * @param roleName 角色名称
     * @param status 状态
     * @return 角色的ID和名称信息集合(如Map：{text:"",value:""})
     */
    public List<Map<String,String>> searchRoles( String roleName, Integer status )
    {
        Map<String,Object> param = new HashMap<String,Object>() ;
        param.put( "roleName", roleName ) ;
        param.put( "status", status ) ;
        return selectList( "platformRole.selectRole", param ) ;
    }
    
    /**
     * 根据角色ID获取对应功能权限的集合。
     * @param roleId 角色ID
     * @return 功能权限集合
     */
    public List<Map<String,String>> selectRoleFunctions( String roleId )
    {
        return selectList( "platformRole.selectRoleFunctions", roleId ) ;
    }
    
    /**
     * 删除角色对应的功能权限。
     * @param roleId 角色ID
     * @return 删除的记录数
     */
    public int deleteRoleFunctions( String roleId )
    {
        return delete( "platformRole.deleteRoleFunctions", roleId ) ;
    }
    
    /**
     * 保存角色功能权限信息。
     * @param roleId 角色ID
     * @param functionList 功能权限集合
     * @param operator 操作人
     */
    public void saveRoleFunctions( String roleId, List<Map<String,String>> functionList, String operator )
    {
        deleteRoleFunctions( roleId ) ;
        if( functionList == null || functionList.isEmpty() )
            return ;
        Map<String,Object> param = new HashMap<String,Object>() ;
        param.put( "roleId", roleId ) ;
        param.put( "functions", functionList ) ;
        param.put( "createTime", new Date() ) ;
        param.put( "operator", operator ) ;
        insert( "platformRole.insertRoleFunctions", param ) ;
    }
    
    
    
    /**
     * 删除角色对应的数据权限。
     * @param roleId 角色ID
     * @return 删除的记录数
     */
    public int deleteRoleDataAuthoritys( String roleId )
    {
        return delete( "platformRole.deleteRoleDataAuthoritys", roleId ) ;
    }
    
    /**
     * 保存角色数据权限信息。
     * @param roleId 角色ID
     * @param dataAuthorityList 数据权限集合
     * @param operator 操作人
     */
    public void saveRoleDataAuthoritys( String roleId, List<String> dataAuthorityList, String operator )
    {
        if( dataAuthorityList == null || dataAuthorityList.isEmpty() )
            return ;
        Map<String,Object> param = new HashMap<String,Object>() ;
        param.put( "roleId", roleId ) ;
        param.put( "dataAuthoritys", dataAuthorityList ) ;
        param.put( "createTime", new Date() ) ;
        param.put( "operator", operator ) ;
        insert( "platformRole.insertRoleDataAuthoritys", param ) ;
    }
    
    /**
     * 根据角色ID查找用户List
     */
    public List<EntityPlatformUser> selectUsersByRoleId( String roleId )
    {
        return selectList( "platformRole.selectUsersByRoleId", roleId ) ;
    }
    
    /**
     * 根据角色ID删除关联的用户映射
     * 添加新的映射
     */
    public void roleUserMapping( String roleId,String[] userIds )
    {
    	delete("platformRole.deleteMappingByRoleId",roleId);
    	
    	for (String userId : userIds) {
    		Map<String,Object> param = new HashMap<String,Object>() ;
            param.put( "roleId", roleId ) ;
            param.put( "userId", userId ) ;
            
            insert("platformRole.insertMappingForRole",param);
		}
    }
    
    
    /**
     * 删除角色对应的数据权限。
     * @param roleId 角色ID
     * @return 删除的记录数
     */
    public int deleteUserRole( String userId )
    {
        return delete( "platformRole.deleteUserRole", userId ) ;
    }
    
    /**
     * 根据角色的ApprovalName查询 相应的组(中心组，工会等等) List
     */
    public List<EntityPlatformRole> selectRoleTeamsByApprovalName(String approvalName )
    {
        return selectList( "platformRole.selectRoleTeamsByApprovalName", approvalName ) ;
    }
    
    /**
     * 根据角色的ApprovalName查询 相应的组(中心组，工会等等) List
     */
    public List<Map<String,Object>> selectUsersByUserId(String userId )
    {
        return selectList( "platformRole.selectUsersByUserId", userId ) ;
    }
}