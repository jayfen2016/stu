package data.framework.utility;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream ;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URLEncoder;
import java.util.Map;
import javax.servlet.http.HttpServletRequest ;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * <p>在 web 容器中进行导出word的静态工具类。</p>
 * <p>不可继承，不可实例化。</p>
 * @author JohnXU
 */
public final class ExportWordHelper
{
    private ExportWordHelper() {}
    
    /**
     * 
     * @param dataMap 处理后的数据
     * @param request 请求实例
	 * @param destWordPath 上传的路径  如果为空，默认使用到    storeFiles目录
     * @param destWordName 要生成的word的名称
     * @param ftlName ftl模板文件名称
     * @param cls 当前类的class
     * @param ftlPath ftl模板文件的相对路径
     */
    @SuppressWarnings("deprecation")
	public static String generatingFile(Map<String,Object> dataMap,HttpServletRequest request,String destWordPath,String destWordName,String ftlName,Class<?> cls,String ftlPath)
    {
    	String tomcatStoreFilesPath="";
    	ApplicationContext ac1=WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
		FreeMarkerConfigurer freemarkerConfig=(FreeMarkerConfigurer)ac1.getBean("freemarkerConfig");
		Configuration  config=freemarkerConfig.getConfiguration();
		config.setClassForTemplateLoading(cls, ftlPath);
		Writer out = null ;
    	try 
    	{
    		Template template=null;
	    	template = config.getTemplate(ftlName, "utf-8");
			if(destWordPath==null||"".equals(destWordPath)){
				destWordPath=request.getRealPath("")+File.separator+"storeFiles"+File.separator;
			}
			out= new OutputStreamWriter(new FileOutputStream(destWordPath+destWordName), "utf-8");
	    	template.process(dataMap, out);
	    	
	    	tomcatStoreFilesPath=request.getRealPath("/")+File.separator+"storeFiles"+File.separator+destWordName;
    	} catch (Exception ex) 
    	{
	    	ex.printStackTrace();
	    	throw new RuntimeException(ex);
    	}
    	finally
    	{
				try {
					if(out!=null)
						out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    			
    	}
    	return tomcatStoreFilesPath;
      }
    
    /**
     * @param dataMap 处理后的数据
     * @param request 请求实例
     * @param destWordName 要生成的word的名称
     * @param ftlName ftl模板文件名称
     * @param cls 当前类的class
     * @param ftlPath ftl模板文件的相对路径
     * @param response 响应实例
     */
    public static void exportInfoToWord(Map<String,Object> dataMap,HttpServletRequest request,String destWordName,String ftlName,Class<?> cls,String ftlPath,HttpServletResponse response)
    {
    	FileInputStream fin =null;
		BufferedOutputStream bos=null;
		OutputStream os =null;
		try 
		{
			String tomcatStoreFilesPath=generatingFile(dataMap,request,null,destWordName,ftlName,cls,ftlPath);
			fin = new FileInputStream(tomcatStoreFilesPath);
			response.reset();
			os=response.getOutputStream();
			response.setCharacterEncoding("utf-8");  
			response.setHeader( "Content-disposition", "attachment; filename="+URLEncoder.encode(destWordName,"UTF-8")) ;
			response.setContentType("application/msword");  
			byte[] buffer = new byte[512];	// 缓冲区
			int bytesToRead = -1;
			bos = new BufferedOutputStream(os); 
			while((bytesToRead = fin.read(buffer)) != -1) 
			{
				bos.write(buffer, 0, bytesToRead);
			}
			bos.flush();
			bos.close();
		} catch (Exception e) 
		{
			e.printStackTrace();
		}finally 
		{
			try 
			{
				if(fin != null)fin.close();
				if(bos != null) bos.close();
			}catch (IOException e) 
			{
				e.printStackTrace();
			}
		  }
    }
}