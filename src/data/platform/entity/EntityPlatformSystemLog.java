package data.platform.entity ;

import java.util.Date ;

import data.framework.support.AbstractEntity ;

/**
 * 平台-系统日志实体类。
 * 
 * @author wanggq
 * 
 */
public class EntityPlatformSystemLog extends AbstractEntity
{
    /**
     * 获取日志输出时间。
     * @return 日志输出时间
     */
    public Date getLogTime()
    {
        return logTime ;
    }

    /**
     * 设置日志输出时间。
     * @param logTime 日志输出时间
     */
    public void setLogTime( Date logTime )
    {
        this.logTime = logTime ;
    }

    /**
     * 获取日志类型（如：INFO、DEBUG、ERROR等）。
     * @return 日志类型
     */
    public String getLogType()
    {
        return logType ;
    }

    /**
     * 设置日志类型（如：INFO、DEBUG、ERROR等）。
     * @param logType 日志类型
     */
    public void setLogType( String logType )
    {
        this.logType = logType ;
    }
    
    /**
     * 获取日志来源。
     * @return 日志来源
     */
    public String getSource()
    {
        return source ;
    }

    /**
     * 设置日志来源。
     * @param source 日志来源
     */
    public void setSource( String source )
    {
        this.source = source ;
    }

    /**
     * 获取日志描述。
     * @return 日志描述
     */
    public String getRemark()
    {
        return remark ;
    }

    /**
     * 设置日志描述。
     * @param remark 日志描述
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

    private Date logTime ;
    private String logType ;
    private String source ;
    private String remark ;
    private Date createTime ;
    private Date updateTime ;
    private static final long serialVersionUID = -5269698126786612223L ;
}