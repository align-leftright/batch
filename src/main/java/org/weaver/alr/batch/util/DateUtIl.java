package org.weaver.alr.batch.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtIl {
	
	private final static String DEFAULT_FORMAT="yyyy-MM-dd hh:mm:ss";
	
	public static String getDate(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_FORMAT);
		String strDate = "";
		if (date != null) {
			strDate = sdf.format(date);
		}
		return strDate;
	}
	
	public static String getDate(Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String strDate = "";
		if (date != null) {
			strDate = sdf.format(date);
		}
		return strDate;
	}
	
}
