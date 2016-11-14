package data.platform.authority.security ;

import java.util.Collection ;

import org.springframework.security.access.AccessDecisionManager ;
import org.springframework.security.access.AccessDeniedException ;
import org.springframework.security.access.ConfigAttribute ;
import org.springframework.security.authentication.InsufficientAuthenticationException ;
import org.springframework.security.core.Authentication ;
import org.springframework.security.web.FilterInvocation ;
import org.springframework.stereotype.Service ;

/**
 * 平台－基于 Spring Security 的访问控制管理器(Access control Manager)。
 * @author wanggq
 */
@Service
public class AccessControlManager implements AccessDecisionManager
{
    @Override
    public void decide( Authentication authentication, Object securityObject, Collection<ConfigAttribute> configAttributes ) throws AccessDeniedException, InsufficientAuthenticationException
    {
        if( authentication == null || !authentication.isAuthenticated() )
        {
            throw new InsufficientAuthenticationException( "您没有访问该页面的权限！" ) ;
        }

        PrincipalDetails details = (PrincipalDetails)authentication.getPrincipal() ;

        String url = "" ;
        if( securityObject instanceof FilterInvocation )
        {
            FilterInvocation filter = (FilterInvocation)securityObject ;
            
            url = filter.getRequestUrl() ;
            url = url.substring( 1, url.length() ) ;
        }

        String leftUrl = url ;
        if( leftUrl.indexOf( "?" ) != -1 )
        {
            leftUrl = leftUrl.split( "\\?" )[0] ;
        }
        int d = leftUrl.lastIndexOf( "." ) ;
        if( d != -1 && !leftUrl.startsWith( "." ) && leftUrl.charAt( d - 1 ) == '_' )
        {
            return ;
        }
        if(leftUrl.startsWith("meetingMangement/meetingNoticeAccepter/meetingNoticeAwaitingSignedRecordForAccepter.do"))
        {
        	return ;
        }
        if(leftUrl.startsWith("meetingMangement/meetingNoticeAccepter/meetingNoticeBasicInfoForAccepter.do"))
        {
        	return ;
        }
        if(leftUrl.startsWith("meetingMangement/meetingNoticeAccepter/meetingNoticeSignedPerson.do"))
        {
        	return ;
        }
        if(leftUrl.startsWith("propertyManage/advancedQuery.do"))
        {
        	return ;
        }
        
        if(leftUrl.startsWith("meetingMangement/meetingDispatch.do"))
        {
        	return ;
        }
       
        if(leftUrl.startsWith("announcement/announcementPublishList.do"))
        {
        	return ;
        }
        
        if(leftUrl.startsWith("announcement/announcementList.do"))
        {
        	return ;
        }
        
        if(leftUrl.startsWith("announcement/announcementView.do"))
        {
        	return ;
        }
        
        if( leftUrl.startsWith( "common/exportExcel" ) )
        {
            return ;
        }
        if( leftUrl.startsWith( "humanResource/employmee/entryCompanyManagement.do"))
        {
        	return ;
        }
        if( leftUrl.startsWith( "meetingMangement/meetingRoomList/choosePerson.do"))
        {
        	return ;
        }
        
        if( leftUrl.indexOf( "varianceAnalysisStatus" ) != -1 )
        {
            return ;
        }
        if( leftUrl.indexOf( "editUserInfo" ) != -1 )
        {
            return ;
        }
        if( leftUrl.indexOf( "updatePasswd" ) != -1 )
        {
            return ;
        }
        if( leftUrl.startsWith( "declaration" ) )
        {
            return ;
        }
        if( leftUrl.startsWith( "report" ) )
        {
            return ;
        }

        if( !details.canAccessUrl( url ) )
        {
            throw new AccessDeniedException( "您没有访问该页面的权限！" ) ;
        }
    }

    @Override
    public boolean supports( ConfigAttribute configAttribute )
    {
        return true ;
    }

    @Override
    public boolean supports( Class<?> classid )
    {
        return true ;
    }
}
