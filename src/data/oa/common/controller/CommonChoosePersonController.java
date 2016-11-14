package data.oa.common.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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

import data.framework.support.AbstractBaseController;
import data.platform.entity.EntityPlatformUser;
import data.platform.service.PlatformOrganizationService;

/**
 * OA-会议室管理-选择会议室管理人员的控制器类。
 * @author JohnXU
 *
 */
@Controller
@RequestMapping( "common/choosePerson" )
public class CommonChoosePersonController extends AbstractBaseController
{

    @Override
    protected void init( ModelMap model, HttpServletRequest request )
    {
    	String idsParam=request.getParameter("idsParam");
    	String orgId=request.getParameter("orgId");
    	String orgName=request.getParameter("orgName");
    	String moduleType=request.getParameter("moduleType");
    	String currentUserId=request.getParameter("currentUserId");
    	String isNeedAllOrg=request.getParameter("isNeedAllOrg");
    	try 
    	{
            if(orgName !=null){
                orgName=new String( orgName.getBytes( "iso-8859-1" ),"UTF-8") ;
            }
		} catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
    	model.addAttribute("idsParam", idsParam);
    	model.addAttribute("orgId", orgId);
    	model.addAttribute("orgName", orgName);
    	model.addAttribute("moduleType", moduleType);
    	model.addAttribute("currentUserId", currentUserId);
    	model.addAttribute("isNeedAllOrg", isNeedAllOrg);
    }

	/**
	 * 查询某个组织机构下的所有用户信息的 Web 方法。<br /><br />
	 * 命令: "selectOrganizationUser" ；<br/><br/>
	 * @param out 响应输出对象
	 */
	@RequestMapping( params = "command=selectOrganizationUser" )
	public void selectOrganizationUser(HttpServletRequest request, java.io.PrintWriter out )
	{
		String orgId=request.getParameter("id");
		List<EntityPlatformUser> userTree = organizationService.selectOrganizationUser(orgId) ;
	    out.print( getSerializer().formatList( userTree ) ) ;
	}

	@RequestMapping( params = "command=selectOther" )
	public void selectTest( @RequestParam( "data" ) String data, java.io.PrintWriter out )
	{
		 Map<String,Object> paramMap = getSerializer().parseMap( data ) ;
		 String orgId=trimString(paramMap.get("id"));
		//查询组织机构
		List<Map<String,Object>>  orgList= organizationService.selectTest(orgId);
		List<Map<String,Object>> orgResultList=new ArrayList<Map<String,Object>>();
		for(Map<String,Object> orgMap : orgList)
		{
			HashMap<String,Object> perOrgMap=new HashMap<String,Object>();
			perOrgMap.put("id", orgMap.get("id"));
			perOrgMap.put("name", orgMap.get("OrganizationName"));
			perOrgMap.put("isParent",true);
			perOrgMap.put("nocheck",true);
			orgResultList.add(perOrgMap);
		}
		
		//组织机构下的人员
		List<Map<String,Object>> userMapList = organizationService.selectUsers(orgId) ;
		if(userMapList!=null && userMapList.size()>0)
		{
			for(Map<String,Object> userMap : userMapList)
	    	{
	    		HashMap<String,Object> perUserMap=new HashMap<String,Object>();
	    		perUserMap.put("id", userMap.get("id"));
	    		perUserMap.put("name", userMap.get("ChineseName"));
	    		perUserMap.put("isParent",false);
	    		//perUserMap.put("checked",true);
	    		orgResultList.add(perUserMap);
	    	}
		}
		
	    out.print( getSerializer().formatList( orgResultList ) ) ;
	}
	
	@RequestMapping( params = "command=selectAllDatas" )
	public void selectAllDatas(@RequestParam( "data" ) String data,java.io.PrintWriter out,HttpServletRequest request ) throws UnsupportedEncodingException
	{
		 Map<String,Object> paramMap = getSerializer().parseMap( data ) ;
		 String ids=parseString(paramMap.get("idsParam"));
		 String perOrgId=request.getParameter("orgId");
		 String perOrgName = request.getParameter("orgName");
		 perOrgName=(perOrgName==null ? "" : new String( request.getParameter("orgName").getBytes( "iso-8859-1" ),"UTF-8")) ;
		 String moduleType=request.getParameter("moduleType");
		 String currentUserId=request.getParameter("currentUserId");
		 String isNeedAllOrg=request.getParameter("isNeedAllOrg");
		 if("meetingApplyReturnList".equals(moduleType))
		 {
			 selectJoinDepts(data,out,ids);
		 }else if("organization".equals(moduleType))
		 {
			 if(StringUtils.isNotBlank(perOrgId) && !"3A4E2F2B-5558-4AE6-A123-04B5407FD766".equals(perOrgId) && !("yes".equals(isNeedAllOrg)))
			 {
					//存放第二层json数据
					List<Map<String,Object>> secondOrgList=new ArrayList<Map<String,Object>>();
					HashMap<String,Object> perSecondOrgMap=new HashMap<String,Object>();
					List<Map<String,Object>> userMapList=null;
					userMapList= organizationService.selectUsers(perOrgId);
					List<Map<String,Object>> userList =new ArrayList<Map<String,Object>>();
					List<Boolean> booleanList=new ArrayList<Boolean>();
					if(userMapList!=null && userMapList.size()>0)
					{
						for(Map<String,Object> userMap : userMapList)
						{
							HashMap<String,Object> perUserMap=new HashMap<String,Object>();
							String userId=parseString(userMap.get("id"));
							perUserMap.put("id", userId);
							perUserMap.put("name", userMap.get("ChineseName"));
							boolean isEq=compareStr(ids,userId);
							if(isEq)
							{
								perUserMap.put("checked",true);
							}
							booleanList.add(isEq);
							userList.add(perUserMap);
						}
					}
					perSecondOrgMap.put("id", perOrgId);
					if(StringUtils.isBlank(perOrgName))
					{
						perSecondOrgMap.put("name",organizationService.load(perOrgId)==null ? "" :organizationService.load(perOrgId).getOrganizationName());
					}else
					{
						perSecondOrgMap.put("name",perOrgName);
					}
					
					perSecondOrgMap.put("isParent",true);
					perSecondOrgMap.put("nocheck",true);
					perSecondOrgMap.put("children",userList);
					/*if(booleanList.size()>0 && booleanList.contains(true))
					{
						perSecondOrgMap.put("open",true);
					}*/
					perSecondOrgMap.put("open",true);
					secondOrgList.add(perSecondOrgMap);
					out.print( getSerializer().formatList( secondOrgList ) ) ;
			 }else
			 {
				 	//存放第一层json数据
					List<Map<String,Object>> orgResultList=new ArrayList<Map<String,Object>>();
					
					//加载第一层节点
					List<Map<String,Object>>  orgList= organizationService.selectTest("");
					
					for(Map<String,Object> firstMap : orgList)
					{
						HashMap<String,Object> perOrgMap=new HashMap<String,Object>();
						List<Boolean> booleanFirstList=new ArrayList<Boolean>();
						
						String orgId=parseString(firstMap.get("id"));
						//分门别类的加载第一层节点的子节点
						List<Map<String,Object>> secondOrgMapList=organizationService.selectTest(orgId);//第二层 部门
						//存放第二层json数据
						List<Map<String,Object>> secondOrgList=new ArrayList<Map<String,Object>>();
						for(Map<String,Object> secondMap : secondOrgMapList)
						{
							HashMap<String,Object> perSecondOrgMap=new HashMap<String,Object>();
							String secondOrgId=parseString(secondMap.get("id"));
							
							List<Map<String,Object>> userMapList = organizationService.selectUsers(secondOrgId) ;
							List<Map<String,Object>> userList =new ArrayList<Map<String,Object>>();
							List<Boolean> booleanList=new ArrayList<Boolean>();
							if(userMapList!=null && userMapList.size()>0)
							{
								for(Map<String,Object> userMap : userMapList)
								{
									HashMap<String,Object> perUserMap=new HashMap<String,Object>();
									String userId=parseString(userMap.get("id"));
									perUserMap.put("id", userId);
									perUserMap.put("name", userMap.get("ChineseName"));
									boolean isEq=compareStr(ids,userId);
									if(isEq)
									{
										perUserMap.put("checked",true);
									}
									booleanList.add(isEq);
									userList.add(perUserMap);
								}
							}
							perSecondOrgMap.put("id", secondOrgId);
							perSecondOrgMap.put("name", secondMap.get("OrganizationName"));
							perSecondOrgMap.put("isParent",true);
							perSecondOrgMap.put("nocheck",true);
							perSecondOrgMap.put("children",userList);
							if(booleanList.contains(true))
							{
								perSecondOrgMap.put("open",true);
								booleanFirstList.add(true);
							}
							secondOrgList.add(perSecondOrgMap);
						}
						perOrgMap.put("id", orgId);
						perOrgMap.put("name", firstMap.get("OrganizationName"));
						perOrgMap.put("isParent",true);
						perOrgMap.put("nocheck",true);
						if(booleanFirstList.size()>0 && booleanFirstList.contains(true))
						{
							perOrgMap.put("open",true);
						}
						perOrgMap.put("open",true);
						perOrgMap.put("children",secondOrgList);
						orgResultList.add(perOrgMap);
						
					}
					out.print( getSerializer().formatList( orgResultList ) ) ;
			 }
		 }else if("shareSchedule".equals(moduleType))
		 {
			 	//存放第一层json数据
				List<Map<String,Object>> orgResultList=new ArrayList<Map<String,Object>>();
				
				//加载第一层节点
				List<Map<String,Object>>  orgList= organizationService.selectTest("");
				
				for(Map<String,Object> firstMap : orgList)
				{
					HashMap<String,Object> perOrgMap=new HashMap<String,Object>();
					List<Boolean> booleanFirstList=new ArrayList<Boolean>();
					
					String orgId=parseString(firstMap.get("id"));
					//分门别类的加载第一层节点的子节点
					List<Map<String,Object>> secondOrgMapList=organizationService.selectTest(orgId);//第二层 部门
					//存放第二层json数据
					List<Map<String,Object>> secondOrgList=new ArrayList<Map<String,Object>>();
					for(Map<String,Object> secondMap : secondOrgMapList)
					{
						HashMap<String,Object> perSecondOrgMap=new HashMap<String,Object>();
						String secondOrgId=parseString(secondMap.get("id"));
						
						List<Map<String,Object>> userMapList = organizationService.selectUsersForShareSchedule(secondOrgId,currentUserId) ;
						List<Map<String,Object>> userList =new ArrayList<Map<String,Object>>();
						List<Boolean> booleanList=new ArrayList<Boolean>();
						if(userMapList!=null && userMapList.size()>0)
						{
							for(Map<String,Object> userMap : userMapList)
							{
								HashMap<String,Object> perUserMap=new HashMap<String,Object>();
								String userId=parseString(userMap.get("id"));
								perUserMap.put("id", userId);
								perUserMap.put("name", userMap.get("ChineseName"));
								boolean isEq=compareStr(ids,userId);
								if(isEq)
								{
									perUserMap.put("checked",true);
								}
								booleanList.add(isEq);
								userList.add(perUserMap);
							}
						}
						perSecondOrgMap.put("id", secondOrgId);
						perSecondOrgMap.put("name", secondMap.get("OrganizationName"));
						perSecondOrgMap.put("isParent",true);
						perSecondOrgMap.put("nocheck",true);
						perSecondOrgMap.put("children",userList);
						if(booleanList.contains(true))
						{
							perSecondOrgMap.put("open",true);
							booleanFirstList.add(true);
						}
						secondOrgList.add(perSecondOrgMap);
					}
					perOrgMap.put("id", orgId);
					perOrgMap.put("name", firstMap.get("OrganizationName"));
					perOrgMap.put("isParent",true);
					perOrgMap.put("nocheck",true);
					if(booleanFirstList.size()>0 && booleanFirstList.contains(true))
					{
						perOrgMap.put("open",true);
					}
					perOrgMap.put("children",secondOrgList);
					orgResultList.add(perOrgMap);
					
				}
				out.print( getSerializer().formatList( orgResultList ) ) ;
		 
		 }
	}

	@RequestMapping( params = "command=selectUserDatas" )
	public void selectUserDatas(@RequestParam( "data" ) String data,java.io.PrintWriter out )
	{
		//存放第一层json数据
		List<Map<String,Object>> orgResultList=new ArrayList<Map<String,Object>>();
		
		//加载第一层节点
		List<Map<String,Object>>  orgList= organizationService.selectTest("");
		
		for(Map<String,Object> firstMap : orgList)
		{
			HashMap<String,Object> perOrgMap=new HashMap<String,Object>();
			String orgId=parseString(firstMap.get("id"));
			//分门别类的加载第一层节点的子节点
			List<Map<String,Object>> secondOrgMapList=organizationService.selectTest(orgId);//第二层 部门
			//存放第二层json数据
			List<Map<String,Object>> secondOrgList=new ArrayList<Map<String,Object>>();
			for(Map<String,Object> secondMap : secondOrgMapList)
			{
				HashMap<String,Object> perSecondOrgMap=new HashMap<String,Object>();
				String secondOrgId=parseString(secondMap.get("id"));
				
				List<Map<String,Object>> userMapList = organizationService.selectUsers(secondOrgId) ;
				List<Map<String,Object>> userList =new ArrayList<Map<String,Object>>();
				if(userMapList!=null && userMapList.size()>0)
				{
					for(Map<String,Object> userMap : userMapList)
					{
						HashMap<String,Object> perUserMap=new HashMap<String,Object>();
						String userId=parseString(userMap.get("id"));
						perUserMap.put("id", userId);
						perUserMap.put("name", userMap.get("ChineseName"));
						perUserMap.put("loginAccount", userMap.get("loginAccount"));
						userList.add(perUserMap);
					}
				}
				perSecondOrgMap.put("id", secondOrgId);
				perSecondOrgMap.put("name", secondMap.get("OrganizationName"));
				perSecondOrgMap.put("isParent",true);
				perSecondOrgMap.put("nocheck",false);
				perSecondOrgMap.put("children",userList);
				secondOrgList.add(perSecondOrgMap);
			}
			//针对系统管理员用户
			List<Map<String,Object>> systemList = organizationService.selectUsers(orgId) ;
			if(systemList!=null && systemList.size()>0)
			{
				for(Map<String,Object> sysUserMap : systemList)
				{
					HashMap<String,Object> adminMap=new HashMap<String,Object>();
					adminMap.put("id", sysUserMap.get("id"));
					adminMap.put("name", sysUserMap.get("ChineseName"));
					adminMap.put("loginAccount", sysUserMap.get("loginAccount"));
					adminMap.put("isParent",false);
					secondOrgList.add(adminMap);
				}
			}
			perOrgMap.put("id", orgId);
			perOrgMap.put("name", firstMap.get("OrganizationName"));
			perOrgMap.put("isParent",true);
			perOrgMap.put("nocheck",false);
			perOrgMap.put("open",true);
			perOrgMap.put("children",secondOrgList);
			orgResultList.add(perOrgMap);
		}
		out.print( getSerializer().formatList( orgResultList ) ) ;
	}
	
	/**
	 * 
	 * @param data
	 * @param out
	 */
	@RequestMapping( params = "command=selectDepartments" )
	public void selectDepartments(@RequestParam( "data" ) String data,java.io.PrintWriter out )
	{
		//存放第一层json数据
		List<Map<String,Object>> orgResultList=new ArrayList<Map<String,Object>>();
		//加载第一层节点
		List<Map<String,Object>>  orgList= organizationService.selectTest("");
		
		for(Map<String,Object> firstMap : orgList)
		{
			HashMap<String,Object> perOrgMap=new HashMap<String,Object>();
			
			String orgId=parseString(firstMap.get("id"));
			//分门别类的加载第一层节点的子节点
			List<Map<String,Object>> secondOrgMapList=organizationService.selectTest(orgId);//第二层 部门
			//存放第二层json数据
			List<Map<String,Object>> secondOrgList=new ArrayList<Map<String,Object>>();
			for(Map<String,Object> secondMap : secondOrgMapList)
			{
				HashMap<String,Object> perSecondOrgMap=new HashMap<String,Object>();
				String secondOrgId=parseString(secondMap.get("id"));
				
				perSecondOrgMap.put("id", secondOrgId);
				perSecondOrgMap.put("name", secondMap.get("OrganizationName"));
				perSecondOrgMap.put("isParent",true);
				perSecondOrgMap.put("nocheck",true);
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
		out.print( getSerializer().formatList( orgResultList ) ) ;
	}
	
	/**
	 * 
	 * @param data
	 * @param out
	 */
	@RequestMapping( params = "command=selectUserOfDepartment" )
	public void selectUserOfDepartment(HttpServletRequest request,java.io.PrintWriter out )
	{
		    String orgId=request.getParameter("orgId");
		    List<Map<String,Object>> userMapList = organizationService.selectUsers(orgId) ;
			List<Map<String,Object>> userList =new ArrayList<Map<String,Object>>();
			if(userMapList!=null && userMapList.size()>0)
			{
				for(Map<String,Object> userMap : userMapList)
				{
					HashMap<String,Object> perUserMap=new HashMap<String,Object>();
					String userId=parseString(userMap.get("id"));
					perUserMap.put("id", userId);
					perUserMap.put("name", userMap.get("ChineseName"));
					userList.add(perUserMap);
				}
			}
			out.print( getSerializer().formatList( userList ) ) ;
	}
	
	/**
	 * 查询所有可以参加会议的部门
	 * @param data
	 * @param out
	 */
	public void selectJoinDepts(String data,java.io.PrintWriter out ,String ids)
	{		 
		//存放第一层json数据
		List<Map<String,Object>> orgResultList=new ArrayList<Map<String,Object>>();
		
		//加载第一层节点
		List<Map<String,Object>>  orgList= organizationService.selectTest("");
		
		for(Map<String,Object> firstMap : orgList)
		{
			HashMap<String,Object> perOrgMap=new HashMap<String,Object>();
			List<Boolean> booleanList=new ArrayList<Boolean>();
			String orgId=parseString(firstMap.get("id"));
			//分门别类的加载第一层节点的子节点
			List<Map<String,Object>> secondOrgMapList=organizationService.selectTest(orgId);//第二层 部门
			//存放第二层json数据
			List<Map<String,Object>> secondOrgList=new ArrayList<Map<String,Object>>();
			for(Map<String,Object> secondMap : secondOrgMapList)
			{
				HashMap<String,Object> perSecondOrgMap=new HashMap<String,Object>();
				String secondOrgId=parseString(secondMap.get("id"));
				perSecondOrgMap.put("id", secondOrgId);
				perSecondOrgMap.put("isParent", true);
				perSecondOrgMap.put("name", secondMap.get("OrganizationName"));
				boolean isEq=compareStr(ids,secondOrgId);
				if(isEq)
				{
					perSecondOrgMap.put("checked",true);
				}
				booleanList.add(isEq);
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
		out.print( getSerializer().formatList( orgResultList ) ) ;
	}
	
	private boolean compareStr(String ids,String dbUserId)
	{
		if(StringUtils.isNotBlank(ids))
		{
			String[] idsAry=ids.split(",");
			for(String id : idsAry)
			{
				if(id.equals(dbUserId))
				{
					return true;
				}
			}
			
		}
		return false;
	}
	
	@RequestMapping( params = "command=selectUserDatasForActivity" )
	public void selectUserDatasForActivity(@RequestParam( "data" ) String data,java.io.PrintWriter out )
	{
		Map<String,Object> paramMap = getSerializer().parseMap( data ) ;
		String activityID=parseString(paramMap.get("activityID"));
		String activityName=parseString(paramMap.get("activityName"));
		String activityType=parseString(paramMap.get("activityType"));
		String operatorType=parseString(paramMap.get("operatorType"));
		String type=parseString(paramMap.get("type"));
		boolean isRadio = true;
		if(!activityType.equals("HDLX-C")){
			isRadio = false;
		}
		//存放第一层json数据
		List<Map<String,Object>> orgResultList=new ArrayList<Map<String,Object>>();
		
		//加载第一层节点
		List<Map<String,Object>>  orgList= organizationService.selectTest("");
		
		for(Map<String,Object> firstMap : orgList)
		{
			HashMap<String,Object> perOrgMap=new HashMap<String,Object>();
			String orgId=parseString(firstMap.get("id"));
			//分门别类的加载第一层节点的子节点
			List<Map<String,Object>> secondOrgMapList=organizationService.selectTest(orgId);//第二层 部门
			//存放第二层json数据
			List<Map<String,Object>> secondOrgList=new ArrayList<Map<String,Object>>();
			for(Map<String,Object> secondMap : secondOrgMapList)
			{
				HashMap<String,Object> perSecondOrgMap=new HashMap<String,Object>();
				String secondOrgId=parseString(secondMap.get("id"));
				
				List<Map<String,Object>> userMapList = organizationService.selectUsers(secondOrgId) ;
				List<Map<String,Object>> userList =new ArrayList<Map<String,Object>>();
				if(userMapList!=null && userMapList.size()>0)
				{
					for(Map<String,Object> userMap : userMapList)
					{
						HashMap<String,Object> perUserMap=new HashMap<String,Object>();
						String userId=parseString(userMap.get("id"));
						perUserMap.put("id", userId);
						perUserMap.put("name", userMap.get("ChineseName"));
						perUserMap.put("loginAccount", userMap.get("loginAccount"));
						perUserMap.put("activityID", activityID);
						perUserMap.put("activityName", activityName);
						perUserMap.put("activityType", activityType);
						perUserMap.put("operatorType", operatorType);
						perUserMap.put("organizationName", userMap.get("OrganizationName"));
						perUserMap.put("type", type);
						userList.add(perUserMap);
					}
				}
				perSecondOrgMap.put("id", secondOrgId);
				perSecondOrgMap.put("name", secondMap.get("OrganizationName"));
				perSecondOrgMap.put("isParent",true);
				perSecondOrgMap.put("nocheck",isRadio);
				perSecondOrgMap.put("children",userList);
				secondOrgList.add(perSecondOrgMap);
			}
			//针对系统管理员用户
			List<Map<String,Object>> systemList = organizationService.selectUsers(orgId) ;
			if(systemList!=null && systemList.size()>0)
			{
				for(Map<String,Object> sysUserMap : systemList)
				{
					HashMap<String,Object> adminMap=new HashMap<String,Object>();
					adminMap.put("id", sysUserMap.get("id"));
					adminMap.put("name", sysUserMap.get("ChineseName"));
					adminMap.put("loginAccount", sysUserMap.get("loginAccount"));
					adminMap.put("isParent",false);
					secondOrgList.add(adminMap);
				}
			}
			perOrgMap.put("id", orgId);
			perOrgMap.put("name", firstMap.get("OrganizationName"));
			perOrgMap.put("isParent",true);
			perOrgMap.put("nocheck",isRadio);
			perOrgMap.put("open",true);
			perOrgMap.put("children",secondOrgList);
			orgResultList.add(perOrgMap);
		}
		out.print( getSerializer().formatList( orgResultList ) ) ;
	}
	
    @Autowired
    private PlatformOrganizationService organizationService ;
}