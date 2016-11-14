package data.framework.pagination.helper ;

import data.framework.pagination.dialect.DatabaseDialectShortName ;
import data.framework.pagination.dialect.Dialect ;
import data.framework.pagination.dialect.DialectFactory ;

import java.util.HashMap ;
import java.util.Map ;

/**
 * Dialect工具类。
 * 
 * @author wanggq
 * 
 */
public abstract class DialectHelper
{
    private static Map<DatabaseDialectShortName,Dialect> MAPPERS = new HashMap<DatabaseDialectShortName,Dialect>() ;

    public static Dialect getDialect( DatabaseDialectShortName dialectShortName )
    {
        if( MAPPERS.containsKey( dialectShortName ) )
        {
            return MAPPERS.get( dialectShortName ) ;
        }
        else
        {
            Dialect dialect = DialectFactory.buildDialect( dialectShortName ) ;
            MAPPERS.put( dialectShortName, dialect ) ;
            return dialect ;
        }
    }
}