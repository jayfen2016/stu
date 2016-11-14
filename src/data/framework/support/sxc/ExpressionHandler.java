package data.framework.support.sxc ;

import java.math.BigDecimal ;
import java.util.Calendar ;
import java.util.List ;
import java.util.Map ;
import java.util.Map.Entry ;
import java.util.Set ;

import neu.sxc.expression.Expression ;
import neu.sxc.expression.syntax.function.Function ;
import neu.sxc.expression.tokens.Valuable ;

/**
 * 规则表达式处理类。
 * 
 * @author wanggq
 */
public class ExpressionHandler
{
    private Expression expressionObj = new Expression() ;
    
    public ExpressionHandler( Function[] functions )
    {
        if( functions != null && functions.length > 0 )
        {
            for( Function function : functions )
                expressionObj.addFunction( function ) ;
        }
    }
    
    public void lexicalAnalysis( String expression )
    {
        //设置表达式
        expressionObj.setExpression( expression ) ;
        //对表达式进行词法分析
        expressionObj.lexicalAnalysis() ;
    }
    
    /**
     * 获取表达式中的所有变量。
     * @return 表达式中的所有变量集合。
     */
    public Set<String> getVariableNames()
    {
        return expressionObj.getVariableNames() ;
    }
    
    /**
     * 获取执行表达式后返回的Object类型结果，可使用instanceof判断具体类型。
     * @return 执行表达式后返回的Object类型结果。
     */
    public Object getObjectResult()
    {
        Valuable result = execute( null ) ;
        return result.getValue() ;
    }
    
    /**
     * 获取执行表达式后返回的字符串类型结果。
     * @return 执行表达式后返回的字符串类型结果。
     */
    public String getStringResult()
    {
        Valuable result = execute( null ) ;
        return result.getStringValue() ;
    }
    
    /**
     * 获取执行表达式后返回的Character类型结果。
     * @return 执行表达式后返回的Character类型结果。
     */
    public Character getCharResult()
    {
        Valuable result = execute( null  ) ;
        return result.getCharValue() ;
    }
    
    /**
     * 获取执行表达式后返回的BigDecimal类型结果。
     * @return 执行表达式后返回的BigDecimal类型结果。
     */
    public BigDecimal getBigDecimalResult()
    {
        Valuable result = execute( null ) ;
        return result.getNumberValue() ;
    }
    
    /**
     * 获取执行表达式后返回的布尔类型结果。
     * @return 执行表达式后返回的布尔类型结果。
     */
    public Boolean getBooleanResult()
    {
        Valuable result = execute( null ) ;
        return result.getBooleanValue() ;
    }
    
    /**
     * 获取执行表达式后返回的日期类型结果。
     * @return 执行表达式后返回的日期类型结果。
     */
    public Calendar getCalendarResult()
    {
        Valuable result = execute( null ) ;
        return result.getDateValue() ;
    }
    
    /**
     * 获取执行表达式后返回的List类型结果。
     * @return 执行表达式后返回的List类型结果。
     */
    public List<?> getListResult()
    {
        Valuable result = execute( null ) ;
        return (List<?>)result.getValue() ;
    }
    
    /**
     * 根据表达式和传入的变量获取执行后Object类型的结果，可使用instanceof判断具体类型。
     * @param variableMap 变量集合（key：变量名称；value：变量值）
     * @return 执行表达式后Object类型的结果。
     */
    public Object getObjectResult( Map<String,Object> variableMap )
    {
        Valuable result = execute( variableMap ) ;
        return result.getValue() ;
    }
    
    /**
     * 根据表达式和传入的变量获取执行后字符串类型的结果。
     * @param variableMap 变量集合（key：变量名称；value：变量值）
     * @return 执行表达式后字符串类型的结果。
     */
    public String getStringResult( Map<String,Object> variableMap )
    {
        Valuable result = execute( variableMap ) ;
        return result.getStringValue() ;
    }
    
    /**
     * 根据表达式和传入的变量获取执行后Character类型的结果。
     * @param variableMap 变量集合（key：变量名称；value：变量值）
     * @return 执行表达式后Character类型的结果。
     */
    public Character getCharResult( Map<String,Object> variableMap )
    {
        Valuable result = execute( variableMap ) ;
        return result.getCharValue() ;
    }
    
    /**
     * 根据表达式和传入的变量获取执行后BigDecimal类型的结果。
     * @param variableMap 变量集合（key：变量名称；value：变量值）
     * @return 执行表达式后BigDecimal类型的结果。
     */
    public BigDecimal getBigDecimalResult( Map<String,Object> variableMap )
    {
        Valuable result = execute( variableMap ) ;
        return result.getNumberValue() ;
    }
    
    /**
     * 根据表达式和传入的变量获取执行后布尔类型的结果。
     * @param variableMap 变量集合（key：变量名称；value：变量值）
     * @return 执行表达式后布尔类型的结果。
     */
    public Boolean getBooleanResult( Map<String,Object> variableMap )
    {
        Valuable result = execute( variableMap ) ;
        return result.getBooleanValue() ;
    }
    
    /**
     * 根据表达式和传入的变量获取执行后日期类型的结果。
     * @param variableMap 变量集合（key：变量名称；value：变量值）
     * @return 执行表达式后日期类型的结果。
     */
    public Calendar getCalendarResult( Map<String,Object> variableMap )
    {
        Valuable result = execute( variableMap ) ;
        return result.getDateValue() ;
    }
    
    /**
     * 获取执行表达式后返回的List类型结果。
     * @param variableMap 变量集合（key：变量名称；value：变量值）
     * @return 执行表达式后返回的List类型结果。
     */
    public List<?> getListResult( Map<String,Object> variableMap )
    {
        Valuable result = execute( variableMap ) ;
        return (List<?>)result.getValue() ;
    }
    
    /**
     * 根据表达式和传入的变量执行表达式，并返回执行后的结果。
     * @param variableMap 变量集合（key：变量名称；value：变量值）
     * @return 执行表达式后的结果。
     */
    private Valuable execute( Map<String,Object> variableMap )
    {
        if( variableMap != null && !variableMap.isEmpty() )
        {
            for( Entry<String,Object> entry : variableMap.entrySet() )
            {
                expressionObj.initVariable( entry.getKey(), entry.getValue() ) ;
            }
        }
        Valuable result = expressionObj.evaluate() ;
        return result ;
    }
}