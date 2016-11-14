package data.framework.data ;

import java.io.PrintWriter ;
import java.util.List ;
import java.util.Map ;
import java.util.Set ;

/**
 * <p>字符串序列化接口。</p>
 * <p>UI层 browser 端和 server 端进行通讯时，使用该接口的实现类来进行对象的字符串序列化和反序列化。</p>
 * @author wanggq
 */
public interface DataSerializer
{
    /**
     * 根据格式化字符串生成一个 Map 对象。
     * @param content 格式化字符串
     * @return 格式化字符串对应的 Map 对象
     */
    public Map<String,Object> parseMap( String content ) ;

    /**
     * 将一个 Map 内容格式化成对应的序列化字符串
     * @param map 输入 Map 实例
     * @return 对应的格式化字符串
     */
    public String formatMap( Map<String,Object> map ) ;

    /**
     * 根据格式化字符串生成一个 List 对象。
     * @param content 格式化字符串
     * @return 格式化字符串对应的 List 对象
     */
    public List<?> parseList( String content ) ;

    /**
     * 根据格式化字符串生成一个对象。
     * @param <T> 对象类类型
     * @param content 格式化字符串
     * @param type 对象类型
     * @return 格式化字符串对应的对象
     */
    public <T extends Object> T  parseObject( String content, Class<T> type ) ;

    /**
     * 将一个 List 内容格式化成对应的序列化字符串。
     * @param list 输入 List 实例
     * @return 对应的格式化字符串
     */
    public String formatList( List<?> list ) ;

    /**
     * 将一个 Set 内容格式化成对应的序列化字符串。
     * @param set 输入 Set 实例
     * @return 对应的格式化字符串
     */
    public String formatSet( Set<?> set ) ;

    /**
     * 根据格式化字符串生成一个 Set 对象。
     * @param content 格式化字符串
     * @return 格式化字符串对应的 Set 对象
     */
    public Set<?> parseSet( String content ) ;

    /**
     * 生成界面消息格式化串。
     * @param message 消息
     * @return 信息格式化串
     */
    public String message( String message ) ;
    
    /**
     * 生成界面错误消息格式化串。
     * @param message 错误消息
     * @return 错误信息格式化串
     */
    public String error( String error ) ;

    /**
     * 将一个 Object 内容格式化成对应的序列化字符串。
     * @param content 输入对象
     * @return 对应的格式化字符串
     */
    public String formatObject( Object content ) ;

    /**
     * 获取一个空的序列化 Map 实例。
     * @return 序列化 Map 实例
     */
    public MapSerializer withMap() ;

    /**
     * 序列化 Map 静态接口。
     * @author plusir
     */
    public static interface MapSerializer
    {
        /**
         * 压入需要序列化的值。
         * @param key 键
         * @param value 值
         * @return 实例自身，用于支持链式操作
         */
        public MapSerializer put( String key, Object value ) ;

        /**
         * 获取序列化字符串。
         * @return 序列化字符串
         */
        public String format() ;

        /**
         * 将序列化字符串直接输出。
         * @param out 输出
         */
        public void render( PrintWriter out ) ;
    }
}