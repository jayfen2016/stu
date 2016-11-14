package data.platform.entity;

import java.util.Date ;

import data.framework.support.AbstractEntity ;

/**
 * 平台-角色功能权限。
 * @author wanggq
 *
 */
public class EntityPlatformRoleFunctionalAuthority extends AbstractEntity
{
    /**
     * 获取受控菜单ID。
     * @return 受控菜单ID
     */
    public String getControlledMenuID()
    {
        return controlledMenuID ;
    }

    /**
     * 设置受控菜单ID。
     * @param controlledMenuID 受控菜单ID
     */
    public void setControlledMenuID( String controlledMenuID )
    {
        this.controlledMenuID = controlledMenuID ;
    }

    /**
     * 获取受控按钮代码。
     * @return 受控按钮代码
     */
    public String getControlledButtonCode()
    {
        return controlledButtonCode ;
    }

    /**
     * 设置受控按钮代码。
     * @param controlledButtonCode 受控按钮代码
     */
    public void setControlledButtonCode( String controlledButtonCode )
    {
        this.controlledButtonCode = controlledButtonCode ;
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

    private String controlledMenuID ;
    private String controlledButtonCode ;
    private Date createTime ;
    private Date updateTime ;
    private static final long serialVersionUID = 5395491198425567302L ;
}