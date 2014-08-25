package com.webservice.android.activities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import com.webservice.android.util.MinutesComparator;
import com.webservice.android.xml.SupervisorListTranslator;
import com.webservice.domain.Job;
import com.webservice.domain.LabourTimeList;
import com.webservice.domain.Timesheet;
import com.webservice.domain.TimesheetList;

public class UpdateTimeActivity extends BaseActivity implements OnClickListener {
	private static int UPDATE_TIME_CODE = 1020;
	private static String NO_JOB="Not Job Related";
	ArrayAdapter<String> adapter;
	ArrayList<String> jobRefList;
	EditText selectedDate;
	Intent enterTimeIntent;
	LabourTimeList labourTimeList;
	Spinner spinner;
	SupervisorListTranslator translator;
	String schedule;
	String disableJobs;
	String lastFinishTime;
	TableLayout tableLayout;
	TextView person;
	TimesheetList timesheetList;
	Timesheet timesheet;
	String response;
	String strDate;
	Button submitButton;
	Button deleteButton;
	Button cancelButton;
	Button addTimeButton;
	Button okButton;
	Job selectedJob;
	List<String> supervisors;
	int count;
	int peopleListSize;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_time);
		Bundle bundle = getIntent().getExtras();
		timesheetList = bundle.getParcelable("com.webservice.domain.TimesheetList");
		labourTimeList = bundle.getParcelable("com.webservice.domain.LabourTimeList");
		strDate = bundle.getCharSequence("dateentry").toString();
		okButton = (Button) findViewById(R.id.ut40);
		okButton.setOnClickListener(this);
		addTimeButton = (Button) findViewById(R.id.ut90);
		disableJobs = super.getContext().getDisableJobsOnTimesheet();
		addTimeButton.setOnClickListener(this);
		selectedDate = (EditText) findViewById(R.id.ut70);
		tableLayout = (TableLayout) findViewById(R.id.ut110);
		selectedDate.setText(strDate);
		lastFinishTime="00:00";
		populateTable(timesheetList, strDate);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		int pos=0;
		String jobRefs="";
		String ref="";
         Intent intent=null;
		if (id == addTimeButton.getId()) {
			timesheet = new Timesheet();
			timesheet.setIsNew("true");
			timesheet.setActionDate(strDate);
			timesheet.setTimeCode("");
			timesheet.setWorkId(getNextTimesheetWorkId(strDate));
			timesheet.setStartTime(lastFinishTime);
			timesheet.setFinishTime("00:00");
			timesheet.setJobRef("");
			enterTimeIntent = new Intent(this, EnterTimeActivity.class);
			enterTimeIntent.putExtra("com.webservice.domain.TimesheetList",timesheetList);
			enterTimeIntent.putExtra("com.webservice.domain.LabourTimeList",labourTimeList);
			enterTimeIntent.putExtra("com.webservice.domain.Timesheet",timesheet);
			enterTimeIntent.putExtra("dateentry", strDate);
            //Log.v("Quantarc:","Job Ref List:"+jobRefList.size());
			for (int j=0;j<jobRefList.size();j++) {
				ref=jobRefList.get(j);
				//Log.v("Quantarc:","Job Ref:"+ref);
				//Log.v("Quantarc:","Job Refs:"+jobRefs);
				pos = jobRefs.indexOf(ref);
				if (pos==-1) {
				   jobRefs = jobRefs + ref +",";
				}
			}
			enterTimeIntent.putExtra("jobrefs",jobRefs);
			startActivityForResult(enterTimeIntent, UPDATE_TIME_CODE);
		} else if (id == okButton.getId()) {
			intent = new Intent();
			intent.putExtra("com.webservice.domain.TimesheetList", timesheetList);
			setResult(3000,intent);
			this.finish();
		} 
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK
	            && event.getRepeatCount() == 0) {
	    	setResult(1080,null);
	    	this.finish();
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent returnIntent) {
		super.onActivityResult(requestCode, resultCode, returnIntent);
		Bundle bundle=null;
		if (resultCode==2000) {
			bundle = returnIntent.getExtras();
    		timesheetList = bundle.getParcelable("com.webservice.domain.TimesheetList");
    		tableLayout.removeAllViewsInLayout();
  		    tableLayout.removeAllViews();
  		    populateTable(timesheetList, strDate);
		}
	}

	private void addRows(int id, String job, String start, String finish,
			String time) {
		TableRow tableRow;
		TextView textView;
		int TEXT_HEIGHT = 18; // Anna 2012-07-07 changed from 35
		tableRow = new TableRow(this);
		tableRow.setId(id);
		tableRow.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		/* Create a TextView to be the row-content. */
		job = job.trim();
		if (job.length()==0) {
			job=NO_JOB;
		}
		textView = new TextView(this);
		textView.setText(job);
		textView.setWidth(160);			// Anna 2012-07-07 changed from 320
		textView.setTextSize(TEXT_HEIGHT);
		textView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		/* Add Button to row. */
		tableRow.addView(textView);

		/* Create a TextView to be the row-content. */
		textView = new TextView(this);
		textView.setText(start);
		textView.setWidth(60);			// Anna 2012-07-07 changed from 100
		textView.setTextSize(TEXT_HEIGHT);
		textView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		/* Add Button to row. */
		tableRow.addView(textView);

		/* Create a TextView to be the row-content. */
		textView = new TextView(this);
		textView.setText(finish);
		textView.setWidth(60);		// Anna 2012-07-07 changed from 100
		textView.setTextSize(TEXT_HEIGHT);
		textView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		/* Add Button to row. */
		tableRow.addView(textView);

		/* Create a TextView to be the row-content. */
		textView = new TextView(this);
		textView.setText(time);
		textView.setTextSize(TEXT_HEIGHT);
		textView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		/* Add Button to row. */
		tableRow.addView(textView);

		/* Add row to TableLayout. */
		tableLayout.addView(tableRow, new TableLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
	}

	private String getNextTimesheetWorkId(String strDate) {
		int id=1;
		int temp=0;
		for (Timesheet timesheet:timesheetList.getAllTimesheets()) {
			if (super.compareDates(strDate, timesheet.getActionDate())==0) {
				temp = Integer.valueOf(timesheet.getWorkId());
				if (temp>id) {
					id = temp;
				}
			}
			
		}
	   return String.valueOf(id+1);
	}
	
	private void populateTable(TimesheetList timesheetList, String strDate) {
		int index=0;
		jobRefList = new ArrayList<String>();
		List<Timesheet> tableSheets = new ArrayList<Timesheet>();
		String jobRef;
		for (Timesheet timesheet : timesheetList.getAllTimesheets()) {
			index = index + 1;
			jobRef = timesheet.getJobRef().trim();
			if ((jobRef.length()>0) && (!jobRefList.contains(jobRef))) {
			    jobRefList.add(jobRef);
			}
			if ((timesheet.getActionDate()!=null) && (compareDates(timesheet.getActionDate(),strDate))==0) {
				tableSheets.add(timesheet);
			}
		}
		if (tableSheets.size()>0) {
		  Collections.sort(tableSheets, new MinutesComparator());
		  for (Timesheet timesheet : tableSheets) {
				addRows(index, timesheet.getJobRef(), timesheet.getStartTime(), timesheet.getFinishTime(), timesheet.getTimeCode());
				if (timesheet.getFinishTime().length()>4) {
				   lastFinishTime = timesheet.getFinishTime();
				}
		  }
		}
	}

}