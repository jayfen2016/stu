package data.platform.authority.security ;

import java.util.ArrayList ;
import java.util.Collection ;
import java.util.HashMap ;
import java.util.List ;
import java.util.Map ;
import java.util.Set ;

import org.springframework.security.core.GrantedAuthority ;
import org.springframework.security.core.userdetails.UserDetails ;

/**
 * 平台－基于  Spring Security 的用户身份标识类。<br />
 * 该类用于在用户通过验证后标识用户身份，通常被置放于 session 中。
 * 可以通过  SecurityContext 的 getPrincipal 方法得到。<br/>
 * 该类的所有属性均为只读属性，只能通过构造方法在构造时赋值。
 *
 * @author wanggq
 */
public class PrincipalDetails implements UserDetails
{
    /**
     * 初始化一个用户身份标识实例。
     *
     * @param id 用户索引
     * @param loginAccount 用户登录帐号
     * @param loginPassword 用户登录密码
     * @param chineseName 用户中文名称
     * @param englishName 用户英文名称
     * @param adAccount   用户adAccount
     * @param accountNonExpired 帐号是否过期
     * @param accountNonLocked 帐号是否锁定
     * @param credentialsNonExpired 帐号密码是否过期
     * @param enabled 帐号是否激活
     * @param authorities 基于 URL 地址的权限集合
     */
	public PrincipalDetails(
            String id,
            String loginAccount,
            String loginPassword,
            String chineseName,
            String englishName,
            String adAccount,
            boolean accountNonExpired,
            boolean accountNonLocked,
            boolean credentialsNonExpired,
            boolean enabled,
            Collection<GrantedAuthority> authorities )
    {
        this.id = id ;
        this.loginAccount = loginAccount ;
        this.loginPassword = loginPassword ;
        this.chineseName = chineseName ;
        this.englishName = englishName ;
        this.adAccount = adAccount ;
        this.accountNonExpired = accountNonExpired ;
        this.accountNonLocked = accountNonLocked ;
        this.credentialsNonExpired = credentialsNonExpired ;
        this.enabled = enabled ;
        this.authorities = authorities ;
        if( authorities == null )
            return ;
        for( GrantedAuthority grantedAuthority : authorities )
        {
            String[] items = grantedAuthority.getAuthority().split( ";" ) ;
            String url = items[0] ;
            String operations = items[1] ;

            if( items[0].length() == 0 )
                continue ;

            String[] operationItems = operations.split( "," ) ;
            List<String> list = new ArrayList<String>() ;
            for( String operationItem : operationItems )
            {
                list.add( operationItem ) ;
            }

            if( purviewMap.containsKey( url ) )
            {
                List<String> operationList = purviewMap.get( url ) ;
                operationList.removeAll( list ) ;
                operationList.addAll( list ) ;
            }
            else
            {
                purviewMap.put( url, list ) ;
            }
        }
    }

    /**
     * 判断当前用户对一个指定的 URL 是否有访问权限。
     * @param url 要判断的 URL
     * @return 能够访问返回 true，否则返回 false
     */
    public boolean canAccessUrl( String url )
    {
        if( url == null || url.length() == 0 )
            return false ;
        if( url.indexOf( ".do" ) != -1 )
        {
            url = url.split( "\\.do" )[0] ;
        }
        Set<String> keySet = purviewMap.keySet() ;
        for( String key : keySet )
        {
            if( key.indexOf( ".do" ) != -1 )
            {
                key = key.split( "\\.do" )[0] ;
            }
            if( url.startsWith( key ) )
                return true ;
        }
        return false ;
    }

    /**
     * 取得当前用户对一个指定的 URL 的操作权限。
     * @param url 要判断的 URL
     * @return 操作权限集合，没有权限返回 null
     */
    public List<String> getOperationsByUrl( String url )
    {
        if( url == null || url.length() == 0 )
            return new ArrayList<String>() ;

        Set<String> keySet = purviewMap.keySet() ;
        for( String key : keySet )
        {
            if( url.startsWith( key ) )
                return purviewMap.get( key ) ;
        }
        return new ArrayList<String>() ;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities()
    {
        return authorities ;
    }

    /**
     * 取得用户索引
     * @return 用户索引
     */
    public String getId()
    {
        return this.id ;
    }

    @Override
    public String getPassword()
    {
        return loginPassword ;
    }

    /**
     * 取得用户帐号。
     */
    @Override
    public String getUsername()
    {
        return loginAccount ;
    }

    /**
     * 取得用户中文姓名。
     * @return 用户中文姓名
     */
    public String getChineseName()
    {
        return this.chineseName ;
    }
    
    /**
     * 取得用户英文姓名。
     * @return 用户英文姓名
     */
    public String getEnglishName()
    {
        return this.englishName ;
    }
    
    /**
     * 取得用户ADAccount。
     * @return 用户ADAccount
     */
    public String getAdAccount()
    {
        return this.adAccount ;
    }

    @Override
    public boolean isAccountNonExpired()
    {
        return accountNonExpired ;
    }

    @Override
    public boolean isAccountNonLocked()
    {
        return accountNonLocked ;
    }

    @Override
    public boolean isCredentialsNonExpired()
    {
        return credentialsNonExpired ;
    }

    @Override
    public boolean isEnabled()
    {
        return enabled ;
    }

    void setCurrentIp( String currentIp )
    {
        this.currentIp = currentIp;
    }
    /**
     * 取得该用户身份标识实例对应的用户的登录IP
     * @return 用户身份标识实例对应的用户的登录IP
     */
    public String getCurrentIp()
    {
        return currentIp;
    }

    /**
     * 取得该用户身份标识实例对应的用户的角色Id。
     * @return 用户身份标识实例对应的用户的角色Id
     */
    public String getRoleId()
    {
        return roleId ;
    }

    /**
     * 设置用户身份标识实例对应的用户的角色Id。
     * @param roleId 用户身份标识实例对应的用户的角色Id
     */
    public void setRoleId( String roleId )
    {
        this.roleId = roleId ;
    }
    
    public String getOrgId() 
    {
		return orgId;
	}
    
    private String id ;
    private String orgId ;
	private String loginAccount ;
    private String loginPassword ;
    private String chineseName ;
    private String englishName ;
    private String adAccount ;
    private String currentIp ;
    private String roleId ;
    private boolean accountNonExpired ;
    private boolean accountNonLocked ;
    private boolean credentialsNonExpired ;
    private boolean enabled ;
    private Collection<GrantedAuthority> authorities ;
    private Map<String,List<String>> purviewMap = new HashMap<String,List<String>>() ;
    private static final long serialVersionUID = 3279098258961708455L ;
}