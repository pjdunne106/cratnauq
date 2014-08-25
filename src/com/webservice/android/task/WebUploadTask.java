package com.webservice.android.task;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;
import android.util.Log;

import com.webservice.android.activities.BaseActivity;
import com.webservice.android.service.CustomHttpClient;
import com.webservice.android.service.CustomHttpsClient;
import com.webservice.android.util.DateUtil;
import com.webservice.domain.JobHistory;
import com.webservice.domain.StatusList;
import com.webservice.domain.TimesheetList;

public class WebUploadTask extends AsyncTask<String, Void, String> {
	private static String LONG_DATE_FORMAT="yyyy-MM-dd kk:mm:ss";
	private static String STANDARD_DATE_FORMAT="dd/MM/yyyy kk:mm:ss";
	private BaseActivity baseActivity;
	private String response="";
	private List<JobHistory> jobHistories;
	private TimesheetList timesheetList;
	private StatusList statusList;
    private String userId, password;
    
    public void setUser(String userId, String password) {
    	this.userId = userId;
    	this.password = password;
    }
    
	public void setJobHistories(List<JobHistory> jobHistories) {
		this.jobHistories = jobHistories;
	}
	
	public void setTimeSheetList(TimesheetList timesheetList) {
		this.timesheetList = timesheetList;
	}

	
	@Override
	protected String doInBackground(String... urls) {
		String protocol = "";
		int location=0;
		ArrayList<NameValuePair> historyParams;
		ArrayList<NameValuePair> timesheetParams;
		NameValuePair histParam;
		NameValuePair timeParam;
		CustomHttpClient httpClient=null;
		CustomHttpsClient httpsClient=null;
		for (String url : urls) {
			try {
				location = url.indexOf(":");
				protocol = url.substring(0, location);
				for (JobHistory jobHistory:jobHistories) {
					historyParams = new ArrayList<NameValuePair>();
					histParam = new BasicNameValuePair("userID",userId);
				    historyParams.add(histParam);
				    histParam = new BasicNameValuePair("password",password);
				    historyParams.add(histParam);
				    histParam = new BasicNameValuePair("job_num",jobHistory.getJobNum());
				    historyParams.add(histParam);
				    histParam = new BasicNameValuePair("description",jobHistory.getDetails().trim());
				    historyParams.add(histParam);
				    histParam = new BasicNameValuePair("individual_num",jobHistory.getLabourPersonId());
				    historyParams.add(histParam);
				    histParam = new BasicNameValuePair("history_date",DateUtil.formatDate(jobHistory.getHistoryDate(),LONG_DATE_FORMAT,STANDARD_DATE_FORMAT));
				    historyParams.add(histParam);
				    histParam = new BasicNameValuePair("startTime",getTime(jobHistory.getStartTime()));
				    historyParams.add(histParam);
				    histParam = new BasicNameValuePair("endTime",getTime(jobHistory.getFinishTime()));
				    historyParams.add(histParam);
				    histParam = new BasicNameValuePair("timeCode",jobHistory.getTimeCode());
				    historyParams.add(histParam);
				    histParam = new BasicNameValuePair("source",jobHistory.getSource());
				    historyParams.add(histParam);
				    histParam = new BasicNameValuePair("job_status",getStatusId(jobHistory.getJobStatus()));
				    historyParams.add(histParam);
				    histParam = new BasicNameValuePair("update_timesheet",jobHistory.getUpdateTimesheet());
				    historyParams.add(histParam);
				    histParam = new BasicNameValuePair("costOfMaterial",jobHistory.getCostOfMaterial());
				    historyParams.add(histParam);
			
				    
				   for (NameValuePair pair:historyParams) {
				    	Log.v("quantarc","Job History Key:"+pair.getName()+"  Value:"+pair.getValue());
				    }
				    
				    if (protocol.equalsIgnoreCase("http")) {
				    	httpClient = new CustomHttpClient();
				    	Log.v("quantarc",url.toString()+historyParams.toString());
				        response = httpClient.executeHttpPost(url, historyParams);
				        Log.v("quantarc","WebUpload Response:"+response);
				    } else if (protocol.equalsIgnoreCase("https")) {
				    	httpsClient = new CustomHttpsClient();
				         response = httpsClient.executeHttpPost(url, historyParams);
				           }
				}
			} catch (Exception exp) {
			   }
		 }
		return response;
	}

	public void setBaseActivity(BaseActivity baseActivity) {
		this.baseActivity = baseActivity;
	}
	
	public void setStatusList(StatusList statusList) {
		this.statusList = statusList;
	}
	
	private String getTime(String historyTime) {
		String time="";
		if (historyTime.length()>10) {
			time=historyTime.substring(11, historyTime.length());
		} else {
			time = historyTime+":00";
		   }
	   return time;
	}
	
	
	
	private String getStatusId(String statusName) {
		Iterator<String> keyList = statusList.getStatusMap().keySet().iterator();
		boolean found=false;
		String key;
		String requiredKey="";
		String status;
		while ((!found) && (keyList.hasNext())) {
			key = keyList.next();
			status = statusList.getStatusMap().get(key);
			if (status.equalsIgnoreCase(statusName)) {
				found=true;
				requiredKey = key;
			}
		}
		return requiredKey;
	}
	
	
	@Override
	protected void onPostExecute(String result) {
		baseActivity.update(response);
	}

	
}