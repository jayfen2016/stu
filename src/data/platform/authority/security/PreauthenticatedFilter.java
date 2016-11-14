package data.platform.authority.security ;

import javax.servlet.http.HttpServletRequest ;
import javax.servlet.http.HttpServletResponse ;

import org.springframework.beans.factory.annotation.Autowired ;
import org.springframework.beans.factory.annotation.Qualifier ;
import org.springframework.security.authentication.AuthenticationManager ;
import org.springframework.security.core.Authentication ;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter ;
import org.springframework.stereotype.Service ;
import org.springframework.util.Assert ;

import data.platform.service.PlatformUserService ;

/**
 * 平台－预验证过滤器。
 * @author wanggq
 */
@Service
public class PreauthenticatedFilter extends AbstractPreAuthenticatedProcessingFilter
{
    @Override
    protected void successfulAuthentication( HttpServletRequest request, HttpServletResponse response, Authentication authResult )
    {
        PrincipalDetails details = (PrincipalDetails)authResult.getPrincipal() ;

        // 刷新用户登录时间
        uService.flashLoginTime( details.getId() ) ;
        
        // 成功登录后记录用户IP
        details.setCurrentIp( request.getRemoteAddr() ) ;

        super.successfulAuthentication( request, response, authResult ) ;
    }

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request)
    {
        if( type.equals( "parameter" ) )
        {
            return request.getParameter( principalRequestHeader ) ;
        }
        else
        {
            return request.getHeader( principalRequestHeader ) ;
        }
    }

    @Override
    protected Object getPreAuthenticatedCredentials( HttpServletRequest request )
    {
        if( type.equals( "parameter" ) )
        {
            return request.getParameter( credentialsRequestHeader ) ;
        }
        else
        {
            return request.getHeader( credentialsRequestHeader ) ;
        }
    }

    @Override
    @Autowired
    @Qualifier("mainManager")
    public void setAuthenticationManager( AuthenticationManager authenticationManager )
    {
        super.setAuthenticationManager( authenticationManager ) ;
    }

    /**
     * 设置预验证包含用户登录名称的域。
     * @param principalRequestHeader 含用户登录名称的域名称
     */
    public void setPrincipalRequestHeader( String principalRequestHeader )
    {
        Assert.hasText( principalRequestHeader, "principalRequestHeader must not be empty or null" ) ;
        this.principalRequestHeader = principalRequestHeader ;
    }

    /**
     * 设置预验证包含用户登录密码的域。
     * @param credentialsRequestHeader 含用户登录密码的域名称
     */
    public void setCredentialsRequestHeader( String credentialsRequestHeader )
    {
        Assert.hasText( credentialsRequestHeader, "credentialsRequestHeader must not be empty or null" ) ;
        this.credentialsRequestHeader = credentialsRequestHeader ;
    }

    @Autowired
    private PlatformUserService uService ;

    private String type = "parameter" ;
    private String principalRequestHeader = "loginname" ;
    private String credentialsRequestHeader = "password" ;
}