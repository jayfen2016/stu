package data.framework.support;

import data.framework.validation.Errors ;
/**
 * 后端验证抽象基类，此类继承表现层控制器抽象基类。
 * 需要后端校验时，继承此基类，并实现validate的抽象方法。
 * @author wanggq
 *
 */
public abstract class AbstractValidatorController extends AbstractBaseController
{
    /**
     * 后端校验公共方法，需手动调用。
     * @param obj 要校验的对象
     * @param out 输出对象
     */
    public Errors validate( Object obj, java.io.PrintWriter out )
    {
        if( obj == null )
            throw new NullPointerException( "需要校验的对象为null！" ) ;
        Errors errors = new Errors() ;
        validate( obj, errors ) ;
        return errors ;
    }
    
    /**
     * 由衍生类负责实现的抽象方法，在校验时使用。
     * @param obj 要校验的对象
     * @param errors 验证错误信息对象
     */
    protected abstract void validate( Object obj, Errors errors ) ;
}