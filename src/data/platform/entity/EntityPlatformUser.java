package data.platform.entity ;

import java.util.Date ;
import java.util.List ;

import javax.xml.bind.annotation.XmlRootElement;

import data.framework.support.AbstractEntity ;

/**
 * 平台－用户实体类。
 * @author wanggq
 */
@XmlRootElement(name = "entityPlatformUser")
public class EntityPlatformUser extends AbstractEntity
{
    /**
     * 获取用户中文名称。
     * @return 用户中文名称
     */
    public String getChineseName()
    {
        return chineseName ;
    }

    /**
     * 设置用户中文名称。
     * @param chineseName 用户中文名称
     */
    public void setChineseName( String chineseName )
    {
        this.chineseName = chineseName ;
    }

    /**
     * 获取用户英文名称。
     * @return 用户英文名称
     */
    public String getEnglishName()
    {
        return englishName ;
    }

    /**
     * 设置用户英文名称。
     * @param englishName 用户英文名称
     */
    public void setEnglishName( String englishName )
    {
        this.englishName = englishName ;
    }
    
    /**
     * 获取AD账号。
     * @return AD账号
     */
    public String getAdAccount()
    {
        return adAccount ;
    }

    /**
     * 设置AD账号。
     * @param adAccount AD账号
     */
    public void setAdAccount( String adAccount )
    {
        this.adAccount = adAccount ;
    }

    /**
     * 获取TMIS登录账号。
     * @return TMIS登录账号
     */
    public String getLoginAccount()
    {
        return loginAccount ;
    }

    /**
     * 设置TMIS登录账号。
     * @param loginAccount TMIS登录账号
     */
    public void setLoginAccount( String loginAccount )
    {
        this.loginAccount = loginAccount ;
    }

    /**
     * 获取TMIS登录密码。
     * @return TMIS登录密码
     */
    public String getLoginPassword()
    {
        return loginPassword ;
    }

    /**
     * 设置TMIS登录密码。
     * @param loginPassword TMIS登录密码
     */
    public void setLoginPassword( String loginPassword )
    {
        this.loginPassword = loginPassword ;
    }
    
    /**
     * 获取所属组织机构ID。
     * @return 所属组织机构ID
     */
    public String getOrganizationId()
    {
        return organizationId ;
    }

    /**
     * 设置所属组织机构ID。
     * @param organizationId 所属组织机构ID
     */
    public void setOrganizationId( String organizationId )
    {
        this.organizationId = organizationId ;
    }
    
    /**
     * 获取用户办公电话。
     * @return 用户办公电话
     */
    public String getOfficePhone()
    {
        return officePhone ;
    }

    /**
     * 设置用户办公电话。
     * @param officePhone 用户办公电话
     */
    public void setOfficePhone( String officePhone )
    {
        this.officePhone = officePhone ;
    }

    /**
     * 获取用户手机。
     * @return 用户手机
     */
    public String getMobile()
    {
        return mobile ;
    }

    /**
     * 设置用户手机。
     * @param mobile 用户手机
     */
    public void setMobile( String mobile )
    {
        this.mobile = mobile ;
    }

    /**
     * 获取用户办公邮箱。
     * @return 用户办公邮箱
     */
    public String getOfficeMail()
    {
        return officeMail ;
    }

    /**
     * 设置用户办公邮箱。
     * @param officeMail 用户办公邮箱
     */
    public void setOfficeMail( String officeMail )
    {
        this.officeMail = officeMail ;
    }

    /**
     * 获取用户最后一次登陆时间。
     * @return 用户最后一次登陆时间
     */
    public Date getLastLoginTime()
    {
        return lastLoginTime ;
    }

    /**
     * 设置用户最后一次登陆时间。
     * @param lastLoginTime 用户最后一次登陆时间。
     */
    public void setLastLoginTime( Date lastLoginTime )
    {
        this.lastLoginTime = lastLoginTime ;
    }

    /**
     * 获取用户状态(0:停用；1：启用)。
     * @return 用户状态。
     */
    public int getStatus()
    {
        return status ;
    }

    /**
     * 设置用户状态(0:停用；1：启用)。
     * @param status 用户状态
     */
    public void setStatus( int status )
    {
        this.status = status ;
    }

    /**
     * 获取用户描述。
     * @return 用户描述
     */
    public String getRemark()
    {
        return remark ;
    }

    /**
     * 设置用户描述。
     * @param remark 用户描述。
     */
    public void setRemark( String remark )
    {
        this.remark = remark ;
    }

    /**
     * 获取用户创建时间。
     * @return 用户创建时间
     */
    public Date getCreateTime()
    {
        return createTime ;
    }

    /**
     * 设置用户创建时间。
     * @param createTime 用户创建时间
     */
    public void setCreateTime( Date createTime )
    {
        this.createTime = createTime ;
    }

    /**
     * 获取用户更新时间。
     * @return 用户更新时间
     */
    public Date getUpdateTime()
    {
        return updateTime ;
    }

    /**
     * 设置用户更新时间。
     * @param updateTime 用户更新时间
     */
    public void setUpdateTime( Date updateTime )
    {
        this.updateTime = updateTime ;
    }

    /**
     * 获取所属组织机构。
     * @return 所属组织机构
     */
    public EntityPlatformOrganization getOrganization()
    {
        return organization ;
    }

    /**
     * 设置所属组织机构。
     * @param organization 所属组织机构
     */
    public void setOrganization( EntityPlatformOrganization organization )
    {
        this.organization = organization ;
    }
    
    /**
     * 获取用户拥有角色集合。
     * @return 用户拥有角色集合
     */
    public List<EntityPlatformRole> getRoles()
    {
        return roles ;
    }

    /**
     * 设置用户拥有角色集合。
     * @param roles 用户拥有角色集合
     */
    public void setRoles( List<EntityPlatformRole> roles )
    {
        this.roles = roles ;
    }

    /**
      * 获取用户职务。
      * @return 用户职务
      */
     public String getPosition() 
     {
 		return position;
 	 }

     /**
      * 设置 用户职务。
      * @param englishName  用户职务
      */
 	public void setPosition(String position) 
 	{
 		this.position = position;
 	}
 	
 	/**
     * 获取用户排序。
     * @return 用户排序
     */
 	public int getSeqNums() 
 	{
		return seqNums;
	}

 	/**
     * 设置用户排序。
     * @param seqNums 用户排序
     */
	public void setSeqNums(int seqNums) 
	{
		this.seqNums = seqNums;
	}
	
	/**
     * 获取用户性别
     * @return 用户性别
     */
    public int getSex()
    {
		return sex;
	}
    
    /**
     * 设置用户性别
     * @param sex 用户性别
     */
	public void setSex(int sex) 
	{
		this.sex = sex;
	}

	private String chineseName ;
    private String englishName ;
    private String adAccount ;
    private String loginAccount ;
    private String loginPassword ;
    private String organizationId ;
    private String officePhone ;
    private String mobile ;
    private String officeMail ;
    private Date lastLoginTime ;
    private int status ;
    private String remark ;
    private Date createTime ;
    private Date updateTime ;
    private EntityPlatformOrganization  organization ;
    private List<EntityPlatformRole> roles ;
    private String position ;
    private int seqNums ;
    private int sex ;
	private static final long serialVersionUID = -6442770655425774883L ;
}