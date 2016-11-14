package data.platform.service;

import java.util.Date ;
import java.util.HashMap ;
import java.util.Map ;

import org.apache.commons.lang.StringUtils ;
import org.springframework.stereotype.Service ;

import data.framework.pagination.model.PagingResult ;
import data.framework.support.AbstractService ;
import data.platform.entity.EntityPlatformSystemLog ;
/**
 * 平台-系统日志服务类。
 * @author wanggq
 */
@Service
public class PlatformSystemLogService extends AbstractService
{
    
    /**
     * 保存系统日志信息。
     * @param entity 系统日志实体
     */
    public void save( EntityPlatformSystemLog entity )
    {
        insert( "platformSystemLog.insertSystemLog", entity ) ;
    }
    
    /**
     * 分页查询数据。
     * @param remark 日志描述
     * @param logType 日志级别
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @param sortField 数据库排序字段
     * @param sort 排序方式（ASC|DESC）
     * @param currentPage 当前页数
     * @param pageSize 页大小
     * @return 分页查询集合
     */
    public PagingResult<EntityPlatformSystemLog> searchSystemLog( String remark, String logType, Date startDate, Date endDate, String sortField, String sort, int currentPage, int pageSize )
    {
        Map<String,Object> param = new HashMap<String,Object>() ;
        param.put( "remark", remark ) ;
        param.put( "logType", logType ) ;
        param.put( "startDate", startDate ) ;
        param.put( "endDate", endDate ) ;
        if( StringUtils.isBlank( sortField ) )
            sortField = "LogTime" ;
        if( StringUtils.isBlank( sort ) )
            sort = "ASC" ;
        return selectPaging( "platformSystemLog.selectPaging", param, sortField, sort, currentPage, pageSize ) ;
    }
}