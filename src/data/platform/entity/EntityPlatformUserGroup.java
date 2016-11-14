package data.platform.entity ;

import java.util.Date ;

import data.framework.support.AbstractEntity ;

/**
 * 平台-用户组组实体类。
 * 
 * @author wanggq
 * 
 */
public class EntityPlatformUserGroup extends AbstractEntity
{
    /**
     * 获取用户组名称。
     * @return 用户组名称
     */
    public String getGroupName()
    {
        return groupName ;
    }

    /**
     * 设置用户组名称。
     * @param groupName 用户组名称
     */
    public void setGroupName( String groupName )
    {
        this.groupName = groupName ;
    }

    /**
     * 获取用户组状态(0:停用；1：启用)。
     * @return 用户组状态。
     */
    public int getStatus()
    {
        return status ;
    }

    /**
     * 设置用户组状态(0:停用；1：启用)。
     * @param status 用户组状态
     */
    public void setStatus( int status )
    {
        this.status = status ;
    }

    /**
     * 获取用户组描述。
     * @return 用户组描述
     */
    public String getRemark()
    {
        return remark ;
    }

    /**
     * 设置用户组描述。
     * @param remark 用户组描述。
     */
    public void setRemark( String remark )
    {
        this.remark = remark ;
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

    private String groupName ;
    private int status ;
    private String remark ;
    private Date createTime ;
    private Date updateTime ;
    private static final long serialVersionUID = -8391782719495640924L ;
}