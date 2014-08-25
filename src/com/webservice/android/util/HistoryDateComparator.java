package com.webservice.android.util;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

import android.util.Log;

import com.webservice.domain.JobHistory;

public class HistoryDateComparator implements Comparator<JobHistory> {
	public String dateFormat="dd/MM/yyyy HH:mm:ss";
	
	public int compare(JobHistory h1, JobHistory h2)
    {  
		Date firstDate=null;
		Date secondDate=null;
		int before=-1;
		SimpleDateFormat simpleDateFormat =new SimpleDateFormat(dateFormat);
		String date1 = h1.getHistoryDate();
		String date2 = h2.getHistoryDate();
		try {
		      firstDate = simpleDateFormat.parse(date1);
		      secondDate = simpleDateFormat.parse(date2);
		} catch (Exception exp) {
			Log.v("Quantarc","HistoryDateComparator:"+date1+":"+date2);
		}
		if ((firstDate!=null) && (secondDate!=null) && (firstDate.before(secondDate))) {
			before=1;
		}
        return before;
    } 

}
