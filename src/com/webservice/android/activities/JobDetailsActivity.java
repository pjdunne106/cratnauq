package com.webservice.android.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.webservice.android.common.QuantarcPopupDialog;
import com.webservice.android.listeners.LoginListener;
import com.webservice.domain.Job;
import com.webservice.domain.StatusList;

public class JobDetailsActivity extends DialogActivity {
	EditText username, password, dob, st;
	StatusList statusList;
	TextView error;
	TextView jobRefTV;
	TextView locTV;
	TextView contactTV;
	TextView targetTV;
	TextView typeTV;
	TextView statusTV;
	EditText jobDetailsTV;
	EditText otherTV;
	String response;
	String statusName;
	String jobDetails;
	String otherDetails; // Anna 2012-07-12
	Button issueButton;
	Button previous;
	Button ppmJob;
	LoginListener loginListener;
	int count;
	String statusId;
	int peopleListSize;
	Job selectedJob;
	QuantarcPopupDialog popupDialog;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.job_details);
		Bundle bundle = getIntent().getExtras();
		selectedJob = bundle.getParcelable("com.webservice.domain.Job");
		statusList = bundle.getParcelable("com.webservice.domain.StatusList");
		jobRefTV = (TextView) findViewById(R.id.jd11);
		jobRefTV.setTypeface(Typeface.DEFAULT_BOLD);
		locTV = (TextView) findViewById(R.id.jd13);
		locTV.setTypeface(Typeface.DEFAULT_BOLD);
		contactTV = (TextView) findViewById(R.id.jd15);
		contactTV.setTypeface(Typeface.DEFAULT_BOLD);
		targetTV = (TextView) findViewById(R.id.jd17);
		targetTV.setTypeface(Typeface.DEFAULT_BOLD);
		typeTV = (TextView) findViewById(R.id.jd19);
		typeTV.setTypeface(Typeface.DEFAULT_BOLD);
		statusTV = (TextView) findViewById(R.id.jd21);
		statusTV.setTypeface(Typeface.DEFAULT_BOLD);
		statusId = selectedJob.getJobNumLong();
		/* comment id not required */
		ppmJob = (Button) findViewById(R.id.jd50);
		if (selectedJob.getJobNumLong().contains("PPM"))
		       ppmJob.setOnClickListener(this);
		else
			   ppmJob.setVisibility(View.INVISIBLE);
		jobDetailsTV = (EditText) findViewById(R.id.jd23);
		jobDetails = selectedJob.getJobDetails();
		jobDetailsTV.setText(jobDetails);
		jobDetailsTV.setClickable(true);
		jobDetailsTV.setFocusable(false);
		jobDetailsTV.setEnabled(true);
		jobDetailsTV.setOnClickListener(this);
		otherTV = (EditText) findViewById(R.id.jd25);
		otherDetails = selectedJob.getOtherDetails(); // Anna 2012-07-12
		otherTV.setText(otherDetails); // Anna 2012-07-12
		otherTV.setEnabled(false);
		jobRefTV.setText(selectedJob.getJobNumLong());
		locTV.setText(selectedJob.getLocDesc());
		contactTV.setText(selectedJob.getRequestor() + " (ext: "
				+ selectedJob.getTelExt() + ")");
		targetTV.setText(selectedJob.getTargetDate());
		typeTV.setText(selectedJob.getJobType());
		statusTV.setText(statusList.getStatus(selectedJob.getJobStatus()));
		popupDialog = new QuantarcPopupDialog(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		int id = v.getId();
		if (id==jobDetailsTV.getId()) {
			 popupDialog.showPopupDialog(jobDetailsTV.getText().toString(), "ok");
			 /* comment if not required */
		} else if (id==ppmJob.getId()) {
			intent = new Intent(this, PPMViewTabActivity.class);
			intent.putExtra("com.webservice.domain.Job", selectedJob);
			startActivity(intent);
		   }
	}
}
