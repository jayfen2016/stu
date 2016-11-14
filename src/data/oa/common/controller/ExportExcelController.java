package data.oa.common.controller ;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractValidatorController;
import data.framework.utility.ExcelHelper;
import data.framework.validation.Errors;
import data.platform.entity.EntityPlatformUser;
import data.platform.service.PlatformUserService;

@Controller
@RequestMapping( "common/exportExcel" )
public class ExportExcelController extends AbstractValidatorController
{
	@Override
    protected void validate( Object obj, Errors errors )
    {
    }

    @Override
    protected void init( ModelMap model, HttpServletRequest request )
    {
    }
    
    public ExportExcelController()
    {
    }

    @RequestMapping( params = "command=export" )
    public void export( @RequestParam( "data" ) String data, HttpServletRequest request, HttpServletResponse response )
    {
        String type = request.getParameter( "type" ) ;
        try
        {
            data = java.net.URLDecoder.decode( data, "utf-8" ) ;
        }
        catch( UnsupportedEncodingException ex )
        {
            ex.printStackTrace() ;
        }
        Map<String,Object> paramMap = getSerializer().parseMap( data ) ;

         if( type.equals( "unitContact" ) )
        {
            ExcelHelper.exportExcelByEntityList( searchUsers( paramMap ), ExportExcelTemplate.getTemplate( "unitContact" ), "单位联系人", response ) ;
        }
    }


    // 系统用户
    private List<EntityPlatformUser> searchUsers( Map<String,Object> paramMap )
    {
        boolean isFast = parseBoolean( paramMap.get( "isFast" ) ) ;
        String chineseName = "" ;
        String departmentId=parseString( paramMap.get( "id" ) ) ;
        if( isFast )
        {
        	chineseName = trimString( paramMap.get( "q" ) ) ;
        }
        else
        {
        	chineseName = trimString( paramMap.get( "chineseName" ) ) ;
        }
        String sortField = trimString( paramMap.get( "sidx" ) ) ;
        String sort = trimString( paramMap.get( "sord" ) ) ;
        PagingResult<EntityPlatformUser> pagingResult = userService.searchUsers(chineseName, null, null, null, null, null, null, null, departmentId, sortField, sort, 1, Integer.MAX_VALUE);
        return pagingResult.getRows() ;
    }
    
    @Autowired
    private PlatformUserService userService ;
}
