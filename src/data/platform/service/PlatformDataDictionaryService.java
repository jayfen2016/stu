package data.platform.service;

import java.util.HashMap ;
import java.util.List ;
import java.util.Map ;

import org.apache.commons.lang.StringUtils ;
import org.springframework.stereotype.Service ;

import data.framework.data.DataTree ;
import data.framework.pagination.model.PagingResult ;
import data.framework.support.AbstractService ;
import data.platform.entity.EntityPlatformDataDictionary ;
/**
 * 平台-数据字典服务类。
 * @author wanggq
 *
 */
@Service
public class PlatformDataDictionaryService extends AbstractService
{
    /**
     * 保存或更新数据字典信息。
     * @param entity 数据字典实体
     * @throws Exception
     */
    public void saveOrUpdate( EntityPlatformDataDictionary entity )
    {
        if( StringUtils.isNotBlank( entity.getId() ) )
            update( "platformDataDictionary.updateDataDictionary", entity ) ;
        else
            insert( "platformDataDictionary.insertDataDictionary", entity ) ;
    }
    
    /**
     * 根据数据字典ID获取数据字典信息。
     * @param id 数据字典ID
     * @return 数据字典实体
     */
    public EntityPlatformDataDictionary load( String id )
    {
        Map<String,String> param = new HashMap<String,String>() ;
        param.put( "id", id ) ;
        return selectOne( "platformDataDictionary.loadDataDictionary", param ) ;
    }
    
    /**
     * 根据数据字典ID删除数据字典信息。
     * @param id 数据字典集合
     * @return 删除记录数
     */
    public int remove( List<String> idAry )
    {
        Map<String,Object> param = new HashMap<String,Object>() ;
        param.put( "idAry", idAry ) ;
        return delete( "platformDataDictionary.deleteDataDictionary", param ) ;
    }
    
    /**
     * 分页查询数据。
     * @param dictionaryName 字典名称
     * @param dictionaryCode 字典代码
     * @param sortField 数据库排序字段
     * @param sort 排序方式（ASC|DESC）
     * @param currentPage 当前页数
     * @param pageSize 页大小
     * @return 分页查询集合
     */
    public PagingResult<EntityPlatformDataDictionary> searchDataDictionary( String dictionaryName, String dictionaryCode, String sortField, String sort, int currentPage, int pageSize )
    {
        Map<String,Object> param = new HashMap<String,Object>() ;
        param.put( "dictionaryName", dictionaryName ) ;
        param.put( "dictionaryCode", dictionaryCode ) ;
        if( StringUtils.isBlank( sortField ) )
            sortField = "serial" ;
        if( StringUtils.isBlank( sort ) )
            sort = "ASC" ;
        return selectPaging( "platformDataDictionary.selectPaging", param, sortField, sort, currentPage, pageSize ) ;
    }
    
    /**
     * 获取数据字典树形结构数据。
     * @return 树形结构数据集合
     */
    public List<DataTree> getDataDictionaryTree()
    {
        return selectList( "platformDataDictionary.selectTreeDataDictionary" ) ;
    }
    
    /**
     * 获取数据字典树形结构数据。
     * @return 树形结构数据集合
     */
    public List<DataTree> selectChildDataDictionary(String id)
    {
        return selectList( "platformDataDictionary.selectChildDataDictionary" ,id) ;
    }
    
    /**
     *
     * @param parentCode 父节点的code值
     * @return 树形结构数据集合
     */
    public List<DataTree> getDataDictionaryTreeByParentCode(String parentCode){
      return selectList("platformDataDictionary.selectTreeDataDictionaryByParentCode",parentCode);
    }
    
    
    /**
    *
    * @param dictionaryCode 数据字典的code值
    * @return 对应的数据字典数据
    */
   public EntityPlatformDataDictionary selectDictionaryValueByCode(String dictionaryCode){
       return selectOne("platformDataDictionary.selectDictionaryValueByCode",dictionaryCode);
   }
}