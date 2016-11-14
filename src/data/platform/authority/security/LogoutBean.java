package data.platform.authority.security ;

import java.io.IOException ;
import java.util.HashMap ;
import java.util.Map ;
import javax.servlet.ServletException ;
import javax.servlet.http.HttpServletRequest ;
import javax.servlet.http.HttpServletResponse ;
import org.springframework.beans.factory.annotation.Autowired ;
import org.springframework.beans.factory.annotation.Value ;
import org.springframework.security.core.Authentication ;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler ;
import org.springframework.stereotype.Service ;
import data.framework.data.DataSerializer ;
import data.framework.utility.CookieUtil;

/**
 * 平台－注销处理类。
 * @author wanggq
 */
@Service
public class LogoutBean implements LogoutSuccessHandler
{
    @Override
    public void onLogoutSuccess( HttpServletRequest request, HttpServletResponse response, Authentication authentication ) throws IOException, ServletException
    {
        Map<String,Object> out = new HashMap<String,Object>() ;

        //注销清除Cookie
        CookieUtil.clearCookie(response);
        out.put( "status", true ) ;
        response.getWriter().write( serializer.formatMap( out ) ) ;
    }

    @Autowired
    @Value("#{implementsMap['dataSerializer']}")
    private DataSerializer serializer ;
}