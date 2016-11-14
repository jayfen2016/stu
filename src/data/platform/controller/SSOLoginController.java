package data.platform.controller ;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping( "ssoLogin" )
public class SSOLoginController
{
    @RequestMapping
    public void ssoLogin( HttpServletRequest request, HttpServletResponse response ) throws IOException
    {
        String username = request.getParameter( "username" ) ;
        //根据用户名username加载userDetails 
        UserDetails user = userDetailsService.loadUserByUsername( username ) ;
        //根据userDetails构建新的Authentication
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken( user, "", user.getAuthorities() ) ;
        // 设置authentication中details
        authentication.setDetails( new WebAuthenticationDetails( request ) ) ;
        SecurityContextHolder.getContext().setAuthentication( authentication ) ;
        HttpSession session = request.getSession( true ) ;
        // 在session中存放security context,方便同一个session中控制用户的其他操作
        session.setAttribute( HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext() ) ;
        response.sendRedirect( "homePage_.do" ) ;
    }

    @Autowired
    @Qualifier( "databaseProvider" )
    private UserDetailsService userDetailsService ;
}