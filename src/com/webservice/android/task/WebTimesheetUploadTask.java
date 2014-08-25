package com.webservice.android.task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;
import android.util.Log;

import com.webservice.android.activities.BaseActivity;
import com.webservice.android.service.CustomHttpClient;
import com.webservice.android.service.CustomHttpsClient;
import com.webservice.domain.Timesheet;
import com.webservice.domain.TimesheetList;

public class WebTimesheetUploadTask extends AsyncTask<String, Void, String> {
	private BaseActivity baseActivity;
	private String response = "";
	private TimesheetList timesheetList;
	private String userId, password;

	public void setUser(String userId, String password) {
		this.userId = userId;
		this.password = password;
	}

	public void setTimeSheetList(TimesheetList timesheetList) {
		this.timesheetList = timesheetList;
	}


	@Override
	protected String doInBackground(String... urls) {
		String protocol = "";
		int location = 0;
		ArrayList<NameValuePair> timesheetParams;
		NameValuePair timeParam;
		CustomHttpClient httpClient=null;
		CustomHttpsClient httpsClient=null;
		if (timesheetList != null) {
			for (String url : urls) {
				location = url.indexOf(":");
				protocol = url.substring(0, location);
				try {
					for (Timesheet timesheet : timesheetList.getAllTimesheets()) {
						if (timesheet.getIsNew().equalsIgnoreCase("true")) {
							timesheetParams = new ArrayList<NameValuePair>();
							timeParam = new BasicNameValuePair("userID", userId);
							timesheetParams.add(timeParam);
							timeParam = new BasicNameValuePair("password",
									password);
							timesheetParams.add(timeParam);
							if (timesheet.getJobRef().length() > 0) {
								// timeParam = new BasicNameValuePair("ts_id",timesheet.getWorkId());
								timeParam = new BasicNameValuePair("ts_id","-1");
							} else {
								timeParam = new BasicNameValuePair("ts_id","-1");
							}
							timesheetParams.add(timeParam);
							timeParam = new BasicNameValuePair("ts_status","INSERT");
							timesheetParams.add(timeParam);
							timeParam = new BasicNameValuePair("ts_indnum",userId);
							timesheetParams.add(timeParam);
							if (timesheet.getJobRef().length() > 0) {
								timeParam = new BasicNameValuePair("ts_date",formatDate(timesheet.getActionDate()) + " "+ timesheet.getStartTime() + ":00");
							} else {
								timeParam = new BasicNameValuePair("ts_date",timesheet.getActionDate()+" "+timesheet.getStartTime()+":00");
							}
							timesheetParams.add(timeParam);
							timeParam = new BasicNameValuePair("ts_timeCode",timesheet.getTimeCode());
							timesheetParams.add(timeParam);
							if (timesheet.getJobRef().length() > 0) {
								timeParam = new BasicNameValuePair("ts_startTime",timesheet.getStartTime() + ":00");
								timesheetParams.add(timeParam);
								timeParam = new BasicNameValuePair("ts_endTime", timesheet.getFinishTime() + ":00");
								timesheetParams.add(timeParam);
							} else {
								timeParam = new BasicNameValuePair("ts_startTime",timesheet.getStartTime()+":00");
								timesheetParams.add(timeParam);
								timeParam = new BasicNameValuePair("ts_endTime", timesheet.getFinishTime()+":00");
								timesheetParams.add(timeParam);
							}
							if (timesheet.getJobRef().length() > 0) {
								timeParam = new BasicNameValuePair("ts_jobRef",timesheet.getJobRef());
								timesheetParams.add(timeParam);
							}
							Iterator<NameValuePair> iter = timesheetParams
									.iterator();
							BasicNameValuePair basicNameValuePair;
							
							while (iter.hasNext()) {
								basicNameValuePair = (BasicNameValuePair) iter
										.next();
								//Log.v("Quantarc",
								//		"Pair:" + basicNameValuePair.getName()
								//				+ "::"
								//				+ basicNameValuePair.getValue());
							}
							
							if (protocol.equalsIgnoreCase("http")) {
								httpClient = new CustomHttpClient();
							    response = httpClient.executeHttpPost(url, timesheetParams);
							} else if (protocol.equalsIgnoreCase("https")) {
								httpsClient = new CustomHttpsClient();
								response = httpsClient.executeHttpPost(url, timesheetParams);
							}
						}
					}
				} catch (Exception exp) {
				}
			}
		}
		return response;
	}

	public void setBaseActivity(BaseActivity baseActivity) {
		this.baseActivity = baseActivity;
	}

	@Override
	protected void onPostExecute(String result) {
		baseActivity.update(response);
	}

	private String formatDate(String fromDate) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat fromFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = null;
		String toDate = "";
		try {
			date = fromFormat.parse(fromDate);
			toDate = dateFormat.format(date);
		} catch (Exception exp) {

		}
		return toDate;
	}
}