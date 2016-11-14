package data.framework.pagination.dialect ;

import org.apache.commons.lang.StringUtils;

/**
 * SqlServer数据库方言。
 * 
 * @author wanggq
 * 
 */
public class SqlServerDialect extends Dialect
{
    @Override
    public boolean supportsLimit()
    {
        return true ;
    }

    @Override
    public String getLimitString( String sql, int offset, int limit, String sortField, String sort )
    {
        if( offset < 0 )
            throw new UnsupportedOperationException( "offset must be greater than or equal to zero" ) ;
        StringBuilder pagingSelect = new StringBuilder( sql.length() + 100 ) ;
        pagingSelect.append( "select * from (select u.*,ROW_NUMBER() over(order by " ) ;
        pagingSelect.append( sortField ) ;
        pagingSelect.append( " " ) ;
        pagingSelect.append( sort ) ;
        pagingSelect.append( ") as rownum FROM (" ) ;
        pagingSelect.append( sql ) ;
        pagingSelect.append( ") u" ) ;
        pagingSelect.append( ") t where t.rownum>" ).append( offset ) ;
        int end = limit + offset ;
        pagingSelect.append( " and t.rownum<=" ).append( end ) ;
        return pagingSelect.toString() ;
    }

	@Override
	public String getComplexLimitString(String sql, int offset, int limit,
			String sortFieldOne, String sortFieldTwo, String sortOne,
			String sortTwo) 
	{
		if( offset < 0 )
            throw new UnsupportedOperationException( "offset must be greater than or equal to zero" ) ;
        StringBuilder pagingSelect = new StringBuilder( sql.length() + 100 ) ;
        pagingSelect.append( "select * from (select u.*,ROW_NUMBER() over(order by " ) ;
        pagingSelect.append( sortFieldOne ) ;
        pagingSelect.append( " " ) ;
        pagingSelect.append( sortOne ) ;
        if(StringUtils.isNotEmpty(sortFieldTwo) && StringUtils.isNotEmpty(sortTwo))
        {
        	 pagingSelect.append(",");
             pagingSelect.append( sortFieldTwo ) ;
             pagingSelect.append( " " ) ;
             pagingSelect.append( sortTwo ) ;
        }
        pagingSelect.append( ") as rownum FROM (" ) ;
        pagingSelect.append( sql ) ;
        pagingSelect.append( ") u" ) ;
        pagingSelect.append( ") t where t.rownum>" ).append( offset ) ;
        int end = limit + offset ;
        pagingSelect.append( " and t.rownum<=" ).append( end ) ;
        return pagingSelect.toString() ;
	}
}