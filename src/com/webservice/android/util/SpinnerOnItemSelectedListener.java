package com.webservice.android.util;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;

import com.webservice.domain.JobHistory;
import com.webservice.domain.JobList;

public class SpinnerOnItemSelectedListener implements OnItemSelectedListener {

	  private JobHistory jobHistory;
	  private JobList jobList;
      private String field;
      private EditText detailsText;
	  
	  public SpinnerOnItemSelectedListener(JobHistory jobHistory,String field, JobList jobList) {
		  this.jobHistory = jobHistory;
		  this.field=field;
		  this.jobList = jobList;
	  }
	
	  public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		  String value = (String)parent.getItemAtPosition(pos);
		  String startTime;
		  String finishTime;
		  String timeCode;
		  String status;
		  int location;
		    if (field.equalsIgnoreCase("fromHour")) {
			  startTime = jobHistory.getStartTime(); 
			  startTime =  value + startTime.substring(2, startTime.length());
			  jobHistory.setStartTime(startTime);
		    } else if (field.equalsIgnoreCase("fromMinute")) {
			  startTime = jobHistory.getStartTime();
			  startTime = startTime.substring(0, 3) + value + startTime.substring(5, startTime.length());
			  jobHistory.setStartTime(startTime);
		    } else if (field.equalsIgnoreCase("toHour")) {
			  finishTime = jobHistory.getFinishTime();
			  finishTime =  value + finishTime.substring(2, finishTime.length());
			  jobHistory.setFinishTime(finishTime);
			  jobList.setLastStartTime(finishTime);
		    } else if (field.equalsIgnoreCase("timeCode")) {
		    	location = value.indexOf("-");
		    	if (location>0) {
		    		value = value.substring(0, location);
		    	}
			  timeCode = value;
			  jobHistory.setTimeCode(timeCode);
		    } else if (field.equalsIgnoreCase("status")) {
			  status = value;
			  Log.v("JobHistory","History Status:"+status);
			  jobHistory.setJobStatus(status);
		    } else if (field.equalsIgnoreCase("toMinute")) {
			  finishTime = jobHistory.getFinishTime();
			  finishTime = finishTime.substring(0,3)+value + finishTime.substring(5, finishTime.length());;
			  jobHistory.setFinishTime(finishTime);
			  jobList.setLastStartTime(finishTime);
		    } else if (field.equalsIgnoreCase("person")) {
				  jobHistory.setLabourPersonId(value);
			} else if (field.equalsIgnoreCase("histories")) {
				  detailsText.setText(value);
		    }
          }
	  
	  public void setDetailsText(EditText detailsText) {
		  this.detailsText = detailsText;
	  }
	  
	  public void onNothingSelected(AdapterView<?> parent) {
		  
	  }
}
