package com.webservice.android.activities;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.webservice.android.common.QuantarcPopupDialog;
import com.webservice.android.listeners.TimesheetSpentItemSelectedListener;
import com.webservice.android.util.DateUtil;
import com.webservice.android.util.EnterTimeSpinnerListener;
import com.webservice.domain.Job;
import com.webservice.domain.JobHistory;
import com.webservice.domain.LabourTime;
import com.webservice.domain.LabourTimeList;
import com.webservice.domain.Timesheet;
import com.webservice.domain.TimesheetList;

public class EnterTimeActivity extends DialogActivity {
	private static String STANDARD_DATE_FORMAT = "dd/MM/yyyy kk:mm:ss";
	List<String> labourCodeList, fromMinuteList, toMinuteList, spentList, jobRefList;
	ArrayAdapter<String> timeCodesAdapter, fromHourAdapter, fromMinuteAdapter;
	ArrayAdapter<String> toHourAdapter, toMinuteAdapter, spentAdapter, jobRefAdapter;
	EditText dateText;
	EnterTimeSpinnerListener spinnerListener1,spinnerListener2,spinnerListener3,spinnerListener4;
	EnterTimeSpinnerListener spinnerListener5,spinnerListener6;
	TimesheetSpentItemSelectedListener spentListener;
	LabourTimeList labourTimeList;
	Map<String, List<String>> spentMap;
	TextView error, jobText;
	TimesheetList timesheetList;
	Timesheet timesheet;
	Spinner timeCodesSpinner, fromHourSpinner, fromMinuteSpinner, toHourSpinner, toMinuteSpinner;
	Spinner jobSpinner, spentSpinner;
	String response, displayJob;
	String strDate;
	String jobRefs;
	StringTokenizer jobRefTokenizer;
	String defaultFinish, defaultStartMinute, defaultToMinute, defaultToHour;
	String defaultFrom, defaultFromHour, defaultFromMinute;
	List<String> fromHourList, toHourList;
	int defaultStartHour;
	int defaultStopHour;
	int defaultMinute;
	Button submitButton, cancelButton;
	Button previous;
	int count;
	int peopleListSize;
	QuantarcPopupDialog popupDialog;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.enter_time);
		Bundle bundle = getIntent().getExtras();
		timesheetList = bundle.getParcelable("com.webservice.domain.TimesheetList");
		labourTimeList = bundle.getParcelable("com.webservice.domain.LabourTimeList");
		timesheet = bundle.getParcelable("com.webservice.domain.Timesheet");
		strDate = bundle.getCharSequence("dateentry").toString();
		jobRefs = bundle.getCharSequence("jobrefs").toString();
		spentList = new ArrayList<String>();
		dateText = (EditText) findViewById(R.id.et30);
		dateText.setText(strDate);
		defaultStartMinute="00";
		defaultFinish = getContext().getDefaultFinish();
		defaultToHour = defaultFinish.substring(0,2);
		defaultToMinute = defaultFinish.substring(3,5);
		defaultFrom = getContext().getDefaultStart();
		defaultFromHour = defaultFrom.substring(0,2);
		defaultFromMinute = defaultFrom.substring(3,5);
		popupDialog = new QuantarcPopupDialog(this);
        if (timesheet.getIsNew().equalsIgnoreCase("true")) {
        	 
        	 fromHourList = super.getHourList();
        	 toHourList = super.getHourList();
    		 fromMinuteList = super.getMinuteList();
    		 toMinuteList = super.getMinuteList();
        	 calculateDefaultHourTimes(timesheet);
		     spentList = getLabourSpentList();
		     timesheet.setTimeCode(spentList.get(0));
		     labourCodeList = getLabourCodeList(spentList.get(0));
        } 
		spinnerListener1 = new EnterTimeSpinnerListener(timesheet, "starthour");
		spinnerListener2 = new EnterTimeSpinnerListener(timesheet, "startminute");
		spinnerListener3 = new EnterTimeSpinnerListener(timesheet, "finishhour");
		spinnerListener4 = new EnterTimeSpinnerListener(timesheet, "finishminute");
		spinnerListener5 = new EnterTimeSpinnerListener(timesheet, "timecode");
		spinnerListener6 = new EnterTimeSpinnerListener(timesheet, "jobref");
		spentListener = new TimesheetSpentItemSelectedListener(this, timesheet);
		fromHourSpinner = (Spinner) findViewById(R.id.et60);
		fromMinuteSpinner = (Spinner) findViewById(R.id.et70);
		toHourSpinner = (Spinner) findViewById(R.id.et90);
		toMinuteSpinner = (Spinner) findViewById(R.id.et100);
		spentSpinner = (Spinner) findViewById(R.id.et120);
		spentSpinner.setOnItemSelectedListener(spentListener);
		spentAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, spentList);
		spentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spentSpinner.setAdapter(spentAdapter);
		timeCodesSpinner = (Spinner) findViewById(R.id.et140);
		
		timeCodesSpinner.setOnItemSelectedListener(spinnerListener5);
		timeCodesAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, labourCodeList);
		timeCodesSpinner.setAdapter(timeCodesAdapter);
		fromHourAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, fromHourList);
		fromHourAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		fromHourSpinner.setAdapter(fromHourAdapter);
		fromHourSpinner.setOnItemSelectedListener(spinnerListener1);
		fromHourSpinner.setSelection(defaultStartHour);
		fromMinuteAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, fromMinuteList);
		fromMinuteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		fromMinuteSpinner.setAdapter(fromMinuteAdapter);
		if (timesheet.getIsNew().equalsIgnoreCase("true")) {
			fromMinuteSpinner.setSelection(getValueLocation(fromMinuteList, defaultStartMinute));
		}
		fromMinuteSpinner.setOnItemSelectedListener(spinnerListener2);
		toHourAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, toHourList);
		toHourAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		toHourSpinner.setAdapter(toHourAdapter);
		toHourSpinner.setOnItemSelectedListener(spinnerListener3);
		toHourSpinner.setSelection(getValueLocation(toHourList, defaultToHour));
		toMinuteAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, toMinuteList);
		toMinuteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		toMinuteSpinner.setAdapter(toMinuteAdapter);
		toMinuteSpinner.setSelection(getValueLocation(toMinuteList, defaultToMinute));
		toMinuteSpinner.setOnItemSelectedListener(spinnerListener4);
		jobText = (TextView)findViewById(R.id.et150);
		jobSpinner = (Spinner) findViewById(R.id.et160);
		displayJob = super.getContext().getDisableJobsOnTimesheet();
		if (displayJob.equalsIgnoreCase("1")) {
			jobText.setVisibility(View.GONE);
			jobSpinner.setVisibility(View.GONE);
		} else {
			     jobRefTokenizer = new StringTokenizer(jobRefs,",");
			     jobRefList = new ArrayList<String>();
			     jobRefList.add(" ");
			     while (jobRefTokenizer.hasMoreTokens()) {
			    	 jobRefList.add(jobRefTokenizer.nextToken());
			     }
			     jobRefAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, jobRefList);
				 jobRefAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				 jobSpinner.setAdapter(jobRefAdapter);
				 jobSpinner.setOnItemSelectedListener(spinnerListener6);
			     
		}
		submitButton = (Button) findViewById(R.id.et170);
		submitButton.setOnClickListener(this);
		cancelButton = (Button) findViewById(R.id.et180);
		cancelButton.setOnClickListener(this);
		if (timesheet.getIsNew().equalsIgnoreCase("false")) {
			submitButton.setEnabled(false);
		}
	}
	
	@Override
	public void onClick(View v) {
	  Intent intent=null;
	  String errorMessage="";
	  int id = v.getId();
	  if (id==cancelButton.getId()) {
		setResult(1050,null);
		this.finish();
	  } else if (id==submitButton.getId()) {
		       errorMessage = this.validateTimesheet(timesheet);
		       if (errorMessage.length()==0) {
		         intent = new Intent();
		         timesheetList.getAllTimesheets().add(timesheet);
			     intent.putExtra("com.webservice.domain.TimesheetList", timesheetList);
			     setResult(2000,intent);
			     this.finish();
		       } else {
		    	   popupDialog.showPopupDialog(errorMessage, "OK");
		       }
	  } 
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK
	            && event.getRepeatCount() == 0) {
	    	setResult(1050,null);
	    	this.finish();
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	private void calculateDefaultHourTimes(Timesheet newTimesheet) {
		String strStart="0";
		defaultStartMinute = newTimesheet.getStartTime().substring(3, 5);
		strStart = newTimesheet.getStartTime().substring(0, 2);
		if (strStart.equalsIgnoreCase("00")) {
			strStart = defaultFromHour;
		}
		defaultStartHour = getValueLocation(fromHourList, strStart);
	}
	
	private List<String> getLabourSpentList() {
		List<String> list=new ArrayList<String>();
		String spentCode="";
		list.add(" ");
		for(LabourTime labourTime:labourTimeList.getLabourTimeList()) {
			spentCode = labourTime.getSpent();
			if (!list.contains(spentCode)) {
			   list.add(labourTime.getSpent());
			}
		}
		return list;
	}
	
	public Spinner getTimeCodeSpinner() {
		return timeCodesSpinner;
	}
	
	public List<String> getSpentListFromTimeCode(String timeCode) {
		List<String> list=new ArrayList<String>();
		String code="";
		list.add(" ");
		for(LabourTime labourTime:labourTimeList.getLabourTimeList()) {
			code = labourTime.getCode();
			if (code.equalsIgnoreCase(timeCode)) {
			   list.add(labourTime.getSpent());
			}
		}
		return list;
	}
	
	public List<String> getLabourCodeList(String spentCode) {
		List<String> list=new ArrayList<String>();
		String timeCode="";
		for(LabourTime labourTime:labourTimeList.getLabourTimeList()) {
			timeCode = labourTime.getCode();
			if (labourTime.getSpent().equalsIgnoreCase(spentCode)) {
			   list.add(timeCode + "-" + labourTime.getDescription());
			}
		}
		return list;
	}
	
	private String validateTimesheet(Timesheet timesheet) {
		String message="";
		String displayMessage="The following errors must be corrected, ";
		boolean fault=false;
		if (!validTimesheetTime(timesheet.getStartTime(),timesheet.getFinishTime())) {
			displayMessage = displayMessage + "the To time must before the From time";
			fault = true;
		}
		if ((!fault) && (!validTimesheetOverlapTime(timesheet.getActionDate(),timesheet.getStartTime(),timesheet.getFinishTime()))) {
			displayMessage = displayMessage + "From and To time overlap with an existing Timesheet entry";
			fault = true;
		}
		if (fault) {
			displayMessage = displayMessage+".";
			message = displayMessage;
		}
		return message;
	}
	
	private boolean validTimesheetTime(String startTime, String finishTime) {
		boolean validTime=true;
		String startHour = startTime.substring(0, 2);
		String startMinute = startTime.substring(3,5);
		String finishHour = finishTime.substring(0, 2);
		String finishMinute = finishTime.substring(3,5);
		int startLength = Integer.valueOf(startHour)*60+Integer.valueOf(startMinute);
		int finishLength = Integer.valueOf(finishHour)*60+Integer.valueOf(finishMinute);
		if (startLength>=finishLength) {
			validTime=false;
		}
		return validTime;
	}
	
	private boolean validTimesheetOverlapTime(String actionDate, String startTime, String finishTime) {
		boolean validTime = false;
		Date newStartDate = DateUtil.toDate(actionDate +" " + startTime+":00", STANDARD_DATE_FORMAT);
		Date newFinishDate = DateUtil.toDate(actionDate + " " + finishTime+":00", STANDARD_DATE_FORMAT);
		String newActionDate = actionDate;
		String oldActionDate = "";
		Date storedFinishDate;
		Date storedStartDate;
		int count=0;
		int numberOfTimeEntries = timesheetList.getAllTimesheets().size();
		Iterator<Timesheet> iter = timesheetList.getAllTimesheets().iterator();
		Timesheet timesheet = null;
		while (iter.hasNext()) {
			timesheet = iter.next();
			oldActionDate = timesheet.getActionDate();
			if (newActionDate.equalsIgnoreCase(oldActionDate)) {
				storedFinishDate = DateUtil.toDate(oldActionDate + " " +timesheet.getFinishTime()+":00",STANDARD_DATE_FORMAT);
				storedStartDate = DateUtil.toDate(oldActionDate + " " + timesheet.getStartTime()+":00",STANDARD_DATE_FORMAT);
				if (((newStartDate.after(storedFinishDate)) || (newStartDate.equals(storedFinishDate))) || 
				   ((newFinishDate.before(storedStartDate)) || (newFinishDate.equals(storedStartDate)))) {
					count=count+1;
				}
			} else {
				count = count +1;
			}
		}
		if (count==numberOfTimeEntries) {
			validTime=true;
		}
		return validTime;
	}
	
}