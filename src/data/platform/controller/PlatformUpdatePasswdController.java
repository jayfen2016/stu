package data.platform.controller;

import java.util.Date;
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
import data.framework.utility.EncryptHelper;
import data.platform.authority.security.SecurityContext;
import data.platform.entity.EntityPlatformUser;
import data.platform.service.PlatformUserService;

/**
 * 平台-框架编辑用户口令控制器类。
 * @author JohnXU
 *
 */
@Controller
@RequestMapping( "homePage_/updatePasswd" )
public class PlatformUpdatePasswdController extends AbstractBaseController
{

	@Override
	protected void init(ModelMap model, HttpServletRequest request) 
	{
	}

	@RequestMapping(params="command=updatePasswd" )
    public void updateCheckFillingStatus( @RequestParam( "data" ) String data, java.io.PrintWriter out )
    {
        try
        {
        	EntityPlatformUser userEntity=userService.load(SecurityContext.getPrincipal().getId());
            Map<String,Object> paramMap = getSerializer().parseMap( data ) ;
            String prevPasswd=parseString(paramMap.get("prevPasswd"));
            //原密码校验
            String secretPasswdStr=EncryptHelper.encryptEncode( prevPasswd );
            if(!secretPasswdStr.equals(userEntity.getLoginPassword())){
            	 out.print( getSerializer().error( "密码不正确！<br/>" ) ) ;
            	 return;
            }
            String newPasswd=parseString(paramMap.get("newPasswd"));
            String confirmNewPasswd=parseString(paramMap.get("confirmNewPasswd"));
            if(!newPasswd.equals(confirmNewPasswd)){
            	 out.print( getSerializer().error( "两次输入的新密码不匹配！<br/>" ) ) ;
            	 return;
            }
            userEntity.setLoginPassword(EncryptHelper.encryptEncode( newPasswd ));
            userEntity.setUpdateTime(new Date());
            userService.saveOrUpdate(userEntity);
            Map<String,Object> resultMap = new HashMap<String,Object>() ;
            resultMap.put( "message", "密码修改成功！<br/>注意：新密码在 <b style=color:#ff0000;>下次</b> 登陆系统时生效！" ) ;
            out.print( getSerializer().formatMap( resultMap ) ) ;
        }
        catch( Exception e )
        {
            Logger.logError( e ) ;
            out.print( getSerializer().error( "修改口令出错！<br/><li>请刷新页面重试。</li>" ) ) ;
        }
   }
	@Autowired
	private PlatformUserService userService;
}
