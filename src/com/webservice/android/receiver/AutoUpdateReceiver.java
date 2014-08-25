package com.webservice.android.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Messenger;
import android.util.Log;

import com.webservice.android.service.AutoDownloadService;
import com.webservice.domain.JobList;

public class AutoUpdateReceiver extends BroadcastReceiver {
	
	@Override
    public void onReceive(Context context, Intent intent) {
	  Messenger messenger;
      Bundle bundle = intent.getExtras();
      String userid;
      JobList jobList=null;
      Intent updater = new Intent(context, AutoDownloadService.class);
      messenger = intent.getParcelableExtra("messenger");
      userid = bundle.getString("userid");
      jobList = bundle.getParcelable("com.webservice.domain.JobList");
      updater.putExtra("userid",userid);
      updater.putExtra("com.webservice.domain.JobList",jobList);
      updater.putExtra("messenger", messenger);
      context.startService(updater);
      Log.v("AlarmReceiver", "Called context.startService from AlarmReceiver.onReceive");
	}
	
}
