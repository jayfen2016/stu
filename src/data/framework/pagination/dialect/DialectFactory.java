package data.framework.pagination.dialect ;

/**
 * 获取方言工厂类。
 * @author wanggq
 *
 */
public abstract class DialectFactory
{
    public static Dialect buildDialect( DatabaseDialectShortName databaseName )
    {
        switch( databaseName )
        {
            case MSSQL:
                return new SqlServerDialect() ;
            default:
                throw new UnsupportedOperationException() ;
        }
    }
}