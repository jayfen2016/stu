package data.framework.validation ;

import java.io.Serializable ;

/**
 * 字段验证错误信息类。
 * @author wanggq
 *
 */
public class FieldError implements Serializable
{
    public FieldError()
    {
    }

    public FieldError( String field, String message )
    {
        this.field = field ;
        this.message = message ;
    }

    public String getField()
    {
        return field ;
    }

    public void setField( String field )
    {
        this.field = field ;
    }

    public String getMessage()
    {
        return message ;
    }

    public void setMessage( String message )
    {
        this.message = message ;
    }
    
    private String field ;
    private String message ;
    private static final long serialVersionUID = 2992984525506720353L ;
}