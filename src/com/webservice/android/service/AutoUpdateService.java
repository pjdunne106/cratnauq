package com.webservice.android.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.webservice.android.activities.QuantarcContext;
import com.webservice.android.task.WebDownloadTask;
import com.webservice.android.xml.JobListTranslator;
import com.webservice.domain.JobList;

public class AutoUpdateService extends Service {
public static final int MSG_INCREMENT = 1;
public static final int MSG_COUNTER = 2;

private Intent autoUpdateIntent;
private String userId;
private JobList downloadedJobList;
private JobListTranslator jobListTranslator;
private Messenger messenger; 
private QuantarcContext quantarcContext;
private WebDownloadTask webDownloadTask;

public AutoUpdateService() {
}

@Override
public void onCreate() {
	 super.onCreate();
	 quantarcContext = (QuantarcContext)this.getApplication();
}

   public void update(String data) {
	 downloadedJobList=new JobList();
	 jobListTranslator = new JobListTranslator();
		if (data.length() > 0) {
			downloadedJobList = jobListTranslator.translate(data);
			 try {
				   	final Message message = Message.obtain(null, 1234);
				   	Bundle bundle = new Bundle();
				   	bundle.putParcelable("joblist", downloadedJobList);
				   	message.setData(bundle);
				   	messenger.send(message);
			   } catch(RemoteException remote) {
				       remote.printStackTrace();   
			   }
		}
	}
   
   public void getNewJobs() { 
    String callingUrl = quantarcContext.getServiceUrl()+ "jobs.xml." + quantarcContext.getServerType() + "?uid=" + userId;
    Log.v("AutoUpdateService","UserId:"+userId);
   // webDownloadTask = new WebDownloadTask();
	//webDownloadTask.setService(this);
	//webDownloadTask.execute(callingUrl);
   }

   @Override
   public int onStartCommand(Intent intent, int flags, int startId) {
	  Log.v("AutoUpdateService","onStartCommand");
	 autoUpdateIntent = intent;
	 Bundle bundle = autoUpdateIntent.getExtras();
	 messenger = (Messenger) intent.getParcelableExtra("messenger");
	 userId = intent.getStringExtra("userid");
	 getNewJobs();
     return this.START_NOT_STICKY;
   }
  
   
   @Override
   public IBinder onBind(Intent intent) {
	   return null;
   }
   
   @Override
   public void onDestroy() {
       super.onDestroy();
       Log.i("MyService", "Service Stopped.");
   }    
   

}