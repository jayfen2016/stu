package data.framework.pagination ;

import java.sql.Connection ;
import java.util.Properties ;

import org.apache.commons.lang.StringUtils ;
import org.apache.ibatis.executor.Executor ;
import org.apache.ibatis.mapping.BoundSql ;
import org.apache.ibatis.mapping.MappedStatement ;
import org.apache.ibatis.mapping.ParameterMapping ;
import org.apache.ibatis.mapping.SqlSource ;
import org.apache.ibatis.plugin.Interceptor ;
import org.apache.ibatis.plugin.Intercepts ;
import org.apache.ibatis.plugin.Invocation ;
import org.apache.ibatis.plugin.Plugin ;
import org.apache.ibatis.plugin.Signature ;
import org.apache.ibatis.session.ResultHandler ;
import org.apache.ibatis.session.RowBounds ;

import data.framework.pagination.dialect.DatabaseDialectShortName ;
import data.framework.pagination.dialect.Dialect ;
import data.framework.pagination.helper.DialectHelper ;
import data.framework.pagination.helper.SqlHelper ;
import data.framework.pagination.rowbounds.PaginationRowBounds ;
import data.framework.support.Logger ;

/**
 * 分页过滤器。
 * @author wanggq
 *
 */
@Intercepts( { @Signature( type = Executor.class, method = "query", args = { MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class } ) } )
public class PaginationInterceptor implements Interceptor
{
    private Logger logger = Logger.getLogger( this.getClass() ) ;
    private static final int MAPPED_STATEMENT_INDEX = 0 ;
    private static final int PARAMETER_INDEX = 1 ;
    private static final int ROWBOUNDS_INDEX = 2 ;
//    private static final int RESULT_HANDLER_INDEX = 3 ;
    private static final ThreadLocal<Integer> PAGINATION_TOTAL = new ThreadLocal<Integer>()
    {
        @Override
        protected Integer initialValue()
        {
            return 0 ;
        }
    } ;
    
    private static final ThreadLocal<Integer> PAGINATION_CURRENT_PAGE = new ThreadLocal<Integer>()
    {
        @Override
        protected Integer initialValue()
        {
            return 0 ;
        }
    } ;

    private Dialect dialect ;

    /**
     * 获取查询数据总量。
     * @return 数据总量
     */
    public static int getPaginationTotal()
    {
        return PAGINATION_TOTAL.get() ;
    }
    
    /**
     * 获取查询当前页。
     * @return 当前页
     */
    public static int getPaginationCurrentPage()
    {
        return PAGINATION_CURRENT_PAGE.get() ;
    }

    public static void clean()
    {
        PAGINATION_TOTAL.remove() ;
    }

    @Override
    public Object intercept( Invocation invocation ) throws Throwable
    {
        final Object[] queryArgs = invocation.getArgs() ;
        final MappedStatement ms = (MappedStatement)queryArgs[MAPPED_STATEMENT_INDEX] ;

        boolean intercept = ms.isNeedPaging() ;
        if( !intercept )
            return invocation.proceed() ;
        final Object parameter = queryArgs[PARAMETER_INDEX] ;
        final Object boundsObject = queryArgs[ROWBOUNDS_INDEX] ;
        
        if( dialect.supportsLimit() && boundsObject instanceof PaginationRowBounds )
        {
            final PaginationRowBounds rowBounds = (PaginationRowBounds)boundsObject ;

            int offset = rowBounds.getOffset() ;
            int limit = rowBounds.getLimit() ;
            int currentPage = rowBounds.getCurrentPage() ;
            final BoundSql boundSql = ms.getBoundSql( parameter ) ;
            String sql = boundSql.getSql().trim() ;
            logger.info( "sql: {0}", boundSql.getSql().trim() ) ;
            logger.info( "limit sql: {0}", dialect.getComplexLimitString( boundSql.getSql(), offset, limit, rowBounds.getSortFieldOne(),rowBounds.getSortFieldTwo(),rowBounds.getSortOne(),rowBounds.getSortTwo() ) ) ;

            final Executor executor = (Executor)invocation.getTarget() ;
            Connection connection = executor.getTransaction().getConnection() ;
            int count = SqlHelper.getCount( ms, connection, parameter, dialect ) ;
            PAGINATION_TOTAL.set( count ) ;
            int totalPage = 1 ;
            if( count > limit )
                totalPage = count % limit == 0 ? ( count / limit ) : ( count / limit + 1 ) ;
            if( currentPage > totalPage )
            {
                currentPage = currentPage - 1 ;
                offset = ( currentPage - 1 ) * limit ;
            }
            PAGINATION_CURRENT_PAGE.set( currentPage ) ;
            
            String limitSql = dialect.getComplexLimitString( sql, offset, limit, rowBounds.getSortFieldOne(),rowBounds.getSortFieldTwo(), rowBounds.getSortOne(),rowBounds.getSortTwo() ) ;
            MappedStatement newMs = newMappedStatement( ms, boundSql, limitSql ) ;

            queryArgs[ROWBOUNDS_INDEX] = new RowBounds( RowBounds.NO_ROW_OFFSET, RowBounds.NO_ROW_LIMIT ) ;
            queryArgs[MAPPED_STATEMENT_INDEX] = newMs ;
        }

        return invocation.proceed() ;
    }

    @Override
    public Object plugin( Object target )
    {
        return Plugin.wrap( target, this ) ;
    }

    @Override
    public void setProperties( Properties properties )
    {
        String dialectClass = properties.getProperty( "dialectClass" ) ;
        if( StringUtils.isBlank( dialectClass ) )
        {
            String dialectShortName = properties.getProperty( "dialect" ) ;
            checkDialect( dialectShortName ) ;
            dialect = DialectHelper.getDialect( DatabaseDialectShortName.valueOf( dialectShortName.toUpperCase() ) ) ;
        }
        else
        {
            try
            {
                dialect = (Dialect)Class.forName( dialectClass ).newInstance() ;
            }
            catch( Exception e )
            {
                throw new RuntimeException( "Plug-in [PaginationInterceptor] cannot create dialect instance by dialectClass: " + dialectClass ) ;
            }
        }
    }

    private void checkDialect( String dialectShortName )
    {
        try
        {
            DatabaseDialectShortName.valueOf( dialectShortName.toUpperCase() ) ;
        }
        catch( Exception e )
        {
            throw new RuntimeException( "Plug-in [PaginationInterceptor] the dialect of the attribute value is invalid!" ) ;
        }

    }

    private MappedStatement newMappedStatement( final MappedStatement ms, final BoundSql boundSql, final String sql )
    {
        BoundSql newBoundSql = newBoundSql( ms, boundSql, sql ) ;
        RawSqlSource sqlSource = new RawSqlSource( newBoundSql ) ;
        MappedStatement.Builder builder = new MappedStatement.Builder( ms.getConfiguration(), ms.getId(), sqlSource, ms.getSqlCommandType() ) ;

        builder.resource( ms.getResource() ) ;
        builder.fetchSize( ms.getFetchSize() ) ;
        builder.statementType( ms.getStatementType() ) ;
        builder.keyGenerator( ms.getKeyGenerator() ) ;
        String[] keyProperties = ms.getKeyProperties() ;
        builder.keyProperty( keyProperties == null ? null : keyProperties[0] ) ;

        // setStatementTimeout()
        builder.timeout( ms.getTimeout() ) ;

        // setStatementResultMap()
        builder.parameterMap( ms.getParameterMap() ) ;

        // setStatementResultMap()
        builder.resultMaps( ms.getResultMaps() ) ;
        builder.resultSetType( ms.getResultSetType() ) ;

        // setStatementCache()
        builder.cache( ms.getCache() ) ;
        builder.flushCacheRequired( ms.isFlushCacheRequired() ) ;
        builder.useCache( ms.isUseCache() ) ;

        builder.needDataAuthority( ms.isNeedDataAuthority() ) ;
        builder.needPaging( ms.isNeedPaging() ) ;
        
        return builder.build() ;
    }

    private BoundSql newBoundSql( final MappedStatement ms, final BoundSql boundSql, final String sql )
    {
        BoundSql newBoundSql = new BoundSql( ms.getConfiguration(), sql, boundSql.getParameterMappings(), boundSql.getParameterObject() ) ;

        for( ParameterMapping mapping : boundSql.getParameterMappings() )
        {
            String prop = mapping.getProperty() ;
            if( boundSql.hasAdditionalParameter( prop ) )
            {
                newBoundSql.setAdditionalParameter( prop, boundSql.getAdditionalParameter( prop ) ) ;
            }
        }

        return newBoundSql ;
    }

    public class RawSqlSource implements SqlSource
    {
        private BoundSql boundSql ;

        public RawSqlSource( BoundSql boundSql )
        {
            this.boundSql = boundSql ;
        }

        @Override
        public BoundSql getBoundSql( Object parameterObject )
        {
            return boundSql ;
        }
    }
}