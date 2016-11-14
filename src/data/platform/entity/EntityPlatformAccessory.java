package data.platform.entity ;

import java.util.Date ;

import data.framework.support.AbstractEntity ;

/**
 * 平台-附件实体类。
 * 
 * @author wanggq
 * 
 */
public class EntityPlatformAccessory extends AbstractEntity
{

    /**
     * 获取关联对象ID。
     * @return 关联对象ID
     */
    public String getAssociatedObjectID()
    {
        return associatedObjectID ;
    }

    /**
     * 设置关联对象ID。
     * @param associatedObjectID 关联对象ID
     */
    public void setAssociatedObjectID( String associatedObjectID )
    {
        this.associatedObjectID = associatedObjectID ;
    }

    /**
     * 获取文件来源。
     * @return 文件来源
     */
    public String getFileSource()
    {
        return fileSource ;
    }

    /**
     * 设置文件来源。
     * @param fileSource 文件来源
     */
    public void setFileSource( String fileSource )
    {
        this.fileSource = fileSource ;
    }

    /**
     * 获取文件名称。
     * @return 文件名称
     */
    public String getFileName()
    {
        return fileName ;
    }

    /**
     * 设置文件名称。
     * @param fileName 文件名称
     */
    public void setFileName( String fileName )
    {
        this.fileName = fileName ;
    }

    /**
     * 获取文件扩展名。
     * @return 文件扩展名
     */
    public String getFileNameExtension()
    {
        return fileNameExtension ;
    }

    /**
     * 设置文件扩展名。
     * @param fileNameExtension 文件扩展名
     */
    public void setFileNameExtension( String fileNameExtension )
    {
        this.fileNameExtension = fileNameExtension ;
    }

    /**
     * 获取文件在服务器的名称。
     * @return 文件在服务器的名称
     */
    public String getFileNameInServer()
    {
        return fileNameInServer ;
    }

    /**
     * 设置文件在服务器的名称。
     * @param fileNameInServer 文件在服务器的名称
     */
    public void setFileNameInServer( String fileNameInServer )
    {
        this.fileNameInServer = fileNameInServer ;
    }

    /**
     * 获取文件在服务器的路径。
     * @return 文件在服务器的路径
     */
    public String getFilePathInServer()
    {
        return filePathInServer ;
    }

    /**
     * 设置文件在服务器的路径。
     * @param filePathInServer 文件在服务器的路径
     */
    public void setFilePathInServer( String filePathInServer )
    {
        this.filePathInServer = filePathInServer ;
    }

    /**
     * 获取文件大小。
     * @return 文件大小
     */
    public long getFileSize()
    {
        return fileSize ;
    }

    /**
     * 设置文件大小。
     * @param fileSize 文件大小
     */
    public void setFileSize( long fileSize )
    {
        this.fileSize = fileSize ;
    }

    /**
     * 获取文件上传时间。
     * @return 文件上传时间
     */
    public Date getFileUploadTime()
    {
        return fileUploadTime ;
    }

    /**
     * 设置文件上传时间。
     * @param fileUploadTime 文件上传时间
     */
    public void setFileUploadTime( Date fileUploadTime )
    {
        this.fileUploadTime = fileUploadTime ;
    }

    private String associatedObjectID ;
    private String fileSource ;
    private String fileName ;
    private String fileNameExtension ;
    private String fileNameInServer ;
    private String filePathInServer ;
    private long fileSize ;
    private Date fileUploadTime = new Date() ;
    private static final long serialVersionUID = -9130338607175030478L ;
}