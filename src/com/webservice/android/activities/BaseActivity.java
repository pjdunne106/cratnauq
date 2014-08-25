package com.webservice.android.activities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.webservice.android.service.CustomHttpClient;
import com.webservice.android.service.CustomHttpsClient;

public class BaseActivity extends Activity implements OnClickListener {
	public static String AUTOUPDATE_NOTIFICATION="autoupdate";
	public static String OK_RESPONSE = "OK";
	public static String SCHEDULED_JOBS = "sched.xml";
	public static String USER_LOGIN = "login.xml";
	public static String NEW_JOBS = "newJobs.xml";
	public static String JOBS_LIST = "jobs.xml";
	public static String USER_TIMESHEET = "timesheet.xml";
	public static String USER_NEWPASSWORD = "change_password.xml";
	public static String UPLOAD_HISTORY = "upload.xml";
	public static String REASSIGN_JOB = "reassignJob.xml";
	public String dateTimeFormat = "dd/MM/yyyy kk:mm:ss";
	public String STANDARD_DATE_FORMAT = "dd/MM/yyyy";
	private String timeFormat = "kk:mm";
	private ArrayAdapter<String> timeAdapter;
	private QuantarcContext context;
	private int displayHeight = 0;
	private int displayWidth = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = (QuantarcContext) this.getApplicationContext();
	}

	@Override
	public void onClick(View v) {
		
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	    Log.v("BaseActivity","Switching Orientation");
	}
	
	public String getServiceUrl() {
		return context.getServiceUrl();
	}

	public void saveServiceURL(String urlText) {
		context.saveServiceURL(urlText);
	}

	public int compareDates(String date1, String date2) {
		SimpleDateFormat simpleFormat = new SimpleDateFormat(STANDARD_DATE_FORMAT);
		Date firstDate=null; 
		Date secondDate=null;	
		int result=99;
		  try {
			   firstDate = simpleFormat.parse(date1);
			   secondDate = simpleFormat.parse(date2);
			   result = firstDate.compareTo(secondDate);
		  } catch (ParseException parseException) {
			  result=99;
		  }
		 return result;
	}
	
	public String getCurrentDate() {
		String strDate;
		SimpleDateFormat simpleFormat = new SimpleDateFormat(STANDARD_DATE_FORMAT);
		Date todaysDate = Calendar.getInstance().getTime();
		strDate = simpleFormat.format(todaysDate);
		return strDate;
	}

	public String getCurrentDateTime() {
		String strDate;
		SimpleDateFormat simpleFormat = new SimpleDateFormat(dateTimeFormat);
		Date todaysDate = Calendar.getInstance().getTime();
		strDate = simpleFormat.format(todaysDate);
		return strDate;
	}

	public String getCurrentTime() {
		String strTime;
		SimpleDateFormat simpleFormat = new SimpleDateFormat(timeFormat);
		Date todaysDate = Calendar.getInstance().getTime();
		strTime = simpleFormat.format(todaysDate);
		return strTime;
	}
	
	public String getData(String urlString, String urlOffSet) {
		String data = "";
		String protocol="";
		int location=0;
		CustomHttpClient customHttpClient;
		CustomHttpsClient customHttpsClient;
		try {
			location = urlString.indexOf(":");
			protocol = urlString.substring(0, location);
			urlString = urlString.concat(urlOffSet);
			if (protocol.equalsIgnoreCase("http")) {
				customHttpClient = new CustomHttpClient();
			    data = customHttpClient.executeHttpGet(urlString);
			} else if (protocol.equalsIgnoreCase("https")) {
				customHttpsClient = new CustomHttpsClient();
			    data = customHttpsClient.executeHttpGet(urlString);
			    }
		} catch (Exception exp) {
		}
		return data;
	}

	public String getData(String urlString, ArrayList<NameValuePair> params) {
		String data = "";
		String protocol="";
		int location=0;
		CustomHttpClient customHttpClient;
		CustomHttpsClient customHttpsClient;
		try {
			 location = urlString.indexOf(":");
			 protocol = urlString.substring(0, location);
			 if (protocol.equalsIgnoreCase("http")) {
				 customHttpClient = new CustomHttpClient();
			    data = customHttpClient.executeHttpPost(urlString, params);
			   // Log.v("Quantarc","GetData Result:"+data);
			 } else if (protocol.equalsIgnoreCase("https")) {
					customHttpsClient = new CustomHttpsClient();
				    data = customHttpsClient.executeHttpPost(urlString, params);
				    Log.v("Quantarc","GetData Result:"+data);
				    }
		} catch (Exception exp) {
			// Log.v("Quantarc","HTTP Post Error");
			exp.printStackTrace();
		}
		return data;
	}
	
	public List<String> getHourList() {
		List<String> hourList = new ArrayList<String>();
		String strTime;
		for (int i=0;i<24;i++) {
			strTime = Integer.toString(i);
			if (strTime.length() == 1) {
				strTime = "0" + strTime;
			}
			hourList.add(strTime);
		}
		return hourList;
	}
	
	public int getDefaultMinutePosition(List<String> list, String value) {
      int position=0;
      int index = value.indexOf(":");
      boolean finish = false;
      String minute="";
      if (index>0) {
    	  value = value.substring(index+1,value.length());
      }
      while ( (position<list.size()) && (!finish)) {
    	  minute = list.get(position);
    	  if (minute.equalsIgnoreCase(value)) {
    		  finish = true;
    	  } else {
    		  position = position + 1;
    	    }
      }
      if (!finish) {
    	  list.add(value);
    	  position = list.size()-1;
      }
      return position;
	}
	
	public int getValueLocation(List<String> list, String value) {
		int storedValue;
		int defaultSelection=0;
		int index=0;
		int last = Integer.valueOf(value);
		String minute;
		boolean found=false;
		while ((!found) && (index<list.size())) {
			minute = list.get(index);
			storedValue = Integer.valueOf(minute);
			if (storedValue==last) {
				found=true;
				defaultSelection = index;
			} else {
				     index=index+1;
						
			}
		}
		return defaultSelection;
	}
	
	public void showDownloadJobMessage() {
		Toast.makeText(this, "A New Job Has Been Downloaded", Toast.LENGTH_LONG).show();
	}
	
	public int getDefaultHourPosition(String[] list, String value) {
	      int position=0;
	      int index = value.indexOf(":");
	      if (index>0) {
	    	  value = value.substring(0, index);
	      }
	      for (int i=0;i<list.length;i++) {
	    	  if (list[i].equalsIgnoreCase(value)) {
	    		  position=i;
	    	  }
	      }
	      return position;
		}

	public int getDisplayWidth() {
		DisplayMetrics displaymetrics; 
		if (displayWidth==0) {	
			displaymetrics = new DisplayMetrics();
		    getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		    displayWidth = displaymetrics.widthPixels;
		}
       return displayWidth;
	}

	public int getDisplayHeight() {
		DisplayMetrics displaymetrics;
		if (displayHeight == 0) {
			displaymetrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
			displayHeight = displaymetrics.heightPixels;
		}
		return displayHeight;
	}

	public ArrayList<String> getMinuteList() {
		ArrayList<String> minuteList = new ArrayList<String>();
		String timeSpan = getContext().getTimeBand();
		String strTime;
		boolean finished = false;
		Integer timeUnit = 0;
		if (timeSpan==null) {
			timeSpan="0";
		}
		int pos = timeSpan.indexOf("0");
		if ((pos==0) && (timeSpan.length()==2)) {
			timeSpan = timeSpan.substring(1);
		}
		if (timeSpan.equalsIgnoreCase("0")) {
			timeSpan="5";
		}
		Integer timeBand = Integer.valueOf(timeSpan);
		timeUnit = timeBand;
		while (!finished) {
			strTime = Integer.toString(timeUnit);
			if (strTime.length() == 1) {
				strTime = "0" + strTime;
			}
			if (strTime.equalsIgnoreCase("60")) {
				minuteList.add("00");
			} else {
			         minuteList.add(strTime);
			    }
			timeUnit = timeUnit + timeBand;
			if (timeUnit > 60) {
				finished = true;
			}
		}
	  return minuteList;
	}
	
	public QuantarcContext getContext() {
		return context;
	}

	public void update(String data) {
	}

	public void doAction() {
	}
}
