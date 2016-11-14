package data.platform.logback ;

import java.util.Date ;

import org.apache.commons.lang.StringUtils ;

import ch.qos.logback.classic.Level ;
import ch.qos.logback.classic.spi.LoggingEvent ;
import ch.qos.logback.core.AppenderBase ;
import data.framework.support.GlobalContext ;
import data.platform.entity.EntityPlatformSystemLog ;
import data.platform.service.PlatformSystemLogService ;
/**
 * 扩展logback中日志自动入库的功能。
 * 统一数据源，无需在logback.xml中配置数据源。
 * 增加过滤功能，可过滤多种级别的日志。
 * 可更改保存日志的数据库表。
 * @author wanggq
 *
 */
public class DBAppender extends AppenderBase<LoggingEvent>
{
    private PlatformSystemLogService logService ;
    
    //过滤级别（如：INFO、DEBUG等）
    private String filterLevel ;

    public String getFilterLevel()
    {
        return filterLevel ;
    }

    public void setFilterLevel( String filterLevel )
    {
        this.filterLevel = filterLevel ;
    }

    @Override
    protected void append( LoggingEvent event )
    {
        try
        {
            if( logService == null )
                logService = GlobalContext.getObject( "platformSystemLogService", PlatformSystemLogService.class ) ;
            //获取当前输出日志的级别
            Level levelObj = event.getLevel() ;
            String level = levelObj.levelStr ;
            //根据配置的日志级别来过滤日志
            if( StringUtils.isBlank( filterLevel ) || filterLevel.indexOf( level ) != -1 )
            {
                //将日志保存的指定的数据库表（可修改）
                EntityPlatformSystemLog logEntity = new EntityPlatformSystemLog() ;
                logEntity.setLogType( level ) ;
                logEntity.setRemark( event.getMessage() ) ;
                logEntity.setLogTime( new Date( event.getTimeStamp() ) ) ;
                logEntity.setCreateTime( new Date() ) ;
                logService.save( logEntity ) ;
            }
        }
        catch( Exception ex )
        {
            ex.printStackTrace() ;
        }
    }
}
