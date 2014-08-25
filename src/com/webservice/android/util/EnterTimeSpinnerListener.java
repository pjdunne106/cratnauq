package com.webservice.android.util;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.webservice.domain.Timesheet;

public class EnterTimeSpinnerListener implements OnItemSelectedListener {

	  private Timesheet timesheet;
	  private String field;
	  
	  public EnterTimeSpinnerListener(Timesheet timesheet, String field) {
		  this.timesheet = timesheet;
		  this.field = field;
	  }
	
	  public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		  String value = (String)parent.getItemAtPosition(pos);
		  String startTime;
		  String finishTime;
		  String timeCode;
		  String job;
		  int loc=0;
		  
          if (field.equalsIgnoreCase("starthour")) {
			  startTime = timesheet.getStartTime();
			  startTime = value + startTime.substring(2, startTime.length());
			  timesheet.setStartTime(startTime);
		  }
		  else if (field.equalsIgnoreCase("startminute")) {
			  startTime = timesheet.getStartTime();
			  startTime = startTime.substring(0,3)+value;
			  timesheet.setStartTime(startTime);
		  }
		  else if (field.equalsIgnoreCase("finishhour")) {
			  finishTime = timesheet.getFinishTime();
			  finishTime = value + finishTime.substring(2, finishTime.length());
			  timesheet.setFinishTime(finishTime);
		  }
		  else if (field.equalsIgnoreCase("finishminute")) {
			  finishTime = timesheet.getFinishTime();
			  finishTime = finishTime.substring(0,3)+value;
			  timesheet.setFinishTime(finishTime);
		  }
		  else if (field.equalsIgnoreCase("timecode")) {
			  loc = value.indexOf("-");
			  if (loc>0) {
				  value = value.substring(0,loc);
			  }
			  timeCode = value;
			  timesheet.setTimeCode(timeCode);
		  }
		  else if (field.equalsIgnoreCase("jobref")) {
			  job = value;
			  timesheet.setJobRef(job);
		  }
		  
	  }
	  
	  public void onNothingSelected(AdapterView<?> parent) {
		  
	  }
	  
	  public Timesheet getTimesheet() {
		  return timesheet;
	  }
}
