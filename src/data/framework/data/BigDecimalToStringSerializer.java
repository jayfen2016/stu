package data.framework.data;

import java.io.IOException ;
import java.math.BigDecimal ;

import org.codehaus.jackson.JsonGenerator ;
import org.codehaus.jackson.JsonProcessingException ;
import org.codehaus.jackson.map.JsonSerializer ;
import org.codehaus.jackson.map.SerializerProvider ;
/**
 * 平台-自定义BigDecimal转String格式JSON序列化类。
 * @author wanggq
 *
 */
public class BigDecimalToStringSerializer extends JsonSerializer<BigDecimal>
{
    @Override
    public void serialize( BigDecimal value, JsonGenerator jsonGenerator, SerializerProvider provider ) throws IOException, JsonProcessingException
    {
        String val = "" ;
        if( value != null )
            val = value.toString() ;
        jsonGenerator.writeString( val ) ;
    }
}