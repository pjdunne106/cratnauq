package com.webservice.android.activities;

import java.util.Iterator;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.webservice.android.util.StringComparator;
import com.webservice.domain.Job;
import com.webservice.domain.StatusList;


public class ConfirmStatusActivity extends DialogActivity {
	ArrayAdapter<String> statusAdapter;
	List<String> statuses;
    StatusList statusList=null;
	EditText username, password, dob, st;
	Job selectedJob;
	TextView error;
	String response;
	Button okButton;
	Spinner statusSpinner;
	int count;
	int peopleListSize;
	int currentStatus;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.confirm_status);
		Bundle bundle = getIntent().getExtras();
		okButton = (Button) findViewById(R.id.cs40);
		okButton.setOnClickListener(this);
		statusList = bundle.getParcelable("com.webservice.domain.StatusList");
		selectedJob = bundle.getParcelable("com.webservice.domain.Job");
		statusSpinner = (Spinner) findViewById(R.id.cs110);
		statuses = statusList.getValues();
		statusAdapter = new ArrayAdapter<String>(this,
		android.R.layout.simple_spinner_item, statuses);
		currentStatus = this.getCurrentSelection(selectedJob, statusList);
		statusAdapter.sort(new StringComparator());												// Anna 2012-07-06 sorting
		statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(statusAdapter);
        statusSpinner.setSelection(currentStatus);
	}
	
	@Override
	public void onClick(View v) {
		String status = statusSpinner.getSelectedItem().toString();
		Intent intent = new Intent();
		intent.putExtra("confirmstatus", status);
		setResult(1560,intent);
		this.finish();
	}
	
	private int getCurrentSelection(Job selectedJob, StatusList statusList) {
		String status="";
		String jobStatus="";
		int selection = 0;
		int index = 0;
		Iterator<String> iter = statuses.iterator();
		boolean found = false;
		String jobStatusId = selectedJob.getJobStatus().trim();
		if ((jobStatusId != null) && (jobStatusId.length()>0)) {
			jobStatus = statusList.getStatusMap().get(jobStatusId);
			status = "";
			while ((iter.hasNext() && (!found))) {
				status = iter.next();
				if (jobStatus.equalsIgnoreCase(status)) {
					found = true;
				} else {
					index = index + 1;
				}
			}
		}
		if (found) {
			selection = index;
		}
		return selection;
	}
	
}