package data.framework.schedule ;

import java.io.Serializable ;
import java.util.Collection ;
import java.util.HashMap ;
import java.util.Map ;
import java.util.Set ;
import java.util.Map.Entry ;

/**
 * <p>用来初始化任务的初始数据容器类。</p>
 * <p>该容器类可以在开始一个任务的时候传递两种数据：</p>
 * <ol>
 * <li>任务持久化数据，值可以在每次任务执行时更改以便传递给下一次任务执行， 并能够进行持久化。该类数据由带 "job" 字样的方法簇维护。<br><br></li>
 * <li>任务运行时数据，值可以在每次任务执行时更改以便传递给下一次任务执行， 不能持久化。该类数据用来传递对实例敏感(例如未能正确实现序列化的单例实例)或者未实现序列化的类实例。由带 "runtime" 字样的方法簇维护。</li>
 * </ol>
 *
 * @author wanggq
 */
public class Detail
{
    /**
     * 默认无参构造方法。
     */
    public Detail() {}

    /**
     * 用包含两种数据的映射集合来实例化一个 "data.framework.schedule.Detail" 实例。
     * @param job 任务持久化数据映射集合
     * @param runtime 任务运行时数据映射集合
     */
    public Detail( Map<String,Serializable> job, Map<String,Object> runtime )
    {
        this.job = job ;
        this.runtime = runtime ;
    }

    /**
     * 从任务持久化数据映射中移除所有映射关系。
     * @return 实例本身，用来支持链式操作
     */
    public Detail clearJobMap()
    {
        job.clear() ;
        return this ;
    }
    /**
     * 从任务运行时数据映射中移除所有映射关系。
     * @return 实例本身，用来支持链式操作
     */
    public Detail clearRuntimeMap()
    {
        runtime.clear() ;
        return this ;
    }

    /**
     * 如果任务持久化数据映射包含指定键的映射关系，则返回 true。
     * @param key 测试是否存在于此映射中的键
     * @return 如果此映射包含指定键的映射关系，则返回 true
     */
    public boolean containsJobKey( String key )
    {
        return job.containsKey( key ) ;
    }
    /**
     * 如果任务运行时数据映射包含指定键的映射关系，则返回 true。
     * @param key 测试是否存在于此映射中的键
     * @return 如果此映射包含指定键的映射关系，则返回 true
     */
    public boolean ontainsRuntimeKey( String key )
    {
        return runtime.containsKey( key ) ;
    }

    /**
     * 如果任务持久化数据映射将一个或多个键映射到指定值，则返回 true。
     * @param value 测试是否存在于此映射中的值
     * @return 如果此映射将一个或多个键映射到指定值，则返回 true
     */
    public boolean containsJobValue( Serializable value )
    {
        return job.containsValue( value ) ;
    }
    /**
     * 如果任务运行时数据映射将一个或多个键映射到指定值，则返回 true。
     * @param value 测试是否存在于此映射中的值
     * @return 如果此映射将一个或多个键映射到指定值，则返回 true
     */
    public boolean containsRuntimeValue( Object value )
    {
        return runtime.containsValue( value ) ;
    }

    /**
     * 返回任务持久化数据映射中包含的映射关系的 Set 视图。
     * @return 此映射中包含的映射关系的 set 视图
     */
    public Set<Entry<String,Serializable>> jobMapEntrySet()
    {
        return job.entrySet() ;
    }
    /**
     * 返回任务运行时数据映射中包含的映射关系的 Set 视图。
     * @return 此映射中包含的映射关系的 set 视图
     */
    public Set<Entry<String,Object>> runtimeMapEntrySet()
    {
        return runtime.entrySet() ;
    }

    /**
     * 返回指定键所映射的值；如果任务持久化数据映射不包含该键的映射关系，则返回 null。
     * @param key 要返回其关联值的键
     * @return 指定键所映射的值；如果此映射不包含该键的映射关系，则返回 null
     */
    public Serializable getJobData( String key )
    {
        return job.get( key ) ;
    }
    /**
     * 返回指定键所映射的值；如果任务运行时数据映射不包含该键的映射关系，则返回 null。
     * @param key 要返回其关联值的键
     * @return 指定键所映射的值；如果此映射不包含该键的映射关系，则返回 null
     */
    public Object getRuntimeData( String key )
    {
        return runtime.get( key ) ;
    }

    /**
     * 如果任务持久化数据映射未包含键-值映射关系，则返回 true。
     * @return 如果此映射未包含键-值映射关系，则返回 true
     */
    public boolean jobMapIsEmpty()
    {
        return job.isEmpty() ;
    }
    /**
     * 如果任务运行时数据映射未包含键-值映射关系，则返回 true。
     * @return 如果此映射未包含键-值映射关系，则返回 true
     */
    public boolean runtimeMapIsEmpty()
    {
        return runtime.isEmpty() ;
    }

    /**
     * 返回任务持久化数据映射中包含的键的 Set 视图。
     * @return 此映射中包含的键的 set 视图
     */
    public Set<String> jobMapKeySet()
    {
        return job.keySet() ;
    }
    /**
     * 返回任务运行时数据映射中包含的键的 Set 视图。
     * @return 此映射中包含的键的 set 视图
     */
    public Set<String> runtimeMapKeySet()
    {
        return runtime.keySet() ;
    }

    /**
     * <p>将指定的值与任务持久化数据映射中的指定键关联。</p>
     * <p>如果此映射以前包含一个该键的映射关系，则用指定值替换旧值。</p>
     * @param key 与指定值关联的键
     * @param value 与指定键关联的值
     * @return 实例本身，用来支持链式操作
     */
    public Detail putJobData( String key, Serializable value )
    {
        job.put( key, value ) ;
        return this ;
    }
    /**
     * <p>将指定的值与任务运行时数据映射中的指定键关联。</p>
     * <p>如果此映射以前包含一个该键的映射关系，则用指定值替换旧值。</p>
     * @param key 与指定值关联的键
     * @param value 与指定键关联的值
     * @return 实例本身，用来支持链式操作
     */
    public Detail putRuntimeData( String key, Object value )
    {
        runtime.put( key, value ) ;
        return this ;
    }

    /**
     * <p>从指定映射中将所有映射关系复制到任务持久化数据映射中。</p>
     * <p>如果正在进行此操作的同时修改了指定的映射，则此操作的行为是不确定的。</p>
     * @param job 要存储在此映射中的映射关系
     * @return 实例本身，用来支持链式操作
     */
    public Detail putAllJobData( Map<String,Serializable> job )
    {
        this.job.putAll( job ) ;
        return this ;
    }
    /**
     * <p>从指定映射中将所有映射关系复制到任务运行时数据映射中。</p>
     * <p>如果正在进行此操作的同时修改了指定的映射，则此操作的行为是不确定的。</p>
     * @param runtime 要存储在此映射中的映射关系
     * @return 实例本身，用来支持链式操作
     */
    public Detail putAllRuntimeData( Map<String,Object> runtime )
    {
        this.runtime.putAll( runtime ) ;
        return this ;
    }

    /**
     * 如果存在一个键的映射关系，则将其从任务持久化数据映射中移除。
     * @param key 从映射中移除其映射关系的键
     * @return 实例本身，用来支持链式操作
     */
    public Detail removeJobData( String key )
    {
        job.remove( key ) ;
        return this ;
    }
    /**
     * 如果存在一个键的映射关系，则将其从任务运行时数据映射中移除。
     * @param key 从映射中移除其映射关系的键
     * @return 实例本身，用来支持链式操作
     */
    public Detail removeRuntimeData( String key )
    {
        runtime.remove( key ) ;
        return this ;
    }

    /**
     * <p>返回任务持久化数据映射中的键-值映射关系数。</p>
     * <p>如果该映射包含的元素大于 Integer.MAX_VALUE，则返回 Integer.MAX_VALUE。</p>
     * @return 此映射中的键-值映射关系数
     */
    public int jobMapSize()
    {
        return job.size() ;
    }
    /**
     * <p>返回任务运行时数据映射中的键-值映射关系数。</p>
     * <p>如果该映射包含的元素大于 Integer.MAX_VALUE，则返回 Integer.MAX_VALUE。</p>
     * @return 此映射中的键-值映射关系数
     */
    public int runtimeMapSize()
    {
        return runtime.size() ;
    }

    /**
     * 返回任务持久化数据映射中包含的值的 Collection 视图。
     * @return 此映射中包含的值的 collection 视图
     */
    public Collection<Serializable> jobMapValues()
    {
        return job.values() ;
    }
    /**
     * 返回任务运行时数据映射中包含的值的 Collection 视图。
     * @return 此映射中包含的值的 collection 视图
     */
    public Collection<Object> runtimeMapValues()
    {
        return runtime.values() ;
    }

    /**
     * 获取任务持久化数据映射集合。
     * @return 任务持久化数据映射集合。
     */
    public Map<String,Serializable> getJobDataMap()
    {
        return job ;
    }

    /**
     * 获取任务运行时数据映射集合。
     * @return 任务运行时数据映射集合
     */
    public Map<String,Object> getRuntimeDataMap()
    {
        return runtime ;
    }

    private Map<String,Serializable> job = new HashMap<String,Serializable>() ;
    private Map<String,Object> runtime = new HashMap<String,Object>() ;
}