package data.framework.web.widget;

import java.util.List ;

import org.apache.commons.lang.StringUtils ;

import data.framework.web.ui.AbstractComponent ;
import data.framework.web.ui.ActionButton ;

/**
 * 按钮控件。
 * <br /><br />
 * <table border="1">
 * <tr><td>id*</td><td>id</td></tr>
 * <tr><td>cssClass</td><td>cssClass</td></tr>
 * <tr><td>value</td><td>value</td></tr>
 * <tr><td>title</td><td>title</td></tr>
 * <tr><td>purview</td><td>purview</td></tr>
 * <tr><td>style</td><td>style</td></tr>
 * <tr><td>disabled</td><td>disabled</td></tr>
 * </table>
 *
 * @author wanggq
 */
public class ActionButtonTag extends AbstractTag
{
    public static String CONTROL_PURVIEW_OPERATIONS = "__control_operations__" ;

    @Override
    public AbstractComponent getComponent()
    {
        return component ;
    }

    @SuppressWarnings( "unchecked" )
    @Override
    protected void pushProperties()
    {
        component.setId( id ) ;
        if( StringUtils.isNotBlank( cssClass ) )
            component.setCssClass( cssClass ) ;
        if( StringUtils.isNotBlank( title ) )
            component.setTitle( title ) ;
        if( StringUtils.isNotBlank( disabled ) )
            component.setDisabled( Boolean.parseBoolean( disabled ) ) ;
        if( StringUtils.isNotBlank( style ) )
            component.setStyle( style ) ;
        if( StringUtils.isNotBlank( value ) )
            component.setValue( value ) ;
        if( StringUtils.isNotBlank( "iconClass" ) )
            component.setIconClass( iconClass ) ;
        
        if( StringUtils.isNotBlank( purview ) )
        {
            Object opList = pageContext.getRequest().getAttribute( CONTROL_PURVIEW_OPERATIONS ) ;
            if( opList != null )
            {
                List<String> operations = (List<String>)opList ;
                if( operations != null && !operations.isEmpty() )
                {
                    if( operations.contains( purview ) )
                    {
                        component.setHasAuthority( true ) ;
                        return ;
                    }
                    component.setHasAuthority( false ) ;
                }
                else
                {
                    component.setHasAuthority( false ) ;
                }
            }
            else
            {
                component.setHasAuthority( false ) ;
            }
        }
        else
        {
            component.setHasAuthority( true ) ;
        }
    }

    @Override
    protected boolean hasBody()
    {
        return true ;
    }

    /**
     * 设置按钮文本。
     * @param value 按钮文本
     */
    public void setValue( String value )
    {
        this.value = value ;
    }
    
    /**
     * 设置该控件应用的样式表类名称（对应 HTML 元素的 class 属性）。
     * @param cssClass 样式表类名称
     */
    public void setCssClass( String cssClass )
    {
        this.cssClass = cssClass;
    }

    /**
     * 设置按钮的权限字符串。<br />
     * @param purview 权限字符串
     */
    public void setPurview( String purview )
    {
        this.purview = purview;
    }
    /**
     * 设置是否禁用。
     * @param disabled 是否禁用
     */
    public void setDisabled( String disabled )
    {
        this.disabled = disabled;
    }

    /**
     * 设置按钮提示信息。
     * @param title 提示信息
     */
    public void setTitle( String title )
    {
        this.title = title;
    }
    
    /**
     * 设置按钮style。
     * @param style 按钮style
     */
    public void setStyle( String style )
    {
        this.style = style;
    }
    
    /**
     * 设置按钮图标样式。
     * @param iconClass 按钮图标样式
     */
    public void setIconClass( String iconClass )
    {
        this.iconClass = iconClass;
    }

    private String value ;
    private String iconClass ;
    private String cssClass ;
    private String title ;
    private String purview ;
    private String disabled ;
    private String style ;
    private ActionButton component = new ActionButton() ;
    private static final long serialVersionUID = 6067182238328801931L ;
}