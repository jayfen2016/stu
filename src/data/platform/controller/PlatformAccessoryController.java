package data.platform.controller ;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import data.framework.data.DataSerializer;
import data.framework.support.AbstractBaseController;
import data.framework.support.ConfigContext;
import data.framework.utility.FileHelper;
import data.framework.utility.UploadHelper;
import data.platform.entity.EntityPlatformAccessory;
import data.platform.service.PlatformAccessoryService;

@Controller
@RequestMapping( "platform/accessory_" )
public class PlatformAccessoryController extends AbstractBaseController
{
    @Override
    protected void init( ModelMap model, HttpServletRequest request )
    {
    }

    @RequestMapping( params = "command=upload" )
    public void upload( HttpServletRequest request, HttpServletResponse response ) throws Exception {
    	
        FileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setHeaderEncoding(charSetName);
        
        String userName = request.getParameter( "userId" );
        userName=new String( userName.getBytes( "iso-8859-1" ),"UTF-8") ;
        Calendar today = Calendar.getInstance();
        String year = String.valueOf(today.get(Calendar.YEAR));
        String month = String.valueOf(today.get(Calendar.MONTH) + 1);
        
        String upPath = uploadPath + File.separator + year + File.separator + month + File.separator;
        File uploadFolder = new File(upPath);
        if (!uploadFolder.exists()) {
            uploadFolder.mkdirs();
        }
        List<Map<String, String>> result = new ArrayList<Map<String, String>>();
        List<FileItem> items = upload.parseRequest(request);
        for (FileItem item : items) {
            String fileName = item.getName();
            if (fileName != null && fileName.length() != 0) {
                // 上传文件
                File fullFile = new File(fileName);
                String realName = fullFile.getName();
                String realExtName = FileHelper.getExtFromFileName(realName);
                String serverName = UploadHelper.createFileName(request, realExtName);
                String serverPath = upPath + serverName;
                File savedFile = new File(serverPath);
                item.write(savedFile);

                // 持久化文件信息
                EntityPlatformAccessory fileEntity = new EntityPlatformAccessory();
                fileEntity.setFileName(realName);
                fileEntity.setFileNameExtension(realExtName);
                fileEntity.setFileNameInServer(serverName);
                fileEntity.setFilePathInServer(savedFile.getPath());
                fileEntity.setFileSize(item.getSize());
                fileEntity.setOperator(userName);
                accessoryService.save(fileEntity);
                Map<String, String> map = new HashMap<String, String>();
                map.put("fileName", realName);
                System.out.println("filePath" + realName);
                map.put("path", "");
                map.put("id", fileEntity.getId());
                map.put("filePathInServer", fileEntity.getFilePathInServer());
                map.put("fileNameExtension", fileEntity.getFileNameExtension());
                result.add(map);
            }
        }
        
        String oie = request.getParameter("oie");
        if (StringUtils.isNotBlank(oie)) {
            response.getWriter().write("<html><head><meta http-equiv='Content-Type' content='text/html;charset=UTF-8'></head><body>"+serializer.formatList(result)+"</body></html>");
        } else {
            response.getWriter().write(serializer.formatList(result));
        }
        
    }
    
   
    @RequestMapping( params = "command=uploadById" )
    public void uploadById( HttpServletRequest request, HttpServletResponse response ) throws Exception {
    	
        FileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setHeaderEncoding(charSetName);
        
        Calendar today = Calendar.getInstance();
        String year = String.valueOf(today.get(Calendar.YEAR));
        String month = String.valueOf(today.get(Calendar.MONTH) + 1);
        
        String upPath = uploadPath + File.separator + year + File.separator + month + File.separator;
        File uploadFolder = new File(upPath);
        if (!uploadFolder.exists()) {
            uploadFolder.mkdirs();
        }
        List<Map<String, String>> result = new ArrayList<Map<String, String>>();
        List<FileItem> items = upload.parseRequest(request);
        for (FileItem item : items) {
            String fileName = item.getName();
            if (fileName != null && fileName.length() != 0) {
                // 上传文件
            	EntityPlatformAccessory fileEntity = accessoryService.load(request.getParameter("id"));
                String serverPath = upPath + fileEntity.getFileNameInServer();
                File savedFile = new File(serverPath);
                item.write(savedFile);

                // 持久化文件信息
                fileEntity.setFilePathInServer(savedFile.getPath());
                fileEntity.setFileSize(item.getSize());
                accessoryService.update(fileEntity);
                Map<String, String> map = new HashMap<String, String>();
                map.put("fileName", fileEntity.getFileName());
                System.out.println("filePath" + fileEntity.getFileName());
                map.put("path", "");
                map.put("id", fileEntity.getId());
                map.put("filePathInServer", fileEntity.getFilePathInServer());
                map.put("fileNameExtension", fileEntity.getFileNameExtension());
                result.add(map);
            }
        }
        String oie = request.getParameter("oie");
        if (StringUtils.isNotBlank(oie)) {
            response.getWriter().write("<html><head><meta http-equiv='Content-Type' content='text/html;charset=UTF-8'></head><body>"+serializer.formatList(result)+"</body></html>");
        } else {
            response.getWriter().write(serializer.formatList(result));
        }
        
    }
    
    
    @RequestMapping( params = "command=download" )
    public void download( HttpServletRequest request, HttpServletResponse response ) throws IOException
    {
        String fileId = request.getParameter( "id" ) ;
        if( fileId != null && fileId.length() != 0 )
        {
            EntityPlatformAccessory fileEntity = accessoryService.load( fileId ) ;
            if( fileEntity != null )
            {
                UploadHelper.sendFile(
                        response,
                        fileEntity.getFilePathInServer(),
                        fileEntity.getFileName(),
                        "application/x-msdownload" ) ;
                return ;
            }
        }
        response.getWriter().write( "<script type=\"text/javascript\">alert('文件不存在！');</script>" ) ;
    }
    
    @RequestMapping( params = "command=downloadFile" )
    public void downloadFile( HttpServletRequest request, HttpServletResponse response ) throws IOException
    {
    	String filePathInServer = request.getParameter( "filePathInServer" ) ;
    	String fileName = request.getParameter( "fileName" ) ;
    	
    	if(StringUtils.isEmpty(filePathInServer)){
    		filePathInServer = (String) request.getAttribute("filePathInServer");
    	}
    	if(StringUtils.isEmpty(fileName)){
    		fileName = (String) request.getAttribute("fileName");
    	}
    	
    	filePathInServer=new String( filePathInServer.getBytes( "iso-8859-1" ),"UTF-8") ;
    	fileName=new String( fileName.getBytes( "iso-8859-1" ),"UTF-8") ;
    	File uploadFolder = new File(filePathInServer);
        if (!uploadFolder.exists()) 
        	response.getWriter().write( "<script type=\"text/javascript\">alert('文件不存在！');</script>" ) ;
        else
        	UploadHelper.sendFile(response, filePathInServer, fileName, "application/x-msdownload") ;
                
       
    }
    
    @RequestMapping( params = "command=downloadZip" )
    public void downloadZip( HttpServletRequest request, HttpServletResponse response ) throws IOException
    {
        String idParam = request.getParameter( "ids" ) ;
        String[] ids = idParam.split( "," ) ;
        if( ids != null && ids.length > 0 )
        {
            StringBuffer zipFilePath = new StringBuffer() ; 
            String zipFileName = UUID.randomUUID().toString() + ".zip" ;
            zipFilePath.append( uploadPath ).append( zipFileName ) ;
            File zipFile = new File( zipFilePath.toString() ) ;
            if ( !zipFile.exists() )
            {   
                zipFile.createNewFile();   
            }
            List<EntityPlatformAccessory> fileEntityList = accessoryService.searchAccessoryByIdList( Arrays.asList( ids ) ) ;
            List<File> fileList = new ArrayList<File>() ;
            for( EntityPlatformAccessory entity : fileEntityList )
            {
                File file = new File( entity.getFilePathInServer() ) ;
                fileList.add( file ) ;
            }
            UploadHelper.zipFile( fileList, zipFile ) ;
            UploadHelper.sendFile(
                    response,
                    zipFilePath.toString(),
                    zipFileName,
                    "application/x-msdownload" ) ;
            zipFile.delete() ;
        }
        else
        {
            response.getWriter().write( "<script type=\"text/javascript\">alert('文件不存在！');</script>" ) ;
        }
    }

    /**
     * 删除指定数据库记录并且删除文件
     * 上传空间默认删除方法
     * @param id
     * @param out
     */
    @RequestMapping( params = "command=remove" )
    public void remove( @RequestParam( "id" )String id, java.io.PrintWriter out ) {
    	EntityPlatformAccessory entity = accessoryService.load( id ) ;
        accessoryService.remove( id ) ;
        File file = new File( entity.getFilePathInServer() ) ;
        if( file.exists() ){
            file.delete() ;
        }
        out.print("success");
        
    }
    
    /**
     * 删除指定数据库文件记录(不删除文件)
     * @param id
     * @param out
     */
    @RequestMapping( params = "command=removeWithFile" )
    public void removeWithFile( @RequestParam( "id" )String id, java.io.PrintWriter out ) {
        
        accessoryService.remove( id ) ;
        out.print("success");
        
    }
    
    @RequestMapping( params = "command=loadFileById" )
    public void loadFileById( @RequestParam( "id" ) String id, java.io.PrintWriter out ){
        EntityPlatformAccessory entity = accessoryService.load( id ) ;
        if(null !=entity){
            out.print(this.getSerializer().formatObject(entity));
        }
    }

    @RequestMapping( params = "command=loadFileByIdsArr" )
    public void loadFileByIdsArr( @RequestParam( "data" ) String data, java.io.PrintWriter out ){
        DataSerializer serializer1= this.getSerializer();
        Map<String, Object> map=serializer1.parseMap(data);
        if(null !=map && !map.isEmpty()){
           List<String> listids= (List<String>) map.get("id");
            if(listids !=null && !listids.isEmpty()){
                List<EntityPlatformAccessory> filelist=accessoryService.searchAccessoryByIdList(listids);
                out.print(serializer1.formatList(filelist));
            }
        }
    }


    @Autowired
    @Value( "#{implementsMap['dataSerializer']}" )
    private DataSerializer serializer ;
    @Autowired
    private PlatformAccessoryService accessoryService ;
    
    private static String uploadPath = UploadHelper.getRealUploadPath() ;
    private static String charSetName = ConfigContext.getStringSection( "framework.web.charset" ) ;
    
}