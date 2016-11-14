package data.framework.schedule ;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SimpleTrigger;
import org.quartz.impl.calendar.HolidayCalendar;

import data.framework.support.Logger ;

/**
 * 计划任务调度接口 "Scheduler" 的  "Quartz" 实现类。
 * @author plusir
 */
public class SchedulerQuartzImpl implements data.framework.schedule.Scheduler
{
    /**
     * 缺省的构造函数，根据一个 "Quartz Scheduler" 实例来初始化自己。
     * @param scheduler Quartz的Scheduler实现实例 
     */
    public SchedulerQuartzImpl( Scheduler scheduler )
    {
        this.scheduler = scheduler ;
        try
        {
            if( !this.scheduler.isStarted() )
            {
                this.scheduler.start() ;
            }
        }
        catch( Exception ex )
        {
            Logger.logError( ex ) ;
        }
    }

    @Override
    public String addJob( Job job, Detail initData )
    {
        return addJobHelper( job, initData, new Date(), null, -1, 1 ) ;
    }

    @Override
    public String addJob( Job job, Detail initData, Date startTime )
    {
        return addJobHelper( job, initData, startTime, null, -1, 1 ) ;
    }

    @Override
    public String addJob( Job job, Detail initData, long delay )
    {
        return addJobHelper( job, initData, new Date(), null, delay, SimpleTrigger.REPEAT_INDEFINITELY ) ;
    }

    @Override
    public String addJob( Job job, Detail initData, long delay, Date startTime )
    {
        return addJobHelper( job, initData, startTime, null, delay, SimpleTrigger.REPEAT_INDEFINITELY ) ;
    }

    @Override
    public String addJob( Job job, Detail initData, long delay, Date startTime, Date endTime )
    {
        return addJobHelper( job, initData, startTime, endTime, delay, SimpleTrigger.REPEAT_INDEFINITELY ) ;
    }

    @Override
    public String addJob( Job job, Detail initData, long delay, Date startTime, int period )
    {
        return addJobHelper( job, initData, startTime, null, delay, period ) ;
    }

    @Override
    public String addJob( Job job, Detail initData, String cronExpression )
    {
        return addJobHelper( job, initData, cronExpression ) ;
    }

    @Override
    public String addJob( Job job, Detail initData, String cronExpression, Date... excludeDate )
    {
        return addJobHelper( job, initData, cronExpression, excludeDate ) ;
    }

    @Override
    public String addJob( Job job, Detail initData, String cronExpression, Date startTime, Date endTime )
    {
        String name = UUID.randomUUID().toString() ;
        try
        {
            CronTrigger trigger = new CronTrigger(
                    name,
                    Scheduler.DEFAULT_GROUP,
                    name,
                    Scheduler.DEFAULT_GROUP,
                    startTime,
                    endTime,
                    cronExpression ) ;

            JobDetail jobDetail = new JobDetail( name, Scheduler.DEFAULT_GROUP, JobInvokingAdapter.class ) ;
            jobDetail.getJobDataMap().put( "targetObject", job ) ;

            if( initData != null )
            {
                jobDetail.getJobDataMap().put( "initData", initData.getJobDataMap() ) ;
                this.scheduler.getContext().put( name, initData.getRuntimeDataMap() ) ;
            }

            this.scheduler.scheduleJob( jobDetail, trigger );
        }
        catch( Exception ex )
        {
            Logger.logError( ex ) ;
            name = null ;
        }

        return name ;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String,Object> getContextDataMap()
    {
        try
        {
            return this.scheduler.getContext() ;
        }
        catch( Exception ex )
        {
            Logger.logError( ex ) ;
        }
        return null ;
    }

    @Override
    public void removeJob( String name )
    {
        new RemoveThread( name ).start() ;
    }

    private class RemoveThread extends Thread
    {
        public RemoveThread( String argName )
        {
            threadName = argName ;
        }

        @Override
        public void run()
        {
            try
            {
                pauseJob( threadName ) ;
                scheduler.unscheduleJob( threadName, Scheduler.DEFAULT_GROUP  ) ;
                scheduler.deleteJob( threadName, Scheduler.DEFAULT_GROUP ) ;
                scheduler.deleteCalendar( threadName ) ;
                scheduler.getContext().remove( threadName ) ;
            }
            catch( Exception ex )
            {
                Logger.logError( "移除job '" + threadName + "' 时出错！", ex ) ;
            }
        }

        private String threadName ;
    }

    @Override
    public void resumeJob( String name )
    {
        try
        {
            this.scheduler.resumeJob( name, Scheduler.DEFAULT_GROUP ) ;
            this.scheduler.resumeTrigger( name, Scheduler.DEFAULT_GROUP ) ;
        }
        catch( Exception ex )
        {
            Logger.logError( ex ) ;
        }
    }

    @Override
    public void pauseJob( String name )
    {
        try
        {
            this.scheduler.pauseTrigger( name, Scheduler.DEFAULT_GROUP ) ;
            this.scheduler.pauseJob( name, Scheduler.DEFAULT_GROUP ) ;
        }
        catch( Exception ex )
        {
            Logger.logError( ex ) ;
        }
    }

    @Override
    public void updateJob( String name, String cronExpression, Date startTime, Date endTime )
    {
        CronTrigger trigger = null ;
        try
        {
            trigger = (CronTrigger)this.scheduler.getTrigger( name, Scheduler.DEFAULT_GROUP ) ;
            trigger.setCronExpression( cronExpression ) ;
            trigger.setStartTime( startTime ) ;
            trigger.setEndTime( endTime ) ;
            trigger.setJobName( name ) ;
            trigger.setJobGroup( Scheduler.DEFAULT_GROUP ) ;
            this.scheduler.rescheduleJob( name, Scheduler.DEFAULT_GROUP, trigger ) ;
        }
        catch( Exception e )
        {
            Logger.logError( e ) ;
        }
    }

    private String addJobHelper( Job job, Detail initData, Date startTime, Date endTime, long delay, int period )
    {
        String name = UUID.randomUUID().toString() ;
    
        try
        {
            JobDetail jobDetail = new JobDetail( name, Scheduler.DEFAULT_GROUP, JobInvokingAdapter.class ) ;
            jobDetail.getJobDataMap().put( "targetObject", job ) ;

            if( initData != null )
            {
                jobDetail.getJobDataMap().put( "initData", initData.getJobDataMap() ) ;
                this.scheduler.getContext().put( name, initData.getRuntimeDataMap() ) ;
            }

            this.scheduler.scheduleJob(
                    jobDetail,
                    ( delay != -1 ? new SimpleTrigger( name, Scheduler.DEFAULT_GROUP, startTime, endTime, period, delay )
                                  : new SimpleTrigger( name, Scheduler.DEFAULT_GROUP, startTime ) )
            );
        }
        catch( Exception ex )
        {
            Logger.logError( ex ) ;
            name = null ;
        }

        return name ;
    }

    private String addJobHelper( Job job, Detail initData, String cronExpression, Date... excludeDate )
    {
        String name = UUID.randomUUID().toString() ;
        try
        {
            CronTrigger trigger = new CronTrigger( name, Scheduler.DEFAULT_GROUP, cronExpression ) ;
            if( excludeDate.length != 0 )
            {
                HolidayCalendar addCalendar = new HolidayCalendar() ;
                for( Date item : excludeDate )
                {
                    addCalendar.addExcludedDate( item ) ;
                }
                this.scheduler.addCalendar( name, addCalendar, false, false ) ;
                trigger.setCalendarName( name ) ;
            }

            JobDetail jobDetail = new JobDetail( name, Scheduler.DEFAULT_GROUP, JobInvokingAdapter.class ) ;
            jobDetail.getJobDataMap().put( "targetObject", job ) ;

            if( initData != null )
            {
                jobDetail.getJobDataMap().put( "initData", initData.getJobDataMap() ) ;
                this.scheduler.getContext().put( name, initData.getRuntimeDataMap() ) ;
            }

            this.scheduler.scheduleJob( jobDetail, trigger );
        }
        catch( Exception ex )
        {
            Logger.logError( ex ) ;
            name = null ;
        }

        return name ;
    }

    private Scheduler scheduler ;
}