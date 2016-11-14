package data.framework.web.ui;

import java.io.Writer ;

import org.apache.commons.lang.StringUtils ;

/**
 * 按钮控件的组件模型类。
 * @author wanggq
 */
public class ActionButton extends AbstractComponent
{
    @Override
    public String getControlName()
    {
        return "ActionButton" ;
    }

    @Override
    public void render( Writer out ) throws Exception
    {
        if( getHasAuthority() != null && getHasAuthority() )
        {
            out.append( "<button" ) ;
            if( StringUtils.isNotBlank( getId() ) )
            {
                out.append( " id = \"" ).append( getId() ).append( "\"" ) ;
            }
            if( StringUtils.isNotBlank( getCssClass() ) )
            {
                out.append( " class = \"" ).append( getCssClass() ).append( "\"" ) ;
            }
            if( StringUtils.isNotBlank( getTitle() ) )
            {
                out.append( " title = \"" ).append( getTitle() ).append( "\"" ) ;
            }
            if( getDisabled() != null )
            {
                if( getDisabled() )
                    out.append( " disabled = \"disabled\"" ) ;
            }
            if( StringUtils.isNotBlank( getStyle()) )
            {
                out.append( " style = \"" ).append( getStyle() ).append( "\"" ) ;
            }
            out.append( ">" ) ;
            if( StringUtils.isNotBlank( getIconClass() ) )
                out.append( "<i class=\"" ).append( getIconClass() ).append( "\"></i>" ) ;
            if( StringUtils.isNotBlank( getValue() ) )
                out.append( getValue() ) ;
            out.append( "</button>" ) ;
        }
    }

    /**
     * 设置控件ID，必须的属性。
     * @param id 控件ID
     */
    public void setId( String id )
    {
        this.id = id;
    }
    
    /**
     * 获取控件ID。
     * @return 控件ID
     */
    public String getId()
    {
        return id;
    }

    /**
     * 获取按钮文本。
     * @return 按钮文本
     */
    public String getValue()
    {
        return value ;
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
     * 获取该控件应用的样式表类名称（对应 HTML 元素的 class 属性）。
     * @return 样式表类名称
     */
    public String getCssClass()
    {
        return cssClass;
    }
    
    /**
     * 获取按钮图标样式。
     * @return 按钮图标样式
     */
    public String getIconClass()
    {
        return iconClass ;
    }

    /**
     * 设置按钮图标样式。
     * @param iconClass 按钮图标样式
     */
    public void setIconClass( String iconClass )
    {
        this.iconClass = iconClass ;
    }

    /**
     * 设置是否禁用。
     * @param disabled 是否禁用
     */
    public void setDisabled( Boolean disabled )
    {
        this.disabled = disabled;
    }
    /**
     * 获取是否禁用。
     * @return 是否禁用
     */
    public Boolean getDisabled()
    {
        return disabled;
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
     * 获取按钮style。
     * @return 按钮style
     */
    public String getStyle()
    {
        return style;
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
     * 获取按钮提示信息。
     * @return 提示信息
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * 获取一个布尔值，按钮是否有操作权限。
     * @return 按钮是否有操作权限
     */
    public Boolean getHasAuthority()
    {
        return hasAuthority;
    }
    
    /**
     * 设置一个布尔值，指示按钮是否有操作权限。
     * @param hasAuthority 按钮是否有操作权限
     */
    public void setHasAuthority( Boolean hasAuthority )
    {
        this.hasAuthority = hasAuthority;
    }

    private String id ;
    private String value ; 
    private String cssClass ;
    private String iconClass ;
    private String title ;
    private Boolean disabled ;
    private String style ;
    private Boolean hasAuthority ;
    private static final long serialVersionUID = -3925409892616037755L ;
}