package data.platform.entity ;

import java.util.Date ;

import data.framework.support.AbstractEntity ;

/**
 * 平台-组织机构实体类。
 * 
 * @author wanggq
 * 
 */
public class EntityPlatformOrganization extends AbstractEntity
{
    /**
     * 获取组织机构名称。
     * @return 组织机构名称
     */
    public String getOrganizationName()
    {
        return organizationName ;
    }

    /**
     * 设置组织机构名称。
     * @param organizationName 组织机构名称
     */
    public void setOrganizationName( String organizationName )
    {
        this.organizationName = organizationName ;
    }

    /**
     * 获取组织机构代码。
     * @return 组织机构代码
     */
    public String getOrganizationCode()
    {
        return organizationCode ;
    }

    /**
     * 设置组织机构代码。
     * @param organizationCode 组织机构代码
     */
    public void setOrganizationCode( String organizationCode )
    {
        this.organizationCode = organizationCode ;
    }

    /**
     * 获取父级组织机构对象。
     * @return 父级组织机构对象
     */
    public String getParentId()
    {
        return parentId ;
    }

    /**
     * 设置父级组织机构对象.
     * @param parent 父级组织机构对象
     */
    public void setParentId( String parentId )
    {
        this.parentId = parentId ;
    }

    /**
     * 获取组织机构描述。
     * @return 组织机构描述
     */
    public String getRemark()
    {
        return remark ;
    }

    /**
     * 设置组织机构描述。
     * @param remark 组织机构描述
     */
    public void setRemark( String remark )
    {
        this.remark = remark ;
    }

    /**
     * 获取组织机构级别。
     * @return 组织机构级别
     */
    public int getOrgLevel()
    {
        return orgLevel ;
    }

    /**
     * 设置组织机构级别。
     * @param orgLevel 组织机构级别
     */
    public void setOrgLevel( int orgLevel )
    {
        this.orgLevel = orgLevel ;
    }
    
    /**
     * 获取组织机构创建时间。
     * @return 组织机构创建时间
     */
    public Date getCreateTime()
    {
        return createTime ;
    }

    /**
     * 设置组织机构创建时间。
     * @param createTime 组织机构创建时间
     */
    public void setCreateTime( Date createTime )
    {
        this.createTime = createTime ;
    }

    /**
     * 获取组织机构更新时间。
     * @return 组织机构更新时间
     */
    public Date getUpdateTime()
    {
        return updateTime ;
    }

    /**
     * 设置组织机构更新时间。
     * @param updateTime 组织机构更新时间
     */
    public void setUpdateTime( Date updateTime )
    {
        this.updateTime = updateTime ;
    }

    private String organizationName ;
    private String organizationCode ;
    private String parentId ;
    private String remark ;
    private int orgLevel ;
    private Date createTime ;
    private Date updateTime ;
    private static final long serialVersionUID = 3884461055507762907L ;
}