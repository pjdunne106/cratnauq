package com.webservice.android.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.webservice.android.activities.QuantarcContext;
import com.webservice.android.task.WebDownloadTask;
import com.webservice.android.xml.JobListTranslator;
import com.webservice.domain.Job;
import com.webservice.domain.JobList;

public class AutoDownloadService extends IntentService {
	
	WebDownloadTask webDownloadTask;
	JobList downloadedJobList;
	JobList jobList;
	JobList newJobs;
	JobListTranslator jobListTranslator;
	QuantarcContext quantarcContext;
	Messenger messenger;
	String userId;
	
	public AutoDownloadService() {
		super("AutoDownloadService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		
		Log.v("MyService", "About to execute MyTask");
		userId = intent.getExtras().getString("userid");
		jobList = intent.getParcelableExtra("com.webservice.domain.JobList");
		messenger = intent.getParcelableExtra("messenger");
		getNewJobs();
	}
	
	 public void getNewJobs() { 
		    quantarcContext = (QuantarcContext)this.getApplication();
		    String callingUrl = quantarcContext.getServiceUrl()+ "jobs.xml." + quantarcContext.getServerType() + "?uid=" + userId;
		    Log.v("AutoUpdateService","UserId:"+userId);
		    webDownloadTask = new WebDownloadTask();
			webDownloadTask.setService(this);
			webDownloadTask.execute(callingUrl);
		   }

	 public void update(String data) {
		 boolean different = false;
		 boolean found=false;
		 int counter=0;
		 int numberOfJobs=0;
		 Job job;
		 downloadedJobList=null;;
		 jobListTranslator = new JobListTranslator();
			if (data.length() > 0) {
				downloadedJobList = jobListTranslator.translate(data);
			}
			if (downloadedJobList != null) {
				numberOfJobs = downloadedJobList.getJobList().size();
			}
			Log.v("AutoDownLoad","Downloaded Jobs:"+downloadedJobList.getJobList().size());
			Log.v("AutoDownLoad","Current Jobs:"+jobList.getJobList().size());
			newJobs = new JobList();
			while (counter<numberOfJobs) {
			     job = downloadedJobList.getJobList().get(counter);
			     found = false;
				 for (Job newjob:jobList.getJobList()) {
					 if (job.getJobNum().equalsIgnoreCase(newjob.getJobNum())) {						 
				        found=true;
					 }
			     }
			     if (!found) {
			    	 different = true;
			    	 newJobs.getJobList().add(job);
			     } 
			     counter++;
		    }
		if (different) {
			sendMessage();
		}
     }

	private void sendMessage() {
		final Message message = Message.obtain(null, 1234);
		Bundle b = new Bundle();
		b.putParcelable("NewJobs", newJobs);
		message.setData(b);
		try {
                messenger.send(message);
         } catch (RemoteException exception) {
                exception.printStackTrace();
            }
	}

}
