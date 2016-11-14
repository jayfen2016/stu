package data.framework.schedule ;

import java.util.Date ;
import java.util.Map ;

/**
 * 计划任务调度接口。
 * @author wanggq
 */
public interface Scheduler
{
    /**
     * 立即执行调度任务，次数为1。
     * @param job 要执行的任务
     * @param initData 初始化该任务的初始信息，如果无初始化信息，可以传递null。
     * @return 添加的任务的唯一名称
     */
    public String addJob( Job job, Detail initData ) ;

    /**
     * 在 "startTime" 时执行调度一次。
     * @param job 要执行的任务
     * @param initData 初始化该任务的初始信息，如果无初始化信息，可以传递null。
     * @param startTime 调度开始时间
     * @return 添加的任务的唯一名称
     */
    public String addJob( Job job, Detail initData, Date startTime ) ;

    /**
     * 添加任务并立即开始执行调度，除非显示停止该调度，否则将无限次执行。
     * @param job 要执行的任务
     * @param initData 初始化该任务的初始信息，如果无初始化信息，可以传递null。
     * @param delay 执行间隔
     * @return 添加的任务的唯一名称
     */
    public String addJob( Job job, Detail initData, long delay ) ;

    /**
     * 添加任务并在 "startTime" 指定的时间开始执行调度，除非手动停止该调度，否则将无限次执行。
     * @param job 要执行的任务
     * @param initData 初始化该任务的初始信息，如果无初始化信息，可以传递null。
     * @param delay 执行间隔
     * @param startTime 调度开始时间
     * @return 添加的任务的唯一名称
     */
    public String addJob( Job job, Detail initData, long delay, Date startTime ) ;

    /**
     * 添加任务并在 "startTime" 指定的时间开始执行调度，在 "endTime" 指定的时间结束调度。
     * @param job 要执行的任务
     * @param initData 初始化该任务的初始信息，如果无初始化信息，可以传递null。
     * @param delay 执行间隔
     * @param startTime 调度开始时间
     * @param endTime 调度结束时间
     * @return 添加的任务的唯一名称
     */
    public String addJob( Job job, Detail initData, long delay, Date startTime, Date endTime ) ;

    /**
     * 添加任务并在 "startTime" 指定的时间开始执行调度，执行 "period" 次后结束调度。
     * @param job 要执行的任务
     * @param initData 初始化该任务的初始信息，如果无初始化信息，可以传递null。
     * @param delay 执行间隔
     * @param startTime 调度开始时间
     * @param period 调度执行次数
     * @return 添加的任务的唯一名称
     */
    public String addJob( Job job, Detail initData, long delay, Date startTime, int period ) ;

    /**
     * 根据 "cron" 表达式来执行调度任务。
     * @param job 要执行的任务
     * @param initData 初始化该任务的初始信息，如果无初始化信息，可以传递null。
     * @param cronExpression Cron表达式
     * @return 添加的任务的唯一名称
     */
    public String addJob( Job job, Detail initData, String cronExpression ) ;

    /**
     * 根据 "cron" 表达式来执行调度任务。
     * @param job 要执行的任务
     * @param initData 初始化该任务的初始信息，如果无初始化信息，可以传递null。
     * @param cronExpression Cron表达式
     * @param excludeDate 排除时间集合
     * @return 添加的任务的唯一名称
     */
    public String addJob( Job job, Detail initData, String cronExpression, Date... excludeDate ) ;

    /**
     * 根据 "cron" 表达式来执行调度任务。
     * @param job 要执行的任务
     * @param initData 初始化该任务的初始信息，如果无初始化信息，可以传递null。
     * @param cronExpression Cron表达式
     * @param startTime 任务起始时间
     * @param endTime 任务结束时间
     * @return 添加的任务的唯一名称
     */
    public String addJob( Job job, Detail initData, String cronExpression, Date startTime, Date endTime ) ;

    /**
     * 暂停一个计划任务的执行。
     * @param name 任务名称
     */
    public void pauseJob( String name ) ;

    /**
     * 更新任务。
     * @param name 任务名称
     * @param cronExpression cron表达式
     * @param startTime 开始时间
     * @param endTime 结束时间
     */
    public void updateJob( String name, String cronExpression, Date startTime, Date endTime ) ;

    /**
     * 恢复一个暂停的计划任务。
     * @param name 任务名称
     */
    public void resumeJob( String name ) ;

    /**
     * 移除一个计划任务。
     * @param name 任务名称
     */
    public void removeJob( String name ) ;

    /**
     * 获取全局上下文数据映射集合。
     * @return 全局上下文数据映射集合
     */
    public Map<String,Object> getContextDataMap() ;
}