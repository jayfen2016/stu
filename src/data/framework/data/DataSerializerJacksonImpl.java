package data.framework.data ;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.JsonParser.Feature;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Service;

import data.framework.support.Logger;

/**
 * DataSerializer 接口的 json 实现。
 * @author wanggq
 */
@Service
public class DataSerializerJacksonImpl implements DataSerializer
{
	public DataSerializerJacksonImpl() {
		mapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true) ; 
		mapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true); 
	}
	
    @Override
    @SuppressWarnings( "unchecked" )
    public Map<String,Object> parseMap( String content )
    {
        if( content == null || content.length() == 0 )
            return new HashMap<String,Object>() ;

        try
        {
            return mapper.readValue( content, Map.class ) ;
        }
        catch( Exception ex )
        {
            Logger.logError( ex ) ;
        }

        return null ;
    }

    @Override
    public String formatMap( Map<String,Object> map )
    {
        if( map == null || map.size() == 0 )
            return "{}" ;

        StringWriter writer = new StringWriter();

        try
        {
            mapper.writeValue( writer, map ) ;
        }
        catch( Exception ex )
        {
            Logger.logError( ex ) ;
        }

        return writer.toString() ;
    }

    @Override
    public List<?> parseList( String content )
    {
        if( content == null || content.length() == 0 )
            return new ArrayList<Object>() ;

        try
        {
            return mapper.readValue( content, List.class ) ;
        }
        catch( Exception ex )
        {
            Logger.logError( ex ) ;
        }

        return null ;
    }

    @Override
    public <T extends Object> T  parseObject( String content, Class<T> type )
    {
        if( content == null || content.length() == 0 )
            return null ;

        try
        {
            return mapper.readValue( content, type ) ;
        }
        catch( Exception ex )
        {
            Logger.logError( ex ) ;
        }

        return null ;
    }

    @Override
    public String formatList( List<?> list )
    {
        if( list == null || list.size() == 0 )
            return "[]" ;

        StringWriter writer = new StringWriter();

        try
        {
            mapper.writeValue( writer, list ) ;
        }
        catch( Exception ex )
        {
            Logger.logError( ex ) ;
        }

        return writer.toString() ;
    }

    @Override
    public String formatSet( Set<?> set )
    {
        if( set == null || set.size() == 0 )
            return "[]" ;

        StringWriter writer = new StringWriter();

        try
        {
            mapper.writeValue( writer, set ) ;
        }
        catch( Exception ex )
        {
            Logger.logError( ex ) ;
        }

        return writer.toString() ;
    }

    @Override
    public Set<?> parseSet( String content )
    {
        if( content == null || content.length() == 0 )
            return new HashSet<Object>() ;

        try
        {
            return mapper.readValue( content, Set.class ) ;
        }
        catch( Exception ex )
        {
            Logger.logError( ex ) ;
        }

        return null ;
    }

    @Override
    public String message( String message )
    {
        Map<String,Object> result = new HashMap<String,Object>() ;
        result.put( "message", message ) ;
        
        return formatMap( result ) ;
    }
    
    @Override
    public String error( String error )
    {
        Map<String,Object> result = new HashMap<String,Object>() ;
        result.put( "error", error ) ;
        
        return formatMap( result ) ;
    }

    @Override
    public String formatObject( Object content )
    {
        StringWriter writer = new StringWriter();

        try
        {
            mapper.writeValue( writer, content ) ;
        }
        catch( Exception ex )
        {
            Logger.logError( ex ) ;
        }

        return writer.toString() ;
    }

    @Override
    public MapSerializer withMap()
    {
        return new MapSerializerImpl( this ) ;
    }

    private static class MapSerializerImpl implements DataSerializer.MapSerializer
    {
        private MapSerializerImpl( DataSerializerJacksonImpl parent )
        {
            this.parent = parent ;
        }

        @Override
        public String format()
        {
            return parent.formatMap( innerMap ) ;
        }

        @Override
        public MapSerializer put( String key, Object value )
        {
            innerMap.put( key, value ) ;
            return this ;
        }

        @Override
        public void render( PrintWriter out )
        {
            out.print( format() ) ;
        }

        private DataSerializerJacksonImpl parent = null ;
        private Map<String,Object> innerMap = new HashMap<String,Object>() ;
    }

    private ObjectMapper mapper = new ObjectMapper() ;
}