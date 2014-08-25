package com.webservice.android.task;

import android.os.AsyncTask;
import android.util.Log;

import com.webservice.android.activities.BaseActivity;
import com.webservice.android.service.AutoDownloadService;
import com.webservice.android.service.AutoUpdateService;
import com.webservice.android.service.CustomHttpClient;
import com.webservice.android.service.CustomHttpsClient;

public class WebDownloadTask extends AsyncTask<String, Void, String> {
	private static String PAGE_NOT_FOUND="The page cannot be found";
	private BaseActivity baseActivity;
	private AutoDownloadService baseService;
	private String response="";
	
	public WebDownloadTask() {
		baseActivity=null;
		baseService=null;
	}
	
	@Override
	protected String doInBackground(String... urls) {
		String protocol = "";
		CustomHttpClient httpClient=null;
		CustomHttpsClient httpsClient=null;
		int location=0;
		for (String url : urls) {
			Log.v("Quantarc","Download URL:"+url);
			try {
				location = url.indexOf(":");
				protocol = url.substring(0, location);
				if (protocol.equalsIgnoreCase("http")) {
					httpClient = new  CustomHttpClient();
				    response = httpClient.executeHttpGet(url);
				} else if (protocol.equalsIgnoreCase("https")) {
					httpsClient = new  CustomHttpsClient();
				    response = httpsClient.executeHttpGet(url);
				    }
			} catch (Exception exp) {
				exp.printStackTrace();
				response="0";
			   }
		}
		return response;
	}

	public void setBaseActivity(BaseActivity baseActivity) {
		this.baseActivity = baseActivity;
	}
	
	public void setService(AutoDownloadService theService) {
		this.baseService = theService;
	}
	
	
	@Override
	protected void onPostExecute(String result) {
		if (response.contains(PAGE_NOT_FOUND)) {
			response="0";
		}
		if (baseActivity != null) {
		    baseActivity.update(response);
		}
		if (baseService != null) {
			baseService.update(response);
		}
	}
}