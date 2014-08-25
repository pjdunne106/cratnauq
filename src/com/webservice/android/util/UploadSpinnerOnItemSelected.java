package com.webservice.android.util;

import java.util.Map;
import java.util.StringTokenizer;

import com.webservice.android.activities.TimeSheetListActivity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TableLayout;

public class UploadSpinnerOnItemSelected implements OnItemSelectedListener {

	  private Map<String,String[]> dateMap;
	  private TimeSheetListActivity timesheetActivity;
	  private TableLayout tableLayout;
	  private String dateStr="";
	  
	  public UploadSpinnerOnItemSelected() {
	  }
	
	  public void setDateMap(Map<String, String[]> dateMap) {
		  this.dateMap = dateMap;
	  }
	  
      public void setDateStr(String dateStr) {
    	  this.dateStr = dateStr;
      }
      
      public String getDateStr() {
    	  return dateStr;
      }
	  
	  public void setTableLayout(TableLayout tableLayout) {
		  this.tableLayout = tableLayout;
	  }

	  
	  public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		  String value = (String)parent.getItemAtPosition(pos);
		  if (!value.equalsIgnoreCase(dateStr)) {
			  dateStr = value;
		      this.alterTable(value);
		  }
	  }
	  
	  public void onNothingSelected(AdapterView<?> parent) {
		  
	  }
	  
	  private void alterTable(String dateStr) {
		  tableLayout.removeAllViewsInLayout();
		  tableLayout.removeAllViews();
		  timesheetActivity.addRowsToTable(dateStr, dateMap);
	  }

	public void setTimesheetActivity(TimeSheetListActivity timesheetActivity) {
		this.timesheetActivity = timesheetActivity;
	}

	public TimeSheetListActivity getTimesheetActivity() {
		return timesheetActivity;
	}
	  
}
