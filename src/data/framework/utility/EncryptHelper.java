package data.framework.utility;

import java.io.FileInputStream ;
import java.io.InputStream ;
import java.io.RandomAccessFile ;
import java.security.MessageDigest ;
import java.util.Random ;

import data.framework.support.ConfigContext ;
import data.framework.support.Logger ;

/**
 * <p>加密静态工具类，提供如下三个主要功能：</p>
 * <ul>
 * <li>"BASE64" 算法的编码跟解码</li>
 * <li>根据配置文件对字符串进行单向加密</li>
 * <li>生成指定长度的随机字符串序列</li>
 * </ul>
 * <p>该类不可实例化，不可继承。</p>
 * 
 * @author wanggq
 */
public final class EncryptHelper
{
    private EncryptHelper() {}

    private final static byte[] encodingTable =
    {
        (byte) 'A', (byte) 'B', (byte) 'C', (byte) 'D', (byte) 'E',
        (byte) 'F', (byte) 'G', (byte) 'H', (byte) 'I', (byte) 'J',
        (byte) 'K', (byte) 'L', (byte) 'M', (byte) 'N', (byte) 'O',
        (byte) 'P', (byte) 'Q', (byte) 'R', (byte) 'S', (byte) 'T',
        (byte) 'U', (byte) 'V', (byte) 'W', (byte) 'X', (byte) 'Y',
        (byte) 'Z', (byte) 'a', (byte) 'b', (byte) 'c', (byte) 'd',
        (byte) 'e', (byte) 'f', (byte) 'g', (byte) 'h', (byte) 'i',
        (byte) 'j', (byte) 'k', (byte) 'l', (byte) 'm', (byte) 'n',
        (byte) 'o', (byte) 'p', (byte) 'q', (byte) 'r', (byte) 's',
        (byte) 't', (byte) 'u', (byte) 'v', (byte) 'w', (byte) 'x',
        (byte) 'y', (byte) 'z', (byte) '0', (byte) '1', (byte) '2',
        (byte) '3', (byte) '4', (byte) '5', (byte) '6', (byte) '7',
        (byte) '8', (byte) '9', (byte) '+', (byte) '/'
    };

    private final static byte[] decodingTable ;

    static
    {
        decodingTable = new byte[128] ;
        for( int i = 0; i < 128; i++ )
        {
            decodingTable[i] = (byte)-1 ;
        }
        for( int i = 'A'; i <= 'Z'; i++ )
        {
            decodingTable[i] = (byte)( i - 'A' ) ;
        }
        for( int i = 'a'; i <= 'z'; i++ )
        {
            decodingTable[i] = (byte)( i - 'a' + 26 ) ;
        }
        for( int i = '0'; i <= '9'; i++ )
        {
            decodingTable[i] = (byte)( i - '0' + 52 ) ;
        }
        decodingTable['+'] = 62 ;
        decodingTable['/'] = 63 ;
    }

    /**
     * 对输入的字符串进行 "BASE64" 编码。
     * @param inputString 要编码的字符串
     * @return 编码后的字符串，如果输入为 "null" 或空字符串将原样返回
     */
    public final static String stringBASE64Encode( String inputString )
    {
        if( inputString == null || inputString.length() == 0 )
            return inputString ;

        return encoder( inputString.getBytes() );
    }

    private final static String encoder( byte[] data )
    {
        byte[] bytes ;
        int modulus = data.length % 3 ;
        if( modulus == 0 )
        {
            bytes = new byte[( 4 * data.length ) / 3] ;
        }
        else
        {
            bytes = new byte[4 * ( ( data.length / 3 ) + 1 )] ;
        }

        int dataLength = data.length - modulus ;
        int a1 ;
        int a2 ;
        int a3 ;

        for( int i = 0, j = 0; i < dataLength; i += 3, j += 4 )
        {
            a1 = data[i] & 0xff ;
            a2 = data[i + 1] & 0xff ;
            a3 = data[i + 2] & 0xff ;
            bytes[j] = encodingTable[( a1 >>> 2 ) & 0x3f] ;
            bytes[j + 1] = encodingTable[( ( a1 << 4 ) | ( a2 >>> 4 ) ) & 0x3f] ;
            bytes[j + 2] = encodingTable[( ( a2 << 2 ) | ( a3 >>> 6 ) ) & 0x3f] ;
            bytes[j + 3] = encodingTable[a3 & 0x3f];
        }

        int b1 ;
        int b2 ;
        int b3 ;
        int d1 ;
        int d2 ;

        switch( modulus )
        {
            case 0:
                break ;
            case 1:
                d1 = data[data.length - 1] & 0xff ;
                b1 = ( d1 >>> 2 ) & 0x3f ;
                b2 = ( d1 << 4 ) & 0x3f ;
                bytes[bytes.length - 4] = encodingTable[b1] ;
                bytes[bytes.length - 3] = encodingTable[b2] ;
                bytes[bytes.length - 2] = (byte)'=' ;
                bytes[bytes.length - 1] = (byte)'=' ;
                break ;
            case 2:
                d1 = data[data.length - 2] & 0xff ;
                d2 = data[data.length - 1] & 0xff ;
                b1 = ( d1 >>> 2 ) & 0x3f ;
                b2 = ( ( d1 << 4 ) | ( d2 >>> 4 ) ) & 0x3f ;
                b3 = ( d2 << 2 ) & 0x3f ;
                bytes[bytes.length - 4] = encodingTable[b1] ;
                bytes[bytes.length - 3] = encodingTable[b2] ;
                bytes[bytes.length - 2] = encodingTable[b3] ;
                bytes[bytes.length - 1] = (byte)'=' ;
                break ;
        }
        return new String( bytes ) ;
    }

    /**
     * 对输入的字符串进行 "BASE64" 解码。
     * @param inputString 要解码的字符串
     * @return 解码后的字符串，如果输入为 "null" 或空字符串将原样返回
     */
    public final static String stringBASE64Decode( String inputString )
    {
        if( inputString == null || inputString.length() == 0 )
            return inputString ;

        byte[] bytes ;
        byte b1 ;
        byte b2 ;
        byte b3 ;
        byte b4 ;

        inputString = discardNonBase64Chars( inputString ) ;
        if( inputString.charAt( inputString.length() - 2 ) == '=' )
        {
            bytes = new byte[( ( ( inputString.length() / 4 ) - 1 ) * 3 ) + 1] ;
        }
        else if( inputString.charAt( inputString.length() - 1 ) == '=' )
        {
            bytes = new byte[( ( ( inputString.length() / 4 ) - 1 ) * 3 ) + 2] ;
        }
        else
        {
            bytes = new byte[( ( inputString.length() / 4 ) * 3 )] ;
        }

        for( int i = 0, j = 0; i < ( inputString.length() - 4 ); i += 4, j += 3 )
        {
            b1 = decodingTable[inputString.charAt( i )] ;
            b2 = decodingTable[inputString.charAt( i + 1 )] ;
            b3 = decodingTable[inputString.charAt( i + 2 )] ;
            b4 = decodingTable[inputString.charAt( i + 3 )] ;
            bytes[j] = (byte)( ( b1 << 2 ) | ( b2 >> 4 ) ) ;
            bytes[j + 1] = (byte)( ( b2 << 4 ) | ( b3 >> 2 ) ) ;
            bytes[j + 2] = (byte)( ( b3 << 6 ) | b4 ) ;
        }

        if( inputString.charAt(inputString.length() - 2 ) == '=' )
        {
            b1 = decodingTable[inputString.charAt( inputString.length() - 4 )] ;
            b2 = decodingTable[inputString.charAt( inputString.length() - 3 )] ;
            bytes[bytes.length - 1] = (byte)( ( b1 << 2 ) | ( b2 >> 4 ) ) ;
        }
        else if( inputString.charAt( inputString.length() - 1 ) == '=' )
        {
            b1 = decodingTable[inputString.charAt( inputString.length() - 4 )] ;
            b2 = decodingTable[inputString.charAt( inputString.length() - 3 )] ;
            b3 = decodingTable[inputString.charAt( inputString.length() - 2 )] ;
            bytes[bytes.length - 2] = (byte)( ( b1 << 2 ) | ( b2 >> 4 ) ) ;
            bytes[bytes.length - 1] = (byte)( ( b2 << 4 ) | ( b3 >> 2 ) ) ;
        }
        else
        {
            b1 = decodingTable[inputString.charAt( inputString.length() - 4 )] ;
            b2 = decodingTable[inputString.charAt( inputString.length() - 3 )] ;
            b3 = decodingTable[inputString.charAt( inputString.length() - 2 )] ;
            b4 = decodingTable[inputString.charAt( inputString.length() - 1 )] ;
            bytes[bytes.length - 3] = (byte)( ( b1 << 2 ) | ( b2 >> 4 ) ) ;
            bytes[bytes.length - 2] = (byte)( ( b2 << 4 ) | ( b3 >> 2 ) ) ;
            bytes[bytes.length - 1] = (byte)( ( b3 << 6 ) | b4 ) ;
        }

        return new String( bytes ) ;
    }

    private final static String discardNonBase64Chars( String data )
    {
        StringBuffer sb = new StringBuffer() ;
        int length = data.length() ;
        for( int i = 0; i < length; i++ )
        {
            if( isValidBase64Byte( (byte)( data.charAt( i ) ) ) )
            {
                sb.append( data.charAt( i ) ) ;
            }
        }
        return sb.toString() ;
    }

    private final static boolean isValidBase64Byte( byte b )
    {
        if( b == '=' )
        {
            return true ;
        }
        else if( ( b < 0 ) || ( b >= 128 ) )
        {
            return false ;
        }
        else if( decodingTable[b] == -1 )
        {
            return false ;
        }
        return true ;
    }

    /**
     * 对一个二进制流进行BASE64编码。
     * @param inputStream 要编码的二进制流
     * @return BASE64编码输出
     */
    public static String streamBASE64Encode( InputStream inputStream )
    {
        String content = null ;
        try
        {
            byte[] bytes = new byte[inputStream.available()] ;
            inputStream.read( bytes ) ;
            content = new sun.misc.BASE64Encoder().encode( bytes ) ;
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
        return content ;
    }

    /**
     * 对一个二进制文件进行BASE64编码。
     * @param filePath 文件路径
     * @return BASE64编码输出
     */
    public static String streamBASE64Encode( String filePath )
    {
        String content = null ;
        FileInputStream fileForInput = null ;
        try
        {
            fileForInput = new FileInputStream( filePath ) ;
            byte[] bytes = new byte[fileForInput.available()] ;
            fileForInput.read( bytes ) ;
            content = org.apache.commons.codec.binary.Base64.encodeBase64String( bytes ) ;
            fileForInput.close() ;
        }
        catch( Exception ex )
        {
            Logger.logError( ex ) ;
        }
        finally
        {
            try
            {
                if( fileForInput != null )
                {
                    fileForInput.close() ;
                }
            }
            catch( Exception ex )
            {
                Logger.logError( ex ) ;
            }
        }
        return content ;
    }

    /**
     * 将输入字符串按BASE64算法进行解码，输出二进制数组。<br />
     * <b>注意：</b>方法返回的输出流由调用端负责关闭。
     * @param inputString BASE64字符串
     * @return 输出数组
     */
    public static byte[] streamBASE64Decode( String inputString )
    {
        byte[] result = null ;
        try
        {
            result = new sun.misc.BASE64Decoder().decodeBuffer( inputString.trim() ) ;
        }
        catch( Exception ex )
        {
            Logger.logError( ex ) ;
        }
        return result ;
    }

    /**
     * 将输入字符串按BASE64算法进行解码，写入指定位置的文件。
     * @param inputString BASE64字符串
     * @param filePath 输出文件路径
     */
    public static void streamBASE64Decode( String inputString, String filePath )
    {
        RandomAccessFile inOut = null ;
        try
        {
            byte[] result = new sun.misc.BASE64Decoder().decodeBuffer( inputString.trim() ) ;
            inOut = new RandomAccessFile( filePath, "rw" ) ;
            inOut.write( result ) ;
            inOut.close() ;
        }
        catch( Exception ex )
        {
            Logger.logError( ex ) ;
        }
        finally
        {
            try
            {
                if( inOut != null )
                    inOut.close() ;
            }
            catch( Exception ex )
            {
                Logger.logError( ex ) ;
            }
        }
    }

    /**
     * <p>根据输入的字符串生成一个单向加密的字符串，通常用于密码的存储。</p>
     * <p>采用的加密算法由配置文件中的 "framework.encrypt.encode" 小节决定，有下列三种值：<p>
     * <ul>
     * <li>MD5 采用 "MD5(Message-Digest Algorithm 5)" 加密算法</li>
     * <li>SHA1 采用 "SHA-1(Secure Hash Algorithm)" 加密算法</li>
     * <li>NONE 不加密，返回明文</li>
     * </ul>
     * @param inputString 要加密的字符串
     * @return 加密过的字符串，如果输入为 "null" 或空字符串将原样返回
     */
    public final static String encryptEncode( String inputString )
    {
        if( inputString == null || inputString.length() == 0 )
            return inputString ;

        String encryptType = ConfigContext.getStringSection( "framework.encrypt.encode" ) ;
        if( encryptType.equals( "MD5" ) || encryptType.equals( "SHA1" ) )
        {
            return generateMD5String( inputString, encryptType ) ;
        }
        return inputString ;
    }

    private final static String generateMD5String( String inputString, String type )
    {
        String result = inputString ;
        try
        {
            MessageDigest encoderDigest = MessageDigest.getInstance( type ) ;
            result = encoder( encoderDigest.digest( inputString.getBytes() ) ) ;
        }
        catch( Exception ex )
        {
            result = inputString ;
            Logger.logError( ex ) ;
        }

        return result ;
    }

    private final static String[] passwordDictionary = new String[] {
        "2" ,"3", "4" ,"5", "6", "7", "8", "9",
        "a" ,"b", "c", "d", "e", "f", "g", "h",
        "j", "k", "m", "n", "p", "q", "r", "s",
        "t", "u", "v", "w", "x", "y", "z", "A",
        "B", "C", "D", "E", "F", "G" ,"H", "J",
        "K", "L", "M", "N", "P", "Q", "R", "S",
        "T", "U" ,"W", "X", "Y", "Z"
    };

    /**
     * <p>生成一个指定长度的随机字符串，由数字跟大小写字母混合而成。</p>
     * <p>为了避免混淆，随机序列去掉了两组容易混淆的字符：</p>
     * <ul>
     * <li>小写的字母 <b>"l"</b> 跟数字  <b>"1"</b></li>
     * <li>大写的字母  <b>"O"</b> 跟数字  <b>"0"</b></li>
     * </ul>
     * @param codeMaxLength 随机字符串长度
     * @return 指定长度的随机字符串 
     */
    public final static String generateRandomCode( int codeMaxLength )
    {
        StringBuffer sBuffer = new StringBuffer() ;
        Random random = new Random() ;
        int maxLength = passwordDictionary.length ;

        for( int i = 0; i < codeMaxLength; ++i )
        {
            sBuffer.append( passwordDictionary[random.nextInt( maxLength )] ) ;
        }

        return sBuffer.toString();
    }
}