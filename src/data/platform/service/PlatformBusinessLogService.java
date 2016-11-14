package data.platform.service ;

import java.util.Date ;
import java.util.HashMap ;
import java.util.Map ;

import org.apache.commons.lang.StringUtils ;
import org.springframework.stereotype.Service ;

import data.framework.pagination.model.PagingResult ;
import data.framework.support.AbstractService ;
import data.platform.entity.EntityPlatformBusinessLog ;

/**
 * 平台－业务日志服务类。
 * 
 * @author wanggq
 * 
 */
@Service
public class PlatformBusinessLogService extends AbstractService
{
    /**
     * 分页查询数据
     * 
     * @param operatorChineseName 操作人中文名
     * @param operatorTMISAccount 操作人TMIS账号
     * @param operatorADAccount 操作人AD账号
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @param operatorType 操作类型
     * @param module 模式
     * @param sortField 数据库排序字段
     * @param sort 排序方式（ASC|DESC）
     * @param currentPage 当前页数
     * @param pageSize 页大小
     * @return 分页查询集合
     */
    public PagingResult<EntityPlatformBusinessLog> serachBusinessLogs( String operatorChineseName, String operatorTMISAccount, String operatorADAccount, Date startDate, Date endDate,
            String operateType, String module, String sortField, String sort, int currentPage, int pageSize )
    {
        Map<String,Object> param = new HashMap<String,Object>() ;
        param.put( "operatorChineseName", operatorChineseName ) ;
        param.put( "operatorTMISAccount", operatorTMISAccount ) ;
        param.put( "operatorADAccount", operatorADAccount ) ;
        param.put( "startDate", startDate ) ;
        param.put( "endDate", endDate ) ;
        param.put( "operateType", operateType ) ;
        param.put( "module", module ) ;
        if( StringUtils.isBlank( sortField ) )
            sortField = "CreateTime" ;
        if( StringUtils.isBlank( sort ) )
            sort = "DESC" ;
        return selectPaging( "platformBusinessLog.selectPaging", param, sortField, sort, currentPage, pageSize ) ;
    }

}
