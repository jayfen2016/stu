package data.platform.authority.security;

import org.springframework.security.core.context.SecurityContextHolder;


/**
 * 平台－安全上下文类。<br/>
 * 静态工具类，不可继承，不可实例化。
 * @author wanggq
 */
public final class SecurityContext
{
    private SecurityContext() {}

    /**
     * 取得当前用户的身份标识。在未验证的情况下返回 null。
     * @return 当前用户的身份标识
     */
    public static PrincipalDetails getPrincipal()
    {
        if( SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated() )
            return (PrincipalDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal() ;
        else
            return null ;
        
    }

    /**
     * 判断当前会话是否是一个已验证的会话。
     * @return true 表示是一个已验证会话，否则返回 false
     */
    public static boolean isAuthenticated()
    {
        return SecurityContextHolder.getContext().getAuthentication() == null ? false : SecurityContextHolder.getContext().getAuthentication().isAuthenticated() ;
    }
}