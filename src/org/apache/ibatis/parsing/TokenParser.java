package org.apache.ibatis.parsing;

/**
 * 关键字解析接口。
 * 
 * @author wanggq
 */
public interface TokenParser
{
    public String parse( String text ) ;
}