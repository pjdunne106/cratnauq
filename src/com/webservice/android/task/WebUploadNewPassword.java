package com.webservice.android.task;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;
import android.util.Log;

import com.webservice.android.activities.BaseActivity;
import com.webservice.android.service.CustomHttpClient;
import com.webservice.android.service.CustomHttpsClient;

public class WebUploadNewPassword extends AsyncTask<String, Void, String> {
	private BaseActivity baseActivity;
	private String response = "";
	private String userId, password, newpassword;

	public void setUser(String userId, String password) {
		this.userId = userId;
		this.password = password;
	}

	public void setNewPassword(String newpassword) {
		this.newpassword = newpassword;
	}

	@Override
	protected String doInBackground(String... urls) {
		String protocol = "";
		int location = 0;
		ArrayList<NameValuePair> passwordParams;
		NameValuePair passParam;
		CustomHttpClient httpClient = null;
		CustomHttpsClient httpsClient = null;
		for (String url : urls) {
			try {
				location = url.indexOf(":");
				protocol = url.substring(0, location);
				passwordParams = new ArrayList<NameValuePair>();
				passParam = new BasicNameValuePair("userID", userId);
				passwordParams.add(passParam);
				passParam = new BasicNameValuePair("password", password);
				passwordParams.add(passParam);
				passParam = new BasicNameValuePair("newpassword", newpassword);
				passwordParams.add(passParam);

				 for (NameValuePair pair:passwordParams) {
				   Log.v("quantarc","Job History Key:"+pair.getName()+"  Value:"+pair.getValue());
				 }

				if (protocol.equalsIgnoreCase("http")) {
					httpClient = new CustomHttpClient();
					Log.v("quantarc",
							url.toString() + passwordParams.toString());
					response = httpClient.executeHttpPost(url, passwordParams);
					Log.v("quantarc", "WebUpload Response:" + response);
				} else if (protocol.equalsIgnoreCase("https")) {
					httpsClient = new CustomHttpsClient();
					response = httpsClient.executeHttpPost(url, passwordParams);
				}
			} catch (Exception exp) {}
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