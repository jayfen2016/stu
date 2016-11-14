package data.framework.support ;

import java.util.Map ;

import org.springframework.beans.BeansException ;
import org.springframework.beans.factory.BeanDefinitionStoreException ;
import org.springframework.beans.factory.BeanFactory ;
import org.springframework.beans.factory.BeanFactoryAware ;
import org.springframework.beans.factory.BeanNameAware ;
import org.springframework.beans.factory.config.BeanDefinition ;
import org.springframework.beans.factory.config.BeanDefinitionVisitor ;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor ;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory ;
import org.springframework.stereotype.Service ;
import org.springframework.util.StringValueResolver ;

/**
 * <p>spring IoC容器预处理类。</p>
 * <p>该类将 spring.xml 中的 "$()" 表达式转义成 config.xml 中相应配置节的值。</p>
 * @author wanggq
 */
@Service
public class ConfigPlaceholderConfigurer implements BeanFactoryPostProcessor, BeanNameAware, BeanFactoryAware
{
    private static final String DEFAULT_PLACEHOLDER_PREFIX = "$(" ;
    private static final String DEFAULT_PLACEHOLDER_SUFFIX = ")" ;
    private static final String IMPLEMENTS_STRING_PREFIX = "#{implementsMap['" ;
    private static final String IMPLEMENTS_STRING_SUFFIX = "']}" ;

    private static Map<String,String> implementsMap = ConfigContext.getMapSection( "framework.implements.map" ) ;

    @Override
    public void postProcessBeanFactory( ConfigurableListableBeanFactory beanFactoryToProcess ) throws BeansException
    {
        StringValueResolver valueResolver = new PlaceholderResolvingStringValueResolver() ;
        BeanDefinitionVisitor visitor = new BeanDefinitionVisitor( valueResolver ) ;

        String[] beanNames = beanFactoryToProcess.getBeanDefinitionNames() ;
        for( String curName : beanNames )
        {
            if( !( curName.equals( this.beanName ) && beanFactoryToProcess.equals( this.beanFactory ) ) )
            {
                BeanDefinition bd = beanFactoryToProcess.getBeanDefinition( curName ) ;
                try
                {
                    visitor.visitBeanDefinition( bd ) ;
                }
                catch( Exception ex )
                {
                    throw new BeanDefinitionStoreException( bd.getResourceDescription(), curName, ex.getMessage() ) ;
                }
            }
        }

        beanFactoryToProcess.resolveAliases( valueResolver ) ;
        beanFactoryToProcess.addEmbeddedValueResolver( valueResolver ) ;
    }

    @Override
    public void setBeanName( String beanName )
    {
        this.beanName = beanName ;
    }

    @Override
    public void setBeanFactory( BeanFactory beanFactory ) throws BeansException
    {
        this.beanFactory = beanFactory ;
    }

    private class PlaceholderResolvingStringValueResolver implements StringValueResolver
    {
        public String resolveStringValue( String strVal ) throws BeansException
        {
            String result = strVal ;
            if( strVal.startsWith( DEFAULT_PLACEHOLDER_PREFIX ) && strVal.endsWith( DEFAULT_PLACEHOLDER_SUFFIX ) )
            {
                result = ConfigContext.getValue( strVal.replace( DEFAULT_PLACEHOLDER_PREFIX, "" ).replace( DEFAULT_PLACEHOLDER_SUFFIX, "" ) ) ;
            }
            if( strVal.startsWith( IMPLEMENTS_STRING_PREFIX ) && strVal.endsWith( IMPLEMENTS_STRING_SUFFIX ) )
            {
                String implName = strVal.replace( IMPLEMENTS_STRING_PREFIX, "" ).replace( IMPLEMENTS_STRING_SUFFIX, "" ) ;
                result = "#{" + implementsMap.get( implName ) + "}" ;
            }
            return result ;
        }
    }

    private String beanName ;
    private BeanFactory beanFactory ;
}
