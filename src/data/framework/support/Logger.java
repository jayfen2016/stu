package data.framework.support ;

import java.text.MessageFormat ;

import org.slf4j.LoggerFactory ;

/**
 * <p>系统日志类，用于输出系统级别的日志信息。</p>
 * <p><b>注意：业务级别的日志不建议在该类上进行扩展。</b></p>
 * @author wanggq
 */
public final class Logger
{
    private Logger( Class<?> clazz )
    {
        log = LoggerFactory.getLogger( clazz ) ;
    }

    private Logger( String logName )
    {
        log = LoggerFactory.getLogger( logName ) ;
    }

    /**
     * 根据类信息获取日志输出器实例。
     * @param clazz 类信息
     * @return 日志输出器实例
     */
    public static Logger getLogger( Class<?> clazz )
    {
        return new Logger( clazz ) ;
    }

    /**
     * 根据日志输出器名称获取日志输出器实例。
     * @param logName 日志输出器名称
     * @return 日志输出器实例
     */
    public static Logger getLogger( String logName )
    {
        return new Logger( logName ) ;
    }

    /**
     * 记录错误日志。系统中所有被 try 掉的日志，均应该调用该方法进行统一记录。
     * @param ex 异常实例
     */
    public static void logError( Exception ex )
    {
        logError.error( "系统异常", ex ) ;
        ex.printStackTrace() ;
    }

    /**
     * 记录错误日志。系统中所有被 try 掉的日志，均应该调用该方法进行统一记录。
     * @param ex 异常实例
     */
    public static void logError( Throwable ex )
    {
        logError.error( "系统异常", ex ) ;
        ex.printStackTrace() ;
    }

    /**
     * 记录错误日志。系统中所有被 try 掉的日志，均应该调用该方法进行统一记录。
     * @param message 提示消息
     * @param ex 异常实例
     */
    public static void logError( String message, Exception ex )
    {
        StringBuffer sb = new StringBuffer() ;
        sb.append( message ) ;
        sb.append( sp ) ;
        if( ex != null )
        {
            for( StackTraceElement ste : ex.getStackTrace() )
            {
                sb.append( ste.toString() ) ;
                sb.append( sp ) ;
            }
            logError.error( sb.toString() ) ;
    
            ex.printStackTrace() ;
        }
    }

    /**
     * 记录错误日志。系统中所有被 try 掉的日志，均应该调用该方法进行统一记录。
     * @param message 提示消息
     * @param ex 异常实例
     */
    public static void logError( String message, Throwable ex )
    {
        StringBuffer sb = new StringBuffer() ;
        sb.append( message ) ;
        sb.append( sp ) ;
        if( ex != null )
        {
            for( StackTraceElement ste : ex.getStackTrace() )
            {
                sb.append( ste.toString() ) ;
                sb.append( sp ) ;
            }
            logError.error( sb.toString() ) ;
    
            ex.printStackTrace() ;
        }
    }

    /**
     * 输出 debug 级别的日志。
     * @param message 日志内容
     */
    public void debug( String message )
    {
        log.debug( message ) ;
    }
    /**
     * 输出 debug 级别的日志。
     * @param message 日志内容
     * @param args 消息格式化信息替换参数
     */
    public void debug( String message, Object... args )
    {
        log.debug( MessageFormat.format( message, args ) ) ;
    }

    /**
     * 输出 info 级别的日志。
     * @param message 日志内容
     */
    public void info( String message )
    {
        log.info( message ) ;
    }
    /**
     * 输出 info 级别的日志。
     * @param message 日志内容
     * @param args 消息格式化信息替换参数
     */
    public void info( String message, Object... args )
    {
        log.info( MessageFormat.format( message, args ) ) ;
    }

    /**
     * 输出 trace 级别的日志。
     * @param message 日志内容
     */
    public void trace( String message )
    {
        log.trace( message ) ;
    }
    /**
     * 输出 trace 级别的日志。
     * @param message 日志内容
     * @param args 消息格式化信息替换参数
     */
    public void trace( String message, Object... args )
    {
        log.trace( MessageFormat.format( message, args ) ) ;
    }

    /**
     * 输出 warn 级别的日志。
     * @param message 日志内容
     */
    public void warn( String message )
    {
        log.warn( message ) ;
    }
    /**
     * 输出 warn 级别的日志。
     * @param message 日志内容
     * @param args 消息格式化信息替换参数
     */
    public void warn( String message, Object... args )
    {
        log.warn( MessageFormat.format( message, args ) ) ;
    }

    /**
     * 输出 error 级别的日志。
     * @param message 日志内容
     */
    public void error( String message )
    {
        log.error( message ) ;
    }
    /**
     * 输出 error 级别的日志。
     * @param message 日志内容
     * @param args 消息格式化信息替换参数
     */
    public void error( String message, Object... args )
    {
        log.error( MessageFormat.format( message, args ) ) ;
    }

    private org.slf4j.Logger log = null ;
    private static org.slf4j.Logger logError = org.slf4j.LoggerFactory.getLogger( "ERROR" ) ;
    private static String sp = System.getProperty( "line.separator" ) ;
}