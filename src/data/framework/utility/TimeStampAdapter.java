package data.framework.utility;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * TimeStamp类型数据转换器，用于支持手机端TimeStamp数据类型的传输
 * @author zhengguiyu
 *
 */
public class TimeStampAdapter extends XmlAdapter<String, Timestamp> {

	private static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";

	@Override
	public String marshal(Timestamp arg0) throws Exception {
		return TimetampToStr(arg0);
	}

	@Override
	public Timestamp unmarshal(String arg0) throws Exception {
		return StrToTimestamp(arg0);
	}

	public String TimetampToStr(Timestamp time) {
		SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_FORMAT);
		Date date = new Date(time.getTime());
		return sdf.format(date);
	}

	public Timestamp StrToTimestamp(String str) throws ParseException {
		 SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_FORMAT);
		Date date = sdf.parse(str);;
		return new Timestamp(date.getTime());
	}
}
