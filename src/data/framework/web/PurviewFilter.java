package data.framework.web;

import java.io.IOException ;
import javax.servlet.Filter ;
import javax.servlet.FilterChain ;
import javax.servlet.FilterConfig ;
import javax.servlet.ServletException ;
import javax.servlet.ServletRequest ;
import javax.servlet.ServletResponse ;
import javax.servlet.http.HttpServletRequest ;
import org.springframework.security.core.GrantedAuthority ;
import data.platform.authority.security.PrincipalDetails ;
import data.platform.authority.security.SecurityContext ;

public class PurviewFilter implements Filter
{
    @Override
    public void init( FilterConfig config ) throws ServletException {}

    @Override
    public void destroy() {}

    @Override
    public void doFilter( ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain ) throws IOException, ServletException
    {
        String currentUrl = ( (HttpServletRequest)servletRequest ).getRequestURI().replace( "/TMIS/", "" ) ;
      /*  if( currentUrl.startsWith( "collection/year/fillDetail_" ) )
        {
            currentUrl = "collection/year/yearCollect" ;
        }*/
        PrincipalDetails principalDetails = SecurityContext.getPrincipal() ;
        if( principalDetails != null )
        {
            for( GrantedAuthority item : principalDetails.getAuthorities() )
            {
                String[] array = item.getAuthority().split( ";" ) ;
                if( array[0].length() != 0 && currentUrl.startsWith( ( array[0].split( "\\?" )[0] ).replace( ".do", "" ) ) )
                {
                    String[] parray = array[1].split( "," ) ;
                    for( String pur : parray )
                    {
                        servletRequest.setAttribute( "__" + pur + "___", true ) ;
                    }
                }
            }
        }
        chain.doFilter( servletRequest, servletResponse ) ;
    }
}
