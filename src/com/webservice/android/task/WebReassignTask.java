package com.webservice.android.task;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;
import android.util.Log;

import com.webservice.android.activities.BaseActivity;
import com.webservice.android.service.CustomHttpClient;
import com.webservice.android.service.CustomHttpsClient;
import com.webservice.android.xml.LoginTranslator;

public class WebReassignTask extends AsyncTask<String, Void, String> {
	private BaseActivity baseActivity;
	private String response="";
    ArrayList<NameValuePair> params;
	
//	public void setParameters(String userId, String password, String jobNum, String individualNum, String groupNum, String sectionNum) {
    // Anna
    public void setParameters(String userId, String password, String jobNum, String individualNum, String groupNum, String sectionNum, String description) {
		NameValuePair paramPair;
 		params=new ArrayList<NameValuePair>();
 		paramPair = new BasicNameValuePair("userID",userId);
        params.add(paramPair);
        paramPair = new BasicNameValuePair("password",password);
        params.add(paramPair);
        paramPair = new BasicNameValuePair("job_num",jobNum);
        params.add(paramPair);
        paramPair = new BasicNameValuePair("individual_num",individualNum);
        params.add(paramPair);
        paramPair = new BasicNameValuePair("group_num",groupNum);
        params.add(paramPair);
        paramPair = new BasicNameValuePair("section_num",sectionNum);
        params.add(paramPair);
        // Anna added the following:
        paramPair = new BasicNameValuePair("description",description);
        params.add(paramPair);
	}
	
	@Override
	protected String doInBackground(String... urls) {
		String protocol = "";
		int location=0;
		CustomHttpClient httpClient=null;
		CustomHttpsClient httpsClient=null;
		for (String url : urls) {
			try {
				location = url.indexOf(":");
				protocol = url.substring(0, location);
				location = url.indexOf(":");
				protocol = url.substring(0, location);
				if (protocol.equalsIgnoreCase("http")) {
					   httpClient = new  CustomHttpClient();
				       response = httpClient.executeHttpPost(url, params);
				       //Log.v("Quantarc","Http Send:"+response);
				 } else if (protocol.equalsIgnoreCase("https")) {
					        httpsClient = new  CustomHttpsClient();
				            response = httpsClient.executeHttpPost(url, params);
				           }
			 } catch (Exception exp) {
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
}