package com.webservice.android.activities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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

import com.webservice.android.common.QuantarcPopupDialog;
import com.webservice.android.listeners.HistoryItemSelectedListener;
import com.webservice.android.listeners.LoginListener;
import com.webservice.android.listeners.TimeSpentItemSelectedListener;
import com.webservice.android.util.CostOfMaterialsTextWatcher;
import com.webservice.android.util.JobHistoryTextWatcher;
import com.webservice.android.util.SpinnerOnItemSelectedListener;
import com.webservice.android.util.StringComparator;
import com.webservice.domain.Job;
import com.webservice.domain.JobDetails;
import com.webservice.domain.JobDetailsList;
import com.webservice.domain.JobHistory;
import com.webservice.domain.JobList;
import com.webservice.domain.LabourPerson;
import com.webservice.domain.LabourTime;
import com.webservice.domain.LabourTimeList;
import com.webservice.domain.StatusList;
import com.webservice.domain.User;
import com.webservice.domain.UserList;

public class JobHistoryActivity extends DialogActivity {
	private static String STANDARD_DATE_FORMAT = "dd/MM/yyyy";
	private static String LONG_DATE_FORMAT = "yyyy-MM-dd kk:mm:ss";
	EditText username, password, dob, st;
	TextView error;
	EditText dateText;
	EditText details;
	EditText costOfMaterialsText;
	String schedule;
	String response;
	String action;
	TextView jobText;
	TextView timeText;
	ArrayAdapter<String> labourSpentAdapter;
	ArrayAdapter<String> timeCodeAdapter;
	ArrayAdapter<String> nameAdapter;
	ArrayAdapter<String> statusAdapter;
	ArrayAdapter<String> fromMinuteAdapter;
	ArrayAdapter<String> fromHourAdapter;
	ArrayAdapter<String> toMinuteAdapter;
	ArrayAdapter<String> historiesAdapter;
	ArrayAdapter<String> toHourAdapter;
	HistoryItemSelectedListener historyItemSelectedListener;
	HistoryItemSelectedListener historyListener;
	JobHistory jobHistory;
	JobList jobList;
	JobDetailsList jobDetailsList;
	JobHistoryTextWatcher historyDateWatcher, detailsTextWatcher;
	CostOfMaterialsTextWatcher costOfMaterialsTextWatcher;
	String[] personList;
	HashMap< String, Object> savedValues;
	List<String> labourCodeList;
	List<String> labourSpentList;
	List<String> allJobDetails;
	List<String> minuteList=Arrays.asList("00","00","00","00","00");
	List<String> statuses;
	Button issueButton;
	Button previous;
	boolean hasTime=false;;
	LoginListener loginListener;
	LabourTimeList labourTimeList;
	SpinnerOnItemSelectedListener spinnerListener1, spinnerListener2,
			spinnerListener3, spinnerListener4;
	SpinnerOnItemSelectedListener spinnerListener5, spinnerListener6,
			spinnerListener7, spinnerListener8;
	Spinner labourSpentSpinner;
	Spinner personNameSpinner;
	Spinner statusSpinner;
	Spinner fromMinuteSpinner;
	Spinner fromHourSpinner;
	Spinner toMinuteSpinner;
	Spinner toHourSpinner;
	Spinner timeCodeSpinner;
	Spinner historiesSpinner;
	StatusList statusList;
	UserList allUsers;
	List<String> hourList;
	String defaultStartTime = "";
	TimeSpentItemSelectedListener timeSpentItemSelectedListener;
	int count;
	int defaultStartHour;
	int defaultStartMinute;
	int defaultEndHour;
	int defaultEndMinute;
	int defaultUserSelection = 0;
	int peopleListSize;
	int statusCurrentSelection = 0;
	Job selectedJob;
	QuantarcPopupDialog popupDialog;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.job_history);
		schedule = getContext().getSchedule();
		Bundle bundle = getIntent().getExtras();
		savedValues = (HashMap<String, Object>)this.getLastNonConfigurationInstance();
		
		selectedJob = bundle.getParcelable("com.webservice.domain.Job");
		statusList = bundle.getParcelable("com.webservice.domain.StatusList");
		jobHistory = bundle.getParcelable("com.webservice.domain.JobHistory");
		jobHistory.setLabourPersonId(getContext().getUsername());
		allUsers = bundle.getParcelable("com.webservice.domain.UserList");
		labourTimeList = bundle.getParcelable("com.webservice.domain.LabourTimeList");
		jobDetailsList = bundle.getParcelable("com.webservice.domain.JobDetailsList");
		jobList = bundle.getParcelable("com.webservice.domain.JobList");
		details = (EditText) findViewById(R.id.ajh200);
		if (savedValues==null) {
		 jobHistory.setDetails("");
		 historyListener = new HistoryItemSelectedListener(details);
		} else {
			jobHistory.setDetails((String)savedValues.get("details"));
			details.setText("fred");
			historyListener = new HistoryItemSelectedListener(details);
		}
			
		personList = populatePersonList(allUsers);
		hourList = super.getHourList();
	
		defaultStartTime = jobHistory.getStartTime();
		defaultStartHour = Integer.valueOf(defaultStartTime.substring(0, 2));
		defaultEndHour = Integer.valueOf(jobHistory.getFinishTime().substring(0, 2));
	    defaultEndMinute = Integer.valueOf(jobHistory.getFinishTime().substring(3, 5));
		if (!super.getContext().getDisableTimes().equalsIgnoreCase("1")) {
		  minuteList = super.getMinuteList();
		  if (defaultStartHour == 0) {
			 hasTime=false;
			 defaultStartHour = super.getValueLocation(hourList, getContext()
					.getDefaultStart().substring(0, 2));
		  } else {
			hasTime = true;
			defaultStartHour = super.getValueLocation(hourList,
					defaultStartTime.substring(0, 2));
		  }
		  defaultStartMinute = Integer.valueOf(defaultStartTime.substring(3, 5));
		  if (!hasTime) {
			defaultStartMinute = super.getValueLocation(minuteList,
					getContext().getDefaultStart().substring(3, 5));
		  } else {
			defaultStartMinute = super.getValueLocation(minuteList,
					defaultStartTime.substring(3, 5));
		  }
		  defaultEndHour = super.getValueLocation(hourList, getContext()
				.getDefaultFinish().substring(0, 2));
		  defaultEndMinute = super.getDefaultMinutePosition(minuteList,
				getContext().getDefaultFinish().substring(3, 5));
		}
		jobText = (TextView) findViewById(R.id.ajh3);
		jobText.setText(selectedJob.getJobNumLong());
		dateText = (EditText) findViewById(R.id.ajh40);
		costOfMaterialsText = (EditText) findViewById(R.id.ajh170);
		costOfMaterialsText.setText(jobHistory.getCostOfMaterial());
		costOfMaterialsTextWatcher = new CostOfMaterialsTextWatcher(jobHistory);
		costOfMaterialsText.addTextChangedListener(costOfMaterialsTextWatcher);
		jobHistory.setHistoryDate(super.getCurrentDateTime());
		dateText.setText(jobHistory.getHistoryDate());
		historyDateWatcher = new JobHistoryTextWatcher(jobHistory, "date");
		
		
		detailsTextWatcher = new JobHistoryTextWatcher(jobHistory, "details");
		details.addTextChangedListener(detailsTextWatcher);
		spinnerListener1 = new SpinnerOnItemSelectedListener(jobHistory,
				"status", jobList);
		spinnerListener2 = new SpinnerOnItemSelectedListener(jobHistory,
				"timeCode", jobList);
		spinnerListener3 = new SpinnerOnItemSelectedListener(jobHistory,
				"fromHour", jobList);
		spinnerListener4 = new SpinnerOnItemSelectedListener(jobHistory,
				"fromMinute", jobList);
		spinnerListener5 = new SpinnerOnItemSelectedListener(jobHistory,
				"toHour", jobList);
		spinnerListener6 = new SpinnerOnItemSelectedListener(jobHistory,
				"toMinute", jobList);
		spinnerListener7 = new SpinnerOnItemSelectedListener(jobHistory,
				"person", jobList);
		spinnerListener8 = new SpinnerOnItemSelectedListener(jobHistory,
				"histories", jobList);
		spinnerListener8.setDetailsText(details);
		timeSpentItemSelectedListener = new TimeSpentItemSelectedListener(this,
				jobHistory);
		dateText.addTextChangedListener(historyDateWatcher);
		statusSpinner = (Spinner) findViewById(R.id.ajh110);
		statusSpinner.setOnItemSelectedListener(spinnerListener1);
		allJobDetails = getAllJobDetails(jobDetailsList);
		historiesSpinner = (Spinner) findViewById(R.id.ajh190);
		
		labourSpentSpinner = (Spinner) findViewById(R.id.ajh130);
		historiesAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, allJobDetails);
		historiesAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Anna
																							// 2012-07-03
																							// converted
																							// to
																							// radio
																							// button
																							// drop
																							// down
																							// list
		historiesSpinner.setAdapter(historiesAdapter);
		historiesSpinner.setOnItemSelectedListener(historyListener);
		labourSpentSpinner
				.setOnItemSelectedListener(timeSpentItemSelectedListener);
		timeCodeSpinner = (Spinner) findViewById(R.id.ajh150);
		timeCodeSpinner.setOnItemSelectedListener(spinnerListener2);
		fromHourSpinner = (Spinner) findViewById(R.id.ajh60);
		fromHourSpinner.setOnItemSelectedListener(spinnerListener3);
		fromMinuteSpinner = (Spinner) findViewById(R.id.ajh70);
		fromMinuteSpinner.setOnItemSelectedListener(spinnerListener4);
		toHourSpinner = (Spinner) findViewById(R.id.ajh80);
		toHourSpinner.setOnItemSelectedListener(spinnerListener5);
		toMinuteSpinner = (Spinner) findViewById(R.id.ajh90);
		toMinuteSpinner.setOnItemSelectedListener(spinnerListener6);
		labourSpentList = getLabourSpentList();
		labourCodeList = getLabourCodeList(labourSpentList.get(0));
		personNameSpinner = (Spinner) findViewById(R.id.ajh20);
		nameAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, personList);
		nameAdapter.sort(new StringComparator());
		nameAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		personNameSpinner.setOnItemSelectedListener(spinnerListener7);
		personNameSpinner.setAdapter(nameAdapter);
		setKnownUser();
	
		
		labourSpentAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, labourSpentList);
		labourSpentAdapter.sort(new StringComparator());
		labourSpentAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		timeCodeAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, labourCodeList);
		timeCodeAdapter.sort(new StringComparator());
		timeCodeAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		labourSpentSpinner.setAdapter(labourSpentAdapter);
		timeCodeSpinner.setAdapter(timeCodeAdapter);
		statuses = statusList.getValues();
		statusAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, statuses);
		statusAdapter.sort(new StringComparator());
		statusAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		statusSpinner.setAdapter(statusAdapter);
		statusCurrentSelection = getCurrentSelection(selectedJob, statusList);
		statusSpinner.setSelection(statusCurrentSelection);
		fromHourAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, hourList);
		fromHourAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		fromHourSpinner.setAdapter(fromHourAdapter);
		fromHourSpinner.setSelection(defaultStartHour);

		fromMinuteAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, minuteList);
		fromMinuteAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Anna
																							// 2012-07-03
																							// replaced
																							// to
																							// radio
																							// button
																							// spinner
		fromMinuteSpinner.setAdapter(fromMinuteAdapter);
		fromMinuteSpinner.setSelection(defaultStartMinute);
		toHourAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, hourList);
		toHourAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		toHourSpinner.setAdapter(toHourAdapter);
		toHourSpinner.setSelection(defaultEndHour);
		toMinuteAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, minuteList);
		toMinuteAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Anna
																							// 2012-07-03
																							// replaced
																							// to
																							// radio
																							// button
																							// spinner
		toMinuteSpinner.setAdapter(toMinuteAdapter);
		toMinuteSpinner.setSelection(defaultEndMinute);
		if (super.getContext().getDisableTimes().equalsIgnoreCase("1")) {
			fromMinuteSpinner.setEnabled(false);
			fromMinuteSpinner.setClickable(false);
			fromHourSpinner.setEnabled(false);
			fromHourSpinner.setClickable(false);
			toMinuteSpinner.setEnabled(false);
			toMinuteSpinner.setClickable(false);
			toHourSpinner.setEnabled(false);
			toHourSpinner.setClickable(false);
			labourSpentSpinner.setEnabled(false);
			labourSpentSpinner.setClickable(false);
			timeCodeSpinner.setEnabled(false);
			timeCodeSpinner.setClickable(false);
		}
		popupDialog= new QuantarcPopupDialog(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, IssueStockActivity.class);
		startActivity(intent);

	}


	public Spinner getTimeCodeSpinner() {
		return timeCodeSpinner;
	}

	public List<String> getLabourCodeList(String spentCode) {
		List<String> list = new ArrayList<String>();
		String timeCode = "";
		String desc = "";
		list.add(" ");
		for (LabourTime labourTime : labourTimeList.getLabourTimeList()) {
			timeCode = labourTime.getCode();
			desc = labourTime.getDescription();
			if (labourTime.getSpent().equalsIgnoreCase(spentCode)) {
				list.add(timeCode + "-" + desc);
			}
		}
		return list;
	}

	private List<String> getLabourSpentList() {
		List<String> list = new ArrayList<String>();
		String spentCode = "";
		list.add(" ");
		for (LabourTime labourTime : labourTimeList.getLabourTimeList()) {
			spentCode = labourTime.getSpent();
			if (!list.contains(spentCode)) {
				list.add(labourTime.getSpent());
			}
		}
		return list;
	}
	
	private String getStartTime(JobHistory jobHistory) {
		String startTime="00:00";
		if (jobHistory.getStartTime().length()>15) {
			startTime = jobHistory.getStartTime().substring(11,16);
		}
		return startTime;
	}
	
	@Override
    public Object onRetainNonConfigurationInstance() {
        HashMap<String, Object> savedValues = new HashMap<String, Object>();
        savedValues.put("details", jobHistory.getDetails());
        return savedValues;
    }

	private String[] populatePersonList(UserList allUsers) {
		String[] personList = new String[allUsers.getAllUsers().size()];
		String id = getContext().getUserId();
		String name = "";
		LabourPerson labourPerson;
		int index = 0;
		for (User user : allUsers.getAllUsers()) {
			name = user.getFirstname() + " " + user.getSurname();
			personList[index] = name;
			labourPerson = getLabourPerson(name);
			if (labourPerson.getId().equalsIgnoreCase(id)) {
				defaultUserSelection = index;
			}
			index++;
		}
		return personList;
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

	private List<String> getAllJobDetails(JobDetailsList jobDetailsList) {
		List<String> list = new ArrayList<String>();
		list.add(" ");
		for (JobDetails details : jobDetailsList.getJobDetails()) {
			list.add(details.getJobDetails());
		}
		return list;
	}

	private int getCurrentSelection(Job selectedJob, StatusList statusList) {
		String status = "";
		String jobStatus = "";
		int selection = 0;
		int index = 0;
		Iterator<String> iter = statuses.iterator();
		boolean found = false;
		String jobStatusId = selectedJob.getJobStatus().trim();
		if ((jobStatusId != null) && (jobStatusId.length() > 0)) {
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


	private int getUserPosition(String userName) {
		String name = "";
		boolean found = false;
		boolean finish = false;
		int size = nameAdapter.getCount();
		int position = 0;
		int index = 0;
		while ((!found) && (!finish)) {
			name = nameAdapter.getItem(position);
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

	private void setKnownUser() {
		int position = 0;
		position = getUserPosition(getContext().getUsername());
		personNameSpinner.setSelection(position);
	}

}
