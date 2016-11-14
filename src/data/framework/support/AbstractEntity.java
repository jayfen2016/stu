package data.framework.support;

import java.io.Serializable ;

import data.platform.authority.security.SecurityContext ;
/**
 * 实体对象的抽象基类。<br />
 * 提供了公有的 getId 方法和公共的 setId 方法。<br />
 * 提供公共的operator（操作者）属性，默认获取当前登录用户的用户名。
 * 保护的 version 属性为乐观锁字段。<br />
 * 重载的 hashCode 方法取 getId 方法返回值的 hashCode，如果是未保存对象，将返回从 Object 继承的 hashCode。<br />
 * 重载的 equals 方法根据重载的 hashCode 进行比较。
 *
 * @author wanggq
 */
public class AbstractEntity implements Serializable
{
    /**
     * 取得对象的索引值，如果是未经数据访问对象处理过的实例，该方法将返回 null。
     * @return 对象的索引值
     */
    public String getId()
    {
        return id ;
    }

    /**
     * 取得对象的索引值。<br />
     * @param id 对象的索引值
     */
    public void setId( String id )
    {
        this.id = id ;
    }
    
    /**
     * 获取操作人。
     * @return 操作人
     */
    public String getOperator()
    {
        return operator ;
    }
    
    /**
     * 设置操作人。
     * @param operator 操作人
     */
    public void setOperator( String operator )
    {
        this.operator = operator ;
    }

    /**
     * 获取乐观锁的值。
     * @return 乐观锁的值
     */
    protected Integer getVersion()
    {
        return version ;
    }

    /**
     * 设置乐观锁的值。
     * @param version 乐观锁的新值
     */
    protected void setVersion( Integer version )
    {
        this.version = version ;
    }

    @Override
    public int hashCode()
    {
        return ( id == null ) ? super.hashCode() : id.hashCode() ;
    }

    @Override
    public boolean equals( Object o )
    {
        if( o == this )
            return true ;

        if( o == null )
            return false ;

        if( !(o instanceof AbstractEntity) )
            return false ;

        if( ( this.id == null ) || ( ((AbstractEntity)o).id == null ) )  
            return false ;

        return ( id.equals( ((AbstractEntity)o).id ) ) ;   
    }

    private String id ;
    private String operator = SecurityContext.getPrincipal() == null ? "" : SecurityContext.getPrincipal().getUsername() ;
    private Integer version ;
    private static final long serialVersionUID = 1822271057009738491L ;
}