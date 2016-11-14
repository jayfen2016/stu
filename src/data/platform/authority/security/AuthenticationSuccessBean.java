package data.platform.authority.security ;

import java.io.IOException ;
import java.util.HashMap ;
import java.util.Map ;
import javax.servlet.ServletException ;
import javax.servlet.http.HttpServletRequest ;
import javax.servlet.http.HttpServletResponse ;
import javax.servlet.http.HttpSessionBindingEvent ;
import javax.servlet.http.HttpSessionBindingListener ;
import org.springframework.beans.factory.annotation.Autowired ;
import org.springframework.beans.factory.annotation.Value ;
import org.springframework.security.core.Authentication ;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler ;
import org.springframework.stereotype.Service ;
import data.framework.data.DataSerializer ;
import data.framework.utility.CookieUtil;
import data.framework.utility.EncryptHelper;
import data.platform.entity.EntityPlatformUser;
import data.platform.service.PlatformUserService ;

/**
 * 平台－成功登录后将调用该类的 onAuthenticationSuccess 方法，进行登录处理。
 * @author wanggq
 */
@Service
public class AuthenticationSuccessBean implements AuthenticationSuccessHandler
{
    private static int sessionCount = 0 ;

    @Override
    public void onAuthenticationSuccess( HttpServletRequest request, HttpServletResponse response, Authentication authentication ) throws IOException, ServletException
    {
        request.getSession().setAttribute( "LICENSE_LISTENER",  new SessionBindingListener() ) ;

        String jSaveUserName = request.getParameter("j_save_username");
        String jSavePassword = request.getParameter("j_save_password");
        String autoLogin = request.getParameter("autoLogin");
        String ifCookie = request.getParameter("ifCookie");
        if (null != jSaveUserName && null != jSavePassword){
        	//对比数据库中的用户名密码来检测用户输入的用户名密码是否正确
        	EntityPlatformUser entityUser = uService.load(SecurityContext.getPrincipal().getId());
        	String loginAccount = "";
        	String password = "";
        	
        	if (null != entityUser){
        		loginAccount = entityUser.getLoginAccount();
        		password = entityUser.getLoginPassword();
        	}
        	
        	String getMd5 = EncryptHelper.encryptEncode( jSavePassword );
        	//如果检测结果通过，那么就保存信息到客户端
        	if (jSaveUserName.equals(loginAccount) && getMd5.equals(password)){
        		CookieUtil.saveCookie(jSaveUserName, jSavePassword, autoLogin, response);
        	}
        	
        }else {
        	if (null == ifCookie){
            	CookieUtil.clearCookie(response);
        	}
        }

        // 刷新用户登录时间
        uService.flashLoginTime( SecurityContext.getPrincipal().getId() ) ;

        // 成功登录后记录用户IP
        SecurityContext.getPrincipal().setCurrentIp( request.getRemoteAddr() ) ;

        Map<String,Object> out = new HashMap<String,Object>() ;
        out.put( "status", true ) ;
        response.getWriter().write( serializer.formatMap( out ) ) ;
    }

    private class SessionBindingListener implements HttpSessionBindingListener
    {
        @Override
        public void valueBound( HttpSessionBindingEvent event )
        {
            if( sessionCount > ( Integer.parseInt( "364" ) - Integer.parseInt( "360" ) ) )
            {
                System.out.println("ERROR:NON-EXCLUSIVE LICENSE!");
            }
            sessionCount++ ;
        }

        @Override
        public void valueUnbound( HttpSessionBindingEvent event )
        {
            sessionCount-- ;
        }
    }
    
    @Autowired
    @Value("#{implementsMap['dataSerializer']}")
    private DataSerializer serializer ;
    @Autowired
    private PlatformUserService uService ;
}