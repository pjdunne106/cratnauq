package com.webservice.android.activities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.webservice.android.common.QuantarcPopupDialog;
import com.webservice.android.task.WebReassignTask;
import com.webservice.android.util.StringComparator;
import com.webservice.android.xml.SupervisorListTranslator;
import com.webservice.domain.Job;
import com.webservice.domain.LabourGroupList;
import com.webservice.domain.LabourPerson;
import com.webservice.domain.User;
import com.webservice.domain.UserList;

public class UserRolesActivity extends DialogActivity {
	ArrayAdapter adapter;
	EditText username, password, dob, st;
	Spinner spinner;
	SupervisorListTranslator translator;
	String schedule;
	String currentUser;
	String status;
	String newHistories;
	TextView person;
	UserList allUsers;
	String response;
	Button applyButton;
	Button previous;
	Job selectedJob;
	List<String> supervisors;
	LabourGroupList labourGroupList;
	ProgressDialog pDialog;
	int count;
	int peopleListSize;
	QuantarcPopupDialog popupDialog;
	WebReassignTask webReassignTask;
	EditText details;	// Anna 2012-06-25

	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_roles);
		schedule = getContext().getSchedule();
		Bundle bundle = getIntent().getExtras();
		labourGroupList = bundle.getParcelable("com.webservice.domain.LabourGroupList");
		applyButton = (Button) findViewById(R.id.ur70);
		spinner = (Spinner) findViewById(R.id.ur60);
		addRoles(spinner, labourGroupList);
		applyButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		String selectedName;
		String listUserName;
		String userId = "", password = "", jobNum = "", individualNum = "", groupNum = "", sectionNum = "", uploadUrl = "", urlString = "";
		String description=""; // Anna 2012-06-25
		boolean found = false;
		Iterator<User> iter;
		Intent intent = new Intent();
		LabourPerson labourPerson;
		User user = null;
		
		if (v.getId() == applyButton.getId()) {
			  setResult(1550,intent);
			  this.finish();
			}
		}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		Intent returnIntent = new Intent();	
		if (status.equalsIgnoreCase("quit")) {
			selectedJob.setJobStatus("Complete");
			returnIntent = new Intent();
			returnIntent.putExtra("confirmstatus", "Complete");
			returnIntent.putExtra("com.webservice.domain.Job", selectedJob);
			getParent().setResult(2060, returnIntent);
			this.finish();
		}
		
	}

	public void update(String data) {
		status = "";
		pDialog.dismiss();
		if (data.equalsIgnoreCase("0")) {
			popupDialog
					.showPopupDialog("Unable to connect to the Server", "ok");
		} else {
			if (data.contains("<RESPONSE><INFO>OK")) {
				popupDialog.showPopupDialog("Job has been reassigned", "ok");
				status = "quit";
			} else {
				popupDialog.showPopupDialog("Unable to reassign Job", "ok");
			}
		}
	}

	private void addRoles(Spinner spinner, LabourGroupList allRoles) {
		List<String> userRoles = new ArrayList<String>();
		Map<String, String> roleMap = allRoles.getLabourGroupMap();
		for (String s : roleMap.keySet()) {
			userRoles.add(roleMap.get(s));
		}
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, userRoles);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapter.sort(new StringComparator());
		adapter.notifyDataSetChanged();
		spinner.setAdapter(adapter);

	}
	
	// Anna 2012-06-25 
	private String validateReassignment(String details) {
		String message = "";
		String displayMessage = "The following error must be corrected: ";
		String value;
		boolean fault = false;
		value = details.trim();
		if (!(value.length() > 0)) {
			displayMessage = displayMessage
					+ "The reason for re-assignment must be entered";
			fault = true;
		}
		if (fault) {
			displayMessage = displayMessage + ".";
			message = displayMessage;
		}
		return message;
	}
}
