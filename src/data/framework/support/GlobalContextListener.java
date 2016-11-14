package data.framework.support ;

import javax.servlet.ServletContextEvent ;
import org.springframework.web.context.ContextLoaderListener ;

/**
 * 继承自 "org.springframework.web.context.ContextLoaderListener" 的 servlet 监听器，
 * 用于初始化 spring 的 WebApplicationContext、ConfigContext、MessageContext 和 GlobalContext。
 * 
 * @author wanggq
 */
public class GlobalContextListener extends ContextLoaderListener
{

    @Override
    public void contextInitialized( ServletContextEvent event )
    {
        super.contextInitialized( event ) ;
        GlobalContext.startup( event.getServletContext() ) ;
    }

    @Override
    public void contextDestroyed( ServletContextEvent event )
    {
        super.contextDestroyed( event ) ;
        GlobalContext.shutdown() ;
    }
}
