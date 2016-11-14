package data.platform.entity ;

import java.util.Date ;
import java.util.List ;

import data.framework.support.AbstractEntity ;

/**
 * 平台－角色实体类。
 * 
 * @author wanggq
 */
public class EntityPlatformRole extends AbstractEntity
{
    /**
     * 获取角色名称。
     * @return 角色名称
     */
    public String getRoleName()
    {
        return roleName ;
    }

    /**
     * 设置角色名称。
     * @param roleName 角色名称
     */
    public void setRoleName( String roleName )
    {
        this.roleName = roleName ;
    }

    /**
     * 获取所属审批角色。
     * @return 所属审批角色
     */
    public String getApprovalRole()
    {
        return approvalRole ;
    }

    /**
     * 设置所属审批角色。
     * @param approvalRole 所属审批角色
     */
    public void setApprovalRole( String approvalRole )
    {
        this.approvalRole = approvalRole ;
    }
    
    /**
     * 获取角色描述。
     * @return 角色描述
     */
    public String getRemark()
    {
        return Remark ;
    }

    /**
     * 设置角色描述。
     * @param remark 角色描述
     */
    public void setRemark( String remark )
    {
        Remark = remark ;
    }

    /**
     * 获取角色状态（0：停用；1：启用）。
     * @return 角色状态
     */
    public int getStatus()
    {
        return status ;
    }

    /**
     * 设置角色状态（0：停用；1：启用）。
     * @param status 角色状态
     */
    public void setStatus( int status )
    {
        this.status = status ;
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
     * 获取角色的功能权限集合。
     * @return 功能权限集合
     */
    public List<EntityPlatformRoleFunctionalAuthority> getFunctionalList()
    {
        return FunctionalList ;
    }

    /**
     * 设置角色的功能权限集合。
     * @param functionalList 功能权限集合
     */
    public void setFunctionalList( List<EntityPlatformRoleFunctionalAuthority> functionalList )
    {
        FunctionalList = functionalList ;
    }
    
    private String roleName ;
    private String approvalRole ;
    private String Remark ;
    private int status ;
    private Date createTime ;
    private Date updateTime ;
    private List<EntityPlatformRoleFunctionalAuthority> FunctionalList ;
    private static final long serialVersionUID = -5472676970400842459L ;
}