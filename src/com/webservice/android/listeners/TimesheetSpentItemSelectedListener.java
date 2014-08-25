package com.webservice.android.listeners;
import java.util.List;

import com.webservice.android.activities.EnterTimeActivity;
import com.webservice.android.activities.JobHistoryActivity;
import com.webservice.domain.JobHistory;
import com.webservice.domain.Timesheet;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;

public class TimesheetSpentItemSelectedListener implements OnItemSelectedListener {

	  private EnterTimeActivity timesheetActivity;
	  private Timesheet timesheet;
	  private ArrayAdapter<String> timeCodeAdapter;
	  private List<String> timeCodeList;
	  
	  public TimesheetSpentItemSelectedListener(EnterTimeActivity enterTimeActivity, Timesheet timesheet) {
		  this.timesheetActivity = enterTimeActivity;
		  this.timesheet = timesheet;
	  }
	
	  public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		  String value = (String)parent.getItemAtPosition(pos);
          Spinner timeCodeSpinner;
		  timesheet.setSpent(value);
		  timeCodeList = timesheetActivity.getLabourCodeList(value);
		  timeCodeSpinner = timesheetActivity.getTimeCodeSpinner();
		  timeCodeAdapter = new ArrayAdapter<String>(timesheetActivity,android.R.layout.simple_spinner_item, timeCodeList);
		  timeCodeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		  timeCodeSpinner.setAdapter(timeCodeAdapter);
		  timeCodeSpinner.refreshDrawableState();
	  }
	  
	  public void onNothingSelected(AdapterView<?> parent) {
		  
	  }
}
