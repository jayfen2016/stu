package data.framework.pagination.rowbounds ;

import org.apache.ibatis.session.RowBounds ;

/**
 * 自定义RowBounds，集成Mybatis的RowBounds。
 * @author wanggq
 *
 */
public class PaginationRowBounds extends RowBounds
{

	private String sortFieldOne ;
    private String sortOne ;
    private String sortFieldTwo ;
    private String sortTwo ;
    
    public String getSortFieldOne() 
    {
    	return sortFieldOne;
    }
    
    public String getSortOne() 
    {
    	return sortOne;
    }
    
    public String getSortFieldTwo() 
    {
		return sortFieldTwo;
	}

	public String getSortTwo() 
	{
		return sortTwo;
	}

	public PaginationRowBounds( int offset, int limit, String sortFieldOne, String sortOne,String sortFieldTwo,String sortTwo)
    {
        super( offset, limit ) ;
        this.sortOne = sortOne ;
        this.sortFieldOne = sortFieldOne ;
        this.sortTwo=sortTwo;
        this.sortFieldTwo=sortFieldTwo;
    }

	public PaginationRowBounds( int offset, int limit, String sortFieldOne, String sortOne)
    {
        super( offset, limit ) ;
        this.sortOne = sortOne ;
        this.sortFieldOne = sortFieldOne ;
    }
	
    public int getCurrentPage()
    {
        if( getOffset() <= 0 )
            return 1 ;
        else
            return getOffset() / getLimit() + 1 ;
    }
    
    public static void main(String[] args)
    {
        System.out.println(10/12) ;
    }
}