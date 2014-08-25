package com.webservice.android.activities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import com.webservice.android.common.QuantarcPopupDialog;
import com.webservice.android.listeners.LoginListener;
import com.webservice.android.util.HistoryDateComparator;
import com.webservice.domain.Job;
import com.webservice.domain.JobDetailsList;
import com.webservice.domain.JobHistory;
import com.webservice.domain.JobList;
import com.webservice.domain.LabourPerson;
import com.webservice.domain.LabourTimeList;
import com.webservice.domain.LocationList;
import com.webservice.domain.StatusList;
import com.webservice.domain.StockItemList;
import com.webservice.domain.StockLevelList;
import com.webservice.domain.StockTradeList;
import com.webservice.domain.StoremanList;
import com.webservice.domain.Timesheet;
import com.webservice.domain.TimesheetList;
import com.webservice.domain.User;
import com.webservice.domain.UserList;

public class HistoryDetailsActivity extends DialogActivity {
	List<String> detailsList;
	String[] titleList;
	EditText username, password, dob, st;
	EditText detailsText;
	HashMap< String, Object> savedValues;
	Intent intent;
	JobDetailsList jobDetailsList;
	JobList jobList;
	LabourTimeList labourTimeList;
	LocationList locationList;

	String response;
	String action;
	StatusList statusList;
	UserList allUsers;
	ScrollView scrollView;
	StoremanList storemanList;
	StockLevelList stockLevelList;
	StockItemList stockItemList;
	StockTradeList stockTradeList;
	TableLayout detailsTableLayout;
	TableLayout tableLayout;
	TextView error;
	TextView jobText;
	TimesheetList timesheetList;
	Button addHistoryButton;
	Button deleteHistoryButton;
	Button previous;
	Job selectedJob;
	JobHistory jobHistory;
	LoginListener loginListener;
	int count;
	int peopleListSize;
	int selectedJobHistoryId;
	QuantarcPopupDialog popupDialog;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getIntent().getExtras();
		action = "";
		savedValues = (HashMap<String, Object>)this.getLastNonConfigurationInstance();
		statusList = bundle.getParcelable("com.webservice.domain.StatusList");
		allUsers = bundle.getParcelable("com.webservice.domain.UserList");
		labourTimeList = bundle
				.getParcelable("com.webservice.domain.LabourTimeList");
		locationList = bundle
				.getParcelable("com.webservice.domain.LocationList");
		jobDetailsList = bundle
				.getParcelable("com.webservice.domain.JobDetailsList");
		
		jobList = bundle.getParcelable("com.webservice.domain.JobList");
		setContentView(R.layout.history_details);
		detailsTableLayout = (TableLayout) findViewById(R.id.hd17);
		addHistoryButton = (Button) findViewById(R.id.hd31);
		addHistoryButton.setOnClickListener(this);
		deleteHistoryButton = (Button) findViewById(R.id.hd32);
		deleteHistoryButton.setOnClickListener(this);
		tableLayout = (TableLayout) findViewById(R.id.hd12);
		popupDialog = new QuantarcPopupDialog(this);
		titleList = buildTitleList();
		selectedJobHistoryId = -1;
		if (savedValues !=null) {
			selectedJob = (Job)savedValues.get("job");
			timesheetList = (TimesheetList)savedValues.get("timesheet");
		} else {
			selectedJob = bundle.getParcelable("com.webservice.domain.Job");
			timesheetList = bundle.getParcelable("com.webservice.domain.TimesheetList");
		}
		this.addRows(selectedJob.getJobHistoryList());
	}

	private void addRows(List<JobHistory> jobHistoryList) {
		TableRow tableRow;
		TextView textView;
		int index = 0;
		int TEXT_HEIGHT = 20; // Anna 2012-07-07 changed it from 25
		tableLayout.removeAllViewsInLayout();
		tableLayout.removeAllViews();
		Collections.sort(jobHistoryList, new HistoryDateComparator());
		for (JobHistory jobH : jobHistoryList) {
			if (jobH.getToBeRemoved().equalsIgnoreCase("0")) {
				index = index + 1;
				tableRow = new TableRow(this);
				tableRow.setId(jobH.getId());
				tableRow.setOnClickListener(this);
				tableRow.setLayoutParams(new LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
				/* Create a TextView to be the row-content. */
				textView = new TextView(this);
				textView.setWidth(240);
				textView.setTextSize(TEXT_HEIGHT);
				textView.setText(jobH.getHistoryDate());
				textView.setLayoutParams(new LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
				/* Add Button to row. */
				tableRow.addView(textView);
				/* Add row to TableLayout. */
				tableLayout.addView(tableRow, new TableLayout.LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		boolean result;
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if (hasNewHistories()) {
				intent = new Intent();
				intent.putExtra("com.webservice.domain.Job", selectedJob);
				intent.putExtra("com.webservice.domain.TimesheetList",
						timesheetList);
				getParent().setResult(7000, intent);
				finish();
				result = true;
			} else {
				this.finish();
				result = true;
			}
		} else {
			result = super.onKeyDown(keyCode, event);
		}
		return result;
	}

	@Override
	public void onClick(View v) {
		boolean flag;
		if (v.getId() == addHistoryButton.getId()) {
			jobHistory = new JobHistory();
			jobHistory.setId(getNextId(selectedJob.getJobHistoryList()));
			jobHistory.setReadOnly("false");
			jobHistory.setToBeRemoved("0");
			jobHistory.setInTimesheet("false");
			jobHistory.setCurrentDate(super.getCurrentDate());
			if (super.getContext().getDisableTimes().equalsIgnoreCase("1")) {
				jobHistory.setStartTime("00:00");
		        jobHistory.setFinishTime("00:00");
			} else {
			        jobHistory.setStartTime(getStartTimeFromTimeList(jobHistory.getCurrentDate()) + ":00");
			        jobHistory.setFinishTime(getContext().getDefaultFinish() + ":00");
			}
			jobHistory.setJobNum(selectedJob.getJobNum());
			jobHistory.setSource(selectedJob.getSource());
			jobHistory.setCostOfMaterial(selectedJob.getCostOfMaterial());
			jobHistory.setUpdateTimesheet(super.getContext().getDisableTimes());
			intent = new Intent(HistoryDetailsActivity.this,
					ScrollActivity.class);
			intent.putExtra("com.webservice.domain.Job", selectedJob);
			intent.putExtra("com.webservice.domain.StatusList", statusList);
			intent.putExtra("com.webservice.domain.JobHistory", jobHistory);
			intent.putExtra("com.webservice.domain.UserList", allUsers);
			intent.putExtra("com.webservice.domain.LabourTimeList",
					labourTimeList);
			intent.putExtra("com.webservice.domain.LocationList", locationList);
			intent.putExtra("com.webservice.domain.JobDetailsList",
					jobDetailsList);
			intent.putExtra("com.webservice.domain.JobList", jobList);
			startActivityForResult(intent, 2000);
		}
		if (v.getId() == deleteHistoryButton.getId()) {
			if (selectedJobHistoryId >= 0) {
				flag = removeJobHistory(selectedJobHistoryId);
				if (flag) {
					removeFromTimesheet(selectedJobHistoryId,super.getCurrentDate());
					detailsTableLayout.removeAllViewsInLayout();
					detailsTableLayout.removeAllViews();
					addRows(selectedJob.getJobHistoryList());
				} else {
					popupDialog.showPopupDialog(
							"Unable to remove-Job History is read only", "ok");
				}
			} else {
				popupDialog.showPopupDialog(
						"Select a Job History to delete", "ok");
			}
		} else {
			selectedJobHistoryId = v.getId();
			TableRow row;
			int count = 0;
			JobHistory jobHistory = getJobHistory(selectedJobHistoryId);
			if (jobHistory != null) {
				while (count < tableLayout.getChildCount()) {
					row = (TableRow) tableLayout.getChildAt(count);
					if (row.getId() == selectedJobHistoryId) {
						row.setBackgroundColor(Color.parseColor("#6699FF"));
					} else {
						row.setBackgroundColor(Color.WHITE);
					}
					count = count + 1;
				}
				if (jobHistory.getReadOnly().equalsIgnoreCase("true")) {
					deleteHistoryButton.setText("Read Only");
					deleteHistoryButton.setEnabled(false);
				} else {
					deleteHistoryButton.setText("Delete History");
					deleteHistoryButton.setEnabled(true);
				}
				detailsList = buildDetailsList(jobHistory);
				populateDetailsTable(titleList, detailsList);
			}
		}
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		if (action.equalsIgnoreCase("back")) {
			switch (which) {
			case DialogInterface.BUTTON_POSITIVE: // Yes button clicked
				finish();
				break;
			case DialogInterface.BUTTON_NEGATIVE: // No button clicked
				break;
			}
			action = "";
		}
	}

	private Integer getNextId(List<JobHistory> jobHistoryList) {
		int id=0;
		for ( JobHistory jobHistory: jobHistoryList) {
			if (jobHistory.getId()>id) {
				id = jobHistory.getId();
			}
		}
		return id+1;
	}
	
	private void populateDetailsTable(String[] titleList,
			List<String> detailsList) {
		TableRow tableRow;
		TextView textView;
		int index = 0;
		detailsTableLayout.removeAllViewsInLayout();
		detailsTableLayout.removeAllViews();
		for (String row : detailsList) {
			index = index + 1;
			tableRow = new TableRow(this);
			tableRow.setId(Integer.valueOf(index));
			tableRow.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT));
			/* Create a TextView to be the row-content. */
			textView = new TextView(this);
			textView.setText(titleList[index - 1]);
			textView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT));
			/* Add Button to row. */
			tableRow.addView(textView);

			/* Create a TextView to be the row-content. */
			textView = new TextView(this);
			textView.setText(row);
			textView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT));
			/* Add Button to row. */
			tableRow.addView(textView);

			/* Add row to TableLayout. */
			detailsTableLayout.addView(tableRow, new TableLayout.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		}
	}

	private void removeFromTimesheet(int jobHistoryId, String historyDate) {
		Iterator<Timesheet> iter = timesheetList.getAllTimesheets().iterator();
		Timesheet timesheet = null;
		Timesheet toBeRemoved = null;
		String historyId = String.valueOf(jobHistoryId);
		boolean found = false;
		while ((!found) && (iter.hasNext())) {
			timesheet = iter.next();
			if (timesheet.getActionDate().equalsIgnoreCase(historyDate)) {
				if (timesheet.getWorkId().equalsIgnoreCase(historyId)) {
					toBeRemoved = timesheet;
					found = true;
				}
			}
		}
		if (found) {
			timesheetList.getAllTimesheets().remove(toBeRemoved);
		}
	}

	private List<String> buildDetailsList(JobHistory jobHistory) {
		LabourPerson labourPerson = getLabourPersonFromId(jobHistory
				.getLabourPersonId());
		String name = labourPerson.getName();
		String jobStatus = jobHistory.getJobStatus();
		Integer JobStatusId;
		if (jobHistory.getReadOnly().equalsIgnoreCase("true")) {
			jobStatus = statusList.getStatus(selectedJob.getJobStatus());
		}
		// Anna 2012-07-11 - commented out on 2012-07-17
		/*
		 * else { JobStatusId = getJobStatusIndex(jobStatus, statusList);
		 * selectedJob.setJobStatus(String.valueOf(JobStatusId)); }
		 */// Anna end
		List<String> details = new ArrayList<String>();
		details.add(jobHistory.getDetails());
		details.add(jobHistory.getHistoryDate());
		details.add(jobHistory.getStartTime());
		details.add(jobHistory.getFinishTime());
		details.add(name);
		details.add(jobStatus);
		details.add(jobHistory.getTimeCode());
		details.add(jobHistory.getTimeSpent());
		return details;
	}

	private LabourPerson getLabourPerson(String userName) {
		LabourPerson labourPerson = null;
		String storedName = "";
		for (User user : allUsers.getAllUsers()) {
			storedName = user.getFirstname() + " " + user.getSurname();
			if (storedName.equalsIgnoreCase(userName)) {
				labourPerson = user.getLabourPersonList().get(0);
			}
		}
		return labourPerson;
	}

	@Override
    public Object onRetainNonConfigurationInstance() {
        HashMap<String, Object> savedValues = new HashMap<String, Object>();
        savedValues.put("job", selectedJob);
        savedValues.put("timesheet", timesheetList);
        return savedValues;
    }
	
	private LabourPerson getLabourPersonFromId(String id) {
		LabourPerson labourPerson = null;
		String storedName = "";
		Iterator<LabourPerson> iterPerson;
		Iterator<User> iter = allUsers.getAllUsers().iterator();
		User user;
		boolean found = false;
		while ((!found) && (iter.hasNext())) {
			user = iter.next();
			iterPerson = user.getLabourPersonList().iterator();
			while (iterPerson.hasNext()) {
				labourPerson = iterPerson.next();
				if (labourPerson.getId().equalsIgnoreCase(id)) {
					found = true;
				}
			}
		}
		return labourPerson;
	}

	private String[] buildTitleList() {
		String[] details = new String[8];
		details[0] = "Details";
		details[1] = "History Date";
		details[2] = "Start Time";
		details[3] = "End Time";
		details[4] = "Labour Person";
		details[5] = "Job Status";
		details[6] = "Time Code";
		details[7] = "Time Spent";
		return details;
	}

	private boolean hasNewHistories() {
		boolean exist = false;
		Iterator<JobHistory> iter = selectedJob.getJobHistoryList().iterator();
		while ((!exist) && (iter.hasNext())) {
			jobHistory = iter.next();
			if (jobHistory.getReadOnly().equalsIgnoreCase("false")) {
				exist = true;
			}
		}
		return exist;
	}

	private String getStartTimeFromTimeList(String strDate) {
		String startTime = "";
		String currentTime = "00:00";
		for (Timesheet timesheet : timesheetList.getAllTimesheets()) {
			if ((timesheet.getActionDate() != null)
					&& (compareDates(timesheet.getActionDate(), strDate)) == 0) {
				if (timeGreater(timesheet.getFinishTime(), currentTime)) {
					currentTime = timesheet.getFinishTime();
				}
			}
		}
		startTime = currentTime;
		return startTime;
	}

	private JobHistory getJobHistory(int id) {
		JobHistory requiredJobHistory = null;
		JobHistory history = null;
		boolean found = false;
		Iterator<JobHistory> iter = selectedJob.getJobHistoryList().iterator();
		while ((!found) && (iter.hasNext())) {
			history = iter.next();
			if (history.getId() == id) {
				found = true;
				requiredJobHistory = history;
			}
		}
		return requiredJobHistory;
	}

	private boolean timeGreater(String firstTime, String secondTime) {
		boolean greater = false;
		Integer startHours1 = Integer.valueOf(firstTime.substring(0, 2));
		Integer startHours2 = Integer.valueOf(secondTime.substring(0, 2));
		Integer startMinutes1 = Integer.valueOf(firstTime.substring(3, 5));
		Integer startMinutes2 = Integer.valueOf(secondTime.substring(3, 5));
		Integer time1 = startHours1 * 60 + startMinutes1;
		Integer time2 = startHours2 * 60 + startMinutes2;
		if (time1 > time2) {
			greater = true;
		}
		return greater;
	}

	private boolean removeJobHistory(int id) {
		JobHistory history = null;
		JobHistory removeHistory=null;
		boolean found = false;
		boolean toBeRemoved = false;
		Iterator<JobHistory> iter = selectedJob.getJobHistoryList().iterator();
		while ((!found) && (iter.hasNext())) {
			history = iter.next();
			if (history.getId() == id) {
				found = true;
				if (history.getReadOnly().equalsIgnoreCase("true")) {
					toBeRemoved = false;
				} else {
					history.setToBeRemoved("1");
					removeHistory = history;
					toBeRemoved = true;
				}
			}
		}
		if (toBeRemoved) {
			selectedJob.getJobHistoryList().remove(removeHistory);
		}
		return toBeRemoved;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		String jobStatus; // Anna 2012-07-08
		String completeStatus; // Anna 2012-07-08
		super.onActivityResult(requestCode, resultCode, data);
		if ((data != null) && (resultCode != 9990)) {
			jobHistory = data.getExtras().getParcelable(
					"com.webservice.domain.JobHistory");
			LabourPerson labourPerson = this.getLabourPerson(jobHistory
					.getLabourPersonId());
			jobHistory.setLabourPersonId(labourPerson.getId());
			// String status = data.getStringExtra("confirmstatus"); // Anna
			// 2012-07-07
			// jobHistory.setJobStatus(status); // Anna 2012-07-07
			Log.v("HistoryDetails",jobHistory.getUpdateTimesheet());
			
			selectedJob.addJobHistory(jobHistory);
			if (super.getContext().getDisableTimes().equalsIgnoreCase("0")) {
			     addJobHistory(jobHistory, timesheetList,selectedJob.getJobNumLong());
			}
			getContext().setNewHistories("true");
			selectedJob.setCostOfMaterial(jobHistory.getCostOfMaterial());

			if (resultCode == 1550) {
				// selectedJob.setJobStatus("Complete"); // Anna 2012-07-08
				// Anna 2012-07-08
				completeStatus = data.getExtras().getString("confirmstatus");
				if (completeStatus.equalsIgnoreCase("qsetting")) {
					jobStatus = String.valueOf(statusList.getQsetting());
					selectedJob.setJobStatus(jobStatus);
					jobHistory.setJobStatus(getStatus(selectedJob.getJobStatus()));
				}
				// Anna end
				Intent returnIntent = new Intent();
				// returnIntent.putExtra("confirmstatus", "Complete"); // Anna
				// 2012-07-08
				returnIntent.putExtra("com.webservice.domain.Job", selectedJob);
				returnIntent.putExtra("com.webservice.domain.TimesheetList",
						timesheetList);
				getParent().setResult(1580, returnIntent);
				this.finish();
			}
			this.addRows(selectedJob.getJobHistoryList());
		}
	}

	private void addJobHistory(JobHistory jobHistory,
			TimesheetList timesheetList, String jobNumLong) {
		Timesheet timesheet = null;
		String historyDate=null;
		if ((jobHistory.getReadOnly().equalsIgnoreCase("false"))
				&& (jobHistory.getInTimesheet().equalsIgnoreCase("false"))
				&& (jobHistory.getToBeRemoved().equalsIgnoreCase("0"))) {
			timesheet = new Timesheet();
			historyDate = jobHistory.getHistoryDate();
			if (historyDate.length()>10) {
				historyDate = jobHistory.getHistoryDate().substring(0, 10);
			}
			timesheet.setActionDate(historyDate);
			timesheet.setStartTime(jobHistory.getStartTime().substring(11, 16));
			timesheet.setFinishTime(jobHistory.getFinishTime()
					.substring(11, 16));
			timesheet.setTimeCode(jobHistory.getTimeCode());
			timesheet.setJobRef(jobNumLong);
			timesheet.setWorkId(String.valueOf(jobHistory.getId()));
			timesheet.setIsNew("false");
			jobHistory.setInTimesheet("true");
			timesheetList.getAllTimesheets().add(timesheet);
		}
	}

	// Anna 2012-07-11 new sub
	private int getJobStatusIndex(String status, StatusList statusList) {
		String value = "";
		String statusId = "";
		String tempId="";
		int selection = 0;
		boolean found = false;
		Iterator<String> statuskeys = statusList.getStatusMap().keySet()
				.iterator();
		
		//while (statuskeys.hasNext()) {
		//	tempId=statuskeys.next();
		//	Log.v("Quantarc","Statuses:"+tempId+":"+statusList.getStatus(tempId));
		// }
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

	private String getStatus(String id) {
		String status = statusList.getStatus(id);
		return status;
	}

}