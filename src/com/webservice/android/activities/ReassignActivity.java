package com.webservice.android.activities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
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
import com.webservice.domain.LabourPerson;
import com.webservice.domain.User;
import com.webservice.domain.UserList;

public class ReassignActivity extends DialogActivity {
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
	Button reassignButton;
	Button previous;
	Job selectedJob;
	List<String> supervisors;
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
		setContentView(R.layout.reassign);
		schedule = getContext().getSchedule();
		translator = new SupervisorListTranslator();
		Bundle bundle = getIntent().getExtras();
		selectedJob = bundle.getParcelable("com.webservice.domain.Job");
		allUsers = bundle.getParcelable("com.webservice.domain.UserList");
		reassignButton = (Button) findViewById(R.id.ra70);
		person = (TextView) findViewById(R.id.ra20);
		person.setTypeface(Typeface.DEFAULT_BOLD);
		person.setGravity(Gravity.CENTER_VERTICAL);
		spinner = (Spinner) findViewById(R.id.ra60);
		details = (EditText) findViewById(R.id.ra65);	// Anna 2012-06-25
		currentUser = getContext().getUsername();
		person.setText(currentUser);
		addUsers(spinner, allUsers, currentUser);
		webReassignTask = new WebReassignTask();
		popupDialog = new QuantarcPopupDialog(this);
		reassignButton.setOnClickListener(this);
		newHistories = getContext().getNewHistories();
	}

	@Override
	public void onClick(View v) {
		String selectedName;
		String listUserName;
		String userId = "", password = "", jobNum = "", individualNum = "", groupNum = "", sectionNum = "", uploadUrl = "", urlString = "";
		String description=""; // Anna 2012-06-25
		boolean found = false;
		Iterator<User> iter;
		LabourPerson labourPerson;
		User user = null;
		
		if (v.getId() == reassignButton.getId()) {
			selectedName = (String) spinner.getSelectedItem();
			if (newHistories.equalsIgnoreCase("true")) {
				status="newhistories";
				popupDialog.showPopupDialog("You have new Job Histories which haven't been uploaded. Press the OK button at the top of the screen to do this", "ok");
			} else if ((selectedName != null) && (!selectedName.equalsIgnoreCase(currentUser))) {
				iter = allUsers.getAllUsers().iterator();
				while ((iter.hasNext()) && (!found)) {
					user = iter.next();
					listUserName = user.getFirstname() + " "
							+ user.getSurname();
					if (listUserName.equalsIgnoreCase(selectedName)) {
						labourPerson = user.getLabourPersonList().get(0);
						jobNum = selectedJob.getJobNum();
						individualNum = labourPerson.getId();
						groupNum = labourPerson.getGroupId();
						sectionNum = labourPerson.getSectionId();
						found = true;
					}
				}
				// Anna 2012-06-25
				if (found) {
					description = details.getText().toString();
					String errorMessage = validateReassignment(description);
					if ((errorMessage.length() == 0) ) {
						description="Re-assigned from PDA: " + description;
					} else if (errorMessage.length() > 0) {
						found = false;
						status="";
						popupDialog.showPopupDialog(errorMessage, "OK");	
					}
				}
				// Anna end
			}
			
			if (found) {
				urlString = getContext().getServiceUrl();
				userId = getContext().getUserId();
				password = getContext().getUserPassword();
				uploadUrl = urlString.concat(super.REASSIGN_JOB) + "."
						+ getContext().getServerType();			
	/*			webReassignTask.setParameters(userId, password, jobNum,
						individualNum, groupNum, sectionNum);
	*/
				// Anna 2012-06-25 replaced with the following and updated the reassignJob.xml.php file to add a record in ops_job_hd_history
				webReassignTask.setParameters(userId, password, jobNum,
						individualNum, groupNum, sectionNum, description);			
				// Anna end
				webReassignTask.setBaseActivity(this);
				webReassignTask.execute(uploadUrl);
				status="";
				pDialog = ProgressDialog.show(this, "Reassigning Job..",
						"Please wait", true, false);
			}
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
		// Log.v("Reassign",data);
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

	private void addUsers(Spinner spinner, UserList allUsers, String currentUser) {
		List<String> userNames = new ArrayList<String>();
		String userName="";
		boolean supervisor;
		for (User user : allUsers.getAllUsers()) {
			supervisor=false;
			userName="";
			for (LabourPerson person : user.getLabourPersonList()) {
				if (person.getSupervisor().equalsIgnoreCase("1")) {
					supervisor=true;
				}
			}
			if (supervisor) {
			  userName = user.getFirstname() + " " + user.getSurname();
			  userName = userName.trim();
			  if (!userName.equalsIgnoreCase(currentUser)) {
			      userNames.add(userName);
			  }
			}
		}
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, userNames);
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