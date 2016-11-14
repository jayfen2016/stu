package data.framework.support.ibatis ;

import org.apache.ibatis.parsing.TokenHandler ;
import org.apache.ibatis.parsing.TokenParser ;
import org.apache.ibatis.scripting.xmltags.TextSqlNode.DynamicCheckerTokenParser ;

import data.framework.support.ConfigContext ;

/**
 * 自动替换Mapper.xml中的变量，变量格式为“&{***}”。
 * 要替换的变量需先在“framework-config.xml”中定义。
 * @author wanggq
 *
 */
public class AutoTokenParser implements TokenParser
{

    private final String openToken ;
    private final String closeToken ;
    private final TokenHandler handler ;

    public AutoTokenParser( String openToken, String closeToken, TokenHandler handler )
    {
        this.openToken = openToken ;
        this.closeToken = closeToken ;
        this.handler = handler ;
    }

    public String parse( String text )
    {
        StringBuilder builder = new StringBuilder() ;
        if( text != null && text.length() > 0 )
        {
            char[] src = text.toCharArray() ;
            int offset = 0 ;
            int start = text.indexOf( openToken, offset ) ;
            while( start > -1 )
            {
                if( start > 0 && src[start - 1] == '\\' )
                {
                    // the variable is escaped. remove the backslash.
                    builder.append( src, offset, start - 1 ).append( openToken ) ;
                    offset = start + openToken.length() ;
                }
                else
                {
                    int end = text.indexOf( closeToken, start ) ;
                    if( end == -1 )
                    {
                        builder.append( src, offset, src.length - offset ) ;
                        offset = src.length ;
                    }
                    else
                    {
                        builder.append( src, offset, start - offset ) ;
                        offset = start + openToken.length() ;
                        String content = new String( src, offset, end - offset ) ;
                        if( handler instanceof DynamicCheckerTokenParser )
                            handler.handleToken( content ) ;
                        builder.append( ConfigContext.getValue( content ) ) ;
                        offset = end + closeToken.length() ;
                    }
                }
                start = text.indexOf( openToken, offset ) ;
            }
            if( offset < src.length )
            {
                builder.append( src, offset, src.length - offset ) ;
            }
        }
        return builder.toString() ;
    }
}
