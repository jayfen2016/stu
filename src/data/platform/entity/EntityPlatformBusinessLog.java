package data.platform.entity ;

import java.util.Date ;

import data.framework.support.AbstractEntity ;

/**
 * 平台－业务日志实体类。
 * 
 * @author wanggq
 */
public class EntityPlatformBusinessLog extends AbstractEntity
{

    /**
     * 获取操作人中文名称。
     * @return 操作人中文名称
     */
    public String getOperatorChineseName()
    {
        return operatorChineseName ;
    }
    
    /**
     * 设置操作人中文名称。
     * @param operatorChineseName 操作人中文名称
     */
    public void setOperatorChineseName( String operatorChineseName )
    {
        this.operatorChineseName = operatorChineseName ;
    }
    
    /**
     * 获取操作人TMIS账号。
     * @return 操作人TMIS账号
     */
    public String getOperatorTMISAccount()
    {
        return operatorTMISAccount ;
    }
    
    /**
     * 设置操作人TMIS账号。
     * @param operatorTMISAccount
     */
    public void setOperatorTMISAccount( String operatorTMISAccount )
    {
        this.operatorTMISAccount = operatorTMISAccount ;
    }
    
    /**
     * 获取操作人AD账号。
     * @return 操作人AD账号
     */
    public String getOperatorADAccount()
    {
        return operatorADAccount ;
    }
    
    /**
     * 设置操作人AD账号。
     * @param operatorADAccount 操作人AD账号
     */
    public void setOperatorADAccount( String operatorADAccount )
    {
        this.operatorADAccount = operatorADAccount ;
    }
    
    /**
     * 获取操作模块。
     * @return 操作模块
     */
    public String getModule()
    {
        return module ;
    }
    
    /**
     * 设置操作模块。
     * @param module 操作模块
     */
    public void setModule( String module )
    {
        this.module = module ;
    }
    
    /**
     * 获取操作类型。
     * @return 操作类型枚举类
     */
    public BusinessLogOperateType getOperateType()
    {
        return operateType ;
    }
    
    /**
     * 设置操作类型。
     * @param operatorType 操作类型枚举类
     */
    public void setOperateType( BusinessLogOperateType operateType )
    {
        this.operateType = operateType ;
    }
    
    /**
     * 获取ip地址。
     * @return ip地址
     */
    public String getIp()
    {
        return ip ;
    }
    
    /**
     * 设置ip地址。
     * @param ip ip地址
     */
    public void setIp( String ip )
    {
        this.ip = ip ;
    }
    
    /**
     * 获取操作时间。
     * @return 操作时间
     */
    public Date getOperateTime()
    {
        return operateTime ;
    }
    
    /**
     * 设置操作时间。
     * @param operaterTime 操作时间
     */
    public void setOperateTime( Date operateTime )
    {
        this.operateTime = operateTime ;
    }
    
    /**
     * 获取操作描述。
     * @return 操作描述
     */
    public String getOperateRemark()
    {
        return operateRemark ;
    }
    
    /**
     * 设置操作描述。
     * @param operaterRemark 操作描述
     */
    public void setOperateRemark( String operateRemark )
    {
        this.operateRemark = operateRemark ;
    }
    
    /**
     * 获取创建时间。
     * @return 创建时间
     */
    public Date getCreateTime()
    {
        return createTime ;
    }
    
    /**
     * 设置创建时间。
     * @param createTime 创建时间
     */
    public void setCreateTime( Date createTime )
    {
        this.createTime = createTime ;
    }
    
    /**
     * 获取更新时间。
     * @return 更新时间
     */
    public Date getUpdateTime()
    {
        return updateTime ;
    }
    
    /**
     * 设置更新时间。
     * @param updateTime 更新时间
     */
    public void setUpdateTime( Date updateTime )
    {
        this.updateTime = updateTime ;
    }
    
    private String operatorChineseName ;
    private String operatorTMISAccount ;
    private String operatorADAccount ;
    private String module ;
    private BusinessLogOperateType operateType ;
    private String ip ;
    private Date operateTime ;
    private String operateRemark ;
    private Date createTime ;
    private Date updateTime ;
    private static final long serialVersionUID = -8438218616437229636L ;
}
