package data.framework.web.ui;

import java.io.Serializable ;
import java.io.Writer ;

/**
 * UI 组件模型抽象基类。
 * @author wanggq
 */
public abstract class AbstractComponent implements Serializable
{
    /**
     * 返回代表该组件类型的字符串名称。
     * @return 该组件类型的字符串名称
     */
    public abstract String getControlName() ;

    /**
     * 渲染组件。
     * @param out 渲染输出对象
     * @throws Exception 异常
     */
    public abstract void render( Writer out ) throws Exception ;

    /**
     * 在处理组件子内容前的预输出。
     * @param out 渲染输出对象
     * @throws Exception 异常
     */
    public void control( Writer out ) throws Exception {}
    
    /**
     * 在处理组件子内容后的预输出。
     * @param out 渲染输出对象
     * @throws Exception 异常
     */
    public void afterControl( Writer out ) throws Exception {}

    /**
     * 输出控件的 HTML 标签。
     * @param out 渲染输出对象
     * @throws Exception 异常
     */
    public void draw( Writer out ) throws Exception {}

    private static final long serialVersionUID = -8020590893988250579L ;
}