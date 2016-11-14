package data.platform.service ;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import data.framework.data.DataSerializerJacksonImpl;
import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractService;
import data.framework.utility.EncryptHelper;
import data.framework.utility.FormatConvertor;
import data.platform.authority.security.PrincipalAuthority;
import data.platform.common.MenuTree;
import data.platform.entity.EntityPlatformDataDictionary;
import data.platform.entity.EntityPlatformRole;
import data.platform.entity.EntityPlatformUser;

/**
 * 平台－用户服务类。
 * 
 * @author wanggq
 */
@Service
@Path(value = "/platformUserService")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PlatformUserService extends AbstractService
{
    /**
     * 保存或更新用户信息。
     * @param entity 用户实体
     */
    public void saveOrUpdate( EntityPlatformUser entity )
    {
        if( StringUtils.isNotBlank( entity.getId() ) )
        {
            update( "platformUser.updateUser", entity ) ;
        }
        else
        {
            String password = entity.getLoginPassword() ;
            if( StringUtils.isNotBlank( password ) )
            {
                entity.setLoginPassword( EncryptHelper.encryptEncode( password ) ) ;
            }
            entity.setCreateTime( new Date() ) ;
            insert( "platformUser.insertUser", entity ) ;
        }
    }
    
    /**
     * 根据用户编号获取用户信息。
     * @param id 用户编号
     * @return 用户实体
     */
    public EntityPlatformUser load( String id )
    {
        Map<String,String> param = new HashMap<String,String>() ;
        param.put( "id", id ) ;
        return selectOne( "platformUser.loadUser", param ) ;
    }
    
    /**
     * 根据用户编号删除用户信息。
     * @param id 用户编号集合
     * @return 删除记录数
     */
    public int remove( List<String> idAry )
    {
        Map<String,Object> param = new HashMap<String,Object>() ;
        param.put( "idAry", idAry ) ;
        return delete( "platformUser.deleteUser", param ) ;
    }
    
    /**
     * 分页查询数据。
     * @param chineseName 用户中文名称
     * @param englishName 用户英文名称
     * @param loginAccount TMIS账号
     * @param organizationId 所属部门编号
     * @param officePhone 办公电话
     * @param mobile 手机
     * @param officeMail 办公邮箱
     * @param status 状态
     * @param departmentId 状所属部门Id
     * @param sortField 数据库排序字段
     * @param sort 排序方式（ASC|DESC）
     * @param currentPage 当前页数
     * @param pageSize 页大小
     * @return 分页查询集合
     */
    public PagingResult<EntityPlatformUser> searchUsers( String chineseName, String englishName, String loginAccount, String organizationId, String officePhone, String mobile, String officeMail, Integer status, String departmentId,String sortField, String sort, int currentPage, int pageSize )
    {
        Map<String,Object> param = new HashMap<String,Object>() ;
        param.put( "chineseName", chineseName ) ;
        param.put( "englishName", englishName ) ;
        param.put( "loginAccount", loginAccount ) ;
        param.put( "organizationId", organizationId ) ;
        param.put( "officePhone", officePhone ) ;
        param.put( "mobile", mobile ) ;
        param.put( "officeMail", officeMail ) ;
        param.put( "status", status ) ;
        param.put( "departmentId", departmentId ) ;
        if( StringUtils.isBlank( sortField ) )
            sortField = "SeqNums" ;
        if( StringUtils.isBlank( sort ) )
            sort = "ASC" ;
        return selectComplexPaging("platformUser.selectPaging", param, "OrganizationCode", "ASC",sortField,sort, currentPage, pageSize);
    }
    
    /**
     * 根据用户ID获取用户拥有的角色ID的集合。
     * @param userId 用户ID
     * @return 角色ID集合
     */
    public List<String> selectUserRoles( String userId )
    {
        return selectList( "platformUser.selectUserRoles", userId ) ;
    }
    
    /**
     * 根据用户ID获取用户拥有的角色ID及默认角色信息的集合。
     * @param userId 用户ID
     * @return 角色ID及默认角色信息集合
     */
    public List<Map<String,Object>> selectUserRolesAndDefault( String userId )
    {
        return selectList( "platformUser.selectUserRolesAndDefault", userId ) ;
    }
    
    /**
     * 删除用户对应的角色。
     * @param userIds 要删除的用户ID集合
     * @return 删除的记录数
     */
    public int deleteUserRoles( List<String> userIds )
    {
        return delete( "platformUser.deleteUserRole", userIds ) ;
    }
    
    /**
     * 根据用户ID获取用户拥有的角色ID的集合。
     * @param userIds 用户ID集合
     * @param roleIds 角色ID集合
     */
    public void addUserRoles( List<String> userIds, List<String> roleIds )
    {
        deleteUserRoles( userIds ) ;
        if( roleIds == null || roleIds.isEmpty() )
            return ;
        for( String userId : userIds )
        {
            Map<String,Object> param = new HashMap<String,Object>() ;
            param.put( "userId", userId ) ;
            param.put( "roleIds", roleIds ) ;
            insert( "platformUser.insertUserRole", param ) ;
        }
    }
    
    /**
     * 刷新用户的最后登录时间。
     * @param userId 要刷新登录时间的用户代理主键
     */
    public void flashLoginTime( String userId )
    {
        update( "platformUser.flashLoginTime", userId ) ;
    }
    
    /**
     * 根据用户登陆名称查找用户，不存在时返回 null。
     * @param loginAccount 用户登陆名称
     * @return 有用户登陆名称标识的用户实体
     */
    public EntityPlatformUser getUserByLoginName( String loginAccount )
    {
        if( loginAccount == null || loginAccount.length() == 0 )
            return null ;

        List<EntityPlatformUser> userList = selectList( "platformUser.getUserByLoginName", loginAccount ) ;
        if( userList == null || userList.size() == 0 )
            return null ;
        else
            return userList.get( 0 ) ;
    }
    
    /**
     * 根据用户登录名获取用户默认角色Id。
     * @param loginAccount 用户登录名
     * @return 角色Id
     */
    public String getDefaultRoleByUser( String loginAccount )
    {
        return selectOne( "platformUser.getDefaultRoleByUser", loginAccount ) ;
    }
    
    /**
     * 取得用户的功能权限集合。
     * @param userId 用户ID
     * @return 功能权限集合
     */
    public List<GrantedAuthority> getFunctionAuthoritiesByUser(  String userId )
    {
    	List<GrantedAuthority> authorityList = new ArrayList<GrantedAuthority>() ;
    	List<String> resultList = selectList( "platformUser.getFunctionAuthoritiesByUser", userId ) ;
        
        if( resultList != null && !resultList.isEmpty() )
        {
            for( String res : resultList )
            {
                authorityList.add( new PrincipalAuthority( FormatConvertor.parseString( res ) ) ) ;
            }
        }
        return authorityList ;
    }
    
    /**
     * 根据当前用户下当前角色的数据权限集合。
     * @param userId 用户ID
     * @param roleId 角色ID
     * @return 数据权限集合（集合数据为纳税主体TMIS代码）
     */
    public List<String> getDataAuthoritiesByUser( String loginAccount, String roleId )
    {
        Map<String,Object> param = new HashMap<String,Object>() ;
        param.put( "loginAccount", loginAccount ) ;
        param.put( "roleId", roleId ) ;
        return selectList( "platformUser.getDataAuthoritiesByUser", param ) ;
    }
    
    /**
     * 获取用户对应的功能权限中的树形菜单集合（用户首页显示的菜单）。
     * @param userId 用户ID
     * @return 用户对应的功能权限中的树形菜单集合
     */
    public List<MenuTree> getMenusByUser( String userId )
    {
        return selectList( "platformUser.getMenusByUser", userId ) ;
    }
    
    /**
     * 获取当前用户Id和角色Id来获取用户的其他角色。
     * @param currentUserId 当前用户ID
     * @param currentRoleId 当前角色ID
     * @return 用户角色集合
     */
    public List<EntityPlatformRole> getOtherRolesByUserId( String currentUserId, String currentRoleId )
    {
        Map<String,Object> param = new HashMap<String,Object>() ;
        param.put( "currentUserId", currentUserId ) ;
        param.put( "currentRoleId", currentRoleId ) ;
        return selectList( "platformUser.getOtherRolesByUserId", param ) ;
    }
    
    /**
     * 根据当前用户的ADAccount来获取与其有相同ADAccount的用户。
     * @param currentUserId 当前用户ID
     * @param adAccount 用户ADAccount
     * @return 用户集合
     */
    public List<EntityPlatformUser> getOtherUsersByADAccount( String currentUserId, String adAccount )
    {
        Map<String,Object> param = new HashMap<String,Object>() ;
        param.put( "currentUserId", currentUserId ) ;
        param.put( "adAccount", adAccount ) ;
        return selectList( "platformUser.getOtherUsersByADAccount", param ) ;
    }
    
    /**
     * 查询用户角色信息
     * @param currentUserId
     * @return
     */
    public List<EntityPlatformRole> getUserRoleInfo(String currentUserId) {
    	Map<String, Object> param = new HashMap<String, Object>();
    	param.put("currentUserId", currentUserId);
    	
    	return selectList("platformUser.getUserRoleInfo", param);
    }
    
    
    /**
     * 修改用户密码。
     * @param userIds 要修改密码的用户ID集合
     * @param newPassword 要修改的密码
     */
    public void changePassword( List<String> userIds, String newPassword )
    {
        Map<String,Object> param = new HashMap<String,Object>() ;
        param.put( "newPassword", EncryptHelper.encryptEncode( newPassword ) ) ;
        param.put( "userIds", userIds ) ;
        update( "platformUser.changePassword", param ) ;
    }
    
    /**
     * 
     * @param roleName 用户角色名称
     * @return 用户实体集合
     * @author JohnXU   
     */
    public List<EntityPlatformUser> searchUserByRoleName(String roleName)
    {
    	return selectList("platformUser.searchUserByRoleName", roleName);
    }
    
    /**
     * 根据角色查询该角色的所有用户
     * @param  角色ID
     * @return 用户实体集合
     * @author lidong   
     */
    public List<Map<String,String>> searchUserByRoleIDs(List<String> roleIds)
    {
    	return selectList("platformUser.searchUserByRoleID", roleIds);
    }
    
    /**
     * 
     * @param id 用户Id
     * @return 用户中文名
     * @author JohnXU   
     */
    public String searchUserChineseNameById(String id)
    {
    	return selectOne("platformUser.searchUserChineseNameById", id);
    }
    
    /**
     * 根据Rsc Tax 对口人的名称短语来查询指定状态的Rsc Tax 对口人数据集合。
     * @param term Rsc Tax 对口人名称短语
     * @return Rsc Tax 对口人数据集合
     */
    public List<Map<String,String>> searchUserByRoleNameByTerm(String term)
    {
        Map<String,Object> param = new HashMap<String,Object>() ;
        param.put( "term", term ) ;
        return selectList( "platformUser.searchUserByRoleNameByTerm", param ) ;
    }
    
    /**
     * 查询用户相关信息(eg:email)。
     * @param chineseName 中文名
     * @param englishName 英文名
     */
    public PagingResult<EntityPlatformUser> searchNoticePersons(String chineseName, String englishName ,String sortField,String sort,int currentPage,int pageSize)
    {
        Map<String,Object> param = new HashMap<String,Object>() ;
        param.put( "chineseName",chineseName) ;
        param.put( "englishName", englishName ) ;
        if( StringUtils.isBlank( sortField ) )
            sortField = "ChineseName" ;
        if( StringUtils.isBlank( sort ) )
            sort = "ASC" ;
        return selectPaging("platformUser.selectNoticePersonsPaging", param, sortField, sort, currentPage, pageSize);
    }
    
    /**
     * 根据职务名称短语来查询指定状态的职务数据集合。
     * @return 职务数据集合
     */
    public List<EntityPlatformDataDictionary> searchPositionNames()
    {
        return selectList( "platformUser.searchUserPosition") ;
    }
    
    /**
     * 分页查询用户信息。
     * @return 用户实体集合
     */
    public PagingResult<EntityPlatformUser> search(String chineseName,int currentPage, int pageSize )
    {
        Map<String,Object> param = new HashMap<String,Object>() ;
        param.put( "chineseName", chineseName ) ;
        return selectPaging( "platformUser.selectUserOfCardTypePaging", param, "ChineseName", "ASC", currentPage, pageSize ) ;
    }
    
    /**
     * 根据用户ID集合返回用户信息集合
     * @param list
     * @return
     */
    public List<EntityPlatformUser> loadUsers(List<String> list){
    	return selectList( "platformUser.loadUsers",list) ;
    }
    
    //===========================APP接口======================================
    
    @Autowired
	private DataSerializerJacksonImpl serializer;
    
    @Autowired
    private PlatformOrganizationService platformOrganizationService ;
    
    @POST
	@Path(value = "/getUserInfoById")
	public String getUserInfoById(String param) {
    	
    	Map<String,Object> paramMap = serializer.parseMap( param ) ;
		String id = FormatConvertor.trimString(paramMap.get("id"));
		
		Map<String,Object> m = new HashMap<String, Object>();
		if(id != null){
			EntityPlatformUser user = load(id);
			
			String rootName = platformOrganizationService.selectRootName();
		
			m.put("rootName", rootName);
			
		}
		return serializer.formatMap(m);
    }
    
    @POST
   	@Path(value = "/search")
   	public String search(String param) {
       	
	   	Map<String,Object> paramMap = serializer.parseMap( param ) ;
		String userName = FormatConvertor.trimString(paramMap.get("userName"));
		
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		
		if(StringUtils.isNotEmpty(userName)){
			HashMap<String,Object> par = new HashMap<String,Object>();
			par.put("userName", userName);
			//list = selectList( "employmeeInfo.searchEmploymeePaging", par ) ;
			PagingResult<Map<String,Object>> pagingResult=null;
			pagingResult =  selectPaging( "employmeeInfo.searchEmploymeePaging", par, "seqNums", "ASC", 1, 1000 ) ;
			list = pagingResult.getRows();
		}
		
		for (Map<String, Object> map : list) {
			Object filePath = map.get("FilePathInServer");
			if(filePath != null) {
				map.put("FilePathInServer", formatPhotoPath(filePath.toString()));
			}
		}
		return serializer.formatList(list);
    }
    
    public String formatPhotoPath(String imgPath) {
		if(StringUtils.isNotEmpty(imgPath)) {
			String sub = imgPath.substring(imgPath.indexOf("upload\\"));
			imgPath = File.separator + sub;
			imgPath = imgPath.replace("\\", "//");
		}
		return imgPath;
    }
    
    @POST
   	@Path(value = "/update_APP")
   	public String updateUser(String param) {
       	
	   	Map<String,Object> paramMap = serializer.parseMap( param ) ;
	   	String id = paramMap.get("userId").toString();
	 	String loginPassword = 	paramMap.get("loginPassword").toString();
	    EntityPlatformUser entity = load(id);
        if( StringUtils.isNotBlank( loginPassword ) )
        {
            entity.setLoginPassword( EncryptHelper.encryptEncode( loginPassword ) ) ;
            saveOrUpdate(entity);
        }
	   
		return serializer.formatObject("");
    }
}