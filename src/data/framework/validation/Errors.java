package data.framework.validation;

import java.io.Serializable ;
import java.util.ArrayList ;
import java.util.List ;

import data.framework.validation.FieldError ;
/**
 * 验证错误信息类。
 * @author wanggq
 *
 */
public class Errors implements Serializable
{
    public List<FieldError> getValidateErrors()
    {
        return validateErrors ;
    }

    public int getStatus()
    {
        return status ;
    }

    public void setStatus( int status )
    {
        this.status = status ;
    }

    /**
     * 添加错误信息。
     * @param field 错误信息字段
     * @param message 错误消息
     */
    public void addErrors( String field, String message )
    {
        validateErrors.add( new FieldError( field, message ) ) ;
    }
    
    /**
     * 验证对象后是否有错。
     * @return 是否有错误
     */
    public boolean hasErrors() 
    {
        return !this.validateErrors.isEmpty() ;
    }
    
    private int status = 0 ;
    private final List<FieldError> validateErrors = new ArrayList<FieldError>() ;
    private static final long serialVersionUID = 1618314296847554560L ;
}