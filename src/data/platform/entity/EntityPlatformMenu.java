package data.platform.entity ;

import java.util.Date ;

import data.framework.support.AbstractEntity ;

/**
 * 平台-菜单实体类。
 * 
 * @author wanggq
 * 
 */
public class EntityPlatformMenu extends AbstractEntity
{
    /**
     * 获取菜单名称。
     * @return 菜单名称
     */
    public String getMenuName()
    {
        return menuName ;
    }

    /**
     * 设置菜单名称。
     * @param menuName 菜单名称
     */
    public void setMenuName( String menuName )
    {
        this.menuName = menuName ;
    }

    /**
     * 获取菜单代码。
     * @return 菜单代码
     */
    public String getMenuCode()
    {
        return menuCode ;
    }

    /**
     * 设置菜单代码。
     * @param menuCode 菜单代码
     */
    public void setMenuCode( String menuCode )
    {
        this.menuCode = menuCode ;
    }
    
    /**
     * 获取菜单级别。
     * @return 菜单级别
     */
    public int getMenuLevel()
    {
        return menuLevel ;
    }

    /**
     * 设置菜单级别。
     * @param menuLevel 菜单级别
     */
    public void setMenuLevel( int menuLevel )
    {
        this.menuLevel = menuLevel ;
    }

    /**
     * 获取上级菜单。
     * @return 上级菜单
     */
    public String getParentId()
    {
        return parentId ;
    }

    /**
     * 设置上级菜单。
     * @param parentId 上级菜单
     */
    public void setParentId( String parentId )
    {
        this.parentId = parentId ;
    }

    /**
     * 获取菜单地址。
     * @return 菜单地址
     */
    public String getMenuAddress()
    {
        return menuAddress ;
    }

    /**
     * 设置菜单地址。
     * @param menuAddress 菜单地址
     */
    public void setMenuAddress( String menuAddress )
    {
        this.menuAddress = menuAddress ;
    }

    /**
     * 获取菜单图标样式表。
     * @return 菜单图标样式表
     */
    public String getIconClass()
    {
        return iconClass ;
    }

    /**
     * 设置菜单图标样式表
     * @param iconClass 菜单图标样式表
     */
    public void setIconClass( String iconClass )
    {
        this.iconClass = iconClass ;
    }
    
    /**
     * 获取菜单状态（0：停用；1：启用）。
     * @return 菜单状态
     */
    public int getStatus()
    {
        return status ;
    }

    /**
     * 设置菜单状态（0：停用；1：启用）。
     * @param status 菜单状态
     */
    public void setStatus( int status )
    {
        this.status = status ;
    }
    
    /**
     * 获取菜单描述。
     * @return 菜单描述
     */
    public String getRemark()
    {
        return remark ;
    }

    /**
     * 设置菜单描述。
     * @param remark 菜单描述
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

    private String menuName ;
    private String menuCode ;
    private int menuLevel ;
    private String parentId ;
    private String menuAddress ;
    private int status ;
    private String remark ;
    private String iconClass ;
    private Date createTime ;
    private Date updateTime ;
    private static final long serialVersionUID = 2173262674332766831L ;
}