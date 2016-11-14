package data.framework.utility ;

import java.io.BufferedOutputStream ;
import java.io.File ;
import java.io.FileInputStream;
import java.io.FileOutputStream ;
import java.io.InputStream ;
import java.io.UnsupportedEncodingException ;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils ;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Table;
import org.apache.poi.hwpf.usermodel.TableCell;
import org.apache.poi.hwpf.usermodel.TableIterator;
import org.apache.poi.hwpf.usermodel.TableRow;
import org.dom4j.Text;

import data.framework.support.ConfigContext ;
import data.framework.support.GlobalContext ;
import data.framework.support.Logger ;

/**
 * <p>本地磁盘 I/O 操作(文件)静态工具类。<p>
 * <p>不可继承，不可实例化。</p>
 * @author wanggq
 */
public final class FileHelper
{
    private FileHelper() {}

    /**
     * 删除指定位置的文件。
     * @param path 要删除的文件的完全路径
     */
    public static void deleteFile( String path )
    {
        try
        {
            File file = new File( path ) ;
            file.delete() ;
        }
        catch( Exception ex )
        {
            Logger.logError( ex ) ;
        }
    }

    /**
     * 在指定位置创建文件。
     * @param filename 文件路径
     */
    public static void createNewFile( String filename )
    {
        File file = new File( filename ) ;
        if( !file.exists() )
        {
            createNewFileOrDir( file, "f" ) ;
        }
    }

    /**
     * 在指定位置创建文件夹。
     * @param dirname 文件夹路径
     */
    public static void createNewDir( String dirname )
    {
        File file = new File( dirname ) ;
        if( file.exists() )
        {
            createNewFileOrDir( file, "d" ) ;
        }
    }

    /**
     * 将字符串写入指定文件。使用配置节 "framework.web.charset" 指定的编码集。
     * @param filename 文件路径
     * @param content 写入内容
     */
    public static void setContentString( String filename, String content )
    {
        setContentString( filename, content, ConfigContext.getStringSection( "framework.web.charset" ) ) ;
    }

    /**
     * 将字符串写入指定文件。
     * @param filename 文件路径
     * @param content 写入内容
     * @param charsetName 字符集名称
     */
    public static void setContentString( String filename, String content, String charsetName )
    {
        try
        {
            setContentBinary(
                    filename,
                    content.getBytes( charsetName ) ) ;
        }
        catch( UnsupportedEncodingException ex )
        {
            Logger.logError( ex ) ;
        }
    }

    /**
     * 将二进制数组写入指定文件。
     * @param filename 文件路径
     * @param arybyte 二进制数组
     */
    public static void setContentBinary( String filename, byte[] arybyte )
    {
        BufferedOutputStream bufferedStream = null ;

        try
        {
            bufferedStream = new BufferedOutputStream( new FileOutputStream( filename ) ) ;
            bufferedStream.write( arybyte ) ;
        }
        catch( Exception ex )
        {
            Logger.logError( ex ) ;
        }
        finally
        {
            try
            {
                if( bufferedStream != null )
                {
                    bufferedStream.close() ;
                }
            }
            catch( Exception ex )
            {
                Logger.logError( ex ) ;
            }
        }
    }

    /**
     * 将输入流写入指定文件。
     * @param filename 文件路径
     * @param inputStream 输入流
     */
    public static void setContentStream( String filename, InputStream inputStream )
    {
        try
        {
            byte[] bytes = IOUtils.toByteArray( inputStream ) ;
            setContentBinary( filename, bytes ) ;
        }
        catch( Exception ex )
        {
            Logger.logError( ex ) ;
        }
        finally
        {
            try
            {
                if( inputStream != null )
                {
                    inputStream.close() ;
                }
            }
            catch( Exception ex )
            {
                Logger.logError( ex ) ;
            }
        }
    }

    /**
     * 将字符串添加进指定文件。使用配置节 "framework.web.charset" 指定的编码集。
     * @param filename 文件路径
     * @param content 写入内容
     */
    public static void appendContentString( String filename, String content )
    {
        appendContentString( filename, content, ConfigContext.getStringSection( "framework.web.charset" ) ) ;
    }

    /**
     * 将字符串添加进指定文件。
     * @param filename 文件路径
     * @param content 写入内容
     * @param charsetName 字符集名称
     */
    public static void appendContentString( String filename, String content, String charsetName )
    {
        try
        {
            appendContentBinary(
                    filename,
                    content.getBytes( charsetName ) ) ;
        }
        catch( UnsupportedEncodingException ex )
        {
            Logger.logError( ex ) ;
        }
    }

    /**
     * 将二进制数组添加进指定文件。
     * @param filename 文件路径
     * @param arybyte 要写入的二进制数组
     */
    public static void appendContentBinary( String filename, byte[] arybyte )
    {
        BufferedOutputStream bufferedStream = null ;

        try
        {
            bufferedStream = new BufferedOutputStream( new FileOutputStream( filename, true ) ) ;
            bufferedStream.write( arybyte ) ;
        }
        catch( Exception ex )
        {
            Logger.logError( ex ) ;
        }
        finally
        {
            try
            {
                if( bufferedStream != null )
                {
                    bufferedStream.close() ;
                }
            }
            catch( Exception ex )
            {
                Logger.logError( ex ) ;
            }
        }
    }

    /**
     * 将输出流添加进指定文件。
     * @param filename 文件路径
     * @param inputStream 要写入的流
     */
    public static void appendContentStream( String filename, InputStream inputStream )
    {
        try
        {
            byte[] bytes = IOUtils.toByteArray( inputStream ) ;
            appendContentBinary( filename, bytes ) ;
        }
        catch( Exception ex )
        {
            Logger.logError( ex ) ;
        }
        finally
        {
            try
            {
                if( inputStream != null )
                {
                    inputStream.close() ;
                }
            }
            catch( Exception ex )
            {
                Logger.logError( ex ) ;
            }
        }
    }

    /**
     * 创建文件并将指定的字符串写入文件。使用配置节 "framework.web.charset" 指定的编码集。
     * @param path 文件路径
     * @param content 写入内容
     */
    public static void writeFileString( String path, String content )
    {
        createNewFile( path ) ;
        setContentString( path, content ) ;
    }

    /**
     * 创建文件并将指定的字符串写入文件。
     * @param path 文件路径
     * @param content 写入内容
     * @param charsetName 字符集名称
     */
    public static void writeFileString( String path, String content, String charsetName )
    {
        createNewFile( path ) ;
        setContentString( path, content, charsetName ) ;
    }

    /**
     * 创建文件并将指定的二进制数组写入文件。
     * @param path 文件路径
     * @param content 要写入的二进制数组
     */
    public static void writeFileBinary( String path, byte[] content )
    {
        createNewFile( path ) ;
        setContentBinary( path, content ) ;
    }

    /**
     * 创建文件并将指定的输入流写入文件。
     * @param path 文件路径
     * @param inputStream 要写入的流
     */
    public static void writeFileStream( String path, InputStream inputStream )
    {
        createNewFile( path ) ;
        setContentStream( path, inputStream ) ;
    }

    /**
     * 取得文件内容，返回二进制数组。
     * @param filename 文件位置
     * @return 文件的二进制表示
     */
    public static byte[] getContentBinary( String filename )
    {
        byte[] result = null ;
        InputStream inputStream = null ;

        try
        {
            inputStream = GlobalContext.getBinary( filename ) ;
            result = IOUtils.toByteArray( inputStream ) ;
        }
        catch( Exception ex )
        {
            Logger.logError( ex ) ;
        }
        finally
        {
            try
            {
                if( inputStream != null )
                {
                    inputStream.close() ;
                }
            }
            catch( Exception ex )
            {
                Logger.logError( ex ) ;
            }
        }

        return result ;
    }

    /**
     * 取得文件内容，返回对应的输入流。
     * @param filename 文件路径
     * @return 文件内容
     */
    public static InputStream getContentStream( String filename )
    {
        return GlobalContext.getBinary( filename ) ;
    }

    /**
     * 取得文件内容，返回文本。使用配置节 "framework.web.charset" 指定的编码集。
     * @param filename 文件路径
     * @return 文本内容
     */
    public static String getContentString( String filename )
    {
        return GlobalContext.getTextResource( filename ) ;
    }

    /**
     * 以指定的字符集读取文件。
     * @param filename 文件路径
     * @param charsetName 字符集名称
     * @return 文本内容
     */
    public static String getContentString( String filename, String charsetName )
    {
        return GlobalContext.getTextResource( filename, charsetName ) ;
    }

    /**
     * 根据文件名获取扩展名。
     * @param filename 文件名
     * @return 扩展名
     */
    public static String getExtFromFileName( String filename )
    {
        int t = filename.lastIndexOf( "\\" ) ;
        if( t != -1 )
            filename = filename.substring( t + 1 ) ;
        t = filename.lastIndexOf( "/" ) ;
        if( t != -1 )
            filename = filename.substring( t + 1 ) ;
        t = filename.lastIndexOf( "." ) ;
        if( t != -1 )
            return filename.substring( t + 1 ).toLowerCase() ;
        return "" ;
    }

    private static void createNewFileOrDir( File file, String type )
    {
        try
        {
            if( type.equals( "d" ) )
                file.mkdirs() ;
            else
            {
                file.getParentFile().mkdirs() ;
                file.createNewFile() ;
            }
        }
        catch( Exception ex )
        {
            Logger.logError( ex ) ;
        }
    }
    
    public static Map<String ,List> upload(HttpServletRequest request, String loadpath) throws Exception {
        File f = new File(loadpath);
        if (!f.exists()) {
            f.mkdir();
        }
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(4 * 1024);
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setSizeMax(40 * 1024 * 1024);
        List<FileItem> list = upload.parseRequest(request);
        Iterator<FileItem> iter = list.iterator();

        List<String> pathList=new ArrayList<String>();
        List<String> nameList=new ArrayList<String>();

        while (iter.hasNext()) {
            FileItem item = iter.next();
            String name = item.getName();
            nameList.add(name);
            String fileName = name.substring(0, name.lastIndexOf("."));
            if (name.contains("\\")) {
                fileName = name.substring(name.lastIndexOf("\\") + 1, name.lastIndexOf("."));
            }
            String suffixName = name.substring(name.lastIndexOf("."));
            String filepath = loadpath + "\\" + System.currentTimeMillis() + suffixName;
            System.out.println(filepath);
            pathList.add(filepath);
            File file = new File(filepath);
            item.write(file);
        }

        Map<String ,List> sendMap=new HashMap<String, List>();
        sendMap.put("fileName",nameList);
        sendMap.put("filePath",pathList);
        return sendMap;
    }
    
    
    public static void poiWordTableReplace(String sourceFile, String newFile,
            Map<String, Text> replaces) throws Exception {
        FileInputStream in = new FileInputStream(sourceFile);
        HWPFDocument hwpf = new HWPFDocument(in);
        Range range = hwpf.getRange();// 得到文档的读取范围
        TableIterator it = new TableIterator(range);
        // 迭代文档中的表格
        while (it.hasNext()) {
            Table tb = (Table) it.next();
            // 迭代行，默认从0开始
            for (int i = 0; i < tb.numRows(); i++) {
                TableRow tr = tb.getRow(i);
                // 迭代列，默认从0开始
                for (int j = 0; j < tr.numCells(); j++) {
                    TableCell td = tr.getCell(j);// 取得单元格
                    // 取得单元格的内容
                    for (int k = 0; k < td.numParagraphs(); k++) {
                        Paragraph para = td.getParagraph(k);

                        String s = para.text();
                        final String old = s;
/*                        for (String key : replaces.keySet()) {
                            if (s.contains(key)) {
                                s = s.replace(key, replaces.get(key).getText());
                            }
                        }
*/                        if (!old.equals(s)) {// 有变化
                            para.replaceText(old, s);
                            s = para.text();
                            System.out.println("old:" + old + "->" + "s:" + s);
                        }

                    } // end for
                } // end for
            } // end for
        } // end while

        FileOutputStream out = new FileOutputStream(newFile);
        hwpf.write(out);

        out.flush();
        out.close();

    }
}
