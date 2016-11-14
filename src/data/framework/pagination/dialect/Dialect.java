package data.framework.pagination.dialect ;

/**
 * 数据库方言。
 * @author wanggq
 *
 */
public abstract class Dialect
{
    /**
     * 数据库本身是否支持分页查询
     * 
     * @return {@code true} 支持分页查询
     */
    public abstract boolean supportsLimit() ;

    /**
     * 将sql包装成数据库支持的特有查询语句
     * 
     * @param sql SQL语句
     * @param offset 开始位置
     * @param limit 每页显示的记录数
     * @param sortField 排序字段
     * @param sort 排序方式
     * @return 数据库专属分页查询sql
     */
    public abstract String getLimitString( String sql, int offset, int limit, String sortField, String sort ) ;

    /**
     * 将sql包装成统计总数SQL
     * 
     * @param sql SQL语句
     * @return 统计总数SQL
     */
    public String getCountString( String sql )
    {
        return "select count(1) from (" + sql + ") tmp_count" ;
    }
    
    /**
     * 将sql包装成数据库支持的特有查询语句(多个排序)
     * @param sql SQL语句
     * @param offset 开始位置
     * @param limit 每页显示的记录数
     * @param sortField 排序字段1
     * @param sortField 排序字段2
     * @param sort 排序方式1
     *  @param sort 排序方式2
     * @return 数据库专属分页查询sql
     */
    public abstract String getComplexLimitString( String sql, int offset, int limit, String sortFieldOne,String sortFieldTwo, String sortOne, String sortTwo ) ;

}