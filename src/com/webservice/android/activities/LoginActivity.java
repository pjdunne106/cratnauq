package com.webservice.android.activities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.webservice.android.common.QuantarcPopupDialog;
import com.webservice.android.listeners.LoginListener;
import com.webservice.android.task.WebDownloadTask;
import com.webservice.android.util.CustomSpinnerOnItemSelected;
import com.webservice.android.util.LoginSpinnerOnItemSelected;
import com.webservice.android.util.StringComparator;
import com.webservice.android.util.XMLUtil;
import com.webservice.android.xml.JobDetailsTranslator;
import com.webservice.android.xml.LabourGroupTranslator;
import com.webservice.android.xml.LabourTimeTranslator;
import com.webservice.android.xml.LocationTranslator;
import com.webservice.android.xml.LoginTranslator;
import com.webservice.android.xml.NewJobsTranslator;
import com.webservice.android.xml.StatusTranslator;
import com.webservice.android.xml.ValidUsersTranslator;
import com.webservice.domain.JobDetailsList;
import com.webservice.domain.LabourGroupList;
import com.webservice.domain.LabourPerson;
import com.webservice.domain.LabourTimeList;
import com.webservice.domain.LocationList;
import com.webservice.domain.NewJob;
import com.webservice.domain.StatusList;
import com.webservice.domain.StockItemList;
import com.webservice.domain.StockLevelList;
import com.webservice.domain.StockTradeList;
import com.webservice.domain.StoremanList;
import com.webservice.domain.User;
import com.webservice.domain.UserList;

public class LoginActivity extends DialogActivity {
	private static String SCHEDULE_HEADER="<schedules>";
	EditText username, password, dob, st;
	LabourTimeList labourTimeList;
	QuantarcPopupDialog popupDialog;
	String senior;
	String masterPassword;
	String urlString;
	TextView error;
	String response;
	String userId;
	String action;
	Spinner usersSpinner;
	StoremanList storemanList;
	StockLevelList stockLevelList;
	StockItemList stockItemList;
	StockTradeList stockTradeList;
	StatusList statusList;
	LabourGroupList labourGroupList;
	Button loginButton;
	Button exitButton;
	LoginListener loginListener;
	CustomSpinnerOnItemSelected customSpinnerOnItemSelected;
	LoginSpinnerOnItemSelected loginSpinnerOnItemSelected;
	LabourPerson loggedInPerson;
	ProgressDialog pDialog;
	int count;
	int peopleListSize;
	JobDetailsList jobDetailsList;
	List<String> allValidUsers;
	List<String> allPersonIds;
	List<User> userList;
	LocationList locationList;
	UserList allUsers;
	ArrayAdapter<String> adapter;
	ArrayAdapter<String> labourPersonAdapter;
	XMLUtil xmlUtil;
	WebDownloadTask webDownloadTask;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		action="";
		senior = getContext().getSupervisorName();
		masterPassword = getContext().getSupervisorPassword();
		popupDialog = new QuantarcPopupDialog(this);
		usersSpinner = (Spinner) findViewById(R.id.log40);
		loginSpinnerOnItemSelected = new LoginSpinnerOnItemSelected(this);
		allValidUsers = new ArrayList<String>();
		allPersonIds=new ArrayList<String>();
		webDownloadTask = new WebDownloadTask();
		webDownloadTask.setBaseActivity(this);
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, allValidUsers);
		labourPersonAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, allPersonIds);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapter.add(senior);
		usersSpinner.setAdapter(adapter);
		usersSpinner.setOnItemSelectedListener(loginSpinnerOnItemSelected);
		password = (EditText) findViewById(R.id.log80);
		loginButton = (Button) findViewById(R.id.log90);
		exitButton = (Button) findViewById(R.id.log100);
		exitButton.setOnClickListener(this);
		loginButton.setOnClickListener(this);
		if (getContext().getFromActivity().equalsIgnoreCase("login")) {
		   getDefaultUsers();
		} else {
			getContext().saveFromActivity("login");
		}
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		if (action.equalsIgnoreCase("checkfornewjobs")) {
			switch (which) {
			case DialogInterface.BUTTON_POSITIVE: // Yes button clicked
				break;
			case DialogInterface.BUTTON_NEGATIVE: // No button clicked
				break;
			}
			action = "";
			showJobList();
		} else if (action.equalsIgnoreCase("fault")) {
			     //finish();
			   }
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		String userName = loginSpinnerOnItemSelected.getLoginName();
		String userPassword = password.getText().toString();
		Intent intent = null;
		int id = v.getId();
		if (id == loginButton.getId()) {
			if (userName.equalsIgnoreCase(senior)) {
				if (userPassword.equalsIgnoreCase(masterPassword)) {
					intent = new Intent(this, FormSettingsTabActivity.class);
					startActivity(intent);
				} else {
					popupDialog.showPopupDialog("Invalid Password", "ok");
				}
			} else {
				if (checkPassword(userName, userPassword)) {
					saveKnownUser(userName, userPassword);
					getContext().saveUsername(userName);
					if (checkForNewJobs(userName)) {
						action = "checkfornewjobs";
						popupDialog
								.showPopupDialog(
										"There are new jobs available, download them now ?",
										"yes", "no");
					} else {
						    showJobList();
					}
				} else {
					popupDialog.showPopupDialog("Invalid Password", "ok");
				}
			}
		} else if (id==exitButton.getId()) {
			getContext().saveFromActivity("login");
			this.finish();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK
	            && event.getRepeatCount() == 0) {
	    	getContext().saveFromActivity("login");
	        this.finish();
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}

	
	public void update(String data) {
		ValidUsersTranslator validUsersTranslator;
		//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		if ((!data.equalsIgnoreCase("0")) && (data.contains(SCHEDULE_HEADER))) {
		    if (action.equalsIgnoreCase("defaultusers")) {
			   validUsersTranslator = new ValidUsersTranslator();
			   if (data.length() > 0) {
				  this.getContext().saveSchedule(data);
				  allUsers = validUsersTranslator.translate(data);
				  locationList = populateLocationList(data);
				  labourTimeList = populateLabourCodes(data);
				  statusList = populateStatusList(data);
				  jobDetailsList = populateJobDetailsList(data);
				  labourGroupList = populateLabourGroupList(data);
				  if (allUsers != null) {
					 for (User user : allUsers.getAllUsers()) {
						adapter.add(user.getFirstname() + " "
								+ user.getSurname());
					  } 
				   }
			   }
			   loginSpinnerOnItemSelected.setAllUsers(allUsers);
			   adapter.sort(new StringComparator());
			   adapter.notifyDataSetChanged();
			   loginSpinnerOnItemSelected.setFirstUser();
			   setKnownUser();
			   pDialog.dismiss();
		    }
		} else {
			action = "fault";
			pDialog.dismiss();
			popupDialog
					.showPopupDialog(
							"Unable to connect to Server","ok");
		  }
		if (action.equalsIgnoreCase("checkpassword")) {
		}
	}

	public int getRequestedOrientation() {
		return 1;
	}
	
	private void getDefaultUsers() {
		urlString = getContext().getServiceUrl();
		String callingUrl = "";
		callingUrl = urlString.concat(SCHEDULED_JOBS+"."+getContext().getServerType());
		action = "defaultusers";
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
		webDownloadTask.execute(callingUrl);
		pDialog = ProgressDialog.show(this, "Downloading Data..",
				"Please wait", true, false);
	}

	private boolean checkForNewJobs(String userName) {
		boolean okay = false;
		NewJobsTranslator newJobsTranslator = new NewJobsTranslator();
		NewJob response;
		userId = loggedInPerson.getId();
		String dataString = this.getData(urlString, NEW_JOBS +"."+getContext().getServerType()+"?userID="
				+ userId);
		if (dataString.length() > 0) {
			response = newJobsTranslator.translate(dataString);
			if (response.getInfo().equalsIgnoreCase(OK_RESPONSE)) {
				okay = true;
			}
		}
		return okay;
	}

	private boolean checkPassword(String userName, String password) {
		boolean okay = false;
		NameValuePair loginUser;
		NameValuePair userPassword;
		ArrayList<NameValuePair> params=new ArrayList<NameValuePair>();
		urlString = getContext().getServiceUrl();
		LoginTranslator loginTranslator = new LoginTranslator();
		String response;
		loggedInPerson = this.getLabourPerson(userName);
		userId = loggedInPerson.getId();
		loginUser = new BasicNameValuePair("userID",userId);
		userPassword = new BasicNameValuePair("password",password);
		params.add(loginUser);
		params.add(userPassword);
		Log.v("quantarc",urlString+USER_LOGIN+"."+getContext().getServerType()+params.toString());
		String dataString = super.getData(urlString+USER_LOGIN+"."+getContext().getServerType(),params);
		if (dataString.length() > 0) {
			response = loginTranslator.translate(dataString);
			if (response.equalsIgnoreCase(OK_RESPONSE)) {
				okay = true;
			}
		}
		return okay;
	}

	private LabourPerson getLabourPerson(String userName) {
		LabourPerson labPerson = null;
		User user = null;
		boolean found = false;
		String storedName = "";
		Iterator<User> iter = allUsers.getAllUsers().iterator();
		while ((!found) && (iter.hasNext())) {
			user = iter.next();
			storedName = user.getFirstname() + " " + user.getSurname();
			if (storedName.equalsIgnoreCase(userName)) {
				found = true;
				if (user.getLabourPersonList().size()>0) {
					labPerson = user.getLabourPersonList().get(0);
				}
			}
		}
		return labPerson;
	}

	private void saveKnownUser(String userName, String password) {
		LabourPerson aUser = loggedInPerson;
		this.getContext().saveUsername(userName);
		this.getContext().saveUserPassword(password);
		this.getContext().saveUserId(aUser.getId());
		Log.v("LoginActivity","UserId:"+aUser.getId());
		this.getContext().saveGroupId(aUser.getGroupId());
		this.getContext().saveSectionId(aUser.getSectionId());
	}

	private void showJobList() {
		Intent intent;
		intent = new Intent(this, JobListActivity.class);
		intent.putExtra("com.webservice.domain.UserList", allUsers);
		intent.putExtra("com.webservice.domain.LocationList", locationList);
		intent.putExtra("com.webservice.domain.StatusList", statusList);
		intent.putExtra("com.webservice.domain.LabourTimeList", labourTimeList);
		intent.putExtra("com.webservice.domain.JobDetailsList", jobDetailsList);
		intent.putExtra("com.webservice.domain.LabourGroupList", labourGroupList);
		startActivity(intent);
	}
	
	private int getUserPosition(String userName) {
		String name = "";
		boolean found = false;
		boolean finish = false;
		int size = adapter.getCount();
		int position = 0;
		int index = 0;
		while ((!found) && (!finish)) {
			name = adapter.getItem(position);
			if (name.equalsIgnoreCase(userName)) {
				found = true;
			} else {
				position = position + 1;
				if (position >= size) {
					finish = true;
				}
			}
		}
		if (found) {
			index = position;
		}
		return index;
	}

	private LocationList populateLocationList(String data) {
		LocationTranslator locationTranslator = new LocationTranslator();
		LocationList locationList = locationTranslator.translate(data);
		return locationList;
	}	

	public LabourTimeList populateLabourCodes(String data) {
		LabourTimeTranslator labourTimeTranslator = new LabourTimeTranslator();
		String xmlInput = getContext().getSchedule();
		return labourTimeTranslator.translate(data);
	}
	
	public StatusList populateStatusList(String data) {
		StatusTranslator statusTranslator = new StatusTranslator();
		return statusTranslator.translate(data);
	}
	
	public JobDetailsList populateJobDetailsList(String data) {
		JobDetailsTranslator jobDetailsTranslator = new JobDetailsTranslator();
		return jobDetailsTranslator.translate(data);
	}
	
	public LabourGroupList populateLabourGroupList(String data) {
		LabourGroupTranslator labourGroupTranslator = new LabourGroupTranslator();
		return labourGroupTranslator.translate(data);
	}
	
	private void setKnownUser() {
		String rememberUser = this.getContext().getRememberUser();
		int position = 0;
		if (rememberUser.equalsIgnoreCase("1")) {
			position = getUserPosition(getContext().getUsername());
			usersSpinner.setSelection(position);
		}
	}

}