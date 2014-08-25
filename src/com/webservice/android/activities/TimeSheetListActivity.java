package com.webservice.android.activities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import com.webservice.android.common.QuantarcPopupDialog;
import com.webservice.android.task.WebDownloadTask;
import com.webservice.android.task.WebTimesheetUploadTask;
import com.webservice.android.util.DateComparator;
import com.webservice.android.util.DateUtil;
import com.webservice.android.util.UploadSpinnerOnItemSelected;
import com.webservice.domain.LabourTimeList;
import com.webservice.domain.StatusList;
import com.webservice.domain.Timesheet;
import com.webservice.domain.TimesheetList;

public class TimeSheetListActivity extends DialogActivity {
	public static int TIMELIST_RESULTCODE = 3000;
	ArrayAdapter<String> uploadAdapter;
	String[] days = { "Mon", "Tues", "Weds", "Thurs", "Fri", "Sat", "Sun" };
	String[] weekList;
	EditText username, password, dob, st;
	PopupWindow popupWindow;
	TextView error;
	TextView contracted;
	TextView recorded;
	TextView contractedTitle;
	TextView recordedTitle;
	TableRow tableRow;
	Spinner uploadSpinner;
	String response;
	String urlString;
	String dataString;
	String selectedDate;
	String action;
	HashMap< String, Object> savedValues;
	LabourTimeList labourTimeList;
	List<String> dateChoice;
	Map<String, String[]> dateMap;
	Intent intent;
	TableLayout tableLayout;
	TimesheetList timesheetList;
	StatusList statusList;
	UploadSpinnerOnItemSelected uploadSpinnerOnItemSelected;
	ProgressDialog pDialog;
	WebDownloadTask webDownloadTask;
	WebTimesheetUploadTask webTimesheetUploadTask;
	QuantarcPopupDialog popup;
	Button saveButton;
	Button checkButton;
	Button viewButton;
	Button chooseButton;
	Button okButton;
	Button previous;
	QuantarcPopupDialog popupDialog;
	int count;
	int peopleListSize;
	int dateSelection;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.time_sheet);
		savedValues = (HashMap<String, Object>)this.getLastNonConfigurationInstance();
		uploadSpinnerOnItemSelected = new UploadSpinnerOnItemSelected();
		dateChoice = new ArrayList<String>();
		dateSelection = 1;
		popup = new QuantarcPopupDialog(this);
		Bundle bundle = getIntent().getExtras();
		labourTimeList = bundle
				.getParcelable("com.webservice.domain.LabourTimeList");
	
		tableRow = (TableRow) findViewById(R.id.ts80);
		contractedTitle = (TextView) findViewById(R.id.ts160);
		recordedTitle = (TextView) findViewById(R.id.ts170);
		uploadSpinner = (Spinner) findViewById(R.id.ts40);
		tableLayout = (TableLayout) findViewById(R.id.ts70);
		okButton = (Button) findViewById(R.id.ts14);
		okButton.setOnClickListener(this);
		contractedTitle.setTypeface(Typeface.DEFAULT_BOLD);
		recordedTitle.setTypeface(Typeface.DEFAULT_BOLD);
		contracted = (TextView) findViewById(R.id.ts180);
		recorded = (TextView) findViewById(R.id.ts190);
		urlString = this.getServiceUrl();
		popupDialog = new QuantarcPopupDialog(this);
		webDownloadTask = new WebDownloadTask();
		webDownloadTask.setBaseActivity(this);
		uploadAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, dateChoice);
		uploadAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		uploadSpinner.setAdapter(uploadAdapter);
		uploadSpinner.setOnItemSelectedListener(uploadSpinnerOnItemSelected);
		if (savedValues==null) {
			timesheetList = bundle.getParcelable("com.webservice.domain.TimesheetList");
		} else {
			timesheetList = (TimesheetList)savedValues.get("timesheet");
			if (timesheetList==null) {
				timesheetList = bundle.getParcelable("com.webservice.domain.TimesheetList");
			}
		}
		populateTimesheetTable(timesheetList);
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		String dateEntry;
		String strDate = "";
		StringTokenizer strTokenizer;
		action = "rowpressed";
		int id = v.getId();
		if ((okButton != null) && (okButton.getId() == id)) {
			intent = new Intent();
			intent.putExtra("com.webservice.domain.TimesheetList",
					timesheetList);
			setResult(9500, intent);
			this.finish();
		} else {
			dateEntry = weekList[id];
			if ((dateEntry != null) && (dateEntry.length() > 0)) {
				strTokenizer = new StringTokenizer(dateEntry, ":");
				strDate = strTokenizer.nextToken();
				intent = new Intent(this, UpdateTimeActivity.class);
				intent.putExtra("com.webservice.domain.TimesheetList",
						timesheetList);
				intent.putExtra("com.webservice.domain.LabourTimeList",
						labourTimeList);
				intent.putExtra("dateentry", strDate);
				this.startActivityForResult(intent, TIMELIST_RESULTCODE);
			}
		}
	}

	public void showSettingsMenu() {
		int POPUP_HEIGHT = 200;
		int POPUP_WIDTH = 200;
		int BUTTON_WIDTH = 150;
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View popupView = inflater.inflate(R.layout.popup_settings, null, false);
		popupView.setBackgroundColor(android.R.color.darker_gray);
		popupWindow = new PopupWindow(popupView, 100, 100, true);
		popupWindow.setHeight(POPUP_HEIGHT);
		popupWindow.setWidth(POPUP_WIDTH);
		checkButton = (Button) popupView.findViewById(R.id.Button02);
		checkButton.setWidth(BUTTON_WIDTH);
		viewButton = (Button) popupView.findViewById(R.id.Button03);
		viewButton.setWidth(BUTTON_WIDTH);
		checkButton.setOnClickListener(this);
		viewButton.setOnClickListener(this);
		// The code below assumes that the root container has an id called
		// 'main'
		popupWindow.showAtLocation(this.findViewById(R.id.jl5), Gravity.CENTER,
				0, 0);
	}

	public void addRowsToTable(String strDate, Map<String, String[]> dateMap) {
		weekList = dateMap.get(strDate);
		StringTokenizer strTokenizer = null;
		String row, date, hours, used;
		for (int i = 0; i < weekList.length; i++) {
			row = weekList[i];
			strTokenizer = new StringTokenizer(row, ":");
			date = strTokenizer.nextToken();
			hours = strTokenizer.nextToken();
			used = strTokenizer.nextToken();
			this.addRows(i, days[i], date, hours, used);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			setResult(1080, null);
			this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
    public Object onRetainNonConfigurationInstance() {
        HashMap<String, Object> savedValues = new HashMap<String, Object>();
        savedValues.put("timesheet", timesheetList);
        return savedValues;
    }

	private void addRows(int id, String day, String date, String hours,
			String symbol) {
		TableRow tableRow;
		TextView textView;
		// int TEXT_HEIGHT = 28;
		int TEXT_HEIGHT = 20; // Anna
		int index = 0;
		tableRow = new TableRow(this);
		tableRow.setId(Integer.valueOf(id));
		tableRow.setOnClickListener(this);
		tableRow.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		/* Create a TextView to be the row-content. */
		textView = new TextView(this);
		textView.setText(day);
		textView.setTextSize(TEXT_HEIGHT);
		textView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		/* Add Button to row. */
		tableRow.addView(textView);

		/* Create a TextView to be the row-content. */
		textView = new TextView(this);
		textView.setText(date);
		textView.setWidth(150);
		textView.setTextSize(TEXT_HEIGHT);
		textView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		/* Add Button to row. */
		tableRow.addView(textView);

		/* Create a TextView to be the row-content. */
		textView = new TextView(this);
		textView.setText(hours);
		textView.setWidth(100);
		textView.setTextSize(TEXT_HEIGHT);
		textView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		/* Add Button to row. */
		tableRow.addView(textView);

		/* Create a TextView to be the row-content. */
		textView = new TextView(this);
		textView.setText(symbol);
		textView.setTextSize(TEXT_HEIGHT);
		textView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		/* Add Button to row. */
		tableRow.addView(textView);

		/* Add row to TableLayout. */
		tableLayout.addView(tableRow, new TableLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		switch (which) {
		case DialogInterface.BUTTON_POSITIVE: // Yes button clicked
			break;
		case DialogInterface.BUTTON_NEGATIVE: // No button clicked
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent returnIntent) {
		super.onActivityResult(requestCode, resultCode, returnIntent);
		String selectedDate = "";
		Bundle bundle = null;
		if (resultCode != 1080) {
			bundle = returnIntent.getExtras();
			timesheetList = bundle
					.getParcelable("com.webservice.domain.TimesheetList");
		}
		tableLayout.removeAllViewsInLayout();
		tableLayout.removeAllViews();
		initialiseHours(dateMap);
		buildTableList(dateMap, timesheetList);
		selectedDate = uploadSpinnerOnItemSelected.getDateStr();
		addRowsToTable(selectedDate, dateMap);

	}

	private String formatDate(Date requiredDate) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String strDate = null;
		strDate = dateFormat.format(requiredDate);
		return strDate;
	}

	private void populateTimesheetTable(TimesheetList timesheetList) {
		String dateStr = "";
		int selectedIndex = 0;
		this.initialiseDateList(timesheetList);
		this.buildTableList(dateMap, timesheetList);
		if (uploadAdapter.getCount() > 0) {
			dateStr = uploadAdapter.getItem(0);
			selectedIndex = getDefaultSelectedIndex(uploadAdapter);
			if (selectedIndex > -1) {
				dateStr = uploadAdapter.getItem(selectedIndex);
				selectedDate = uploadAdapter.getItem(selectedIndex);
				uploadSpinner.setSelection(selectedIndex);
			} else {
				dateStr = uploadAdapter.getItem(0);
				selectedDate = uploadAdapter.getItem(0);
			}
			addRowsToTable(dateStr, dateMap);
			uploadSpinnerOnItemSelected.setDateStr(dateStr);
			uploadSpinnerOnItemSelected.setDateMap(dateMap);
			uploadSpinnerOnItemSelected.setTableLayout(tableLayout);
			uploadSpinnerOnItemSelected.setTimesheetActivity(this);
			contracted.setText(getContext().getUsername());
			recorded.setText(super.getCurrentDateTime());
		}
	}

	private Date formatToDate(String requiredDate) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date someDate = null;
		try {
			someDate = dateFormat.parse(requiredDate);
		} catch (ParseException parse) {
			someDate = null;
		}
		return someDate;
	}
	
	private String formatFromStr(String requiredDate) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date someDate = null;
		String strDate="";
		try {
			someDate = dateFormat.parse(requiredDate);
			strDate = dateFormat.format(someDate);
		} catch (ParseException parse) {
			strDate = null;
		}
		return strDate;
	}

	private int getDayNumber(String dateString) {
		int dayNumber = -1;
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date timesheetDate = null;
		Calendar cal = Calendar.getInstance();
		try {
			timesheetDate = dateFormat.parse(dateString);
			cal.setTime(timesheetDate);
			dayNumber = cal.get(Calendar.DAY_OF_WEEK);
			if (dayNumber == 1) {
				dayNumber = 7;
			} else if (dayNumber > 1) {
				dayNumber = dayNumber - 1;
			}
		} catch (ParseException parseException) {
			dayNumber = -1;
		}
		return dayNumber;
	}

	private String getTotalHours(String startTime, String endTime,
			String currentTime) {
		int pos = 0;
		long diffInSeconds = 0;
		long currentHours = 0;
		long currentMinutes = 0;
		long startHours = 0;
		long startMinutes = 0;
		long endHours = 0;
		long endMinutes = 0;
		String timeDifference = "00:00";
		pos = startTime.indexOf(":");
		try {
			if (pos > 0) {
				startHours = Integer.valueOf(startTime.substring(0, pos));
				startMinutes = Integer.valueOf(startTime.substring(pos + 1,
						startTime.length()));
			}
			pos = endTime.indexOf(":");
			if (pos > 0) {
				endHours = Integer.valueOf(endTime.substring(0, pos));
				endMinutes = Integer.valueOf(endTime.substring(pos + 1,
						endTime.length()));
			}
			if (currentTime.equalsIgnoreCase("0")) {
				currentHours = 0;
				currentMinutes = 0;
			} else {
				pos = currentTime.indexOf(".");
				if (pos > 0) {
					currentHours = Integer.valueOf(currentTime
							.substring(0, pos));
					currentMinutes = Integer.valueOf(currentTime.substring(
							pos + 1, currentTime.length()));
				}

			}
			diffInSeconds = ((endHours * 3600) + (endMinutes * 60))
					- ((startHours * 3600) + (startMinutes * 60));
			long timeInSeconds = diffInSeconds + currentHours * 3600
					+ currentMinutes * 60;
			long hours = timeInSeconds / 3600;
			timeInSeconds = timeInSeconds - (hours * 3600);
			long minutes = timeInSeconds / 60;
			timeInSeconds = timeInSeconds - (minutes * 60);
			timeDifference = (hours < 10 ? "0" + hours : hours) + "."
					+ (minutes < 10 ? "0" + minutes : minutes);
		} catch (NumberFormatException numberException) {
			timeDifference = "";
		}
		return timeDifference;
	}

	private void buildTableList(Map<String, String[]> dateMap,
			TimesheetList timesheetList) {
		String dateEntry = "";
		String strHours;
		String strDate;
		String actionDate;
		String strHrs;
		String mondayStr;
		String formattedHours = "";
		String[] dateList;
		StringTokenizer strTokenizer;
		int dayNumber;
		if ( timesheetList != null) {
		  for (Timesheet timesheet : timesheetList.getAllTimesheets()) {
			actionDate = timesheet.getActionDate();
			actionDate = formatFromStr(actionDate);
			//Log.v("Quantarc","BuildTableList:"+timesheet.getJobNum()+":"+actionDate);
			if (actionDate.length() == 10) {
				mondayStr = getMondayStr(actionDate);
				dayNumber = getDayNumber(actionDate);
				//Log.v("Quantarc","BuildTableList:"+mondayStr+":"+dayNumber);
				if (dayNumber > 0) {
					dateList = dateMap.get(mondayStr);
					if (dateList != null) {
						dateEntry = dateList[dayNumber - 1];
						strTokenizer = new StringTokenizer(dateEntry, ":");
						strDate = strTokenizer.nextToken();
						strHours = strTokenizer.nextToken();
						formattedHours = getTotalHours(
								timesheet.getStartTime(),
								timesheet.getFinishTime(), strHours);
						if (formattedHours.length() > 0) {
							dateList[dayNumber - 1] = strDate + ":"
									+ formattedHours + ": ";
						}
					}
				}
			}
		}
	  }
	}

	private void initialiseHours(Map<String, String[]> dateMap) {
		String dateEntry = "";
		String strDate;
		String mondayStr;
		String[] dateList;
		StringTokenizer strTokenizer;
		int dayNumber;
		Iterator<String> iter = dateMap.keySet().iterator();
		while (iter.hasNext()) {
			mondayStr = iter.next();
			dateList = dateMap.get(mondayStr);
			dayNumber = 1;
			while (dayNumber < 8) {
				dateEntry = dateList[dayNumber - 1];
				strTokenizer = new StringTokenizer(dateEntry, ":");
				strDate = strTokenizer.nextToken();
				dateList[dayNumber - 1] = strDate + ":0:?";
				dayNumber = dayNumber + 1;
			}
		}
	}

	private void initialiseDateList(TimesheetList timesheetList) {
		dateMap = new HashMap<String, String[]>();
		addWeeks(dateMap);
		Iterator<String> iter = dateMap.keySet().iterator();
		while (iter.hasNext()) {
			uploadAdapter.add(iter.next());
		}
		uploadAdapter.notifyDataSetChanged();
		uploadAdapter.sort(new DateComparator());
	}

	private void addWeeks(Map<String, String[]> dateMap) {
		Calendar calendar = Calendar.getInstance();
		Calendar someCalendar = Calendar.getInstance();
		String dateStr = "";
		String mondayStr;
		String numberOfWeeks = getContext().getNumberOfWeeks().trim();
		Date dayDate = calendar.getTime();
		Date someDate;
		int weeks = Integer.valueOf(numberOfWeeks);
		someCalendar.setTime(dayDate);
		for (int j = 0; j < weeks; j++) {
			dateStr = DateUtil.formatDate(dayDate, "dd/MM/yyyy");
			mondayStr = getMondayStr(dateStr);
			someDate = formatToDate(mondayStr);
			weekList = new String[7];
			for (int i = 0; i < 7; i++) {
				calendar.setTime(someDate);
				calendar.add(Calendar.DATE, i);
				dateStr = formatDate(calendar.getTime());
				dateStr = dateStr + ":0:?";
				weekList[i] = dateStr;
			}
			dateMap.put(mondayStr, weekList);
			someCalendar.add(Calendar.DATE, -7);
			dayDate = someCalendar.getTime();
		}
	}

	private int getDefaultSelectedIndex(ArrayAdapter<String> uploadAdapter) {
		Date todaysDate = Calendar.getInstance().getTime();
		String strDate = formatDate(todaysDate);
		int defaultIndex = -1;
		String storedDate = "";
		String mondaysDate = getMondayStr(strDate);
		int index = 0;
		boolean found = false;
		while ((!found) && (index < uploadAdapter.getCount())) {
			storedDate = uploadAdapter.getItem(index);
			if (storedDate.equalsIgnoreCase(mondaysDate)) {
				found = true;
				defaultIndex = index;
			}
			index = index + 1;
		}
		return defaultIndex;
	}

	private String getMondayStr(String dateStr) {
		String mondayStr = "";
		Date someDate;
		Date mondaysDate;
		Calendar calendar = Calendar.getInstance();
		int mondayDay = 1;
		int someDay = getDayNumber(dateStr);
		if (someDay != mondayDay) {
			someDate = formatToDate(dateStr);
			calendar.setTime(someDate);
			calendar.add(Calendar.DATE, -(someDay - 1));
			mondaysDate = calendar.getTime();
			mondayStr = formatDate(mondaysDate);
		} else {
			mondayStr = dateStr;
		}
		return mondayStr;
	}

}