package data.framework.utility;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {
	public static String getProperty( String key )
	{
		InputStream inputStream =  new PropertiesUtil().getClass().getClassLoader().getResourceAsStream("zz.properties") ;
		Properties properties = new Properties();
		try 
		{
			properties.load(inputStream);
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		return properties.getProperty(key) ;
	}
}
