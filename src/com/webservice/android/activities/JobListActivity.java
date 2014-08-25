package com.webservice.android.activities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Timer;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.webservice.android.common.QuantarcPopupDialog;
import com.webservice.android.handler.AutoUpdateHandler;
import com.webservice.android.receiver.AutoUpdateReceiver;
import com.webservice.android.task.WebDownloadTask;
import com.webservice.android.task.WebTimesheetUploadTask;
import com.webservice.android.task.WebUploadNewPassword;
import com.webservice.android.task.WebUploadTask;
import com.webservice.android.util.JobComparator;
import com.webservice.android.util.JobHistoryDateComparator;
import com.webservice.android.xml.JobListTranslator;
import com.webservice.android.xml.TimesheetListTranslator;
import com.webservice.domain.Job;
import com.webservice.domain.JobDetailsList;
import com.webservice.domain.JobHistory;
import com.webservice.domain.JobList;
import com.webservice.domain.LabourGroupList;
import com.webservice.domain.LabourTime;
import com.webservice.domain.LabourTimeList;
import com.webservice.domain.LocationList;
import com.webservice.domain.PPMCheck;
import com.webservice.domain.ReferenceDataList;
import com.webservice.domain.Risk;
import com.webservice.domain.StatusList;
import com.webservice.domain.StockIssue;
import com.webservice.domain.Timesheet;
import com.webservice.domain.TimesheetList;
import com.webservice.domain.UserList;

public class JobListActivity extends DialogActivity {
	private static int DIALOG_ID = 101;
	boolean hasRisks;
	boolean allReadyOn;
	boolean autoUpdateOn;
	Context myContext;
	AlarmManager alarmManager;
	AutoUpdateReceiver autoUpdateReceiver;
	AutoUpdateHandler autoUpdateHandler;
	EditText username, password, dob, st, pass1, pass2;
	PopupWindow popupWindow;
	PendingIntent pendingAlarmIntent;
	TextView error;
	TextView user;
	TextView check;
	TextView userTitle;
	TextView checkTitle;
	TableRow tableRow;
	TableRow headerTableRow;
	String response;
	String urlString;
	String dataString;
	String action;
	String lastMaterialCost;
	String lastJobNum;
	String sortField;
	String newpassword;
	String currentpassword;
	String[] page2SettingsList;
	HashMap< String, Object> savedValues;
	Intent intent;
	Intent autoUpdateIntent;
	Intent notificationIntent;
	Intent serviceIntent;
	ImageView imageView;
	TableLayout tableLayout;
	TimesheetListTranslator timesheetTranslator;
	TimesheetList timesheetList;
	Timer timer;
	JobDetailsList jobDetailsList;
	JobList jobList;
	JobList newJobList;
	LabourTimeList labourTimeList;
	LocationList locationList;
	StatusList statusList;
	LabourGroupList labourGroupList;
	UserList allUsers;
	JobListTranslator jobTranslator;
	ProgressDialog pDialog;
	WebDownloadTask webDownloadTask;
	WebUploadTask webUploadTask;
	QuantarcPopupDialog popup;
	ReferenceDataList page2Settings;
	Button saveButton;
	Button settingsButton;
	Button uploadButton;
	Button checkButton;
	Button logoutButton;
	Button passwordButton;
	Button viewButton;
	Button roleButton;
	Button exitButton;
	Button chooseButton;
	Button previous;
	int count;
	int peopleListSize;
	int currentJobCount;
	QuantarcPopupDialog popupDialog;
	WebTimesheetUploadTask webTimesheetUploadTask;
	Job selectedJob; // Anna 2012-07-08
	String jobStatus; // Anna 2012-07-08

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.job_list);
		currentJobCount=0;
		currentpassword=super.getContext().getUserPassword();
		newpassword="";
		myContext = getApplicationContext();
		savedValues = (HashMap<String, Object>)this.getLastNonConfigurationInstance();
		
		Bundle bundle = getIntent().getExtras();
		allUsers = bundle.getParcelable("com.webservice.domain.UserList");
		locationList = bundle
				.getParcelable("com.webservice.domain.LocationList");
		statusList = bundle.getParcelable("com.webservice.domain.StatusList");
		labourTimeList = bundle
				.getParcelable("com.webservice.domain.LabourTimeList");
		jobDetailsList = bundle
				.getParcelable("com.webservice.domain.JobDetailsList");
		labourGroupList = bundle
				.getParcelable("com.webservice.domain.LabourGroupList");
		popup = new QuantarcPopupDialog(this);
		jobTranslator = new JobListTranslator();
		tableLayout = (TableLayout) findViewById(R.id.jl1);
		tableRow = (TableRow) findViewById(R.id.jl10);
		imageView =  (ImageView) findViewById(R.id.jl15);
		userTitle = (TextView) findViewById(R.id.jl80);
		checkTitle = (TextView) findViewById(R.id.jl90);
		settingsButton = (Button) findViewById(R.id.jl110);
		passwordButton = (Button) findViewById(R.id.jl145);
		logoutButton = (Button) findViewById(R.id.jl150);
		userTitle.setTypeface(Typeface.DEFAULT_BOLD);
		checkTitle.setTypeface(Typeface.DEFAULT_BOLD);
		user = (TextView) findViewById(R.id.jl85);
		check = (TextView) findViewById(R.id.jl95);
		urlString = this.getServiceUrl();
		popupDialog = new QuantarcPopupDialog(this);
		settingsButton.setOnClickListener(this);
		passwordButton.setOnClickListener(this);
		logoutButton.setOnClickListener(this);
		page2Settings = new ReferenceDataList();
		autoUpdateOn = false;
		sortField="jobnum";
		if (savedValues==null) {
			lastJobNum = "";
			lastMaterialCost = "0";
			hasRisks=false;
		    getJobs(true,"jobs");
		} else {
			jobList = (JobList)savedValues.get("jobs");
			timesheetList = (TimesheetList)savedValues.get("timesheet");
			hasRisks = (Boolean)savedValues.get("risks");
			restoreJobs(jobList);
		}
	}

	@Override
	public void onClick(View v) {
		TextView textView;
		int id = v.getId();
		boolean upload=false;
		if (settingsButton.getId() == id) {
			showSettingsMenu();
		} else if ((uploadButton != null) && (uploadButton.getId() == id)) {
			     uploadJobs(true,"jobupload");
		   } else if ((checkButton != null) && (checkButton.getId() == id)) {
			 popupWindow.dismiss();
			 upload = checkForUploads();
			 if (upload) {
				 action="updates";
				 popupDialog.showPopupDialog("You have pending histories on the PDA.  Upload them now ?", "yes","cancel");
			 } else {
				     checkForUpdates();
			 }
		} else if ((viewButton != null) && (viewButton.getId() == id)) {
			popupWindow.dismiss();
			intent = new Intent(this, TimeSheetListActivity.class);
			intent.putExtra("com.webservice.domain.LabourTimeList",
					labourTimeList);
			intent.putExtra("com.webservice.domain.TimesheetList",
					timesheetList);
			this.startActivityForResult(intent, 4000);
		//} else if ((roleButton != null) && (roleButton.getId() == id)) {
		//		popupWindow.dismiss();
		//		intent = new Intent(this, UserRolesActivity.class);
		//		intent.putExtra("com.webservice.domain.LabourGroupList",labourGroupList);
		//		this.startActivityForResult(intent, 2000);
		} else if ((exitButton != null) && (exitButton.getId() == id)) {
			popupWindow.dismiss();
		} else if ((passwordButton != null) && (passwordButton.getId() == id)) {
			 changePassword();
		} else if ((logoutButton != null) && (logoutButton.getId() == id)) {
			if (autoUpdateOn) {
				stopService();
			}
			 upload = checkForUploads();
			 if (upload) {
				 action="logout";
				 popupDialog.showPopupDialog("There is data on the device that must be uploaded. Upload now?", "yes","back");
			 } else {
				   if (super.getContext().getAutoCheckRunning().equalsIgnoreCase("1")) {
				     super.getContext().saveAutoCheckRunning("0");
				   }
			       finish();
			   }
		} else if ((id>899) && (id<999)){
			  textView = (TextView)v;
			  sortField=textView.getText().toString();
			  addRows(jobList.getJobList());
		   } else {
			Job job = getJob(id);
			if (job != null) {
				intent = new Intent(this, JobDetailsTabActivity.class);
				if (job.getJobNumLong().equalsIgnoreCase(lastJobNum)) {
					job.setCostOfMaterial(lastMaterialCost);
				}
				intent.putExtra("com.webservice.domain.Job", job);
				intent.putExtra("com.webservice.domain.StatusList", statusList);
				intent.putExtra("com.webservice.domain.UserList", allUsers);
				intent.putExtra("com.webservice.domain.JobDetailsList",
						jobDetailsList);
				intent.putExtra("com.webservice.domain.LabourTimeList",
						labourTimeList);
				intent.putExtra("com.webservice.domain.LocationList",
						locationList);
				intent.putExtra("com.webservice.domain.TimesheetList",
						timesheetList);
				intent.putExtra("com.webservice.domain.JobList", jobList);
				startActivityForResult(intent, 5000);
			}
		}
	}

	public void showSettingsMenu() {
		int POPUP_HEIGHT = 300;
		int POPUP_WIDTH = 200;
		int BUTTON_WIDTH = 200;
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View popupView = inflater.inflate(R.layout.popup_settings, null, false);
		popupView.setBackgroundColor(this.getResources().getColor(android.R.color.darker_gray));
		popupWindow = new PopupWindow(popupView, 100, 100, true);
		popupWindow.setHeight(POPUP_HEIGHT);
		popupWindow.setWidth(POPUP_WIDTH);
		uploadButton = (Button) popupView.findViewById(R.id.Button01);
		uploadButton.setWidth(BUTTON_WIDTH);
		checkButton = (Button) popupView.findViewById(R.id.Button02);
		checkButton.setWidth(BUTTON_WIDTH);
		viewButton = (Button) popupView.findViewById(R.id.Button03);
		viewButton.setWidth(BUTTON_WIDTH);
		//roleButton = (Button) popupView.findViewById(R.id.Button04);
		//roleButton.setWidth(BUTTON_WIDTH);
		exitButton = (Button) popupView.findViewById(R.id.Button05);
		exitButton.setWidth(BUTTON_WIDTH);
		uploadButton.setOnClickListener(this);
		checkButton.setOnClickListener(this);
		viewButton.setOnClickListener(this);
		// roleButton.setOnClickListener(this);
		exitButton.setOnClickListener(this);
		// The code below assumes that the root container has an id called
		// 'main'
		popupWindow.showAtLocation(this.findViewById(R.id.jl5), Gravity.CENTER,
				0, 0);
	}

	private void addRows(List<Job> jobList) {
		TableRow tableRow;
		String[] fields;
		String[] headers;
		String indicator="";
		// int TEXT_HEIGHT = 30;
		int TEXT_HEIGHT = 18; // Anna
		ImageView imageView;
		TextView textView;
		int index = 0;
		tableLayout.removeAllViewsInLayout();
		tableLayout.removeAllViews();
		headerTableRow = new TableRow(this);
		headers = getHeaderList();
		for (int i = 0; i < headers.length; i++) {
			/* Create a TextView to be the row-content. */
			textView = new TextView(this);
			textView.setId(990+i);
			textView.setTextSize(TEXT_HEIGHT);
			textView.setText(headers[i]);
			textView.setOnClickListener(this);
			textView.setBackgroundColor(Color.parseColor("#6699FF"));
			// textView.setLayoutParams(new
			// LayoutParams(LayoutParams.FILL_PARENT,
			// LayoutParams.WRAP_CONTENT));
			// Anna 2012-07-03
			if (i == 0) {
				textView.setLayoutParams(new LayoutParams(40,
						LayoutParams.WRAP_CONTENT));
			} else if (i == 1) {
				textView.setLayoutParams(new LayoutParams(70,
						LayoutParams.WRAP_CONTENT));
			} else {
				textView.setLayoutParams(new LayoutParams(110,
						LayoutParams.WRAP_CONTENT));
			}
			// Anna end
			/* Add Button to row. */
			headerTableRow.addView(textView);
		}
		/* Add row to TableLayout. */
		tableLayout.addView(headerTableRow, new TableLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		if (jobList.size() > 0) {
			Collections.sort(jobList, new JobComparator(sortField, this));
			for (Job job : jobList) {
				index = index + 1;
				// Log.v("Quantarc",job.getJobNumLong()+":"+job.getLabourPerson()+":"+job.getLocRef()+":"+job.getJobNum());
				tableRow = new TableRow(this);
				tableRow.setId(Integer.valueOf(job.getJobNum()));
				tableRow.setOnClickListener(this);
				tableRow.setLayoutParams(new LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
				fields = getFieldList(job);
				for (int i = 0; i < fields.length; i++) {
						indicator=fields[i];
						if ((indicator==null) && (i==0)) {
							indicator="norisks";
						} else if (indicator==null) {
							indicator=" ";
						}
						if (indicator.equalsIgnoreCase("yesrisks")) {
							  imageView = new ImageView(this);
							  imageView.setImageDrawable(this.getResources().getDrawable(R.drawable.hazard_icon));
							  tableRow.addView(imageView);
						   } else if (indicator.equalsIgnoreCase("norisks")) {
							         imageView = new ImageView(this);
							         imageView.setImageDrawable(this.getResources().getDrawable(R.drawable.nohazard_icon));
						 	         tableRow.addView(imageView);
					               } else {
					                        /* Create a TextView to be the row-content. */
					                       textView = new TextView(this);
					                       textView.setTextSize(TEXT_HEIGHT);
					                       textView.setText(indicator);
					                       textView.setLayoutParams(new LayoutParams(
							               LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
					                       /* Add Button to row. */
					                       tableRow.addView(textView);
					                   }
				}
				/* Add row to TableLayout. */
				tableLayout.addView(tableRow, new TableLayout.LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			}
		}
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		boolean uploaded;
		if (action.equalsIgnoreCase("logout")) {
			switch (which) {
			case DialogInterface.BUTTON_POSITIVE: { // Yes button clicked
				uploaded=uploadJobs(true,"jobupload");
				pDialog.dismiss();
				finish();
			};
			case DialogInterface.BUTTON_NEGATIVE: // No button clicked
				break;
			}
		} else if (action.equalsIgnoreCase("newpassword")) {
			switch (which) {
			case DialogInterface.BUTTON_POSITIVE: { // Yes button clicked
				action="alterpassword";
				checkPassword();
			};
			case DialogInterface.BUTTON_NEGATIVE: // No button clicked
				break;
			}
		} else if (action.equalsIgnoreCase("updates")) {
			switch (which) {
			case DialogInterface.BUTTON_POSITIVE: { // Yes button clicked
				uploaded=uploadJobs(true,"jobupload");
			};
			case DialogInterface.BUTTON_NEGATIVE: // No button clicked
				break;
			}
		} else {
		   switch (which) {
		     case DialogInterface.BUTTON_POSITIVE: // Yes button clicked
			    break;
		     case DialogInterface.BUTTON_NEGATIVE: // No button clicked
			    break;
		}
	  }
	}
	
	public void checkForUpdates() {
		getJobs(true,"jobs");
	}

	
	public void update(String data) {
		//Log.v("JobListActivity","Returned from Call");
		ActivityManager am=null;
		int newjobCount=0;
		Log.v("Password","Action:"+action+":");
		String userId = getContext().getUserId();
		String password = currentpassword;
		String timesheetUploadUrl = getContext().getServiceUrl().concat(
				super.UPLOAD_HISTORY)
				+ "." + getContext().getServerType();
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
		// Log.v("JobListActivity:Action",action);
		if (data.equalsIgnoreCase("0")) {
			pDialog.dismiss();
			popupDialog.showPopupDialog("Unable to connect to Server", "ok");
		} else if (action.equalsIgnoreCase("jobupload")) {
			if (addedTimesheetExists(timesheetList)) {
				action = "timesheetupload";
				webTimesheetUploadTask = new WebTimesheetUploadTask();
				webTimesheetUploadTask.setUser(userId, password);
				webTimesheetUploadTask.setTimeSheetList(timesheetList);
				webTimesheetUploadTask.setBaseActivity(this);
				webTimesheetUploadTask.execute(timesheetUploadUrl);
			} else {
				getJobs(false,"jobs");
			   }
			} else if (action.equalsIgnoreCase("autoupdate")) {
				     action="updatejobs";
				     getJobs(false,"jobs");
		    } else if (action.equalsIgnoreCase("timesheetupload")) {
			        if ((data.contains("<RESPONSE><INFO>OK"))
					    || (data.contains("<RESPONSE><INFO>Error inserting OPS_B_Transactions"))) {
				        for (Timesheet timesheet : timesheetList.getAllTimesheets()) {
					          if (timesheet.getIsNew().equalsIgnoreCase("true")) {
						        timesheet.setIsNew("false");
					    }
				    }
			  }
			getJobs(false,"jobs");
		} else if (action.equalsIgnoreCase("jobs")) {
			// Log.v("JobListActivity","Populating Jobs");
			populateJobs(data);
			getContext().setNewHistories("false");
			getTimesheets();
			pDialog.dismiss();
		} else if (action.equalsIgnoreCase("timesheets")) {
			populateTimesheets(data);
			populateJobHistories();
			pDialog.dismiss();
		}  else if (action.equalsIgnoreCase("alterpassword")) {
			if (data.contains("<RESPONSE><INFO>OK")) {
				currentpassword = newpassword;
				popupDialog.showPopupDialog("Your password has changed", "ok");
			} else {
				popupDialog.showPopupDialog("Unable to change password", "ok");
			}
			// Log.v("Password",data);
			action="";
		}  else if (action.equalsIgnoreCase("updatejobs")) {
			// Log.v("JobListActivity","Updating Jobs");
			 action="";
			 populateJobs(data);
			 newjobCount = jobList.getJobList().size();
		   //  if (newjobCount>currentJobCount) {
		    //	 Intent intent = new Intent(AUTOUPDATE_NOTIFICATION);
		    //	 sendBroadcast(intent);
		    // }
		} else if (action.equalsIgnoreCase("newjobs")) {
			action="";
			
		}
	}

	public static void sendNotification(Context caller, Class<?> activityToLaunch, String title, String msg, int numberOfEvents,boolean sound, boolean flashLed, boolean vibrate, int iconID) {
	    NotificationManager notifier = (NotificationManager) caller.getSystemService(Context.NOTIFICATION_SERVICE);
	    final Notification notify = new Notification(iconID, "", System.currentTimeMillis());
        int number=0;
	    notify.icon=R.drawable.quantarc_icon;
	    notify.tickerText = title;
	    notify.when = System.currentTimeMillis();
	    notify.number = numberOfEvents;
	    notify.flags |= Notification.FLAG_AUTO_CANCEL;
	    if (sound) notify.defaults |= Notification.DEFAULT_SOUND;

	    if (flashLed) {
	    // add lights
	        notify.flags |= Notification.FLAG_SHOW_LIGHTS;
	        notify.ledARGB = Color.CYAN;
	        notify.ledOnMS = 500;
	        notify.ledOffMS = 500;
	    }

	    if (vibrate) {
	        notify.vibrate = new long[] {100, 200, 300};
	    }

	    Intent toLaunch = new Intent(caller, activityToLaunch);
	    toLaunch.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
	    toLaunch.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    PendingIntent intentBack = PendingIntent.getActivity(caller, 0, toLaunch, 0);
        notify.contentIntent=intentBack;
	    notify.setLatestEventInfo(caller, title, msg, intentBack);   
	    notifier.notify(number, notify);
	    number = number+1;
	}
	
	private boolean addedTimesheetExists(TimesheetList timesheetList) {
		boolean found = false;
		Timesheet timesheet;
		Iterator<Timesheet> iter;
		if ((timesheetList!= null) && (timesheetList.getAllTimesheets().size()>0)) {
			iter = timesheetList.getAllTimesheets().iterator();
		    while ((iter.hasNext() && (!found))) {
		 	   timesheet = iter.next();
			   if (timesheet.getIsNew().equalsIgnoreCase("true")) {
				   found = true;
			   }
		    }
		}
		return found;
	}

	private void changePassword() {
		LayoutInflater inflater = getLayoutInflater();
		View dialoglayout = inflater.inflate(R.layout.change_password, (ViewGroup) getCurrentFocus());
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("New Password");
		builder.setView(dialoglayout);
		pass1 = (EditText) dialoglayout.findViewById(R.id.cp20);
		pass2 = (EditText) dialoglayout.findViewById(R.id.cp40);
		action="newpassword";
        builder.setPositiveButton("Ok", this);
        builder.show();
	}
	
	private void checkPassword() {
		WebUploadNewPassword upload;
		String urlString = getContext().getServiceUrl();
		String callingUrl=urlString + USER_NEWPASSWORD + "." + getContext().getServerType();
		String password1 = pass1.getText().toString().trim();
		String password2 = pass2.getText().toString().trim();
        if ((!password1.equals(password2) || password1.length()==0 || password2.length()==0) || 
            (password1.contains(" ") || password2.contains(" "))) {
        	 popupDialog.showPopupDialog("Invalid password, entered values must be the same and they must not contain spaces. Your current password will not be changed", "ok");
        } else {
        	upload = new WebUploadNewPassword();
        	upload.setBaseActivity(this);
        	upload.setNewPassword(password1);
        	newpassword=password1;
        	upload.setUser(super.getContext().getUserId(), currentpassword);
        	upload.execute(callingUrl);
        }
	}
	
	private void getTimesheets() {
		urlString = getContext().getServiceUrl();
		String userId = this.getContext().getUserId();
		String uid = userId;
		String callingUrl = "";
		if (urlString.length() > 0) {
			callingUrl = urlString + USER_TIMESHEET + "."
					+ getContext().getServerType() + "?uid=" + uid;
			action = "timesheets";
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
			webDownloadTask = new WebDownloadTask();
			webDownloadTask.setBaseActivity(this);
			webDownloadTask.execute(callingUrl);
		}
	}

	private void populateTimesheets(String data) {
		timesheetTranslator = new TimesheetListTranslator();
		timesheetTranslator.setDateFormat(STANDARD_DATE_FORMAT);
		if (data.length() > 0) {
			timesheetList = timesheetTranslator.translate(data);
		} else {
			Log.v("Quantarc", "Data Error");
		}
	}

	private String[] getFieldList(Job job) {
		int index = 0;
		int count = 0;
		int position=0;
		int show;
		String entry="";
		String status="";
		String visible="";
		String[] valueList = new String[8];
		String[] orderedList = null;
		Iterator<String> iter = page2Settings.getReferenceMap().keySet().iterator();
		while (iter.hasNext()) {
            entry = iter.next();
            status = page2Settings.getStatus(entry);
            index = status.indexOf(":");
            if (index>=0)  {
               visible = status.substring(0,index);
               position = Integer.parseInt(status.substring(index+1,status.length()));
			   if (visible.equalsIgnoreCase("yes")) {
                 count = count + 1;
				 valueList[position - 1] = getField(job, entry);
			   } 
            }
				
		}
		if (count > 0) {
			orderedList = new String[count];
			for (int j = 0; j < count; j++) {
				orderedList[j] = valueList[j];
			}
		}
		//for (int j = 0; j < count; j++) {
		//	Log.v("Quantarc","OrderedListValue:"+orderedList[j]);
		// }
		return orderedList;
	}

	private String[] getHeaderList() {
		int index = 0;
		int count = 0;
		getContext().populatePage2(page2Settings);
		String order = getContext().getSettingsOrder();
		int show;
		int position;
		String[] valueList = new String[8];
		String[] orderedList = null;
		String entry="";
		String status;
        String visible;
		Iterator<String> iter = page2Settings.getReferenceMap().keySet().iterator();
		while (iter.hasNext()) {
            entry = iter.next();
            status = page2Settings.getStatus(entry);
            index = status.indexOf(":");
            //Log.v("Quantarc","Job List, Entry:"+entry+":"+status+":"+index);
            if (index>=0) {
              visible = status.substring(0,index);
              position = Integer.parseInt(status.substring(index+1,status.length()));
			  if (visible.equalsIgnoreCase("yes")) {
                count = count + 1;
				valueList[position - 1] = getHeader(entry);
			  } 
            }
				
		}
		if (count > 0) {
			orderedList = new String[count];
			for (int j = 0; j < count; j++) {
				orderedList[j] = valueList[j];
			}
		}
		return orderedList;
	}

	private int getValue(String list, int position) {
		int location = (position - 1) * 2;
		String number = list.substring(location, location + 1);
		Integer value = Integer.valueOf(number);
		return value;
	}

	private String getStatus(String id) {
		String status = statusList.getStatus(id);
		return status;
	}

	private boolean checkForUploads() {
		boolean uploadNeeded=false;
		List<JobHistory> jobHistories;
   	    for (Job job : jobList.getJobList()) {
		  jobHistories = job.getJobHistoryList();
		  for (JobHistory jobHistory : jobHistories) {
			if ((jobHistory.getReadOnly().equalsIgnoreCase("false"))
					&& (jobHistory.getToBeRemoved().equalsIgnoreCase("0"))) {
				uploadNeeded = true;
			}
		  }
   	    }
   	    return uploadNeeded;
	}
	
	public boolean uploadJobs(boolean showDialog, String actionName) {
		List<JobHistory> uploadedJobHistories = new ArrayList<JobHistory>();
		urlString = getContext().getServiceUrl();
		List<JobHistory> jobHistories;
		//Log.v("JobListActivity", "Upload Jobs Start");
		String uploadUrl = urlString + UPLOAD_HISTORY + "."
				+ getContext().getServerType();
		boolean uploadedNeeded = false;
		for (Job job : jobList.getJobList()) {
			jobHistories = job.getJobHistoryList();
			for (JobHistory jobHistory : jobHistories) {
				if ((jobHistory.getReadOnly().equalsIgnoreCase("false"))
						&& (jobHistory.getToBeRemoved().equalsIgnoreCase("0"))) {
					uploadedNeeded = true;
					// Anna 2012-07-10
					String jobStatus = jobHistory.getJobStatus();
					Integer selectedStatusId = getJobStatusIndex(jobStatus, statusList);
					job.setJobStatus(String.valueOf(selectedStatusId));
					jobHistory.setJobStatus(getStatus(job.getJobStatus())); // Anna 2012-07-08
					// Anna end
					uploadedJobHistories.add(jobHistory);
				}
			}
		}
		//Log.v("JobListActivity", "Upload Jobs");
		if (uploadedNeeded) {
			//Log.v("JobListActivity", "Upload Jobs Needed");
			// action = "jobupload";
			action=actionName;
			Collections.sort(uploadedJobHistories, new JobHistoryDateComparator());
			webUploadTask = new WebUploadTask();
			webUploadTask.setBaseActivity(this);
			webUploadTask.setStatusList(statusList);
			webUploadTask.setUser(getContext().getUserId(), getContext()
					.getUserPassword());
			webUploadTask.setJobHistories(uploadedJobHistories);
			webUploadTask.execute(uploadUrl);
			if (showDialog) {
			    pDialog = ProgressDialog.show(this, "Uploading Data..", "Please wait", true, false);
			}
		} else if (addedTimesheetExists(timesheetList)) {
			action = "timesheetupload";
			webTimesheetUploadTask = new WebTimesheetUploadTask();
			webTimesheetUploadTask.setUser(getContext().getUserId(),
					getContext().getUserPassword());
			webTimesheetUploadTask.setTimeSheetList(timesheetList);
			webTimesheetUploadTask.setBaseActivity(this);
			webTimesheetUploadTask.execute(uploadUrl);
			if (showDialog) {
			     pDialog = ProgressDialog.show(this, "Uploading Data..", "Please wait", true, false);
			}
		} else {
			if (showDialog)  {
			    popupDialog.showPopupDialog("Nothing to upload", "ok");
			}
		}
		return uploadedNeeded;
	}

	public void getJobs(boolean displayDialog, String actionName) {
		urlString = getContext().getServiceUrl();
		String userId = this.getContext().getUserId();
		String password = this.getContext().getUserPassword();
		String uid = userId;
		String gid = this.getContext().getGroupId();
		String sid = this.getContext().getSectionId();
		String displayJobs = this.getContext().getDisplayJobs();
		String callingUrl = "";
		action = actionName;
		webDownloadTask = new WebDownloadTask();
		webDownloadTask.setBaseActivity(this);
		callingUrl = urlString + JOBS_LIST + "." + getContext().getServerType()
				+ "?userID=" + userId + "&pass=" + password + "&uid=" + uid
				+ "&gid=" + gid + "&sid=" + sid;
		if (displayJobs.equalsIgnoreCase("1")) {
			callingUrl = callingUrl + "&includeJobsIssuedStock=true";
		}
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
		webDownloadTask.execute(callingUrl);
		if (displayDialog) {
			pDialog = ProgressDialog.show(this, "Downloading Data..",
					"Please wait", true, false);
		}
	}
	
	public void restoreJobs(JobList jobList) {
		if ((jobList!=null) && (jobList.getJobList().size() > 0)) {
			this.addRows(jobList.getJobList());
		}
		user.setText(getContext().getUsername());
		check.setText(super.getCurrentDateTime());
	}
	
	public void addNewJobs(JobList downloadedJobList) {
		Job tempJob;
		boolean found = false;
		boolean jobsAdded = false;
		int counter=0;
		Log.v("JobListActivity","DownloadedJobList:"+downloadedJobList.getJobList().size());
		if ((downloadedJobList != null) && (downloadedJobList.getJobList().size()>0)) {
			if (downloadedJobList.getJobList().size()>0) {
            	for(Job job:downloadedJobList.getJobList()) {
            		found = false;
            		counter=0;
            		while ((!found) && (counter<jobList.getJobList().size())) {
            			tempJob = jobList.getJobList().get(counter);
            			if (tempJob.getJobNum().equalsIgnoreCase(job.getJobNum())) {
            				found = true;
            			}
            			counter++;
            		}
            		if (!found) {
            		    jobList.getJobList().add(job);
            		    jobsAdded = true;
            		}
            	}
			  this.addRows(jobList.getJobList());
			  if (jobsAdded) {
				  sendNotification(this);
			  }
			}
		}
		user.setText(getContext().getUsername());
		check.setText(super.getCurrentDateTime());
	}
	
	private void sendNotification(Context context) {
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,null, 0);
		NotificationManager notificationMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(android.R.drawable.star_on, "Refresh",System.currentTimeMillis());
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.defaults |= Notification.DEFAULT_SOUND;
		notification.defaults |= Notification.DEFAULT_VIBRATE;
		notification.setLatestEventInfo(context, "Quantarc", "New Jobs Have Been Downloaded",contentIntent);
		notificationMgr.notify(0, notification);
	}
	
	 private void compareJobLists(JobList downloadedJobList) {
		   boolean same=false;
		   Job newJob=null;
		   JobHistory jobHistory=null;
		   Risk aRisk= null;
		   PPMCheck aCheck=null;
		   StockIssue anIssue=null;
		   
		   if (jobList != null ) {
		     for (Job job : downloadedJobList.getJobList()) {
			   same=false;
			   for (Job ajob : jobList.getJobList()) {
				   if (job.getJobNumLong().equalsIgnoreCase(ajob.getJobNumLong())) {
					   same = true;
				   }
			   }
			   if (!same) {
				   newJob = new Job();
				   newJob.setJobNumLong(job.getJobNumLong());
				   newJob.setJobNum(job.getJobNum());
				   newJob.setJobDetails(job.getJobDetails());
				   newJob.setCostOfMaterial(job.getCostOfMaterial());
				   newJob.setJobStatus(job.getJobStatus());
				   newJob.setLabourPerson(job.getLabourPerson());
				   newJob.setJobType(job.getJobType());
				   newJob.setBuilding(job.getBuilding());
				   newJob.setLocDesc(job.getLocDesc());
				   newJob.setLocRef(job.getLocRef());
				   newJob.setOtherDetails(job.getOtherDetails());
				   newJob.setPriorityCode(job.getPriorityCode());
				   newJob.setRequestor(job.getRequestor());
				   newJob.setSite(job.getSite());
				   for (StockIssue issue: job.getStockIssues()) {
					   anIssue = new StockIssue();
					   anIssue.setItemCount(issue.getItemCount());
					   anIssue.setPartDescription(issue.getPartDescription());
					   anIssue.setPartNumber(issue.getPartNumber());
					   newJob.getStockIssues().add(anIssue);
				   }
				   for ( Risk risk:job.getRisks()) {
					   aRisk = new Risk();
					   aRisk.setDescription(risk.getDescription());
					   aRisk.setFurtherDetails(risk.getFurtherDetails());
					   aRisk.setHelpDeskAction(risk.getHelpDeskAction());
					   aRisk.setRiskAction(risk.getRiskAction());
					   aRisk.setRiskType(risk.getRiskType());
					   newJob.getRisks().add(aRisk);
				   }
				   for ( PPMCheck check:job.getPPMChecks()) {
					   aCheck = new PPMCheck();
					   aCheck.setTaskId(check.getTaskId());
					   aCheck.setTask(check.getTask());
					   newJob.getPPMChecks().add(aCheck);
				   }
				   for (JobHistory jobHist:job.getJobHistoryList()) {
					   jobHistory = new JobHistory();
					   jobHistory.setCostOfMaterial(jobHist.getCostOfMaterial());
					   jobHistory.setCurrentDate(jobHist.getCurrentDate());
					   jobHistory.setFinishTime(jobHist.getFinishTime());
					   jobHistory.setHistoryDate(jobHist.getHistoryDate());
					   jobHistory.setStartTime(jobHist.getStartTime());
					   jobHistory.setDetails(jobHist.getDetails());
					   jobHistory.setHoursWorked(jobHist.getHoursWorked());
					   jobHistory.setLabourPersonId(jobHist.getLabourPersonId());
					   jobHistory.setId(jobHist.getId());
					   jobHistory.setJobNum(jobHist.getJobNum());
					   jobHistory.setJobStatus(jobHist.getJobStatus());
					   newJob.addJobHistory(jobHistory);
				   }
				   jobList.getJobList().add(newJob);
				   Context context = getApplicationContext();
			    	 ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE); 
			    	 // get the info from the currently running task
			    	 Toast.makeText(context, "New Job(s) Have Been Downloaded", Toast.LENGTH_SHORT).show();
				   Log.v("JobListActivity","Added Extra Job");
			   }
		     }
		   }
	   }
	
	
	

	public void populateJobs(String data) {
		jobTranslator = new JobListTranslator();
		if (data.length() > 0) {
			jobList = jobTranslator.translate(data);
			for (Job job:jobList.getJobList()) {
				if (job.getRisks().size()>0) {
					hasRisks=true;
				}
			}
			this.addRows(jobList.getJobList());
			switchOnUpdate();
		}
		user.setText(getContext().getUsername());
		check.setText(super.getCurrentDateTime());
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Job job;
		
		if (resultCode == 9500) {
			timesheetList = data
					.getParcelableExtra("com.webservice.domain.TimesheetList");
		}
		if (resultCode == 2060) {
			job = data.getParcelableExtra("com.webservice.domain.Job");
			removeJob(job);
			addRows(jobList.getJobList());
		} else if (resultCode == 1580) {
			job = data.getParcelableExtra("com.webservice.domain.Job");
			timesheetList = data
					.getParcelableExtra("com.webservice.domain.TimesheetList");
			removeJob(job);
			addJob(job);
			addRows(jobList.getJobList());
		} else if (resultCode == 7000) {
			job = data.getParcelableExtra("com.webservice.domain.Job");
			timesheetList = data
					.getParcelableExtra("com.webservice.domain.TimesheetList");
			lastMaterialCost = job.getCostOfMaterial();
			lastJobNum = job.getJobNumLong();
			removeJob(job);
			addJob(job);
			addRows(jobList.getJobList());
		}
	}

	private void addJob(Job job) {
		jobList.getJobList().add(job);
	}

	private String getTimeCode(JobHistory jobHistory, Job selectedJob) {
		String jobNum = selectedJob.getJobNum();
		String startTime;
		String endTime;
		String timeCode = "";
		String actionDate = "";
		boolean found = false;
		Timesheet timesheet;
		Iterator<Timesheet> iter = timesheetList.getAllTimesheets().iterator();
		while ((!found) && (iter.hasNext())) {
			timesheet = iter.next();
			if (timesheet.getJobNum().equalsIgnoreCase(jobNum)) {
				actionDate = getFormattedDate(timesheet.getActionDate());
				startTime = actionDate + " " + timesheet.getStartTime() + ":00";
				endTime = actionDate + " " + timesheet.getFinishTime() + ":00";
				if ((jobHistory.getStartTime().equalsIgnoreCase(startTime))
						&& (jobHistory.getFinishTime()
								.equalsIgnoreCase(endTime))) {
					found = true;
					timeCode = timesheet.getTimeCode();
				}
			}
		}
		return timeCode;
	}

	private String getFormattedDate(String actionDate) {
		String formattedDate = "";
		String dayStr = "";
		String monthStr = "";
		String yearStr = "";
		StringTokenizer tokenizer = new StringTokenizer(actionDate, "/");
		if (tokenizer.countTokens() > 2) {
			dayStr = tokenizer.nextToken();
			monthStr = tokenizer.nextToken();
			yearStr = tokenizer.nextToken();
			if (dayStr.length() == 1) {
				dayStr = "0" + dayStr;
			}
			if (monthStr.length() == 1) {
				monthStr = "0" + monthStr;
			}
			formattedDate = dayStr + "/" + monthStr + "/" + yearStr;
		}
		return formattedDate;
	}

	private String getTimeSpent(String timeCode, LabourTimeList labourTimeList) {
		LabourTime labourTime = null;
		boolean found = false;
		String timeSpent = "";
		Iterator<LabourTime> iter = labourTimeList.getLabourTimeList()
				.iterator();
		while ((!found) && (iter.hasNext())) {
			labourTime = iter.next();
			if (labourTime.getCode().equalsIgnoreCase(timeCode)) {
				found = true;
				timeSpent = labourTime.getSpent();
			}
		}
		return timeSpent;
	}

	private void populateJobHistories() {
		String timeCode = "";
		String timeSpent = "";
		for (Job job : jobList.getJobList()) {
			for (JobHistory jobHistory : job.getJobHistoryList()) {
				jobHistory.setJobNum(job.getJobNum());
				timeCode = "";
				timeSpent = "";
				timeCode = getTimeCode(jobHistory, job);
				if (timeCode.length() > 0) {
					timeSpent = getTimeSpent(timeCode, labourTimeList);
				}
				jobHistory.setTimeCode(timeCode);
				jobHistory.setTimeSpent(timeSpent);
				jobHistory.setJobStatus(getStatus(job.getJobStatus()));
			}
		}
	}

	private void removeJob(Job job) {
		String jobNum = job.getJobNum();
		Job requiredJob = null;
		Job storedJob = null;
		boolean found = false;
		Iterator<Job> iter = jobList.getJobList().iterator();
		while ((iter.hasNext()) && (!found)) {
			storedJob = iter.next();
			if (storedJob.getJobNum().equalsIgnoreCase(jobNum)) {
				found = true;
				requiredJob = storedJob;
			}
		}
		if (found) {
			jobList.getJobList().remove(requiredJob);
		}
	}

	public String getField(Job job, String name) {
		String field = "";
		int selectedJobHistoryId=selectedJobHistoryId=hasNewHistories(job);	// Anna 2012-07-17

		int completeStatus; // Anna 2012-07-08
		int i = getIndex(name);
		switch (i) {
		case 1:
			if (job.getRisks().size()>0)
			    field = "yesrisks";
			else
				field="norisks";
			break;
		case 2: {
			field = job.getJobNumLong();
			if (statusList.getQsetting()!=null) {
			    completeStatus = Integer.valueOf(statusList.getQsetting());
			// Log.v("Quantarc","Complete Status:"+completeStatus);
			    if (job.getJobStatus().equalsIgnoreCase(
							String.valueOf(completeStatus))) {
				   field = "X" + field;
				// Anna 2012-07-08
				   if (completeStatus > 0) {
					  jobStatus = String.valueOf(completeStatus);
					  job.setJobStatus(jobStatus);
				   }
				// Anna end
			   }
			} else {
				if (selectedJobHistoryId > 0 ) { // Anna 2012-07-11
					field = "+" + field;
				}
			}
		}
			;
			break;
		case 3:
			field = job.getSource();
			break;
		case 4:
			if (selectedJobHistoryId > 0 ) {									// Anna 2012-07-17
				JobHistory jobHistory = getJobHistory(job, selectedJobHistoryId);	// display the updated - not uploaded - job status
				field = jobHistory.getJobStatus();
			}
			else {
				field = getStatus(job.getJobStatus());							// display the stored job status
			}
			break;
		case 5:
			field = job.getTargetDate();
			break;
		case 6:
			field = job.getLocRef() + ":" + job.getLocDesc(); // Anna 2012-07-07
																// transfered
																// the locRef
																// here
			break;
		case 7:
			field = job.getPriorityCode();
			break;
		case 8:
			field = job.getBuilding(); // Anna 2012-07-07 replaced
										// job.getLocDesc();
			break;
		}
		// Log.v("Quantarc","Name:"+name+":"+field);
		return field;
	}

	private String getHeader(String entry) {
		String field = "";
		if (entry.equalsIgnoreCase("risk")) {
			field="Risk";
		}
		else if (entry.equalsIgnoreCase("jobnum")) { 
			    field = "Job#";
			    }
		else if (entry.equalsIgnoreCase("source")) { 
		    field = "Source";
		    }
		else if (entry.equalsIgnoreCase("status")) { 
		    field = "Status";
		    }
		else if (entry.equalsIgnoreCase("target")) { 
		    field = "Target Date";
		    }
		else if (entry.equalsIgnoreCase("location")) { 
		    field = "Location";
		    }
		else if (entry.equalsIgnoreCase("priority")) { 
		    field = "Priority Code";
		    }
		else if (entry.equalsIgnoreCase("building")) { 
		    field = "Building";
		    }
		return field;
	}
	
	private int getIndex(String entry) {
		int field =0;
		if (entry.equalsIgnoreCase("risk")) {
			field=1;
		}
		else if (entry.equalsIgnoreCase("jobnum")) { 
			    field = 2;
			    }
		else if (entry.equalsIgnoreCase("source")) { 
		    field = 3;
		    }
		else if (entry.equalsIgnoreCase("status")) { 
		    field = 4;
		    }
		else if (entry.equalsIgnoreCase("target")) { 
		    field = 5;
		    }
		else if (entry.equalsIgnoreCase("location")) { 
		    field = 6;
		    }
		else if (entry.equalsIgnoreCase("priority")) { 
		    field = 7;
		    }
		else if (entry.equalsIgnoreCase("building")) { 
		    field = 8;
		    }
		return field;
	}

	@Override
	protected void onPause() {
	 // if (getContext().getAutoCheck().equalsIgnoreCase("1")) {
     //     unregisterReceiver(autoUpdateReceiver); 
	//	}
      super.onPause(); 
	}

   @Override 
   protected void onResume() { 
	// if (getContext().getAutoCheck().equalsIgnoreCase("1")) {
    //  autoUpdateReceiver = new AutoUpdateReceiver(this); 
    //  registerReceiver(this.autoUpdateReceiver, new IntentFilter(AUTOUPDATE_NOTIFICATION)); 
	// }
    super.onResume(); 
 } 
	
	@Override
    public Object onRetainNonConfigurationInstance() {
        HashMap<String, Object> savedValues = new HashMap<String, Object>();
        savedValues.put("jobs", jobList);
    	savedValues.put("timesheet",timesheetList);
        savedValues.put("risks", hasRisks);
        return savedValues;
    }
	
	private Job getJob(int jobNum) {
		int number;
		Job requiredJob = null;
		for (Job job : jobList.getJobList()) {
			number = Integer.valueOf(job.getJobNum());
			if (number == jobNum) {
				requiredJob = job;
			}
		}
		return requiredJob;
	}

	// Anna 2012-07-11 new sub
	private int getJobStatusIndex(String status, StatusList statusList) {
		String value = "";
		String statusId = "";
		int selection = 0;
		boolean found = false;
		Iterator<String> statuskeys = statusList.getStatusMap().keySet()
				.iterator();
		while ((statuskeys.hasNext()) && (!found)) {
			statusId = statuskeys.next();
			value = statusList.getStatus(statusId);
			if (value.equalsIgnoreCase(status)) {
				found = true;
				break;
			}
		}
		if (found) {
			selection = Integer.parseInt(statusId);
		}
		return selection;
	}

	// Anna 2012-07-11 new sub
	private int hasNewHistories(Job job) {
		int newHistory = -1;
		JobHistory jobHistory;
		Iterator<JobHistory> iter = job.getJobHistoryList().iterator();
		while ((newHistory < 0 ) && (iter.hasNext())) {
			jobHistory = iter.next();
			if (jobHistory.getReadOnly().equalsIgnoreCase("false")) {
				if (jobHistory.getToBeRemoved().equalsIgnoreCase("0") ) {			// Anna 2012-07-17 check for deleted history
					newHistory = jobHistory.getId();
				}
				break;
			}
		}
		return newHistory;
	}
	// Anna 2012-07-17 new sub
	private JobHistory getJobHistory(Job job, int id) {
		JobHistory requiredJobHistory = null;
		JobHistory history = null;
		boolean found = false;
		Iterator<JobHistory> iter = job.getJobHistoryList().iterator();
		while ((!found) && (iter.hasNext())) {
			history = iter.next();
			if (history.getId() == id) {
				found = true;
				requiredJobHistory = history;
			}
		}
		return requiredJobHistory;
	}
	
	private void switchOnUpdate() {
		String interval = super.getContext().getUpdateInterval();
		Integer intInterval;
		String autoCheck = super.getContext().getAutoCheck();
		timer=null;
		
		if (autoCheck.equalsIgnoreCase("1")) {
			try {
			      intInterval = Integer.parseInt(interval);
			} catch (NumberFormatException exp) {
				intInterval=0;
			}
			if (!autoUpdateOn && ((intInterval>0) && (intInterval<61))) {
				autoUpdateOn = true;
				autoUpdateHandler = new AutoUpdateHandler();
				autoUpdateHandler.setListActivity(this);
				final Messenger messenger = new Messenger(autoUpdateHandler);
				Intent downloader = new Intent(myContext, AutoUpdateReceiver.class);
				downloader.putExtra("userid",getContext().getUserId());
				downloader.putExtra("com.webservice.domain.JobList",jobList);
				downloader.putExtra("messenger", messenger);
				downloader.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				PendingIntent pendingIntent = PendingIntent.getBroadcast(myContext, 0, downloader, PendingIntent.FLAG_CANCEL_CURRENT);
				alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
				alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, intInterval*60*1000, intInterval*60*1000, pendingIntent);
				Log.v("JobListActivity", "Set alarmManager.setRepeating to: " +intInterval*60*1000);
			}
		}
	}
	
	 private void stopService() { 
	        Log.v("JobListActivity", "Stopping service...");
	        autoUpdateOn = false;
	        alarmManager.cancel(pendingAlarmIntent);
	        alarmManager=null;
	    } 
	
	 @Override
	  protected void onDestroy() {
	    super.onDestroy();
	  }

}