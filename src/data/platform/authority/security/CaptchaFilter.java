package data.platform.authority.security;

import java.io.IOException ;
import java.util.HashMap ;
import java.util.Map ;

import javax.servlet.Filter ;
import javax.servlet.FilterChain ;
import javax.servlet.FilterConfig ;
import javax.servlet.ServletException ;
import javax.servlet.ServletRequest ;
import javax.servlet.ServletResponse ;
import javax.servlet.http.HttpServletRequest ;
import javax.servlet.http.HttpServletResponse ;
import javax.servlet.http.HttpSession ;

import org.springframework.beans.factory.annotation.Autowired ;
import org.springframework.beans.factory.annotation.Value ;
import org.springframework.stereotype.Service ;

import data.framework.data.DataSerializer ;
import data.framework.support.ConfigContext ;
import data.framework.support.GlobalContext ;
import data.framework.web.CaptchaServlet ;

/**
 * 平台－验证码过滤器。
 *
 * @author wanggq
 */
@Service
public class CaptchaFilter implements Filter {

    @Override
    public void init(FilterConfig config) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!SecurityContext.isAuthenticated()) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpSession session = httpRequest.getSession();
            Object captchaObj = session.getAttribute(CaptchaServlet.CAPTCHA_STRING_NAME);
            session.removeAttribute(CaptchaServlet.CAPTCHA_STRING_NAME);

            String needCheck = httpRequest.getParameter("captcha");
            if (needCheck != null && "NONE".equals(needCheck)) {
                chain.doFilter(request, response);
                return;
            }

            if (httpRequest.getParameter("j_username") == null || httpRequest.getParameter("j_password") == null) {
                HttpServletResponse httpServletResponse = (HttpServletResponse) response ;
                httpServletResponse.setStatus( 401 ) ;
                StringBuffer forwardUrl = new StringBuffer() ; 
                forwardUrl.append( GlobalContext.getWebRootPath() ).append( "401.html" ) ;
                httpServletResponse.sendRedirect( forwardUrl.toString() ) ;
                return;
            }

            if (captchaObj == null) {
                response.setCharacterEncoding(ConfigContext.getStringSection("framework.web.charset"));
                response.getWriter().write(getMessage("请登录！"));
                return;
            }

            String captchaStr = captchaObj.toString();
            if (!captchaStr.equals(request.getParameter("j_captcha"))) {
                response.setCharacterEncoding(ConfigContext.getStringSection("framework.web.charset"));
                response.getWriter().write(getMessage("验证码错误！"));
                return;
            }
        }

        chain.doFilter(request, response);
    }

    private String getMessage(String value) {
        Map<String, Object> out = new HashMap<String, Object>();
        out.put("status", false);
        out.put("message", value);

        return serializer.formatMap(out);
    }

    @Autowired
    @Value("#{implementsMap['dataSerializer']}")
    private DataSerializer serializer;
}
