package data.platform.entity ;

import java.util.Date ;

import data.framework.support.AbstractEntity ;

/**
 * 平台-角色数据权限。
 * 
 * @author wanggq
 * 
 */
public class EntityRoleDataAuthority extends AbstractEntity
{
    /**
     * 获取受控纳税主体TMIS代码。
     * @return 受控纳税主体TMIS代码
     */
    public String getTmisCodeControlledTaxFilingUnit()
    {
        return tmisCodeControlledTaxFilingUnit ;
    }

    /**
     * 设置受控纳税主体TMIS代码。
     * @param tmisCodeControlledTaxFilingUnit 受控纳税主体TMIS代码
     */
    public void setTmisCodeControlledTaxFilingUnit( String tmisCodeControlledTaxFilingUnit )
    {
        this.tmisCodeControlledTaxFilingUnit = tmisCodeControlledTaxFilingUnit ;
    }

    /**
     * 获取受控纳税主体JDE编号。
     * @return 受控纳税主体JDE编号
     */
    public String getJdeCodeControlledTaxFilingUnit()
    {
        return jdeCodeControlledTaxFilingUnit ;
    }

    /**
     * 设置受控纳税主体JDE编号。
     * @param jdeCodeControlledTaxFilingUnit 受控纳税主体JDE编号
     */
    public void setJdeCodeControlledTaxFilingUnit( String jdeCodeControlledTaxFilingUnit )
    {
        this.jdeCodeControlledTaxFilingUnit = jdeCodeControlledTaxFilingUnit ;
    }

    /**
     * 获取受控纳税主体JDE名称。
     * @return 受控纳税主体JDE名称
     */
    public String getJdeNameControlledTaxFilingUnit()
    {
        return jdeNameControlledTaxFilingUnit ;
    }

    /**
     * 设置受控纳税主体JDE名称。
     * @param jdeNameControlledTaxFilingUnit 受控纳税主体JDE名称
     */
    public void setJdeNameControlledTaxFilingUnit( String jdeNameControlledTaxFilingUnit )
    {
        this.jdeNameControlledTaxFilingUnit = jdeNameControlledTaxFilingUnit ;
    }

    /**
     * 获取受控纳税主体Hyperion Code。
     * @return 受控纳税主体Hyperion Code
     */
    public String getHyperionCodeControlledTaxFilingUnit()
    {
        return hyperionCodeControlledTaxFilingUnit ;
    }

    /**
     * 设置受控纳税主体Hyperion Code。
     * @param hyperionCodeControlledTaxFilingUnit 受控纳税主体Hyperion Code
     */
    public void setHyperionCodeControlledTaxFilingUnit( String hyperionCodeControlledTaxFilingUnit )
    {
        this.hyperionCodeControlledTaxFilingUnit = hyperionCodeControlledTaxFilingUnit ;
    }

    /**
     * 获取受控纳税主体名称。
     * @return 受控纳税主体名称
     */
    public String getControlledTaxFilingUnitName()
    {
        return controlledTaxFilingUnitName ;
    }

    /**
     * 设置受控纳税主体名称。
     * @param controlledTaxFilingUnitName 受控纳税主体名称
     */
    public void setControlledTaxFilingUnitName( String controlledTaxFilingUnitName )
    {
        this.controlledTaxFilingUnitName = controlledTaxFilingUnitName ;
    }

    /**
     * 获取受控纳税主体类型。
     * @return 受控纳税主体类型
     */
    public String getControlledTaxFilingUnitType()
    {
        return controlledTaxFilingUnitType ;
    }

    /**
     * 设置受控纳税主体类型。
     * @param controlledTaxFilingUnitType 受控纳税主体类型
     */
    public void setControlledTaxFilingUnitType( String controlledTaxFilingUnitType )
    {
        this.controlledTaxFilingUnitType = controlledTaxFilingUnitType ;
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

    private String tmisCodeControlledTaxFilingUnit ;
    private String jdeCodeControlledTaxFilingUnit ;
    private String jdeNameControlledTaxFilingUnit ;
    private String hyperionCodeControlledTaxFilingUnit ;
    private String controlledTaxFilingUnitName ;
    private String controlledTaxFilingUnitType ;
    private Date createTime ;
    private Date updateTime ;
    private static final long serialVersionUID = -5692382295619003248L ;
}