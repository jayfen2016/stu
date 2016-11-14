package data.framework.web;

import java.awt.Color ;
import java.awt.Font ;
import java.awt.Graphics ;
import java.awt.image.BufferedImage ;
import java.io.IOException ;
import java.util.Random ;

import javax.imageio.ImageIO ;
import javax.servlet.ServletException ;
import javax.servlet.http.HttpServlet ;
import javax.servlet.http.HttpServletRequest ;
import javax.servlet.http.HttpServletResponse ;
import data.framework.utility.EncryptHelper ;

/**
 * 提供验证码的 servlet。
 * @author wanggq
 */
public class CaptchaServlet extends HttpServlet
{
    /**
     * 保存在 session 中的验证码值的 key 名词。
     */
    public static String CAPTCHA_STRING_NAME = "__plusir_captcha_string__" ;

    @Override
    protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
    {
        response.setHeader( "Pragma", "No-cache" ) ;
        response.setHeader( "Cache-Control", "no-cache" ) ;
        response.setDateHeader( "Expires", 0 ) ;

        int width = 80, height = 20 ;

        BufferedImage image = new BufferedImage( width, height, BufferedImage.TYPE_INT_RGB ) ;
        Graphics graphic = image.getGraphics() ;
        Random random = new Random() ;  

        graphic.setColor( getRandColor( 200, 250 ) ) ;
        graphic.fillRect( 0, 0, width, height ) ;
        Font font = new Font( "Times New Roman", Font.BOLD, 18 ) ;
        graphic.setFont( font ) ;
        graphic.setColor( new Color( 255, 255, 255 ) ) ;
        graphic.drawRect( 0, 0, width - 1, height - 1 ) ;
        graphic.setColor( getRandColor( 160, 200 ) ) ;

        for( int i = 0; i < 155; i++ )
        {
            int x = random.nextInt( width ) ;
            int y = random.nextInt( height ) ;
            int xl = random.nextInt( 12 ) ;
            int yl = random.nextInt( 12 ) ;
            graphic.drawLine( x, y, x + xl, y + yl ) ;
        }

        String validateString = EncryptHelper.generateRandomCode( 4 ).toLowerCase() ;
        request.getSession().setAttribute( CAPTCHA_STRING_NAME, validateString ) ;

        graphic.setColor( new Color( 20 + random.nextInt( 110 ), 20 + random.nextInt( 110 ), 20 + random.nextInt( 110 ) ) ) ;
        graphic.drawString( validateString, 20, 15 ) ;
        graphic.dispose() ;

        ImageIO.write( image, "JPEG", response.getOutputStream() ) ;
    }

    private Color getRandColor( int fc, int bc )
    {
        Random random = new Random() ;
        fc = ( fc > 255 ) ? 255 : fc ;
        bc = ( bc > 255 ) ? 255 : bc ;

        int r = fc + random.nextInt( bc - fc ) ;
        int g = fc + random.nextInt( bc - fc ) ;
        int b = fc + random.nextInt( bc - fc ) ;

        return new Color( r, g, b ) ;
    }
    private static final long serialVersionUID = 8684228160374385430L ;
}