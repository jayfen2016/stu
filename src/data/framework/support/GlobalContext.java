package data.framework.support ;

import java.io.BufferedReader ;
import java.io.InputStream ;
import java.io.InputStreamReader ;
import java.util.Hashtable ;
import java.util.Map ;

import javax.naming.Context ;
import javax.naming.InitialContext ;
import javax.servlet.ServletContext ;

import org.logicalcobwebs.proxool.ProxoolFacade ;
import org.springframework.context.ApplicationContext ;
import org.springframework.context.support.ClassPathXmlApplicationContext ;
import org.springframework.core.io.ClassPathResource ;

import data.framework.utility.UploadHelper ;

/**
 * 全局应用上下文静态工具类。
 * @author wanggq
 */
public final class GlobalContext
{
    private static String defaultThemeName = ConfigContext.getStringSection( "framework.theme.default" ) ;
    private static ApplicationContext springContext ;
    private static boolean isInit = false ;
    private static String crlf = System.getProperty( "line.separator" ) ;
    private static String webRootPath = null ;
    private static String webRealPath = null ;
    private static Context context = null ;

    private GlobalContext() {}

    /**
     * 用一个 'javax.servlet.ServletContext' 实例来启动应用上下文。该方法通常用于在一个 servlet 监听器中调用。
     * @param servletContext 用来初始化 web 环境信息的 'javax.servlet.ServletContext' 实例
     */
    public static void startup( ServletContext servletContext )
    {
        if( isInit )
            return ;

        try
        {
            Map<String,String> map = ConfigContext.getMapSection( "framework.jndi.properties" ) ;
            Hashtable<String,String> table = new Hashtable<String,String>() ;
            if( map != null )
            {
                table.putAll( map ) ;
            }
            context = new InitialContext( table ) ;
        }
        catch( Exception ex )
        {
            throw new RuntimeException( ex ) ;
        }

        springContext = org.springframework.web.context.support.WebApplicationContextUtils.getWebApplicationContext( servletContext ) ;
        isInit = true ;
        webRootPath = servletContext.getContextPath().equals( "/" ) ? "/" : ( servletContext.getContextPath() + "/" ) ;
        webRealPath = UploadHelper.BASE_PATH ;
    }

    /**
     * 启动应用上下文，通常用于非 web 环境，如进行单元测试等。
     */
    public static void startup()
    {
        if( isInit )
            return ;

        try
        {
            Map<String,String> map = ConfigContext.getMapSection( "framework.jndi.properties" ) ;
            Hashtable<String,String> table = new Hashtable<String,String>() ;
            if( map != null )
            {
                table.putAll( map ) ;
            }
            context = new InitialContext( table ) ;
        }
        catch( Exception ex )
        {
            throw new RuntimeException( ex ) ;
        }

        ClassPathXmlApplicationContext xmlSpringContext = new ClassPathXmlApplicationContext( "*-spring.xml" ) ;
        xmlSpringContext.registerShutdownHook() ;

        springContext = xmlSpringContext ;
        isInit = true ;
    }

    /**
     * 关闭内部服务，清理对象并进行销毁。
     */
    public static void shutdown()
    {
        if( isInit )
        {
            ProxoolFacade.shutdown( 0 );

            springContext = null ;
            isInit = false ;
            webRootPath = null ;
        }
    }

    /**
     * 根据类的逻辑名称在容器中取得类的实例。
     * @param name 类的逻辑名称
     * @return 类的逻辑名称对应的类实例，对于非容器管理的类将返回null
     */
    public static Object getObject( String name )
    {
        if( !isInit )
            throw new RuntimeException( "GlobalContext uninitialized!" ) ;

        return springContext.getBean( name ) ;
    }

    /**
     * 根据参数type指定的类型返回类实例。
     * @param <T> 返回类型
     * @param type 返回类型的类类型
     * @param name 类的逻辑名称
     * @return 类的逻辑名称对应的类实例，对于非容器管理的类将返回null
     */
    public static <T extends Object> T getObject( String name, Class<T> type )
    {
        if( !isInit )
            throw new RuntimeException( "GlobalContext uninitialized!" ) ;

        return springContext.getBean( name, type ) ;
    }

    /**
     * 通过 JNDI 查找资源。
     * @param <T> 资源类型
     * @param name 资源名称
     * @param type 资源类类型
     * @return 资源，异常时返回 null
     */
    @SuppressWarnings( "unchecked" )
    public static <T extends Object> T lookup( String name, Class<T> type )
    {
        try
        {
            return (T)context.lookup( name ) ;
        }
        catch( Exception ex )
        {
            ex.printStackTrace() ;
            return null ;
        }
    }

    /**
     * 获取指定位置的文本资源。
     * @param path 文本资源的位置
     * @return 加载的文本资源，不存在时返回null
     */
    public static String getTextResource( String path )
    {
        if( !isInit )
            throw new RuntimeException( "GlobalContext uninitialized!" ) ;

        return getTextResource( path, java.nio.charset.Charset.defaultCharset().name() ) ;
    }

    /**
     * 获取指定位置的文本资源。
     * @param path 文本资源的位置
     * @param chatsetName 文本资源使用的编码名称
     * @return 加载的文本资源，不存在时返回null
     */
    public static String getTextResource( String path, String chatsetName )
    {
        if( !isInit )
            throw new RuntimeException( "GlobalContext uninitialized!" ) ;

        ClassPathResource resource = new ClassPathResource( path ) ;
        if( !resource.exists() )
        {
            return null ;
        }

        StringBuffer result = null ;
        
        try
        {
            InputStream inputStream = resource.getInputStream() ;
            InputStreamReader inputStreamReader = new InputStreamReader( inputStream, chatsetName ) ;
            BufferedReader reader = new BufferedReader( inputStreamReader ) ;

            result = new StringBuffer() ;
            String line = reader.readLine() ;

            while( line != null )
            {
                result.append( line ) ;
                result.append( crlf ) ;
                line = reader.readLine() ;
            }

            inputStream.close() ;
            inputStreamReader.close() ;
            reader.close() ;
        }
        catch( Exception ex )
        {
            result = null ;
            ex.printStackTrace() ;
        }

        return result == null ? null : result.toString() ;
    }

    /**
     * 获取指定位置的二进制资源。
     * @param path 二进制资源的位置
     * @return 加载的二进制资源，不存在时返回null
     */
    public static InputStream getBinary( String path )
    {
        if( !isInit )
            throw new RuntimeException( "GlobalContext uninitialized!" ) ;

        ClassPathResource resource = new ClassPathResource( path ) ;
        InputStream result = null ;

        try
        {
            if( resource.exists() )
            {
                result = resource.getInputStream() ;
            }
        }
        catch( Exception ex )
        {
            result = null ;
            ex.printStackTrace() ;
        }

        return result ;
    }

    /**
     * 取得当前 web 应用的根路径，对应非 web 应用将返回 null。
     * @return 当前 web 应用的根路径
     */
    public static String getWebRootPath()
    {
        return webRootPath ;
    }

    /**
     * 取得当前 web 应用的根路径(物理路径)，对应非 web 应用将返回 null。
     * @return 当前 web 应用的根路径(物理路径)
     */
    public static String getWebRealPath()
    {
        return webRealPath ;
    }

    /**
     * 获取当前 theme 的名字。
     * @return 代表当前 theme 的字符串
     */
    public static String getCurrentThemeName()
    {
        return defaultThemeName ;
    }

    /**
     * 取得类资源的加载路径。
     * @param classic 类
     * @return 类资源加载路径
     */
    public static String getRuntimePathByClass( Class<?> classic )
    {
        return classic.getClassLoader()
            .getResource( classic.getName().replace( ".", "/" )+ ".class" )
            .getFile() ;
    }
}