package com.webservice.android.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	public static String formatDate(String fromDate, String currentFormat, String toFormat) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(currentFormat);
		SimpleDateFormat fromFormat = new SimpleDateFormat(toFormat);
		Date date=null;
		String toDate="";
		try {
		      date = fromFormat.parse(fromDate);
		      toDate = dateFormat.format(date);
		} catch (Exception exp) {
			toDate=null;
		}
		return toDate;
	}
	
	public static String formatDate(Date fromDate, String toFormat) {
		SimpleDateFormat fromFormat = new SimpleDateFormat(toFormat);
		String toDate="";
		try {
		      toDate = fromFormat.format(fromDate);
		} catch (Exception exp) {
			toDate=null;
		}
		return toDate;
	}
	
	public static Date toDate(String fromDate, String currentFormat) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(currentFormat);
		Date date=null;
		try {
		      date = dateFormat.parse(fromDate);
		} catch (Exception exp) {
			date=null;
		}
		return date;
	}
	
	
	
}
