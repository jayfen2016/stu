package data.oa.teacherManagement.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractService;
import data.oa.stuManagement.entity.EntityStudent;
import data.oa.teacherManagement.entity.Teacher;
/**
 * 老师service
 * @author gavin
 *
 */
@Service
public class TeacherService extends AbstractService{

	/**
     * 分页查询老师信息。
     * @return 实体集合
     */
    public PagingResult<Teacher> search(String name,String address,Integer sex,int currentPage, int pageSize ) {
        Map<String,Object> param = new HashMap<String,Object>() ;
        param.put( "name", name ) ;
        param.put( "address", address ) ;
        param.put( "sex", sex ) ;
        return selectPaging( "teacher.selectPaging", param, "name", "ASC", currentPage, pageSize ) ;
    }
    
    /**
     * 保存或更新
     * @param entity 
     */
    public void saveOrUpdate( Teacher entity )
    {
        if( StringUtils.isNotBlank( entity.getId() ) ) {
            update( "teacher.update", entity ) ;
        } else {
            insert( "teacher.insert", entity ) ;
        }
    }
    
    /**
     * 根据编号获取信息。
     * @param id 编号
     * @return 实体
     */
    public Teacher load( String id ) {
        Map<String,String> param = new HashMap<String,String>() ;
        param.put( "id", id ) ;
        return selectOne( "teacher.load", param ) ;
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
        return delete( "teacher.delete", param ) ;
    }
}
