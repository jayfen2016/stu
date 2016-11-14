package data.framework.pagination.helper ;

import data.framework.pagination.dialect.Dialect ;
import data.framework.support.Logger ;

import org.apache.ibatis.mapping.BoundSql ;
import org.apache.ibatis.mapping.MappedStatement ;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler ;

import java.sql.* ;

/**
 * sql语句工具类。
 * 
 * @author wanggq
 */
public abstract class SqlHelper
{
    private static Logger logger = Logger.getLogger( SqlHelper.class ) ;

    public static int getCount( final MappedStatement ms, final Connection connection, final Object parameterObject, Dialect dialect ) throws SQLException
    {
        BoundSql boundSql = ms.getBoundSql( parameterObject ) ;
        String countSql = dialect.getCountString( boundSql.getSql() ) ;

        logger.debug( "Total count SQL：{0}", countSql ) ;
        logger.debug( "Parameters: {0} ", parameterObject ) ;

        PreparedStatement stmt = null ;
        ResultSet rs ;
        try
        {
            stmt = connection.prepareStatement( countSql ) ;
            DefaultParameterHandler handler = new DefaultParameterHandler( ms, parameterObject, boundSql ) ;
            handler.setParameters( stmt ) ;
            rs = stmt.executeQuery() ;

            int count = 0 ;
            if( rs.next() )
            {
                count = rs.getInt( 1 ) ;
            }

            return count ;
        }
        finally
        {
            closeStatement( stmt ) ;
        }
    }

    private static void closeStatement( Statement statement )
    {
        if( statement != null )
        {
            try
            {
                statement.close() ;
            }
            catch( SQLException e )
            {
            }
        }
    }
}