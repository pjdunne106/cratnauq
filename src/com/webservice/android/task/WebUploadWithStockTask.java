package com.webservice.android.task;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;
import android.util.Log;

import com.webservice.android.activities.BaseActivity;
import com.webservice.android.service.CustomHttpClient;
import com.webservice.android.service.CustomHttpsClient;
import com.webservice.domain.JobHistory;
import com.webservice.domain.StockToIssue;
import com.webservice.domain.TimesheetList;

public class WebUploadWithStockTask extends AsyncTask<String, Void, String> {
	private BaseActivity baseActivity;
	private String response = "";
	private JobHistory jobHistory;
	private StockToIssue stockToIssue;
	private String userId, password;

	public void setUser(String userId, String password) {
		this.userId = userId;
		this.password = password;
	}

	public void setJobHistory(JobHistory jobHistory) {
		this.jobHistory = jobHistory;
	}

	@Override
	protected String doInBackground(String... urls) {
		String protocol = "";
		int location = 0;
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
				historyParams = new ArrayList<NameValuePair>();
				histParam = new BasicNameValuePair("userID", userId);
				historyParams.add(histParam);
				histParam = new BasicNameValuePair("password", password);
				historyParams.add(histParam);
				histParam = new BasicNameValuePair("job_num",
						jobHistory.getJobNum());
				historyParams.add(histParam);
				histParam = new BasicNameValuePair("description",
						jobHistory.getDetails());
				historyParams.add(histParam);
				histParam = new BasicNameValuePair("individual_num",
						jobHistory.getLabourPersonId());
				historyParams.add(histParam);
				histParam = new BasicNameValuePair("history_date",
						jobHistory.getHistoryDate());
				historyParams.add(histParam);
				histParam = new BasicNameValuePair("startTime",
						jobHistory.getStartTime());
				historyParams.add(histParam);
				histParam = new BasicNameValuePair("endTime",
						jobHistory.getFinishTime());
				historyParams.add(histParam);
				histParam = new BasicNameValuePair("timeCode",
						jobHistory.getTimeCode());
				historyParams.add(histParam);
				histParam = new BasicNameValuePair("source",
						jobHistory.getSource());
				historyParams.add(histParam);
				histParam = new BasicNameValuePair("job_status",
						jobHistory.getJobStatus());
				historyParams.add(histParam);
				histParam = new BasicNameValuePair("costOfMaterial",
						jobHistory.getCostOfMaterial());
				historyParams.add(histParam);
				Log.v("quantarc", "Job History URL:" + url);
				for (NameValuePair pair : historyParams) {
					Log.v("quantarc", "Job History Key:" + pair.getName()
							+ "  Value:" + pair.getValue());
				}
				if (protocol.equalsIgnoreCase("http")) {
					httpClient = new CustomHttpClient();
					response = httpClient.executeHttpPost(url,historyParams);
				} else if (protocol.equalsIgnoreCase("https")) {
					httpsClient = new CustomHttpsClient();
					response = httpsClient.executeHttpPost(url,historyParams);
				}
			} catch (Exception exp) {
			}
		}
		Log.v("quantarc", "Job History Response:" + response);
		return response;
	}

	public void setBaseActivity(BaseActivity baseActivity) {
		this.baseActivity = baseActivity;
	}

	@Override
	protected void onPostExecute(String result) {
		baseActivity.update(response);
	}

	public void setStockToIssue(StockToIssue stockToIssue) {
		this.stockToIssue = stockToIssue;
	}

	public StockToIssue getStockToIssue() {
		return stockToIssue;
	}
}