package com.webservice.android.listeners;
import java.util.List;

import com.webservice.android.activities.JobHistoryActivity;
import com.webservice.domain.JobHistory;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;

public class TimeSpentItemSelectedListener implements OnItemSelectedListener {

	  private JobHistoryActivity jobHistoryActivity;
	  private JobHistory jobHistory;
	  private ArrayAdapter<String> timeCodeAdapter;
	  private List<String> timeCodeList;
	  
	  public TimeSpentItemSelectedListener(JobHistoryActivity jobHistoryActivity, JobHistory jobHistory) {
		  this.jobHistory = jobHistory;
		  this.jobHistoryActivity = jobHistoryActivity;
	  }
	
	  public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		  String value = (String)parent.getItemAtPosition(pos);
          Spinner timeCodeSpinner;
		  jobHistory.setTimeSpent(value);
		  timeCodeList = jobHistoryActivity.getLabourCodeList(value);
		  timeCodeSpinner = jobHistoryActivity.getTimeCodeSpinner();
		  timeCodeAdapter = new ArrayAdapter<String>(jobHistoryActivity,
					android.R.layout.simple_spinner_item, timeCodeList);
		  timeCodeAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);	// Anna 2012-07-03 converted to radio button drop down list
		  timeCodeSpinner.setAdapter(timeCodeAdapter);
		  timeCodeSpinner.refreshDrawableState();
	  }
	  
	  public void onNothingSelected(AdapterView<?> parent) {
		  
	  }
}
