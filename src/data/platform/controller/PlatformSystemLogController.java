package data.platform.controller;

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
import data.platform.entity.EntityPlatformSystemLog ;
import data.platform.service.PlatformSystemLogService ;
/**
 * 平台-系统日志控制器类。
 * @author wanggq
 */
@Controller
@RequestMapping( "platform/systemLog" )
public class PlatformSystemLogController extends AbstractBaseController
{
    @Override
    protected void init( ModelMap model, HttpServletRequest request )
    {
    }

    /**
     * 分页查询系统日志信息的方法。
     * @param data 输入参数(由 Browser 端 POST 回来的JSON数据)
     * @param out 响应输出对象
     */
    @SuppressWarnings( "unchecked" )
    @RequestMapping( params = "command=search" )
    public void search( @RequestParam("data") String data, java.io.PrintWriter out )
    {
        Map<String,Object> paramMap = getSerializer().parseMap( data ) ;
        boolean isFast = parseBoolean( paramMap.get( "isFast" ) ) ;
        String remark = "" ;
        String logType = "" ;
        Date startDate = null ;
        Date endDate = null ;
        if( isFast )
            remark = trimString( paramMap.get( "q" ) ) ;
        else
        {
            remark = trimString( paramMap.get( "remark" ) ) ;
            startDate = parseDate( paramMap.get( "startDate" ), "yyyy-MM-dd" ) ;
            endDate = parseDate( paramMap.get( "endDate" ), "yyyy-MM-dd" ) ;
            Map<String,Object> logTypeData = (Map<String,Object>)paramMap.get( "logType" );
            logType = trimString( logTypeData == null ? null : logTypeData.get( "value" ) ) ;
        }
        int currentPage = parseInteger( paramMap.get( "page" ) ) ;
        int pageSize = parseInteger( paramMap.get( "rows" ) ) ;
        String sortField = trimString( paramMap.get( "sidx" ) ) ;
        String sort = trimString( paramMap.get( "sord" ) ) ;
        PagingResult<EntityPlatformSystemLog> pagingResult = systemLogService.searchSystemLog( remark, logType, startDate, endDate, sortField, sort, currentPage, pageSize ) ;
        List<EntityPlatformSystemLog> list = pagingResult.getRows() ;
        List<Map<String,String>> newList = new ArrayList<Map<String,String>>() ;
        for( EntityPlatformSystemLog entity : list )
        {
            Map<String,String> map = new HashMap<String,String>() ;
            map.put( "id", formatString( entity.getId() ) ) ;
            map.put( "logTime", formatDate( entity.getLogTime(), "yyyy-MM-dd HH:mm:ss" ) ) ;
            map.put( "logType", formatString( entity.getLogType() ) ) ;
            map.put( "source", formatString( entity.getSource() ) ) ;
            map.put( "remark", formatString( entity.getRemark() ) ) ;
            newList.add( map ) ;
        }
        PagingResult<Map<String,String>> newPagingResult = new PagingResult<Map<String,String>>( newList, pagingResult.getPage(), pagingResult.getPageSize(), pagingResult.getRecords() ) ;
        out.print( getSerializer().formatObject( newPagingResult ) ) ;
    }
    
    @Autowired
    private PlatformSystemLogService systemLogService ;
}