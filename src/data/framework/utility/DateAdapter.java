package data.framework.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Date类型数据转换器，用于支持手机端Date数据类型的传输
 *
 */
public class DateAdapter extends XmlAdapter<String, Date> {

	private static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";

	@Override
	public String marshal(Date arg0) throws Exception {
		return dateToStr(arg0);
	}

	@Override
	public Date unmarshal(String arg0) throws Exception {
		return StrToDate(arg0);
	}

	public String dateToStr(Date time) {
		SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_FORMAT);
		return sdf.format(time);
	}

	public Date StrToDate(String str) throws ParseException {
		 SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_FORMAT);
		Date date = sdf.parse(str);
		return date;
	}
}
