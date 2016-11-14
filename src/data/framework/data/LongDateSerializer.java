package data.framework.data ;

import java.io.IOException ;
import java.text.SimpleDateFormat ;
import java.util.Date ;

import org.codehaus.jackson.JsonGenerator ;
import org.codehaus.jackson.JsonProcessingException ;
import org.codehaus.jackson.map.JsonSerializer ;
import org.codehaus.jackson.map.SerializerProvider ;
/**
 * 平台-自定义长时间（yyyy-MM-dd HH:mm）JSON序列化类。
 * @author wanggq
 *
 */
public class LongDateSerializer extends JsonSerializer<Date>
{

    @Override
    public void serialize( Date date, JsonGenerator jsonGenerator, SerializerProvider provider ) throws IOException, JsonProcessingException
    {
        SimpleDateFormat format = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" ) ;
        String formattedDate = format.format( date ) ;
        jsonGenerator.writeString( formattedDate ) ;
    }
}