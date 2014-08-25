package com.webservice.android.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NotificationActivity extends DialogActivity {

	Button btnsecond_activity;

	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notification_alert);
		btnsecond_activity = (Button) findViewById(R.id.na30);
		btnsecond_activity.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		this.finish();

	}

}
