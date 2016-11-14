package data.platform.authority.data ;

import java.util.Properties ;

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
import org.springframework.beans.factory.annotation.Autowired ;

import data.framework.pagination.PaginationInterceptor ;
import data.framework.support.ConfigContext ;
import data.framework.support.Logger ;
import data.platform.authority.security.PrincipalDetails ;
import data.platform.authority.security.SecurityContext ;

/**
 * 数据权限过滤器。
 * @author wanggq
 *
 */
@Intercepts( { @Signature( type = Executor.class, method = "query", args = { MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class } ) } )
public class DataAuthorityInterceptor implements Interceptor
{
    private Logger logger = Logger.getLogger( this.getClass() ) ;
    private static final int MAPPED_STATEMENT_INDEX = 0 ;
    private static final int PARAMETER_INDEX = 1 ;
    //private static final int ROWBOUNDS_INDEX = 2 ;
    @Autowired
    private PaginationInterceptor paginationInterceptor ;
    
    @Override
    public Object intercept( Invocation invocation ) throws Throwable
    {
        final Object[] queryArgs = invocation.getArgs() ;
        final MappedStatement ms = (MappedStatement)queryArgs[MAPPED_STATEMENT_INDEX] ;
        if( ms.isNeedDataAuthority() )
        {
            String databaseName = ConfigContext.getValue( "framework.table.prefix['platform']" ) ;
            String loginAccount = "" ;
            String roleId = "" ;
            if( SecurityContext.getPrincipal() != null )
            {
                PrincipalDetails userDetails = SecurityContext.getPrincipal() ;
                loginAccount = userDetails.getUsername() ;
                roleId = userDetails.getRoleId() ;
            }
            else
            {
                PrincipalDetails userDetails = DataAuthorityThreadLocal.getCurrentThreadVariables() ;
                loginAccount = userDetails.getUsername() ;
                roleId = userDetails.getRoleId() ;
            }
            final Object parameter = queryArgs[PARAMETER_INDEX] ;
            final BoundSql boundSql = ms.getBoundSql( parameter ) ;
            String sql = boundSql.getSql().trim() ;
            logger.info( "sql: {0}", boundSql.getSql().trim() ) ;
            StringBuffer newSql = new StringBuffer() ;
            newSql.append( "select top 100 percent data.* from(" ) ;
            newSql.append( sql ) ;
            newSql.append( ") data " ) ;
            newSql.append( " inner join " ) ;
            newSql.append( "(" ) ;
            newSql.append( " select dataAuth.TMISCode_ControlledTaxFilingUnit,unit.JDECode,unit.TaxFilingUnitType from " ).append( databaseName ).append( "tbl_Platform_Users users" ) ;
            newSql.append( " inner join " ).append( databaseName ).append( "tbl_Platform_UserRoleMapping mapping on mapping.UserID=users.id" ) ;
            newSql.append( " inner join " ).append( databaseName ).append( "tbl_Platform_RoleDataAuthority dataAuth on dataAuth.RoleID=mapping.RoleID" ) ;
            newSql.append( " inner join " ).append( databaseName ).append( "tbl_MD_TaxFilingUnit unit on unit.TMISCode=dataAuth.TMISCode_ControlledTaxFilingUnit" ) ;
            newSql.append( " where users.LoginAccount='" ).append( loginAccount ).append( "'" ) ;
            newSql.append( " and mapping.RoleID='" ).append( roleId ).append( "'" ) ;
            newSql.append( " group by dataAuth.TMISCode_ControlledTaxFilingUnit,unit.JDECode,unit.TaxFilingUnitType" ) ;
            newSql.append( " ) authority" ) ;
            newSql.append( " on authority.TMISCode_ControlledTaxFilingUnit=data._TMISCodeBelongTaxFilingUnit_" ) ;
            newSql.append( " order by authority.TaxFilingUnitType,authority.JDECode " ) ;
            logger.info( " data authority sql: {0}", newSql.toString() ) ;
            MappedStatement newMs = newMappedStatement( ms, boundSql, newSql.toString() ) ;

            queryArgs[MAPPED_STATEMENT_INDEX] = newMs ;
        }
        if( ms.isNeedPaging() )
        {
            Invocation newInvocation = new Invocation( invocation.getTarget(), invocation.getMethod(), queryArgs ) ;
            return paginationInterceptor.intercept( newInvocation ) ;
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