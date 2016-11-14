package data.framework.web.widget;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

import data.framework.support.GlobalContext;
import data.framework.support.Logger;
import data.framework.web.ui.AbstractComponent;

/**
 * JSP 自定义标签的基类。
 * @author wanggq
 */
public abstract class AbstractTag extends BodyTagSupport
{
    @Override
    public int doStartTag() throws JspException
    {
        return hasBody() ? EVAL_BODY_BUFFERED : SKIP_BODY ;
    }

    @Override
    public int doEndTag() throws  JspException
    {
        pushProperties() ;
        JspWriter out = pageContext.getOut() ;

        try
        {
            if( getComponent() != null )
            {
                getComponent().draw( out ) ;
                getComponent().render( out ) ;
                if( hasBody() && bodyContent != null )
                {
                    if( bodyContent.getString().trim().length() != 0 )
                    {
                        getComponent().control( out ) ;
                    }
                    out.print( bodyContent.getString().trim() ) ;
                }
    
                getComponent().afterControl( out ) ;
            }
        }
        catch( Exception ex )
        {
            Logger.logError( ex ) ;
        }

        return EVAL_PAGE ;
    }

    /**
     * 取得该控件对应的模型组件实例。
     * @return 模型组件实例
     */
    public abstract AbstractComponent getComponent() ;
    
    /**
     * 初始化模型组件的属性。
     */
    protected abstract void pushProperties() ;

    /**
     * 取得当前 web 应用的根路径。
     * @return 当前 web 应用的根路径
     */
    protected String getWebRootPath()
    {
        return webRootPath ;
    }

    /**
     * 标签是否拥有标签体。
     * @return 有标签体返回 true，否则返回 false
     */
    protected boolean hasBody()
    {
        return false ;
    }

    /**
     * 返回一个布尔值，指示该标签是否是一个脚本 render 控件。<br>
     * 如果该值为 true，在调用标签的组件模型的 render 方法前，将先输出一个脚本头&lt;script&gt;，并在 render 方法后闭合该脚本头。
     * 
     * @return 针对一个脚本 render 控件，将返回 true，否则返回 false。
     */
    protected boolean isScript()
    {
        return true ;
    }

    private String webRootPath = GlobalContext.getWebRootPath() ;
    private static final long serialVersionUID = -4369080224191240198L ;
}
