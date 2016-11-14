package data.platform.entity ;

import java.util.Date ;

import data.framework.support.AbstractEntity ;

/**
 * 平台-部门转签人员实体类。
 * 
 * @author JohnXU
 * 
 */
public class EntityPlatformOrgSignPerson extends AbstractEntity
{
	/**
     * 获取部门ID
     * 
     * @return 部门ID
     */
	public String getOrgId() 
	{
		return orgId;
	}
	/**
     * 设置部门ID
     * 
     * @param orgId 部门ID
     */
	public void setOrgId(String orgId) 
	{
		this.orgId = orgId;
	}
	
	/**
     * 获取部门名字
     * 
     * @return 部门名字
     */
	public String getOrgName() 
	{
		return orgName;
	}
	/**
     * 设置部门名字
     * 
     * @param orgName 部门名字
     */
	public void setOrgName(String orgName)
	{
		this.orgName = orgName;
	}
	
	/**
     * 获取转签人员Id
     * 
     * @return 转签人员Id
     */
	public String getSignPersonId() 
	{
		return signPersonId;
	}
	/**
     * 设置转签人员Id
     * 
     * @param signPersonId 转签人员Id
     */
	public void setSignPersonId(String signPersonId) 
	{
		this.signPersonId = signPersonId;
	}
	
	/**
     * 获取模块类型
     * 
     * @return 模块类型
     */
	public String getModuleTypeId() 
	{
		return moduleTypeId;
	}
	/**
     * 设置模块类型
     * 
     * @param moduleTypeId 模块类型
     */
	public void setModuleTypeId(String moduleTypeId) 
	{
		this.moduleTypeId = moduleTypeId;
	}
	
	/**
     * 获取创建人标识
     * 
     * @return 创建人标识
     */
	public String getCreatorId() 
	{
		return creatorId;
	}
	/**
     * 设置创建人标识
     * 
     * @param creatorId 创建人标识
     */
	public void setCreatorId(String creatorId) 
	{
		this.creatorId = creatorId;
	}
	
	/**
     * 获取修改人标识
     * 
     * @return 修改人标识
     */
	public String getModifierId() 
	{
		return modifierId;
	}
	/**
     * 设置修改人标识
     * 
     * @param modifierId 修改人标识
     */
	public void setModifierId(String modifierId) 
	{
		this.modifierId = modifierId;
	}

	/**
     * 获取用户组创建时间。
     * @return 用户组创建时间
     */
    public Date getCreateTime()
    {
        return createTime ;
    }

    /**
     * 设置用户组创建时间。
     * @param createTime 用户组创建时间
     */
    public void setCreateTime( Date createTime )
    {
        this.createTime = createTime ;
    }

    /**
     * 获取用户组更新时间。
     * @return 用户组更新时间
     */
    public Date getUpdateTime()
    {
        return updateTime ;
    }

    /**
     * 设置用户组更新时间。
     * @param updateTime 用户组更新时间
     */
    public void setUpdateTime( Date updateTime )
    {
        this.updateTime = updateTime ;
    }

    /**
     * 获取模块类型名称。
     * @return 模块类型名称
     */
    public String getModuleTypeName() 
    {
		return moduleTypeName;
	}
    
    /**
     * 设置模块类型名称。
     * @param moduleTypeName 模块类型名称
     */
	public void setModuleTypeName(String moduleTypeName) 
	{
		this.moduleTypeName = moduleTypeName;
	}
	
	/**
     * 获取转发人员名称。
     * @return 转发人员名称
     */
	public String getSignPersonName()
	{
		return signPersonName;
	}
	
	 /**
     * 设置转发人员名称。
     * @param signPersonName 转发人员名称
     */
	public void setSignPersonName(String signPersonName)
	{
		this.signPersonName = signPersonName;
	}
	
    private String orgId ;
    private String orgName ;
	private String signPersonId ;
	private String signPersonName ;
	private String moduleTypeId ;
    private String moduleTypeName;
	private String creatorId ;
    private String modifierId ;
    private Date createTime ;
    private Date updateTime ;
	private static final long serialVersionUID = -4940788872216051060L;
}