package data.platform.authority.security;

import java.io.IOException ;

import javax.servlet.Filter ;
import javax.servlet.FilterChain ;
import javax.servlet.FilterConfig ;
import javax.servlet.ServletException ;
import javax.servlet.ServletRequest ;
import javax.servlet.ServletResponse ;
import javax.servlet.http.HttpServletRequest ;
import javax.servlet.http.HttpServletResponse ;

import org.springframework.stereotype.Service ;

import data.framework.web.widget.ActionButtonTag ;

/**
 * 平台－操作权限过滤器。
 * @author wanggq
 */
@Service
public class PurviewFilter implements Filter
{
    @Override
    public void init( FilterConfig config ) throws ServletException {}

    @Override
    public void destroy() {}

    @Override
    public void doFilter( ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain ) throws IOException, ServletException
    {
        // 解决弹出窗口session丢失问题
        HttpServletResponse httpResponse = (HttpServletResponse)servletResponse ;
        httpResponse.setHeader( "P3P", "CP=CAO PSA OUR" ) ;

        if( SecurityContext.isAuthenticated() )
        {
            HttpServletRequest request = (HttpServletRequest)servletRequest ;
            String url = request.getServletPath() ;
            String queryString = request.getQueryString() ;
            url = url.substring( 1, url.length() ) + ( ( queryString == null ) ? "" : ( "?" + queryString ) );
            request.setAttribute(
                    ActionButtonTag.CONTROL_PURVIEW_OPERATIONS,
                    SecurityContext.getPrincipal().getOperationsByUrl( url ) ) ;
        }
        chain.doFilter( servletRequest, servletResponse ) ;
    }
}