package data.platform.common ;

import java.util.List ;

/**
 * 树状数据结构的公共类。
 * 
 * @author wanggq
 */
public class MenuTree
{
    /**
     * 获节点文本。
     * @return 节点文本
     */
    public String getTitle()
    {
        return title ;
    }

    /**
     * 设置节点文本。
     * @param title 节点文本
     */
    public void setTitle( String title )
    {
        this.title = title ;
    }

    /**
     * 获取节点值。
     * @return 节点值
     */
    public String getCode()
    {
        return code ;
    }

    /**
     * 设置节点值。
     * @param code 节点值
     */
    public void setCode( String code )
    {
        this.code = code ;
    }

    /**
     * 获取节点样式表。
     * @return 节点样式表
     */
    public String getCls()
    {
        return cls ;
    }

    /**
     * 设置节点样式表。
     * @param cls 节点样式表
     */
    public void setCls( String cls )
    {
        this.cls = cls ;
    }
    
    /**
     * 获取菜单URL。
     * @return 菜单URL
     */
    public String getUrl()
    {
        return url ;
    }

    /**
     * 设置菜单URL。
     * @param url 菜单URL
     */
    public void setUrl( String url )
    {
        this.url = url ;
    }

    /**
     * 获取子菜单集合。
     * @return 子菜单集合
     */
    public List<MenuTree> getMenus()
    {
        return menus ;
    }

    /**
     * 设置子菜单集合。
     * @param menus 子菜单集合
     */
    public void setMenus( List<MenuTree> menus )
    {
        this.menus = menus ;
    }
    
    private String title ;
    private String code ;
    private String cls ;
    private String url ;
    private List<MenuTree> menus ;
}