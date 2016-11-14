package data.platform.entity ;

/**
 * 平台-附件与前端控件对应的实体。<br>
 * 用于前端控件展示已上传的附件信息。
 * @author wanggq
 * 
 */
public class EntityPlatformFile
{
    /**
     * 获取附件ID。
     * @return 附件ID
     */
    public String getId()
    {
        return id ;
    }

    /**
     * 设置附件ID。
     * @param id 附件ID
     */
    public void setId( String id )
    {
        this.id = id ;
    }

    /**
     * 获取附件的文件名称。
     * @return 附件的文件名称
     */
    public String getFileName()
    {
        return fileName ;
    }

    /**
     * 设置附件的文件名称。
     * @param fileName 附件的文件名称
     */
    public void setFileName( String fileName )
    {
        this.fileName = fileName ;
    }

    /**
     * 获取附件在服务器上的路径。
     * @return 附件在服务器上的路径
     */
    public String getPath()
    {
        return path ;
    }

    /**
     * 设置附件在服务器上的路径。
     * @param path 附件在服务器上的路径
     */
    public void setPath( String path )
    {
        this.path = path ;
    }
    
    /**
     * 获取附件在服务器上的名称。
     * @return 附件在服务器上的名称
     */
    public String getFileNameInServer() 
    {
		return fileNameInServer;
	}

    /**
     * 设置附件在服务器上的名称。
     * @param fileNameInServer 附件在服务器上的名称
     */
	public void setFileNameInServer(String fileNameInServer) 
	{
		this.fileNameInServer = fileNameInServer;
	}
    private String id ;
    private String fileName ;
    private String path ;
    private String fileNameInServer ;
	
}