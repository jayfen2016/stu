package data.platform.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import data.framework.support.AbstractService;
import data.platform.entity.EntityPlatformOrgSignPerson;
import data.platform.entity.EntityPlatformRole;

/**
 * 平台-部门转签人员服务类。
 * @author liuguo
 */
@Service
public class PlatformOrgSignPersonService extends AbstractService
{
    /**
     * 保存或更新部门转签人员信息
     * @param entity 部门转签人员信息
     */
    public void saveOrUpdate( EntityPlatformOrgSignPerson entity )
    {
        if( StringUtils.isNotBlank( entity.getId() ) )
            update( "platformOrgSignPerson.updateOrgSignPerson", entity ) ;
        else
            insert( "platformOrgSignPerson.insertOrgSignPerson", entity ) ;
    }
    
    /**
     * 根据部门转签人员id 删除部门转签人员信息。
     * @param id 部门转签人员信息id集合
     * @return 删除记录数
     */
    public int remove( List<String> idAry )
    {
        Map<String,Object> param = new HashMap<String,Object>() ;
        param.put( "idAry", idAry ) ;
        return delete( "platformOrgSignPerson.deleteOrgSignPerson", param ) ;
    }
    
    
    /**
     * 根据登录名查询部门转发人员信息。
     * @param 
     * @return 部门转发人员实体
     */
   
    public List<EntityPlatformOrgSignPerson> searchInfo (Map<String, Object> paramMap){
		return selectList("platformOrgSignPerson.searchInfo", paramMap);
	}
    /**
     * 加载部门转发人员信息。
     * @param id 部门转发人员编号
     * @return 部门转发人员实体
     */
    public EntityPlatformOrgSignPerson load( String id )
    {
        Map<String,String> param = new HashMap<String,String>() ;
        param.put( "id", id ) ;
        return selectOne( "platformOrgSignPerson.loadOrgSendPerson", param ) ;
    }
    
    /**
     * 加载部门转发人员信息。
     * @param id 部门转发人员编号
     * @return 部门转发人员实体
     */
    public List<Map<String,Object>> loadOrgSendPersonByOrgId( String id )
    {
        Map<String,String> param = new HashMap<String,String>() ;
        param.put( "orgId", id ) ;
        return selectList( "platformOrgSignPerson.loadOrgSendPersonByOrgId", param ) ;
    }
    
    /**
     * 根据部门Id和模块类型名称 来加载 部门转发人员id。
     * @param orgId 部门Id
     * @param moduleTypeName 模块类型名称
     * @return 部门转发人员id
     */
    public Map<String,Object> loadSignedNoticePersonId(String orgId)
    {
        Map<String,String> param = new HashMap<String,String>() ;
        param.put( "orgId", orgId ) ;
        param.put( "moduleTypeId", "MeetingManage" ); 
        return selectOne( "platformOrgSignPerson.loadSignedNoticePersonId", param ) ;
    }
    
    /**
     * 根据部门Id和模块类型名称 来加载 部门转发人员id。
     * @param orgId 部门Id
     * @param moduleTypeId 模块类型名称
     * @return 部门转发人员id
     */
    public Map<String,Object> loadSignedNoticePersonIdAndModuleTypeId(String orgId,String moduleTypeId)
    {
        Map<String,String> param = new HashMap<String,String>() ;
        param.put( "orgId", orgId ) ;
        param.put( "moduleTypeId", moduleTypeId); 
        return selectOne( "platformOrgSignPerson.loadSignedNoticePersonId", param ) ;
    }
}