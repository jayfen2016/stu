package data.framework.support ;

import java.util.ArrayList ;
import java.util.HashMap ;
import java.util.List ;
import java.util.Map ;
import java.util.regex.Matcher ;
import java.util.regex.Pattern ;

import org.dom4j.Document ;
import org.dom4j.Element ;
import org.dom4j.XPath ;
import org.dom4j.io.SAXReader ;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver ;

/**
 * 全局配置上下文，通过 "config.xml" 文件初始化自身。<br />
 * 静态工具类，不可继承。
 *
 * @author wanggq 
 */
public final class ConfigContext
{
    private static Map<String,String> properties = new HashMap<String,String>() ;
    private static Map<String,List<String>> lists = new HashMap<String,List<String>>() ;
    private static Map<String,Map<String,String>> maps = new HashMap<String,Map<String,String>>() ;
    private static boolean isInit = false ;

    private ConfigContext() {}

    static
    {
        try
        {
            PathMatchingResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver ( Thread.currentThread().getContextClassLoader() ) ;
            org.springframework.core.io.Resource[] resources = resourceResolver.getResources( "classpath*:*-config.xml" ) ;
            for( org.springframework.core.io.Resource resourceItem : resources )
            {
                SAXReader reader = new SAXReader() ;
                Document doc = reader.read( resourceItem.getInputStream() ) ;

                parseProperties( doc ) ;
                parseLists( doc ) ;
                parseMaps( doc ) ;
            }

            isInit = true ;
        }
        catch( Exception ex )
        {
            ex.printStackTrace();
        }
    }

    /**
     * 获得指定配置节中的字符串(string)值。
     * @param name 配置节的名称
     * @return 该配置节中的值，当参数值指定的配置信息不存在时返回null值
     */
    public static String getStringSection( String name )
    {
        if( !isInit )
            throw new RuntimeException( "ConfigContext uninitialized!" ) ;

        return properties.get( name ) ;
    }

    /**
     * 获取指定配置节中的列表(list)信息。
     * @param name 配置节的名称
     * @return 该配置节中列表的内容，当参数指定的配置信息不存在时返回null值
     */
    public static List<String> getListSection( String name )
    {
        if( !isInit )
            throw new RuntimeException( "ConfigContext uninitialized!" ) ;

        return lists.get( name ) ;
    }

    /**
     * 获取指定配置节中的键/值对集合(map)信息。
     * @param name 配置节的名称
     * @return 配置节中的键/值对集合信息，当参数指定的配置信息不存在时返回null值
     */
    public static Map<String, String> getMapSection( String name )
    {
        if( !isInit )
            throw new RuntimeException( "ConfigContext uninitialized!" ) ;

        return maps.get( name ) ;
    }

    /**
     * 根据表达式返回对应的配置信息。合法的配置名称表达式如下：<br>
     *  1.直接的配置名称，返回字符串配置节。如："local.all.top" ；<br>
     *  2.检索列表中的元素，配置名称后面跟方括号，括号内容必须为数字。如："local.all.top[0]" ；<br>
     *  3.检索Map中的元素，配置名称后面跟方括号，括号内容是用单引号括起来的 key。如："local.all.top['en']" 。<br>
     *
     * @param name 配置名称表达式
     * @return 配置节信息，配置不存在或传入的参数为 null、空字符串时返回 null
     */
    public static String getValue( String name )
    {
        if( !isInit )
            throw new RuntimeException( "ConfigContext uninitialized!" ) ;

        String result = null ;
        if( name == null || name.length() == 0 )
        {
            result = null ;
        }
        else if( name.indexOf( "[" ) == -1 )
        {
            result = getStringSection( name ) ;
        }
        else
        {
            Pattern listPattern = Pattern.compile("(\\S*)\\[(\\d+)\\]");
            Matcher listMatcher = listPattern.matcher( name ) ;
            if( listMatcher.find() )
            {
                String listName = listMatcher.group( 1 ) ;
                int listIndex = Integer.parseInt( listMatcher.group( 2 ) ) ;
                List<String> list = getListSection( listName ) ;
                if( list != null )
                {
                    result = list.get( listIndex ) ;
                }
            }
            else
            {
                Pattern mapPattern = Pattern.compile("(\\S*)\\['(\\S+)'\\]");
                Matcher mapMatcher = mapPattern.matcher( name ) ;
                if( mapMatcher.find() )
                {
                    String mapName = mapMatcher.group( 1 ) ;
                    String mapKey = mapMatcher.group( 2 ) ;
                    Map<String, String> map = getMapSection( mapName ) ;
                    if( map != null )
                    {
                        result = map.get( mapKey ) ;
                    }
                }
            }
        }

        return result ;
    }

    @SuppressWarnings("unchecked")
    private static void parseProperties( Document xdc )
    {
        XPath xpath = xdc.createXPath( "/configuration/properties/property" ) ;
        List<Element> xnl = xpath.selectNodes( xdc ) ;

        for( Element item : xnl )
        {
            String key = item.attributeValue( "name" ).trim() ;
            if( properties.containsKey( key ) )
            {
                properties.remove( key ) ;
            }
            properties.put( key, item.getTextTrim() ) ;
        }
    }

    @SuppressWarnings("unchecked")
    private static void parseLists( Document xdc )
    {
        XPath xpath = xdc.createXPath( "/configuration/lists/list" ) ;
        List<Element> xnl = xpath.selectNodes( xdc ) ;

        for( Element item : xnl )
        {
            ArrayList<String> items = new ArrayList<String>() ;

            XPath itemPath = item.createXPath( "item" ) ;

            List<Element> itemNodes = itemPath.selectNodes( item ) ;
            for( Element text : itemNodes )
            {
                items.add( text.getTextTrim() ) ;
            }

            String key = item.attributeValue( "name" ).trim() ;
            if( lists.containsKey( key ) )
            {
                lists.get( key ).addAll( items ) ;
            }
            else
            {
                lists.put( key, items ) ;
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static void parseMaps( Document xdc )
    {
        XPath xpath = xdc.createXPath( "/configuration/maps/map" ) ;
        List<Element> xnl = xpath.selectNodes( xdc ) ;

        for( Element item : xnl )
        {
            HashMap<String,String> items = new HashMap<String,String>() ;

            XPath itemPath = item.createXPath( "entry" ) ;

            List<Element> itemNodes = itemPath.selectNodes( item ) ;
            for( Element text : itemNodes )
            {
                items.put( text.attributeValue( "key" ).trim(), text.getTextTrim() ) ;
            }

            String key = item.attributeValue( "name" ).trim() ;
            if( maps.containsKey( key ) )
            {
                maps.get( key ).putAll( items ) ;
            }
            else
            {
                maps.put( key, items ) ;
            }
        }
    }
}