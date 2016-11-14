package data.platform.entity ;

import java.util.Date ;

import data.framework.support.AbstractEntity ;
/**
 * 平台-数据字典实体类。
 * @author wanggq
 *
 */
public class EntityPlatformDataDictionary extends AbstractEntity
{
    /**
     * 获取数据字典代码。
     * @return 数据字典代码
     */
    public String getDictionaryCode()
    {
        return dictionaryCode ;
    }

    /**
     * 设置数据字典代码。
     * @param dictionaryCode 数据字典代码
     */
    public void setDictionaryCode( String dictionaryCode )
    {
        this.dictionaryCode = dictionaryCode ;
    }

    /**
     * 获取数据字典名称。
     * @return 数据字典名称
     */
    public String getDictionaryName()
    {
        return dictionaryName ;
    }

    /**
     * 设置数据字典名称。
     * @param dictionaryName 数据字典名称
     */
    public void setDictionaryName( String dictionaryName )
    {
        this.dictionaryName = dictionaryName ;
    }

    /**
     * 获取父级子典项。
     * @return 父级子典项
     */
    public String getParentDictionary()
    {
        return parentDictionary ;
    }

    /**
     * 设置父级子典项。
     * @param parentDictionary 父级子典项
     */
    public void setParentDictionary( String parentDictionary )
    {
        this.parentDictionary = parentDictionary ;
    }

    /**
     * 获取数据字典项类型。
     * @return 数据字典项类型
     */
    public String getDictionaryType()
    {
        return dictionaryType ;
    }

    /**
     * 设置数据字典项类型。
     * @param dictionaryType 数据字典项类型
     */
    public void setDictionaryType( String dictionaryType )
    {
        this.dictionaryType = dictionaryType ;
    }

    /**
     * 获取字典项对应模块。
     * @return 字典项对应模块
     */
    public String getDictionaryModule()
    {
        return dictionaryModule ;
    }

    /**
     * 设置字典项对应模块。
     * @param dictionaryModule 字典项对应模块
     */
    public void setDictionaryModule( String dictionaryModule )
    {
        this.dictionaryModule = dictionaryModule ;
    }

    /**
     * 获取字典项描述。
     * @return 字典项描述
     */
    public String getRemark()
    {
        return remark ;
    }

    /**
     * 设置字典项描述。
     * @param remark 字典项描述
     */
    public void setRemark( String remark )
    {
        this.remark = remark ;
    }
    
    /**
     * 获取字典项级别。
     * @return 字典项级别
     */
    public int getDictionaryLevel()
    {
        return dictionaryLevel ;
    }

    /**
     * 设置字典项级别。
     * @param dictionaryLevel 字典项级别
     */
    public void setDictionaryLevel( int dictionaryLevel )
    {
        this.dictionaryLevel = dictionaryLevel ;
    }

    /**
     * 获取创建时间。
     * @return 创建时间
     */
    public Date getCreateTime()
    {
        return createTime ;
    }
    
    /**
     * 设置创建时间。
     * @param createTime 创建时间
     */
    public void setCreateTime( Date createTime )
    {
        this.createTime = createTime ;
    }
    
    /**
     * 获取更新时间。
     * @return 更新时间
     */
    public Date getUpdateTime()
    {
        return updateTime ;
    }
    
    /**
     * 设置更新时间。
     * @param updateTime 更新时间
     */
    public void setUpdateTime( Date updateTime )
    {
        this.updateTime = updateTime ;
    }
    
    private String dictionaryCode ;
    private String dictionaryName ;
    private String parentDictionary ;
    private String dictionaryType ;
    private String dictionaryModule ;
    private String remark ;
    private int dictionaryLevel ;
    private Date createTime ;
    private Date updateTime ;
    private static final long serialVersionUID = -5738420005617254043L ;
}