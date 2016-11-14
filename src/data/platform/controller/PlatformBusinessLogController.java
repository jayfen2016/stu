package data.platform.controller ;

import java.util.ArrayList ;
import java.util.Date ;
import java.util.HashMap ;
import java.util.List ;
import java.util.Map ;

import javax.servlet.http.HttpServletRequest ;

import org.springframework.beans.factory.annotation.Autowired ;
import org.springframework.stereotype.Controller ;
import org.springframework.ui.ModelMap ;
import org.springframework.web.bind.annotation.RequestMapping ;
import org.springframework.web.bind.annotation.RequestParam ;

import data.framework.pagination.model.PagingResult ;
import data.framework.support.AbstractBaseController ;
import data.platform.entity.EntityPlatformBusinessLog ;
import data.platform.service.PlatformBusinessLogService ;

/**
 * 平台－业务日志控制器类。
 * @author wanggq
 *
 */
@Controller
@RequestMapping( "platform/businessLog" )
public class PlatformBusinessLogController extends AbstractBaseController
{
    @Override
    protected void init( ModelMap model, HttpServletRequest request )
    {
    }
    
    /**
     * 分页查询业务日志方法
     * @param data 输入参数(由 Browser 端 POST 回来的JSON数据)
     * @param out 响应输出对象
     */
    @SuppressWarnings( "unchecked" )
    @RequestMapping(params = "command=search")
    public void search(@RequestParam("data") String data, java.io.PrintWriter out)
    {
        Map<String,Object> paramMap = getSerializer().parseMap( data ) ;
        boolean isFast = parseBoolean( paramMap.get( "isFast" ) ) ;
        String operatorChineseName = "" ;
        String operatorTMISAccount = "" ;
        String operatorADAccount = "" ;
        Date startDate = null ;
        Date endDate = null ;
        String operateType = null ;
        String module = "" ;
        if( isFast )
            operatorChineseName = trimString( paramMap.get( "q" ) ) ;
        else
        {
            operatorChineseName = trimString( paramMap.get( "operatorChineseName" ) ) ;
            operatorTMISAccount = trimString( paramMap.get( "operatorTMISAccount" ) ) ;
            operatorADAccount = trimString( paramMap.get( "operatorADAccount" ) ) ;
            startDate = parseDate( paramMap.get( "startDate" ), "yyyy-MM-dd" ) ;
            endDate = parseDate( paramMap.get( "endDate" ), "yyyy-MM-dd" ) ;
            Map<String,Object> operateTypeData = (Map<String,Object>)paramMap.get( "operateType" );
            operateType = trimString( operateTypeData == null ? null : operateTypeData.get( "value" ) ) ;
            module = trimString( paramMap.get( "module" ) ) ;
        }
        int currentPage = parseInteger( paramMap.get( "page" ) ) ;
        int pageSize = parseInteger( paramMap.get( "rows" ) ) ;
        String sortField = trimString( paramMap.get( "sidx" ) ) ;
        String sort = trimString( paramMap.get( "sord" ) ) ;
        PagingResult<EntityPlatformBusinessLog> pagingResult = businessLogService.serachBusinessLogs( operatorChineseName, operatorTMISAccount, operatorADAccount, startDate, endDate, operateType, module, sortField, sort, currentPage, pageSize ) ;
        List<EntityPlatformBusinessLog> list = pagingResult.getRows() ;
        List<Map<String,String>> newList = new ArrayList<Map<String,String>>() ;
        for( EntityPlatformBusinessLog entity : list )
        {
            Map<String,String> map = new HashMap<String,String>() ;
            map.put( "id", formatString( entity.getId() ) ) ;
            map.put( "operatorChineseName", formatString( entity.getOperatorChineseName() ) ) ;
            map.put( "operatorTMISAccount", formatString( entity.getOperatorTMISAccount() ) ) ;
            map.put( "operatorADAccount", formatString( entity.getOperatorADAccount() ) ) ;
            map.put( "module", formatString( entity.getModule() ) ) ;
            map.put( "operateType", formatEnum( entity.getOperateType(), "登录|新增|编辑|删除|查询|税务操作|其他操作" ) ) ;
            map.put( "ip", formatString( entity.getIp() ) ) ;
            map.put( "operateTime", formatDate( entity.getOperateTime(), "yyyy-MM-dd HH:mm:ss" ) ) ;
            map.put( "operateRemark", formatString( entity.getOperateRemark() ) ) ;
            newList.add( map ) ;
        }
        PagingResult<Map<String,String>> newPagingResult = new PagingResult<Map<String,String>>( newList, pagingResult.getPage(), pagingResult.getPageSize(), pagingResult.getRecords() ) ;
        out.print( getSerializer().formatObject( newPagingResult ) ) ;
    }
    
    @Autowired
    private PlatformBusinessLogService  businessLogService;
}
