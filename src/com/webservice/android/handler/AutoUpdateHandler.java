package com.webservice.android.handler;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.webservice.android.activities.JobListActivity;
import com.webservice.domain.JobList;

public class AutoUpdateHandler extends Handler {
	
	Context context;
	JobListActivity listActivity;
	JobList downloadedJobList;
	
	public AutoUpdateHandler() {
	}
	
	public void setContext(Context context) {
		this.context= context;
	}

	public void setListActivity(JobListActivity listActivity) {
		this.listActivity=listActivity;
	}
	
	@Override
	public void handleMessage(Message msg) {
		Log.v("AutoUpdateHandler","Here");
		downloadedJobList = (JobList)msg.getData().getParcelable("NewJobs");
		listActivity.addNewJobs(downloadedJobList);
	}
}
