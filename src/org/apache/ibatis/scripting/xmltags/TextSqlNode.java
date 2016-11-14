package org.apache.ibatis.scripting.xmltags ;

import org.apache.commons.lang.StringUtils ;
import org.apache.ibatis.parsing.GenericTokenParser ;
import org.apache.ibatis.parsing.TokenHandler ;
import org.apache.ibatis.parsing.TokenParser ;
import org.apache.ibatis.type.SimpleTypeRegistry ;

import data.framework.support.ibatis.AutoTokenParser ;

/**
 * 修改原mybatis包中的TextSqlNode类，用于自动替换Sql语句中的变量值。
 * @author wanggq
 *
 */
public class TextSqlNode implements SqlNode
{
    private String text ;

    public TextSqlNode( String text )
    {
        this.text = text ;
    }

    public boolean isDynamic()
    {
        DynamicCheckerTokenParser checker = new DynamicCheckerTokenParser() ;
        TokenParser parser = createParser( checker ) ;
        parser.parse( text ) ;
        return checker.isDynamic() ;
    }

    public boolean apply( DynamicContext context )
    {
        TokenParser parser = createParser( new BindingTokenParser( context ) ) ;
        context.appendSql( parser.parse( text ) ) ;
        return true ;
    }

    private TokenParser createParser( TokenHandler handler )
    {
        if( StringUtils.isNotBlank( text ) && text.indexOf( "&{" ) != -1 && text.indexOf( "}" ) != -1 )
            return new AutoTokenParser( "&{", "}", handler ) ;
        else
            return new GenericTokenParser( "${", "}", handler ) ;
    }

    public static class BindingTokenParser implements TokenHandler
    {

        private DynamicContext context ;

        public BindingTokenParser( DynamicContext context )
        {
            this.context = context ;
        }

        public String handleToken( String content )
        {
            Object parameter = context.getBindings().get( "_parameter" ) ;
            if( parameter == null )
            {
                context.getBindings().put( "value", null ) ;
            }
            else if( SimpleTypeRegistry.isSimpleType( parameter.getClass() ) )
            {
                context.getBindings().put( "value", parameter ) ;
            }
            Object value = OgnlCache.getValue( content, context.getBindings() ) ;
            return ( value == null ? "" : String.valueOf( value ) ) ;
        }
    }

    public static class DynamicCheckerTokenParser implements TokenHandler
    {

        private boolean isDynamic ;

        public boolean isDynamic()
        {
            return isDynamic ;
        }

        public String handleToken( String content )
        {
            this.isDynamic = true ;
            return null ;
        }
    }
}