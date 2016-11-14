package data.framework.support ;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

import data.framework.data.DataSerializer;
import data.framework.exception.ControllerInitException;
import data.framework.utility.DateConvertor;
import data.framework.utility.FormatConvertor;
import data.platform.authority.security.SecurityContext;

/**
 * 表现层控制器抽象基类。
 * @author wanggq
 */
public abstract class AbstractBaseController
{
    /**
     * 表现层统一的异常处理方法。
     * @param ex 异常对象
     * @param out 输出对象
     */
    @ExceptionHandler
    public void exception( Exception ex, java.io.PrintWriter out ) 
    {
        Logger.logError( ex ) ;
        if( ex instanceof ControllerInitException )
            out.print( serializer.error( "页面加载出错！" ) ) ;
        else
        {
            out.print( serializer.error( "操作出现错误！" ) ) ;
        }
    }  
    
    /**
     * 内置初始化方法。页面载入时调用。
     * @param model 页面模型
     * @param request 请求实例
     */
    @RequestMapping
    protected void initialize( ModelMap model, HttpServletRequest request ) throws ControllerInitException
    {
        try
        {
            init( model, request ) ;
        }
        catch( Exception ex )
        {
            throw new ControllerInitException( ex ) ;
        }
    }
    
    /**
     * 将阿拉伯数字日期转化成中文日期。
     * @return 中文日期
     */
    protected String getChineseDate(String dateStr)
    {
    	return DateConvertor.formatStr(dateStr);
    } 
    
    /**
     * 获取当前用户登录名。
     * @return 当前用户登录名
     */
    protected String getLoginAccount()
    {
        return SecurityContext.getPrincipal().getUsername() ;
    }

    /**
     * 由衍生类负责实现的抽象方法，在页面载入时调用。
     * @param model 页面模型
     * @param request 请求实例
     */
    protected abstract void init( ModelMap model, HttpServletRequest request ) ;

    /**
     * 将 Object 转换成字符串。
     * @param value 输入值
     * @return 输出值，对于 null 将返回空串
     */
    protected String parseString( Object value )
    {
        return FormatConvertor.parseString( value ) ;
    }
    /**
     * 对字符串值进行格式化输出。例如将 null 输出为空串。
     * @param value 输入值
     * @return 输出值，对于 null 将返回空串
     */
    protected String formatString( Object value )
    {
        return FormatConvertor.formatString( value ) ;
    }
    /**
     * 将 Object 转换成字符串，并去掉字符串首位空格。
     * @param value 输入值
     * @return 输出去掉空格的字符串，对于 null 将返回空串
     */
    protected String trimString( Object value )
    {
        return FormatConvertor.trimString( value ) ;
    }
    /**
     * 将 java.lang.Object 转换成 java.lang.Boolean。
     * @param value 输入值
     * @return 输出值
     */
    protected Boolean parseBoolean( Object value )
    {
        return FormatConvertor.parseBoolean( value ) ;
    }
    /**
     * 将 java.lang.Boolean 转换为字符串。
     * @param value 输入值
     * @return 输出值，对于 null 返回 空串
     */
    protected String formatBoolean( Boolean value )
    {
        return FormatConvertor.formatBoolean( value ) ;
    }
    /**
     * 根据格式化字符串将一个 java.lang.Boolean 转换为字符串。<br/>
     * 格式化字符串的格式为 "true文本|false文本"。
     * @param value 输入值
     * @param format 格式化字符串
     * @return 输出值，对于 null 返回空串
     */
    protected String formatBoolean( Boolean value, String format )
    {
        return FormatConvertor.formatBoolean( value, format ) ;
    }
    /**
     * 将 java.lang.Object 转换为 java.lang.Integer。
     * @param value 输入值
     * @return 输出值
     */
    protected Integer parseInteger( Object value )
    {
        return FormatConvertor.parseInteger( value ) ;
    }
    /**
     * 将 java.lang.Integer 转换为字符串。
     * @param value 输入值
     * @return 输出值，将 null 转换为空串
     */
    protected String formatInteger( Integer value )
    {
        return FormatConvertor.formatInteger( value ) ;
    }
    /**
     * 将 java.lang.Object 转换为 java.lang.Long。
     * @param value 输入值
     * @return 输出值
     */
    protected Long parseLong( Object value )
    {
        return FormatConvertor.parseLong( value ) ;
    }
    /**
     * 将 java.lang.Long 转换为字符串。
     * @param value 输入值
     * @return 输出值，将 null 转换为空串
     */
    protected String formatLong( Long value )
    {
        return FormatConvertor.formatLong( value ) ;
    }
    /**
     * 将 java.lang.Object 转换为 java.lang.Double。
     * @param value 输入值
     * @return 输出值
     */
    protected Double parseDouble( Object value )
    {
        return FormatConvertor.parseDouble( value ) ;
    }
    /**
     * 将 java.lang.Double 转换为字符串。
     * @param value 输入值
     * @return 输出值，将 null 转换为空串
     */
    protected String formatDouble( Double value )
    {
        return FormatConvertor.formatDouble( value ) ;
    }
    /**
     * 将 java.lang.Object 转换为 java.math.BigDecimal。
     * @param value 输入值
     * @return 输出值
     */
    public BigDecimal parseBigDecimal( Object value, int scale )
    {
        return FormatConvertor.parseBigDecimal( value, scale ) ;
    }
    /**
     * 将 java.math.BigDecimal 转换为字符串。
     * @param value 输入值
     * @return 输出值，将 null 转换为空串
     */
    public String formatBigDecimal( BigDecimal value )
    {
        return FormatConvertor.formatBigDecimal( value ) ;
    }
    /**
     * 将 java.lang.Object 转换为 java.lang.Float。
     * @param value 输入值
     * @return 输出值
     */
    protected Float parseFloat( Object value )
    {
        return FormatConvertor.parseFloat( value ) ;
    }
    /**
     * 将 java.lang.Float 转换为字符串。
     * @param value 输入值
     * @return 输出值，将 null 转换为空串
     */
    protected String formatFloat( Float value )
    {
        return FormatConvertor.formatFloat( value ) ;
    }
    /**
     * 将 java.lang.Object 转换为 java.lang.Date。
     * @param value 输入值
     * @return 输出值
     */
    protected Date parseDate( Object value )
    {
        return FormatConvertor.parseDate( value ) ;
    }
    
    /**
     * 将 java.lang.Object 转换为 java.lang.Date。
     * @param value 输入值
     * @param format 格式化字符串
     * @return 输出值
     */
    protected Date parseDate( Object value, String format )
    {
        return FormatConvertor.parseDate( value, format ) ;
    }

    /**
     * 将 java.util.Date 转换为字符串。
     * @param date 输入值
     * @return 输出值，null转换成空串
     */
    protected String formatDate( Date date )
    {
        return FormatConvertor.formatDate( date ) ;
    }

    /**
     * 将 java.util.Date 转换为字符串。
     * @param date 输入值
     * @param format 格式化字符串
     * @return 输出值，null转换成空串
     */
    protected String formatDate( Date date, String format )
    {
        return FormatConvertor.formatDate( date, format ) ;
    }

    /**
     * 根据枚举值的字符串表示返回对应的枚举值。
     * @param <T> 枚举类型
     * @param value 枚举值的字符串表示
     * @param classic 枚举类类型
     * @return 枚举值，null或空字符串返回 null
     */
    protected <T extends Enum<T>> T parseEnum( Object value, Class<T> classic )
    {
        return FormatConvertor.parseEnum( value, classic ) ;
    }

    /**
     * 将枚举值转换为对应的字符串表示。
     * @param value 枚举值
     * @return 枚举值的字符串表示，空值返回空字符串
     */
    protected String formatEnum( Object value )
    {
        return FormatConvertor.formatEnum( value ) ;
    }

    /**
     * <p>将枚举值转换为对应的字符串表示。</p>
     * <p>格式化字符串使用 "|" 分隔的值，按照分隔顺序与枚举 ordinal 值对应。</p>
     * @param value 枚举值
     * @return 枚举值的字符串表示，空值返回空字符串
     */
    protected String formatEnum( Object value, String format )
    {
        return FormatConvertor.formatEnum( value, format ) ;
    }

    /**
     * 页面输出格式化方法。将字符串中的特殊字符用转义字符替代。
     * @param value 输入值
     * @return 输出值
     */
    protected String formatHtml( Object value )
    {
        return FormatConvertor.formatHtml( value ) ;
    }

    /**
     * 对多行字符串值进行格式化输出。替换换行符为 &amp;nbsp;。
     * @param value 输入值
     * @return 输出值，对于 null 将返回空串
     */
    protected String formatMultiLineString( Object value )
    {
        return removeLineSeparator( formatString( value ) ) ;
    }
    /**
     * 对多行字符串值进行格式化输出。替换换行符为 &amp;nbsp;。
     * @param value 输入值
     * @param separator 替换符
     * @return 输出值，对于 null 将返回空串
     */
    protected String formatMultiLineString( Object value, String separator )
    {
        return removeLineSeparator( formatString( value ), separator ) ;
    }

    /**
     * 去除换行回车符。
     * @param value 输入值
     * @return 输出值
     */
    protected String removeLineSeparator( Object value )
    {
        return FormatConvertor.removeLineSeparator( value ) ;
    }

    /**
     * 去除换行回车符。
     * @param value 输入值
     * @param separator 替换符
     * @return 输出值
     */
    protected String removeLineSeparator( Object value, String separator )
    {
        return FormatConvertor.removeLineSeparator( value, separator ) ;
    }

    /**
     * 获取序列化接口的实现实例。
     * @return 序列化接口的实现实例
     */
    protected DataSerializer getSerializer()
    {
        return serializer ;
    }

    /**
     * 获取日志输出器。
     * @return 日志输出器
     */
    protected Logger getLogger()
    {
        return this.logger ;
    }
    
    protected String convert(String dataOne,String dataTwo,String symbolMark)
    {
    	String result=" ";
    	if(StringUtils.isNotBlank(dataOne))
    	{
    		result=dataOne;
    		if(StringUtils.isNotBlank(dataTwo))
    		{
    			result=result+symbolMark+dataTwo;
    		}
    	}else
    	{
    		if(StringUtils.isNotBlank(dataTwo))
    		{
    			result=dataTwo;
    		}
    	}
    	return result;
    }
    
    /**
     * 随机产生十六进制颜色代码。
     * @return 十六进制颜色代码
     */
    protected String randomGenerationHexColorCode()
    {  
		  String r,g,b;  
		  Random random = new Random();  
		  r = Integer.toHexString(random.nextInt(256)).toUpperCase();  
		  g = Integer.toHexString(random.nextInt(256)).toUpperCase();  
		  b = Integer.toHexString(random.nextInt(256)).toUpperCase();  
		  r = r.length()==1 ? "0" + r : r ;  
		  g = g.length()==1 ? "0" + g : g ;  
		  b = b.length()==1 ? "0" + b : b ;  
		  return r+g+b;  
	}
    
    @Autowired
    @Value("#{implementsMap['dataSerializer']}")
    private DataSerializer serializer ;
    private Logger logger = Logger.getLogger( getClass() ) ;
}