package com.webservice.android.common;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.webservice.android.activities.HistoryDetailsActivity;
import com.webservice.android.activities.JobHistoryActivity;
import com.webservice.android.activities.R;
import com.webservice.android.activities.ReassignActivity;

public class JobDetailsTabActivity extends TabActivity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.job_tab);

		TabHost tabHost = getTabHost();
		// no need to call TabHost.Setup()

		// First Tab
		TabSpec spec1 = tabHost.newTabSpec("Details");
		Intent in1 = new Intent(this, JobHistoryActivity.class);
		spec1.setContent(in1);

		TabSpec spec2 = tabHost.newTabSpec("Re-assign");
		Intent in2 = new Intent(this, ReassignActivity.class);
		spec2.setContent(in2);
		
		TabSpec spec3 = tabHost.newTabSpec("History");
		Intent in3 = new Intent(this, HistoryDetailsActivity.class);
		spec3.setContent(in3);

		tabHost.addTab(spec1);
		tabHost.addTab(spec2);
		tabHost.addTab(spec3);
	}
}