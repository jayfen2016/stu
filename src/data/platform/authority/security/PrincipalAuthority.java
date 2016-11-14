package data.platform.authority.security;

import org.springframework.security.core.GrantedAuthority ;

/**
 * 平台－基于 Spring Security 用户权限类。<br/>
 * 一个 PrincipalAuthority 实例代表一个用可以对某一 URL 的访问权限。
 * @author wanggq
 */
public class PrincipalAuthority implements GrantedAuthority
{
    /**
     * 用一个字符串初始化用户权限类。通常是一个 URL。
     * @param authority 受控 URL 字符串
     */
    public PrincipalAuthority( String authority )
    {
        this.authority = authority ;
    }

    /**
     * 取得受控 URL 串。
     */
    @Override
    public String getAuthority()
    {
        return authority ;
    }

    public void setAuthority( String authority )
    {
        this.authority = authority ;
    }

    private String authority ;
    private static final long serialVersionUID = -5145873467181060208L ;
}