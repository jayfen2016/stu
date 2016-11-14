package data.platform.authority.security ;

import java.io.IOException ;
import java.util.HashMap ;
import java.util.Map ;

import javax.servlet.ServletException ;
import javax.servlet.http.HttpServletRequest ;
import javax.servlet.http.HttpServletResponse ;

import org.springframework.beans.factory.annotation.Autowired ;
import org.springframework.beans.factory.annotation.Value ;
import org.springframework.security.core.AuthenticationException ;
import org.springframework.security.web.authentication.AuthenticationFailureHandler ;
import org.springframework.stereotype.Service ;

import data.framework.data.DataSerializer ;
import data.framework.support.ConfigContext ;

/**
 * 平台－验证失败后将调用该类的 onAuthenticationFailure 方法，返回错误信息。
 * @author wanggq
 */
@Service
public class AuthenticationFailureBean implements AuthenticationFailureHandler
{
    @Override
    public void onAuthenticationFailure( HttpServletRequest request, HttpServletResponse response, AuthenticationException exception ) throws IOException, ServletException
    {
        String message = exception.getMessage() ;
        response.setCharacterEncoding( ConfigContext.getStringSection( "framework.web.charset" ) ) ;

        Map<String,Object> out = new HashMap<String,Object>() ;
        out.put( "status", false ) ;
        out.put( "message", message ) ;
        response.getWriter().write( serializer.formatMap( out ) ) ;
    }

    @Autowired
    @Value("#{implementsMap['dataSerializer']}")
    private DataSerializer serializer ;
}