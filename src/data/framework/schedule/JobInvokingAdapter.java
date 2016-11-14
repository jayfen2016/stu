package data.framework.schedule ;

import java.io.Serializable ;
import java.util.Map ;

import org.quartz.JobDataMap ;
import org.quartz.JobExecutionContext ;
import org.quartz.JobExecutionException ;
import org.quartz.SchedulerContext ;

import data.framework.support.Logger ;

/**
 * "org.quartz.StatefulJob" 接口的包装类。
 * @author wanggq
 */
public class JobInvokingAdapter implements org.quartz.Job
{
    /**
     * 默认的无参构造方法。
     */
    public JobInvokingAdapter() {}

    @Override
    @SuppressWarnings("unchecked")
    public void execute( JobExecutionContext context ) throws JobExecutionException
    {
        try
        {
            JobDataMap jobDataMap = context.getJobDetail().getJobDataMap() ;
            Map<String,Serializable> initData = (Map<String,Serializable>)jobDataMap.get( "initData" ) ;
            Job invokerJob = (Job)jobDataMap.get( "targetObject" ) ;

            SchedulerContext serContext = context.getScheduler().getContext() ;

            Context scheduleContext = new Context(
                    context.getJobDetail().getName(),
                    context.getFireTime(),
                    context.getNextFireTime(),
                    context.getPreviousFireTime(),
                    initData,
                    (Map<String,Object>)serContext.get( context.getJobDetail().getName() ),
                    serContext ) ;

            invokerJob.execute( scheduleContext ) ;
            jobDataMap.put( "initData", initData ) ;
        }
        catch( Exception ex )
        {
            Logger.logError( ex ) ;
        }
    }
}