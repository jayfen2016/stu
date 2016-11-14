package data.framework.support ;

import org.junit.After ;
import org.junit.Before ;

/**
 * 测试用例的抽象基类。<br />
 * 重载了 setUp 和 tearDown 方法，用来自动初始化配置上下文、消息上下文和应用上下文并将当前事务(session或connection)绑定到线程。
 * 
 * @author wanggq
 */
public class AbstractUnitTest
{
    /**
     * 初始化方法，在每个测试方法调用前调用。
     */
    @Before
    public void setUp()
    {
        GlobalContext.startup() ;
    }

    /**
     * 清理方法，在每个测试方法调用结束后调用。
     */
    @After
    public void tearDown()
    {
        GlobalContext.shutdown() ;
    }
    
    /**
     * 根据参数type指定的类型返回类实例。直接调用 GlobalContext 上的静态方法。
     * @param <T> 返回类型
     * @param name 返回类型的类类型
     * @param type 类的逻辑名称
     * @return 类的逻辑名称对应的类实例，对于非容器管理的类将返回null
     */
    protected <T extends Object> T getObject( String name, Class<T> type )
    {
        return GlobalContext.getObject( name, type ) ;
    }
}