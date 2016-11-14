package data.platform.authority.security;

import org.springframework.beans.factory.annotation.Autowired ;
import org.springframework.dao.DataAccessException ;
import org.springframework.security.core.userdetails.UserDetails ;
import org.springframework.security.core.userdetails.UserDetailsService ;
import org.springframework.security.core.userdetails.UsernameNotFoundException ;
import org.springframework.stereotype.Service ;

import data.platform.entity.EntityPlatformUser ;
import data.platform.service.PlatformUserService ;

/**
 * 平台－基于  Spring Security 的用户身份标识提供类。<br/>
 * 该实现通过 PlatformUserService 加载指定的用户(EntityPlatformUser)，如果用户存在，转换成用户身份标识类返回给验证器。
 * @author wanggq
 */
@Service("databaseProvider")
public class PrincipalProvider implements UserDetailsService
{
    /**
     * 通过用户登录帐号查找指定的用户。
     */
    @Override
    public UserDetails loadUserByUsername( String loginName ) throws UsernameNotFoundException, DataAccessException
    {
        UserDetails result = null ;
        EntityPlatformUser entity = getUserService().getUserByLoginName( loginName ) ;
        if( entity != null )
        {
            result = new PrincipalDetails(
                entity.getId(),
                entity.getLoginAccount(),
                entity.getLoginPassword(),
                entity.getChineseName(),
                entity.getEnglishName(),
                entity.getAdAccount(),
                true,
                true,
                true,
                entity.getStatus()==1 ? true:false,
                getUserService().getFunctionAuthoritiesByUser( entity.getId() )
            ) ;
        }
        return result ;
    }

    /**
     * 设置 '用户' 业务类，通过 spring 容器来完成注入。
     * @param userService '用户' 业务类实例
     */
    @Autowired
    public void setUserService( PlatformUserService userService )
    {
        this.userService = userService;
    }

    /**
     * 获取 '用户' 业务类实例。
     * @return '用户' 业务类实例
     */
    public PlatformUserService getUserService()
    {
        return userService;
    }

    private PlatformUserService userService ;
}