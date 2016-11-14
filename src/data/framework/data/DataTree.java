package data.framework.data ;

import java.util.List ;

/**
 * 树状数据结构的公共类。
 * 
 * @author wanggq
 */
public class DataTree
{
    /**
     * 获节点文本。
     * @return 节点文本
     */
    public String getLabel()
    {
        return label ;
    }

    /**
     * 设置节点文本。
     * @param label 节点文本
     */
    public void setLabel( String label )
    {
        this.label = label ;
    }

    /**
     * 获取节点值。
     * @return 节点值
     */
    public String getId()
    {
        return id ;
    }

    /**
     * 设置节点值。
     * @param id 节点值
     */
    public void setId( String id )
    {
        this.id = id ;
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
     * 获取子节点集合。
     * @return 子节点集合
     */
    public List<DataTree> getChildren()
    {
        return children ;
    }

    public String getDictionaryCode() {
        return dictionaryCode;
    }

    public void setDictionaryCode(String dictionaryCode) {
        this.dictionaryCode = dictionaryCode;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 设置子节点集合。
     * @param children 子节点集合
     */

    public void setChildren( List<DataTree> children )
    {
        this.children = children ;
    }
    
    private String label ;
    private String id ;
    private String cls ;
    private String dictionaryCode;
    private String remark;
    private List<DataTree> children ;
}