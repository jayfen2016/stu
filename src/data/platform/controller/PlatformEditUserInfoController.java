package data.platform.controller;

import java.util.HashMap ;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import data.framework.support.AbstractBaseController;
import data.framework.support.Logger;
import data.platform.authority.security.SecurityContext;
import data.platform.entity.EntityPlatformUser;
import data.platform.service.PlatformUserService;

/**
 * 平台-框架编辑用户信息控制器类。
 * @author JohnXU
 *
 */
@Controller
@RequestMapping( "homePage_/editUserInfo" )
public class PlatformEditUserInfoController extends AbstractBaseController
{

	@Override
	protected void init(ModelMap model, HttpServletRequest request) 
	{
	}
	
	@RequestMapping(params="command=saveUserInfos" )
    public void updateCheckFillingStatus( @RequestParam( "data" ) String data, java.io.PrintWriter out )
    {
        try
        {
            Map<String,Object> paramMap = getSerializer().parseMap( data ) ;
            String officePhone=parseString(paramMap.get("officePhone"));
            String mobile=parseString(paramMap.get("mobile"));
            String email=parseString(paramMap.get("officeMail"));
            EntityPlatformUser userEntity=userService.load(SecurityContext.getPrincipal().getId());
            userEntity.setOfficePhone(officePhone);
            userEntity.setMobile(mobile);
            userEntity.setOfficeMail(email);
            userService.saveOrUpdate(userEntity);
            Map<String,Object> resultMap = new HashMap<String,Object>() ;
            resultMap.put( "message", "保存成功" ) ;
            out.print( getSerializer().formatMap( resultMap ) ) ;
        }
        catch( Exception e )
        {
            Logger.logError( e ) ;
            out.print( getSerializer().error( "保存用户信息出错！<br/><li>请刷新页面重试。</li>" ) ) ;
        }
   }
	@Autowired
	private PlatformUserService userService;
}
