package com.webservice.android.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class DateComparator implements Comparator<String>  {
    
	public int compare(String object1, String object2) {
		int comparator=1;
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date1;
		Date date2;
		try {
		       date1 = dateFormat.parse(object1);
		       date2 = dateFormat.parse(object2);
		       if (date1.before(date2)) {
			       comparator=-1;
		        } else if (date1.equals(date2)) {
		        	comparator=0;
		        }
		} catch (ParseException parse) {
			
		}
		return comparator;
		
	};
}