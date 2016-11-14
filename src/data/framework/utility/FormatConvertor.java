package data.framework.utility ;

import java.math.BigDecimal ;
import java.text.DateFormat ;
import java.text.ParseException ;
import java.text.SimpleDateFormat ;
import java.util.Arrays ;
import java.util.Calendar ;
import java.util.Date ;
import java.util.regex.Matcher ;
import java.util.regex.Pattern ;

import data.framework.support.Logger ;

/**
 * <p>格式转换静态工具类。</p>
 * <p>不可继承，不可实例化。</p>
 * @author wanggq
 */
public final class FormatConvertor
{
    private FormatConvertor() {}

    /**
     * 将 Object 转换成字符串。
     * @param value 输入值
     * @return 输出值，对于 null 将返回空串
     */
    public static String parseString( Object value )
    {
        if( value == null )
            return "" ;

        return value.toString() ;
    }

    /**
     * 对字符串值进行格式化输出。例如将 null 输出为空串。
     * @param value 输入值
     * @return 输出值，对于 null 将返回空串
     */
    public static String formatString( Object value )
    {
        return ( value == null ) ? "" : value.toString() ; 
    }
    
    /**
     * 将 java.lang.Object 转换成 java.lang.Boolean。
     * @param value 输入值
     * @return 输出值
     */
    public static Boolean parseBoolean( Object value )
    {
        if( value == null || value.toString().length() == 0 )
            return null ;

        return Boolean.parseBoolean( value.toString() ) ;
    }
    
    /**
     * 将 java.lang.Boolean 转换为字符串。
     * @param value 输入值
     * @return 输出值，对于 null 返回 空串
     */
    public static String formatBoolean( Boolean value )
    {
        return ( value == null ) ? "" : value.toString() ; 
    }
    
    /**
     * 根据格式化字符串将一个 java.lang.Boolean 转换为字符串。<br/>
     * 格式化字符串的格式为 "true文本|false文本"。
     * @param value 输入值
     * @param format 格式化字符串
     * @return 输出值，对于 null 返回空串
     */
    public static String formatBoolean( Boolean value, String format )
    {
        if( value == null )
            return "" ;

        if( format != null && format.length() != 0 )
        {
            String[] temp = format.split( "\\|" ) ;
            return value ? temp[0] : temp[1] ;
        }

        return value.toString() ; 
    }
    
    /**
     * 将 java.lang.Object 转换为 java.lang.Integer。
     * @param value 输入值
     * @return 输出值
     */
    public static Integer parseInteger( Object value )
    {
        if( value == null || value.toString().length() == 0 )
            return null ;

        return Integer.parseInt( value.toString() ) ;
    }
    
    /**
     * 将 java.lang.Integer 转换为字符串。
     * @param value 输入值
     * @return 输出值，将 null 转换为空串
     */
    public static String formatInteger( Integer value )
    {
        return ( value == null ) ? "" : value.toString() ; 
    }

    /**
     * 将 java.lang.Object 转换为 java.lang.Long。
     * @param value 输入值
     * @return 输出值
     */
    public static Long parseLong( Object value )
    {
        if( value == null || value.toString().length() == 0 )
            return null ;

        return Long.parseLong( value.toString() ) ;
    }
    
    /**
     * 将 java.lang.Long 转换为字符串。
     * @param value 输入值
     * @return 输出值，将 null 转换为空串
     */
    public static String formatLong( Long value )
    {
        return ( value == null ) ? "" : value.toString() ; 
    }
    
    /**
     * 将 java.lang.Object 转换为 java.lang.Double。
     * @param value 输入值
     * @return 输出值
     */
    public static Double parseDouble( Object value )
    {
        if( value == null || value.toString().length() == 0 )
            return null ;

        return Double.parseDouble( value.toString() ) ;
    }
    
    /**
     * 将 java.lang.Double 转换为字符串。
     * @param value 输入值
     * @return 输出值，将 null 转换为空串
     */
    public static String formatDouble( Double value )
    {
        return ( value == null ) ? "" : value.toString() ; 
    }
    
    /**
     * 将 java.lang.Object 转换为 java.lang.Float。
     * @param value 输入值
     * @return 输出值
     */
    public static Float parseFloat( Object value )
    {
        if( value == null || value.toString().length() == 0 )
            return null ;

        return Float.parseFloat( value.toString() ) ;
    }
    
    /**
     * 将 java.lang.Float 转换为字符串。
     * @param value 输入值
     * @return 输出值，将 null 转换为空串
     */
    public static String formatFloat( Float value )
    {
        return ( value == null ) ? "" : value.toString() ; 
    }
    
    /**
     * 将 java.lang.Object 转换为 java.math.BigDecimal。
     * @param value 输入值
     * @return 输出值
     */
    public static BigDecimal parseBigDecimal( Object value, int scale )
    {
        if( value == null || value.toString().length() == 0 )
        {
            return null ;
        }
        else
        {
            String val = parseString( value ) ;
            val = val.replaceAll( ",", "" ) ;
            return new BigDecimal( val ).setScale( scale, BigDecimal.ROUND_HALF_UP ) ;
        }
    }
    
    /**
     * 将 java.math.BigDecimal 转换为字符串。
     * @param value 输入值
     * @return 输出值，将 null 转换为空串
     */
    public static String formatBigDecimal( BigDecimal value )
    {
        return ( value == null ) ? "" : value.toString() ; 
    }

    /**
     * 将 java.lang.Object 转换为 java.lang.Date。
     * @param value 输入值
     * @return 输出值
     */
    public static Date parseDate( Object value )
    {
        if( value == null || value.toString().length() == 0 )
            return null ;
        
        try
        {
            return DefaultDateFormat.parse( value.toString() ) ;
        }
        catch( ParseException ex )
        {
            Logger.logError( ex ) ;
        }

        return null ;
    }

    /**
     * 将 java.lang.Object 转换为 java.lang.Date。
     * @param value 输入值
     * @param format 格式化字符串
     * @return 输出值
     */
    public static Date parseDate( Object value, String format )
    {
        if( value == null || value.toString().length() == 0 )
            return null ;
        
        try
        {
            return new SimpleDateFormat( format ).parse( value.toString() ) ;
        }
        catch( ParseException ex )
        {
            Logger.logError( ex ) ;
        }

        return null ;
    }
    
    /**
     * 将 java.util.Date 转换为字符串。
     * @param date 输入值
     * @return 输出值，null转换成空串
     */
    public static String formatDate( Date date )
    {
        if( date == null )
            return "" ;

        return DefaultDateFormat.format( date ) ;
    }

    /**
     * 将 java.util.Date 转换为字符串。
     * @param date 输入值
     * @param format 格式化字符串
     * @return 输出值，null转换成空串
     */
    public static String formatDate( Date date, String format )
    {
        if( date == null )
            return "" ;

        return new SimpleDateFormat( format ).format( date ) ;
    }
    
    /**
     * 根据枚举值的字符串表示返回对应的枚举值。
     * @param <T> 枚举类型
     * @param value 枚举值的字符串表示
     * @param classic 枚举类类型
     * @return 枚举值，null或空字符串返回 null
     */
    public static <T extends Enum<T>> T parseEnum( Object value, Class<T> classic )
    {
        if( value == null )
            return null ;
        String strValue = value.toString() ;
        if( strValue.length() == 0 )
            return null ;
        return Enum.valueOf( classic, strValue ) ;
    }

    /**
     * 将枚举值转换为对应的字符串表示。
     * @param value 枚举值
     * @return 枚举值的字符串表示，空值返回空字符串
     */
    public static String formatEnum( Object value )
    {
        if( value == null )
            return "" ;
        return value.toString() ;
    }

    /**
     * <p>将枚举值转换为对应的字符串表示。</p>
     * <p>格式化字符串使用 "|" 分隔的值，按照分隔顺序与枚举 ordinal 值对应。</p>
     * @param value 枚举值
     * @return 枚举值的字符串表示，空值返回空字符串
     */
    public static String formatEnum( Object value, String format )
    {
        if( value == null )
        {
            return "" ;
        }
        if( format == null || format.length() == 0 )
        {
            return value.toString() ;
        }
        try
        {
            String[] temp = format.split( "\\|" ) ;
            return temp[((Enum<?>)value).ordinal()] ;
        }
        catch( Exception ex )
        {
            return "" ;
        }
    }

    /**
     * 页面输出格式化方法。将字符串中的特殊字符用转义字符替代。
     * @param value 输入值
     * @return 输出值
     */
    public static String formatHtml( Object value )
    {
        if( value == null )
            return "" ;
        String strValue = value.toString() ;
        if( strValue.length() == 0 )
            return "" ;

        return strValue
            .replace( "&", "&amp;" )
            .replace( "<", "&lt;" )
            .replace( ">", "&gt;" )
            .replace( "'", "&#39;" )
            .replace( "\"", "&quot;" )
            .replace( System.getProperty( "line.separator" ), "<br/>" ) ;
    }

    /**
     * 去除换行回车符。将回车换行替换为空格。
     * @param value 输入值
     * @return 输出值
     */
    public static String removeLineSeparator( Object value )
    {
        return removeLineSeparator( value, " " ) ;
    }

    /**
     * 去除换行回车符。
     * @param value 输入值
     * @param separator 替换符
     * @return 输出值
     */
    public static String removeLineSeparator( Object value, String separator )
    {
        if( value == null )
            return "" ;
        String strValue = value.toString() ;
        if( strValue.length() == 0 )
            return "" ;

        return strValue
            .replace( System.getProperty( "line.separator" ), separator )
            .replace( "\r\n", separator )
            .replace( "\r", separator )
            .replace( "\n", separator ) ;
    }

    /**
     * XML输出格式化方法。将字符串中的特殊字符用转义字符替代。
     * @param value 输入值
     * @return 输出值
     */
    public static String formatXml( Object value )
    {
        if( value == null )
            return "" ;
        String strValue = value.toString() ;
        if( strValue.length() == 0 )
            return "" ;

        return strValue
            .replace( "&", "&amp;" )
            .replace( "<", "&lt;" )
            .replace( ">", "&gt;" ) ;
    }

    /**
     * <p>将长整形毫秒转换为 HH:mm:ss 格式的时间表示。</p>
     * <p>如果精度超过整形，将直接返回 毫秒+ms 格式的字符串。</p>
     * @param time long类型的毫秒数
     * @return  HH:mm:ss 格式的时间字符串
     */
    public static String formatMilliseconds( long time )
    {
        if( time > Integer.MAX_VALUE )
        {
            return time + "ms" ;
        }
        else
        {
            Calendar c = Calendar.getInstance() ;
            c.clear() ;
            c.add( Calendar.MILLISECOND, (int)time ) ;
            return DefaultDateFormat.format( c.getTime() ) ;
        }
    }

    /**
     * 生成指定长度的固定内容字符串。
     * @param length 长度
     * @param repeatChar 字符
     * @return 定长字符串
     */
    public static String repeatString( int length, char repeatChar )
    {
        char[] array = new char[length] ;
        Arrays.fill( array, repeatChar ) ;

        return new String( array ) ;
    }

    /**
     * 生成指定长度的空格字符串。
     * @param length 字符串长度
     * @return 定长空格字符串
     */
    public static String repeatString( int length )
    {
        char[] array = new char[length] ;
        Arrays.fill( array, ' ' ) ;

        return new String( array ) ;
    }
    
    /**
     * 将 Object 转换成字符串，并去掉字符串首位空格。
     * @param value 输入值
     * @return 输出去掉空格的字符串，对于 null 将返回空串
     */
    public static String trimString( Object value )
    {
        if( value == null )
            return "" ;
        return value.toString().trim() ;
    }

    /**
     * 判断字符串是否为数字。
     * @param str 字符串
     * @return true/false
     */
    public static boolean isNumeric( String str )
    { 
        Pattern pattern = Pattern.compile( "^(-?\\d+)(\\.\\d+)?$" ) ; 
        Matcher isNum = pattern.matcher( str ) ;
        return isNum.matches() ; 
    }
    
    /**
     * 判断字符串是否为百分比。
     * @param str 字符串
     * @return true/false
     */
    public static boolean isPercent( String str )
    { 
        Pattern pattern = Pattern.compile( "\\d+%$" ) ; 
        Matcher isNum = pattern.matcher( str ) ;
        return isNum.matches() ; 
    }
    
    /**
     * BigDecimal类型数据相加。
     * @param nums BigDecimal数组
     * @return 数字相加后的结果
     */
    public static BigDecimal plus( BigDecimal... nums )
    {
        BigDecimal result = new BigDecimal( "0" ) ;
        if( nums != null && nums.length > 0 )
        {
            for( BigDecimal num : nums )
            {
                if( num != null )
                    result = result.add( num ) ;
            }
        }
        return result ;
    }
    
    /**
     * BigDecimal类型数据减。
     * @param sourceNum 源数字
     * @param nums BigDecimal数组
     * @return 数字相减后的结果
     */
    public static BigDecimal subtract( BigDecimal sourceNum, BigDecimal... nums )
    {
        BigDecimal result = new BigDecimal( "0" ) ;
        if( sourceNum != null )
        {
            result = sourceNum ;
            if( nums != null && nums.length > 0 )
            {
                for( BigDecimal num : nums )
                {
                    if( num != null )
                        result = result.subtract( num ) ;
                }
            }
        }
        return result ;
    }
    
    public static final DateFormat DefaultDateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" ) ;
}