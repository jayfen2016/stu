package data.oa.stuManagement.service ;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractService;
import data.oa.stuManagement.entity.EntityStudent;

/**
 * 学生service
 */
@Service
public class StudentService extends AbstractService {
	
	 /**
     * 分页查询学生信息。
     * @return 实体集合
     */
    public PagingResult<EntityStudent> search(String name, String phone, Integer sex,int currentPage, int pageSize )
    {
        Map<String,Object> param = new HashMap<String,Object>() ;
        param.put( "name", name ) ;
        param.put( "phone", phone ) ;
        param.put( "sex", sex ) ;
        return selectPaging( "student.selectPaging", param, "name", "ASC", currentPage, pageSize ) ;
    }
    
    /**
     * 保存或更新
     * @param entity 
     */
    public void saveOrUpdate( EntityStudent entity )
    {
        if( StringUtils.isNotBlank( entity.getId() ) ) {
            update( "student.update", entity ) ;
        } else {
            insert( "student.insert", entity ) ;
        }
    }
    
    /**
     * 根据编号获取信息。
     * @param id 编号
     * @return 实体
     */
    public EntityStudent load( String id ) {
        Map<String,String> param = new HashMap<String,String>() ;
        param.put( "id", id ) ;
        return selectOne( "student.load", param ) ;
    }
    
    /**
     * 根据编号删除信息。
     * @param id 编号集合
     * @return 删除记录数
     */
    public int remove( List<String> idAry )
    {
        Map<String,Object> param = new HashMap<String,Object>() ;
        param.put( "idAry", idAry ) ;
        return delete( "student.delete", param ) ;
    }
    
    
}