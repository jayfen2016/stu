package data.framework.schedule ;

import java.io.Serializable ;
import java.util.Date ;
import java.util.Map ;

/**
 * 调度上下文类。负责在每次任务执行间传递信息。
 * @author wanggq
 */
public final class Context
{
    /**
     * 用相关信息初始化一个 "data.framework.schedule.Context" 实例。
     * @param name 当前任务的名称
     * @param fireTime 当前任务的执行时间
     * @param nextFireTime 下次执行时间
     * @param previousFireTime 上次执行时间
     * @param jobData 任务持久化数据映射集合
     * @param runtimeData 任务运行时数据映射集合
     * @param contextData 全局上下文数据映射集合
     */
    public Context( String name, Date fireTime, Date nextFireTime, Date previousFireTime, Map<String,Serializable> jobData, Map<String,Object> runtimeData, Map<String,Object> contextData )
    {
        this.name = name ;
        this.fireTime = fireTime ;
        this.nextFireTime = nextFireTime ;
        this.previousFireTime = previousFireTime ;
        this.jobData = jobData ;
        this.runtimeData = runtimeData ;
        this.contextData = contextData ;
    }

    /**
     * 取得当前任务的名称。
     * @return name 任务名称
     */
    public String getName()
    {
        return this.name ;
    }

    /**
     * 获取当前任务的执行时间。
     * @return 当前任务的执行时间
     */
    public Date getFireTime()
    {
        return fireTime ;
    }

    /**
     * 获取下次执行时间。
     * @return 下次执行时间
     */
    public Date getNextFireTime()
    {
        return nextFireTime ;
    }

    /**
     * 获取上次执行时间。
     * @return 上次执行时间
     */
    public Date getPreviousFireTime()
    {
        return previousFireTime ;
    }

    /**
     * <p>获取任务持久化数据映射集合。</p>
     * <p>值可以在每次任务执行时更改以便传递给下一次任务执行， 并能够进行持久化。</p>
     * @return 获取任务持久化数据映射集合
     */
    public Map<String,Serializable> getJobDataMap()
    {
        return jobData ;
    }

    /**
     * <p>获取任务运行时数据映射集合。</p>
     * <p>值可以在每次任务执行时更改以便传递给下一次任务 执行， 不能持久化。</p>
     * @return 获取任务运行时数据映射集合
     */
    public Map<String,Object> getRuntimeDataMap()
    {
        return runtimeData ;
    }

    /**
     * <p>获取全局上下文数据映射集合。</p>
     * <p>值不能更改，不能持久化。通过 "data.framework.schedule.Scheduler" 接口的 "getContextDataMap" 方法来维护。</p>
     * @return 全局上下文数据映射集合
     */
    public Map<String,Object> getContextDataMap()
    {
        return contextData ;
    }

    private String name ;
    private Date fireTime ;
    private Date nextFireTime ;
    private Date previousFireTime ;
    private Map<String,Serializable> jobData ;
    private Map<String,Object> contextData ;
    private Map<String,Object> runtimeData ;
}