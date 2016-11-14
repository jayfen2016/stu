package data.platform.service;

import java.io.File;
import java.util.ArrayList;
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
import org.springframework.stereotype.Service;

import data.framework.data.DataSerializerJacksonImpl;
import data.framework.data.DataTree;
import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractService;
import data.platform.entity.EntityPlatformDataDictionary;
import data.platform.entity.EntityPlatformOrganization;
import data.platform.entity.EntityPlatformUser;

/**
 * 平台-组织机构服务类。
 * 
 * @author wanggq
 *
 */
@Service
@Path(value = "/platformOrganizationService")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PlatformOrganizationService extends AbstractService {

	/**
	 * 保存或更新组织机构信息。
	 * 
	 * @param entity
	 *            组织机构实体
	 */
	public void saveOrUpdate(EntityPlatformOrganization entity) {
		if (StringUtils.isNotBlank(entity.getId()))
			update("platformOrganization.updateOrganization", entity);
		else
			insert("platformOrganization.insertOrganization", entity);
	}

	/**
	 * 根据组织机构编号获取组织机构信息。
	 * 
	 * @param id
	 *            组织机构编号
	 * @return 组织机构实体
	 */
	public EntityPlatformOrganization load(String id) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("id", id);
		return selectOne("platformOrganization.loadOrganization", param);
	}

	/**
	 * 根据组织机构编号删除组织机构信息。
	 * 
	 * @param id
	 *            组织机构编号集合
	 * @return 删除记录数
	 */
	public int remove(List<String> idAry) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("idAry", idAry);
		return delete("platformOrganization.deleteOrganization", param);
	}

	/**
	 * 查询多个用户的组织机构根据用户id(选人的顺序)。
	 * 
	 * @param map
	 */
	public List<Map<String, String>> selectOrganizationByUserIDAndDf(
			List<String> idAry) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("idAry", idAry);
		return selectList(
				"platformOrganization.selectOrganizationByUserIDAndDf", map);
	}

	/**
	 * 分页查询数据。
	 * 
	 * @param organizationName
	 *            组织机构名称
	 * @param organizationCode
	 *            组织机构代码
	 * @param sortField
	 *            数据库排序字段
	 * @param sort
	 *            排序方式（ASC|DESC）
	 * @param currentPage
	 *            当前页数
	 * @param pageSize
	 *            页大小
	 * @return 分页查询集合
	 */
	public PagingResult<EntityPlatformOrganization> searchOrganization(
			String organizationName, String organizationCode, String sortField,
			String sort, int currentPage, int pageSize) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("organizationName", organizationName);
		param.put("organizationCode", organizationCode);
		if (StringUtils.isBlank(sortField))
			sortField = "serial";
		if (StringUtils.isBlank(sort))
			sort = "ASC";
		return selectPaging("platformOrganization.selectPaging", param,
				sortField, sort, currentPage, pageSize);
	}

	/**
	 * 根据term查询组织机构数据。
	 * 
	 * @param organizationName
	 *            组织机构名称
	 * @param organizationCode
	 *            组织机构代码
	 * @param sortField
	 *            数据库排序字段
	 * @param sort
	 *            排序方式（ASC|DESC）
	 * @param currentPage
	 *            当前页数
	 * @param pageSize
	 *            页大小
	 * @return 分页查询集合(如：Map{text:"",value:""})
	 */
	public PagingResult<Map<String, String>> searchTermOrganization(
			String organizationName, String organizationCode, String sortField,
			String sort, int currentPage, int pageSize) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("organizationName", organizationName);
		param.put("organizationCode", organizationCode);
		if (StringUtils.isBlank(sortField))
			sortField = "text";
		if (StringUtils.isBlank(sort))
			sort = "ASC";
		return selectPaging("platformOrganization.selectTermPaging", param,
				sortField, sort, currentPage, pageSize);
	}

	/**
	 * 根据组织机构状态获取树形结构数据。
	 * 
	 * @return 树形结构数据集合
	 */
	public List<DataTree> getOrganizationTree() {
		return selectList("platformOrganization.selectTreeOrganization");
	}

	/**
	 * 查询某个组织机构下的所有用户信息。
	 * 
	 * @param entity
	 *            用户信息集合
	 */
	public List<EntityPlatformUser> selectOrganizationUser(String id) {
		return selectList("platformOrganization.selectOrganizationUser", id);
	}

	/**
	 * 查询多个组织机构下的所有用户信息。
	 * 
	 * @param map
	 */
	public List<Map<String, String>> selectOrganizationUserList(
			List<String> idAry) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("idAry", idAry);
		return selectList("platformOrganization.selectOrganizationUserList",
				map);
	}

	/**
	 * 查询多个用户的组织机构根据用户id。
	 * 
	 * @param map
	 */
	public List<Map<String, String>> selectOrganizationByUserID(
			List<String> idAry) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("idAry", idAry);
		return selectList("platformOrganization.selectOrganizationByUserID",
				map);
	}

	/**
	 * 查询某个组织机构下的所有用户信息。
	 * 
	 * @param entity
	 *            用户信息集合
	 */
	public List<Map<String, Object>> selectTest(String id) {
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("id", id);
		return selectList("platformOrganization.one", param);
	}

	/**
	 * 查询某个组织机构下的所有用户信息。
	 * 
	 * @param entity
	 *            用户信息集合
	 */
	public List<Map<String, Object>> selectUsers(String id) {
		return selectList("platformOrganization.selectUsers", id);
	}

	/**
	 * 获取模块类型。
	 * 
	 * @return 模块类型集合
	 */
	public List<EntityPlatformDataDictionary> loadModelTypes(String orgId) {
		HashMap<String, Object> param = new HashMap<String, Object>();
		List<String> modelTypeList = new ArrayList<String>();

		if ("3A4E2F2B-5558-4AE6-A123-04B5407FD766".equals(orgId)) {
			modelTypeList.add("Contact");
			modelTypeList.add("UnitSchedule");
			modelTypeList.add("MeetingManage");
		} else {
			modelTypeList.add("Workflow");
			modelTypeList.add("MeetingManage");
			modelTypeList.add("HumanResource");
			modelTypeList.add("ZBBJ");
		}
		param.put("modelTypeList", modelTypeList);
		return selectList("platformOrganization.loadModelTypes", param);
	}

	/**
	 * 获取组织根节点
	 * 
	 * @return 组织根节点名称
	 */
	public String selectRootName() {
		return selectOne("platformOrganization.selectRootName");
	}

	/**
	 * 查询(中证)的所有用户信息。
	 * 
	 * @param entity
	 *            用户信息集合
	 */
	public List<Map<String, Object>> selecAlltUsers() {
		return selectList("platformOrganization.selecAlltUsers");
	}

	/**
	 * 查询所有可选的用户信息。
	 * 
	 * @param entity
	 *            用户信息集合
	 */
	public List<Map<String, Object>> selectUsersForShareSchedule(String id,
			String currentUserId) {

		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("userId", currentUserId);
		param.put("id", id);
		return selectList("platformOrganization.selectUsersForShareSchedule",
				param);
	}

	/**
	 * 查询所有部门
	 * 
	 * @param entity
	 *            用户信息集合
	 */

	public List<EntityPlatformOrganization> selectAllOrganizations() 
	{
		return selectList("platformOrganization.selectAllOrganizations");
	}

	/**
	 * 查询所有部门(除了中心领导部门)
	 */
	public List<EntityPlatformOrganization> selectAllOrganizationsExceptSeniorOrg() 
	{
		return selectList("platformOrganization.selectAllOrganizationsExceptSeniorOrg");
	}

	

	
	//===========================APP接口======================================
	
	@POST
	@Path(value = "/getPersonsTree")
	public String getPersonsTree(String param) {
		 Map<String, Object> paramMap = serializer.parseMap(param);
		 List<String> list = (List<String>)paramMap.get("ids");
		// 存放第一层json数据
		List<Map<String, Object>> orgResultList = new ArrayList<Map<String, Object>>();
		// 加载第一层节点
		List<Map<String, Object>> orgList = this.selectTest("");

		for (Map<String, Object> firstMap : orgList) {
			HashMap<String, Object> perOrgMap = new HashMap<String, Object>();
			String orgId = firstMap.get("id").toString();
			// 分门别类的加载第一层节点的子节点
			List<Map<String, Object>> secondOrgMapList = this.selectTest(orgId);// 第二层
																				// 部门
			// 存放第二层json数据
			List<Map<String, Object>> secondOrgList = new ArrayList<Map<String, Object>>();
			for (Map<String, Object> secondMap : secondOrgMapList) {
				HashMap<String, Object> perSecondOrgMap = new HashMap<String, Object>();
				String secondOrgId = secondMap.get("id").toString();

				List<Map<String, Object>> userMapList = this.selectUsers(secondOrgId);
				List<Map<String, Object>> userList = new ArrayList<Map<String, Object>>();
				if (userMapList != null && userMapList.size() > 0) {
					for (Map<String, Object> userMap : userMapList) {
						HashMap<String, Object> perUserMap = new HashMap<String, Object>();
						String userId = userMap.get("id").toString();
						if(existValue(list,userId))
						{
							continue;
						}
							
						perUserMap.put("id", userId);
						perUserMap.put("name", userMap.get("ChineseName"));
						userList.add(perUserMap);
					}
				}
				perSecondOrgMap.put("id", secondOrgId);
				perSecondOrgMap.put("name", secondMap.get("OrganizationName"));
				perSecondOrgMap.put("isParent", true);
				perSecondOrgMap.put("nocheck", true);
				perSecondOrgMap.put("children", userList);
				secondOrgList.add(perSecondOrgMap);
			}
			// 针对系统管理员用户
			List<Map<String, Object>> systemList = this.selectUsers(orgId);
			if (systemList != null && systemList.size() > 0) {
				for (Map<String, Object> sysUserMap : systemList) {
					HashMap<String, Object> adminMap = new HashMap<String, Object>();
					if(existValue(list,sysUserMap.get("id").toString()))
					{
						continue;
					}
					adminMap.put("id", sysUserMap.get("id"));
					adminMap.put("name", sysUserMap.get("ChineseName"));
					adminMap.put("isParent", false);
					secondOrgList.add(adminMap);
				}
			}
			perOrgMap.put("id", orgId);
			perOrgMap.put("name", firstMap.get("OrganizationName"));
			perOrgMap.put("isParent", true);
			perOrgMap.put("nocheck", true);
			perOrgMap.put("children", secondOrgList);
			orgResultList.add(perOrgMap);
		}

		return serializer.formatList(orgResultList);
	}
	
	public boolean existValue(List<String> list,String str)
	{
		
		if(list!=null&&list.size()>0)
		{
			for(String ev:list)
			{
				if(ev.equals(str))
				{
					return true;
				}
			}
		}
		
		return false;
	}
	
	
	/**
	 * 查询所有部门
	 */
	@POST
	@Path(value = "/selectDepartments")
	public String selectDepartments() {
		//存放第一层json数据
		List<Map<String,Object>> orgResultList=new ArrayList<Map<String,Object>>();
		//加载第一层节点
		List<Map<String,Object>>  orgList= selectTest("");
		
		for(Map<String,Object> firstMap : orgList) {
			HashMap<String,Object> perOrgMap=new HashMap<String,Object>();
			
			String orgId=(firstMap.get("id")).toString();
			//分门别类的加载第一层节点的子节点
			List<Map<String,Object>> secondOrgMapList=selectTest(orgId);//第二层 部门
			//存放第二层json数据
			List<Map<String,Object>> secondOrgList=new ArrayList<Map<String,Object>>();
			for(Map<String,Object> secondMap : secondOrgMapList) {
				HashMap<String,Object> perSecondOrgMap=new HashMap<String,Object>();
				String secondOrgId=(secondMap.get("id")).toString();
				
				perSecondOrgMap.put("id", secondOrgId);
				perSecondOrgMap.put("name", secondMap.get("OrganizationName"));
				perSecondOrgMap.put("isParent",true);
				perSecondOrgMap.put("nocheck",true);
				
				//获取人员
				HashMap<String,Object> p =new HashMap<String,Object>();
		    	p.put("orgId", secondOrgId);
		    	
		    	List<Map<String,Object>> list=null;
				PagingResult<Map<String,Object>> pagingResult=null;
				pagingResult =  selectPaging( "employmeeInfo.searchEmploymeePaging", p, "seqNums", "ASC", 1, 1000 ) ;
				list = pagingResult.getRows();
		        //List<Map<String,Object>> list = selectList( "employmeeInfo.searchEmploymeePaging", p) ;
		        
		        List<Map<String,Object>> newList = new ArrayList<Map<String,Object>>();
		        
		        for( Map<String,Object> resultMap : list )
		        {
		            Map<String,Object> map = new HashMap<String,Object>() ;
		            map.put( "id", resultMap.get("userId") ) ;
		            map.put( "chineseName", resultMap.get("userName") ) ;
		            map.put( "orgName", resultMap.get("orgName")) ;
		            map.put( "position", resultMap.get("PostLevel")) ;
		            map.put( "telephone", resultMap.get("Mobile") ) ;
		            map.put( "officePhone", resultMap.get("OfficePhone") ) ;
		            map.put( "email", resultMap.get("OfficeMail") ) ;
		            
		            if(resultMap.get("FilePathInServer")!=null){
		            	String imgPath = (resultMap.get("FilePathInServer")).toString();
						map.put("imgUrl", formatPhotoPath(imgPath));
		            }else{
		           	 	map.put("imgUrl","");
		            }
		            newList.add( map ) ;
		        }
		        
		        perSecondOrgMap.put("persons",newList);
				secondOrgList.add(perSecondOrgMap);
			}
			perOrgMap.put("id", orgId);
			perOrgMap.put("name", firstMap.get("OrganizationName"));
			perOrgMap.put("isParent",true);
			perOrgMap.put("nocheck",true);
			perOrgMap.put("open",true);
			perOrgMap.put("children",secondOrgList);
			orgResultList.add(perOrgMap);
			
		}
		return serializer.formatList(orgResultList);
	}
	
	/*
	 ** 获取所有部门
	 */
	@POST
	@Path(value = "/getDepartmentTree")
	public String getDepartments(String param){
		Map<String, Object> paramMap = serializer.parseMap(param);
		List<String> list = (List<String>)paramMap.get("ids");
		// 存放第一层json数据
		List<Map<String, Object>> orgResultList = new ArrayList<Map<String, Object>>();
		// 加载第一层节点
		List<Map<String, Object>> orgList = this.selectTest("");

		for (Map<String, Object> firstMap : orgList) {
			HashMap<String, Object> perOrgMap = new HashMap<String, Object>();
			String orgId = firstMap.get("id").toString();
			// 分门别类的加载第一层节点的子节点
			List<Map<String, Object>> secondOrgMapList = this.selectTest(orgId);// 第二层部门
			// 存放第二层json数据
			List<Map<String, Object>> secondOrgList = new ArrayList<Map<String, Object>>();
			for (Map<String, Object> secondMap : secondOrgMapList) {
				HashMap<String, Object> perSecondOrgMap = new HashMap<String, Object>();
				String secondOrgId = secondMap.get("id").toString();
				if(existValue(list,secondOrgId)){
					continue;
				}
				perSecondOrgMap.put("id", secondOrgId);
				perSecondOrgMap.put("name", secondMap.get("OrganizationName"));
				perSecondOrgMap.put("isParent", true);
				perSecondOrgMap.put("nocheck", true);
				secondOrgList.add(perSecondOrgMap);
			}
			perOrgMap.put("id", orgId);
			perOrgMap.put("name", firstMap.get("OrganizationName"));
			perOrgMap.put("isParent",true);
			perOrgMap.put("nocheck",true);
			perOrgMap.put("open",true);
			perOrgMap.put("children",secondOrgList);
			orgResultList.add(perOrgMap);
		}
		return serializer.formatList(orgResultList);
	}
	
	public String formatPhotoPath(String imgPath) {
		if(StringUtils.isNotEmpty(imgPath)) {
			String sub = imgPath.substring(imgPath.indexOf("upload\\"));
			imgPath = File.separator + sub;
			imgPath = imgPath.replace("\\", "//");
		}
		return imgPath;
	}
	

	@Autowired
	private DataSerializerJacksonImpl serializer;
}