package data.oa.teacherManagement.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractBaseController;
import data.oa.stuManagement.entity.EntityStudent;
import data.oa.teacherManagement.entity.Teacher;
import data.oa.teacherManagement.service.TeacherService;

@Controller
@RequestMapping("oa/teacher")
public class TeacherController extends AbstractBaseController{

	@Autowired
	private TeacherService teacherService;
	
	@Override
	protected void init(ModelMap model, HttpServletRequest request) {
		// TODO Auto-generated method stub
		
	}

	 /**
     * 分页查询老师信息的方法。
     * @param data 输入参数(由 Browser 端 POST 回来的JSON数据)
     * @param out 响应输出对象
     */
    @SuppressWarnings( "unchecked" )
    @RequestMapping( params = "command=search" )
    public void search( @RequestParam("data") String data, java.io.PrintWriter out ) {
    	Map<String,Object> paramMap = getSerializer().parseMap( data ) ;
    	
    	boolean isFast = parseBoolean( paramMap.get( "isFast" ) ) ;
    	
//    	String name = parseString( paramMap.get( "name" ) ) ;
//    	String address = parseString( paramMap.get( "address" ) ) ;
//        Integer sex = parseInteger( paramMap.get( "sex" ) ) ;
        
        String name = "" ;
        String address = "" ;
        Integer sex = null ;
        if( isFast )
            name = trimString( paramMap.get( "q" ) ) ;
        else
        {
            name = trimString( paramMap.get( "name" ) ) ;
            address = trimString( paramMap.get( "address" ) ) ;
            Map<String,Object> sexData = (Map<String,Object>)paramMap.get( "sex" );
            sex = parseInteger( sexData == null ? null : sexData.get( "value" ) ) ;
        }
        
    	 int currentPage = parseInteger( paramMap.get( "page" ) ) ;
         int pageSize = parseInteger( paramMap.get( "rows" ) ) ;
         PagingResult<Teacher> pagingResult = teacherService.search(name,address,sex,currentPage, pageSize);
         
         List<Teacher> list = pagingResult.getRows() ;
         
         
         List<Map<String,String>> newList = new ArrayList<Map<String,String>>() ;
         for( Teacher entity : list ) {
             Map<String,String> map = new HashMap<String,String>() ;
             map.put( "id", formatString( entity.getId() ) ) ;
             map.put( "name", formatString( entity.getName() ) ) ;
             map.put( "address", formatString( entity.getAddress() ) ) ;
             map.put( "sex", formatBoolean( entity.getSex() == 0 ? false : true, "女|男" ) ) ;
             newList.add( map ) ;
         }
         PagingResult<Map<String,String>> newPagingResult = new PagingResult<Map<String,String>>( newList, pagingResult.getPage(), pagingResult.getPageSize(), pagingResult.getRecords() ) ;
         out.print( getSerializer().formatObject( newPagingResult ) ) ;
    }

	
    /**
     * 根据一个实体 ID 来加载学生详细信息的 Web 方法。<br /><br />
     * 命令: "load" ；<br/><br/>
     * @param id 输入参数(由 Browser 端 POST 回来的用户编号)
     * @param out 响应输出对象
     */
    @RequestMapping( params = "command=load" )
    public void load( @RequestParam( "id" ) String id, java.io.PrintWriter out ) {
        Teacher entity = teacherService.load( id ) ;
        Map<String,Object> map = getSerializer().parseMap( getSerializer().formatObject( entity ) ) ;
        out.print( getSerializer().formatMap( map ) ) ;
    }
    
    /**
     * 保存学生信息的Web方法。<br /><br />
     * 命令: "submit" ；<br/><br/>
     * @param data 输入参数(由 Browser 端 POST 回来的JSON数据)
     * @param out 响应输出对象
     */
    @SuppressWarnings( "unchecked" )
    @RequestMapping( params = "command=submit" )
    public void submit( @RequestParam( "data" ) String data, java.io.PrintWriter out ) {
        Map<String,Object> paramMap = getSerializer().parseMap( data ) ;
        Teacher entity = null ;
        String id = trimString( paramMap.get( "id" ) ) ;
        if( StringUtils.isNotBlank( id ) ) {
            entity = teacherService.load( id ) ;
        }
        else {
            entity = new Teacher() ;
        }
        entity.setName( parseString( paramMap.get( "name" ) ) ) ;
        entity.setAddress( parseString( paramMap.get( "address" ) ) ) ;
        entity.setSex( parseInteger( paramMap.get( "sex" ) ) ) ;
        
        teacherService.saveOrUpdate( entity ) ;
        
        out.print( getSerializer().message( "" ) ) ;
    }
    
    /**
     * 根据一个实体 ID 来删除学生实体信息的 Web 方法。<br /><br />
     * 命令: "delete" ；<br/><br/>
     * @param data 输入参数(由 Browser 端 POST 回来的JSON数据)
     * @param out 响应输出对象
     */
    @SuppressWarnings( "unchecked" )
    @RequestMapping( params = "command=delete" )
    public void delete( @RequestParam( "data" ) String data, java.io.PrintWriter out )
    {
        Map<String,Object> paramMap = getSerializer().parseMap( data ) ;
        List<String> list = (List<String>)paramMap.get( "id" ) ;
        
        teacherService.remove( list ) ;
        out.print( getSerializer().message( "" ) ) ;
    }
    
    
}