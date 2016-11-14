package data.platform.service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import data.framework.support.AbstractService;
import data.platform.entity.EntityPlatformAccessory;

@Service
public class PlatformAccessoryService extends AbstractService
{


    /**
     * 加载附件信息。
     * @param id 附件id
     * @return 附件实体
     */
    public EntityPlatformAccessory load( String id )
    {
        Map<String,Object> param = new HashMap<String,Object>() ;
        param.put( "id", id ) ;
        return selectOne( "platformAccessory.loadAccessory", id ) ;
    }
    
    /**
     * 加载无关联的附件信息。
     * @param id 附件id
     * @return 附件实体
     */
    public List<EntityPlatformAccessory> loadNullAssociated() {
        return selectList( "platformAccessory.loadNullAssociated") ;
    }
    
    /**
     * 保存附件信息。
     * @param entity 附件实体
     */
    public void save( EntityPlatformAccessory entity )
    {
        insert( "platformAccessory.inertAccessory", entity ) ;
    }
    
    /**
     * 修改附件信息。
     * @param entity 附件实体
     */
    public void update( EntityPlatformAccessory entity )
    {
        update( "platformAccessory.updateAccessory", entity ) ;
    }
    
    /**
     * 批量保存附件。
     * @param entityList 附件实体集合
     */
    public void batchSave( List<EntityPlatformAccessory> entityList )
    {
        if( entityList != null && !entityList.isEmpty() )
        {
            Map<String,Object> param = new HashMap<String,Object>() ;
            param.put( "list", entityList ) ;
            insert( "platformAccessory.batchInertAccessory", param ) ;
        }
    }
    
    /**
     * 删除附件信息。
     * @param id 附件id
     */
    public void remove( String id )
    {
        Map<String,Object> param = new HashMap<String,Object>() ;
        param.put( "id", id ) ;
        delete( "platformAccessory.deleteAccessory", param ) ;
    }
    
    /**
     * 批量删除附件信息。
     * @param idAry 附件id集合
     */
    public void batchRemove( List<String> idAry )
    {
        if( idAry != null && !idAry.isEmpty() )
        {

            Map<String,Object> param = new HashMap<String,Object>() ;
            param.put( "idAry", idAry ) ;
            delete( "platformAccessory.batchDeleteAccessory", param ) ;
        }
    }

    /**
     * 根据员工id集合，批量删除匹配该集合里面的id的附件信息
     * 
     * @param id 工作经历编号
     * @return 删除记录数
     */
    public int removeByEmploymeeId(List<String> idAry )
    {
        Map<String,Object> param = new HashMap<String,Object>() ;
        param.put( "idAry", idAry ) ;
        return delete( "platformAccessory.deleteAllInfoByEmploymeeId", param ) ;
    }
    
    public void batchRemoveAndFiles(List<String> idAry){
        if(null !=idAry && !idAry.isEmpty()){
            List<EntityPlatformAccessory> list= searchAccessoryByIdList(idAry);
            for(EntityPlatformAccessory entity:list){
                File file = new File( entity.getFilePathInServer() ) ;
                if( file.exists() )
                    file.delete() ;
            }
            batchRemove(idAry);
        }
    }

    /**
     * 根据关联对象Id删除附件信息。
     * @param associatedObjectId 要关联的对象ID
     */
    public void removeAccessoryByAssociatedObjectId( String associatedObjectId )
    {
        Map<String,Object> param = new HashMap<String,Object>() ;
        param.put( "associatedObjectId", associatedObjectId ) ;
        delete( "platformAccessory.deleteAccessoryByAssociatedObjectId", param ) ;
    }
    
    /**
     * 根据附件Id查询附件信息。
     * @param ids 关联的附件ID集合
     * @return 附件实体集合
     */
    public List<EntityPlatformAccessory> searchAccessoryByIdList( List<String> ids )
    {
        if( ids == null || ids.isEmpty() )
            return null ;
        Map<String,Object> param = new HashMap<String,Object>() ;
        param.put( "ids", ids ) ;
        return selectList( "platformAccessory.searchAccessoryByIdList", param ) ;
    }
    
    /**
     * 删除关联附件信息。
     * @param objectId 要关联的对象ID
     * @param ids 关联的附件ID集合
     */
    public void associated( String objectId, List<String> ids )
    {
        if( ids == null || ids.isEmpty() )
            return ;
        Map<String,Object> param = new HashMap<String,Object>() ;
        param.put( "objectId", objectId ) ;
        param.put( "ids", ids ) ;
        update( "platformAccessory.associatedAccessory", param ) ;
        
        //索引文件到solr
        indexFile(ids);
    }
    
    /**
     * 索引上传文件
     * @param ids 文件ID集合
     */
    public void indexFile(List<String> ids) {
    	
    	for (String id : ids) {
    		
    		final EntityPlatformAccessory entity = this.load(id);
    		
    		Thread t = new Thread(new Runnable(){  
                public void run(){  
                	//call index file method
                    //solrUtilService.indexOrDelIndex("addIndex", entity, null);
                }
            });  
            t.start(); 
            
		}
         
    }
    
    /**
     * 根据关联对象Id查询所属附件信息。
     * @param associatedObjectId 关联的对象ID
     * @return 附件信息实体集合
     */
    public List<EntityPlatformAccessory> searchAccessoryByAssociatedObjectId( String associatedObjectId )
    {
        Map<String,Object> param = new HashMap<String,Object>() ;
        param.put( "associatedObjectId", associatedObjectId ) ;
        return selectList( "platformAccessory.searchAccessoryByAssociatedObjectId", param ) ;
    }
}