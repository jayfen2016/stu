package data.framework.schedule ;

import java.io.Serializable ;

/**
 * <p>计划任务调度 "任务" 回调接口。</p>
 * <p>所有在 "scheduler" 中调度的任务必须实现该接口，该接口继承于 "java.io.Serializable" 接口，即任务必须是能序列化的。</p>
 *
 * @author wanggq
 */
public interface Job extends Serializable
{
    /**
     * 任务回调方法。
     * @param context 执行上下文对象
     */
    public void execute( Context context ) ;
}