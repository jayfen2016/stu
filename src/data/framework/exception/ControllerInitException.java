package data.framework.exception ;

/**
 * 平台-控制器init方法自定义异常类。
 * 
 * @author wanggq
 * 
 */
public class ControllerInitException extends Exception
{
    private static final long serialVersionUID = 5980991941020587667L ;

    public ControllerInitException()
    {
        super() ;
    }

    public ControllerInitException( String message )
    {
        super( message ) ;
    }

    public ControllerInitException( String message, Throwable cause )
    {
        super( message, cause ) ;
    }

    public ControllerInitException( Throwable cause )
    {
        super( cause ) ;
    }
}