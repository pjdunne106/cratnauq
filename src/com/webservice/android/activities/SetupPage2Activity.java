package com.webservice.android.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import com.webservice.android.listeners.OrderSettingSpinnerListener;
import com.webservice.android.listeners.VisibleSettingSpinnerListener;
import com.webservice.domain.ReferenceDataList;

public class SetupPage2Activity extends Activity implements OnClickListener, OnCheckedChangeListener {
	ArrayAdapter<CharSequence> visibleAdapter;
	ArrayAdapter<CharSequence> positionAdapter;
	CheckBox loginUser, disableTimes, displayJobs, disableJobs, issuingStock;
	EditText username, password, dob, st;
	TextView error;
	OrderSettingSpinnerListener orderListener;
	String response;
	Spinner columnVisibleSpinner;
	Spinner columnPositionSpinner;
	TableLayout tableLayout;
	Button saveButton;
	Button previous;
	int count;
	int rowId;
	int peopleListSize;
	ReferenceDataList referenceDataList;
	VisibleSettingSpinnerListener visibleListener;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setuppage2);
		rowId = 0;
		loginUser = (CheckBox) findViewById(R.id.spa210);
		loginUser.setOnCheckedChangeListener(this);
		disableTimes = (CheckBox) findViewById(R.id.spa215);
		disableTimes.setOnCheckedChangeListener(this);
		displayJobs = (CheckBox) findViewById(R.id.spa220);
		displayJobs.setOnCheckedChangeListener(this);
		issuingStock = (CheckBox) findViewById(R.id.spa230);
		issuingStock.setOnCheckedChangeListener(this);
		disableJobs = (CheckBox) findViewById(R.id.spa232);
		disableJobs.setOnCheckedChangeListener(this);
		tableLayout = (TableLayout) findViewById(R.id.spa250);
		
		Bundle bundle = getIntent().getExtras();
		referenceDataList = bundle
				.getParcelable("com.webservice.domain.ReferenceDataList");
		populateScreen(referenceDataList);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, EnterTimeActivity.class);
		startActivity(intent);
	}
	
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		String clicked="0";
		if (buttonView.isChecked()) {
			clicked="1";
		}
		if (buttonView.getId()==loginUser.getId()) {
			referenceDataList.put("loginstaff",clicked);	
		} else if (buttonView.getId()==disableTimes.getId()) {
			referenceDataList.put("disabletimes",clicked);		
		} else if (buttonView.getId()==displayJobs.getId()) {
			referenceDataList.put("displayjobs",clicked);			
		} else if (buttonView.getId()==issuingStock.getId()) {
			referenceDataList.put("allowhistories",clicked);
		} else if (buttonView.getId()==disableJobs.getId()) {
			referenceDataList.put("disablejobnumber",clicked);
		}
	}

	private void populateScreen(ReferenceDataList referenceDataList) {
		String temp = referenceDataList.getStatus("loginstaff");
		if ((temp != null) && (temp.equalsIgnoreCase("1"))) {
			loginUser.setChecked(true);
		}
		temp = referenceDataList.getStatus("displayjobs");
		if ((temp != null) && (temp.equalsIgnoreCase("1"))) {
			displayJobs.setChecked(true);
		}
		temp = referenceDataList.getStatus("disabletimes");
		if ((temp != null) && (temp.equalsIgnoreCase("1"))) {
			disableTimes.setChecked(true);
		}
		temp = referenceDataList.getStatus("allowhistories");
		if ((temp != null) && (temp.equalsIgnoreCase("1"))) {
			issuingStock.setChecked(true);
		}
		temp = referenceDataList.getStatus("disablejobnumber");
		if ((temp != null) && (temp.equalsIgnoreCase("1"))) {
			disableJobs.setChecked(true);
		}
		populateTableFields(referenceDataList);
	}
	
	private void populateTableFields(ReferenceDataList referenceDataList) {
		String temp = referenceDataList.getStatus("risk");
		//Log.v("Quantarc","PopulateTableFields, risk:"+temp);
		
	    columnVisibleSpinner = new Spinner(this);
		visibleAdapter = ArrayAdapter.createFromResource(            
				this, R.array.VisibleOptions, android.R.layout.simple_spinner_item);    
	    visibleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    positionAdapter = ArrayAdapter.createFromResource(            
				this, R.array.PositionOptions, android.R.layout.simple_spinner_item);    
	    positionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);    
	    columnVisibleSpinner.setAdapter(visibleAdapter);
	    columnVisibleSpinner.setSelection(getVisibleValue(temp));
	    columnVisibleSpinner.setEnabled(false);
	    columnPositionSpinner = new Spinner(this);
		   columnPositionSpinner.setAdapter(positionAdapter);
	    columnPositionSpinner.setSelection(getOrderValue(temp));
	    visibleListener = new VisibleSettingSpinnerListener(this,1,columnPositionSpinner,referenceDataList,"risk");
	    orderListener = new OrderSettingSpinnerListener(this,1,columnVisibleSpinner,referenceDataList,"risk");
		columnPositionSpinner.setOnItemSelectedListener(orderListener);
		columnVisibleSpinner.setOnItemSelectedListener(visibleListener);
	    addRow("Risk", temp, columnVisibleSpinner, columnPositionSpinner);
	    
		temp = referenceDataList.getStatus("jobnum");
		columnVisibleSpinner = new Spinner(this);
		visibleAdapter = ArrayAdapter.createFromResource(            
				this, R.array.VisibleOptions, android.R.layout.simple_spinner_item);    
	    visibleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    positionAdapter = ArrayAdapter.createFromResource(            
				this, R.array.PositionOptions, android.R.layout.simple_spinner_item);    
	    positionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);    
		columnVisibleSpinner.setAdapter(visibleAdapter);
		columnVisibleSpinner.setSelection(getVisibleValue(temp));
		columnVisibleSpinner.setEnabled(false);
		columnPositionSpinner = new Spinner(this);
		columnPositionSpinner.setAdapter(positionAdapter);
		columnPositionSpinner.setSelection(getOrderValue(temp));
		visibleListener = new VisibleSettingSpinnerListener(this,2,columnPositionSpinner,referenceDataList,"jobnum");
	    orderListener = new OrderSettingSpinnerListener(this,2,columnVisibleSpinner,referenceDataList,"jobnum");
		columnPositionSpinner.setOnItemSelectedListener(orderListener);
		columnVisibleSpinner.setOnItemSelectedListener(visibleListener);
		addRow("Job Num", temp, columnVisibleSpinner, columnPositionSpinner);
		
		temp = referenceDataList.getStatus("source");
		columnVisibleSpinner = new Spinner(this);
		visibleAdapter = ArrayAdapter.createFromResource(            
				this, R.array.VisibleOptions, android.R.layout.simple_spinner_item);    
	    visibleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    positionAdapter = ArrayAdapter.createFromResource(            
				this, R.array.PositionOptions, android.R.layout.simple_spinner_item);    
	    positionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);    
		columnVisibleSpinner.setAdapter(visibleAdapter);
		columnVisibleSpinner.setSelection(getVisibleValue(temp));
		columnVisibleSpinner.setEnabled(false);
		columnPositionSpinner = new Spinner(this);
		columnPositionSpinner.setAdapter(positionAdapter);
		columnPositionSpinner.setSelection(getOrderValue(temp));
		visibleListener = new VisibleSettingSpinnerListener(this,3,columnPositionSpinner,referenceDataList,"source");
	    orderListener = new OrderSettingSpinnerListener(this,3,columnVisibleSpinner,referenceDataList,"source");
		columnPositionSpinner.setOnItemSelectedListener(orderListener);
		columnVisibleSpinner.setOnItemSelectedListener(visibleListener);
		addRow("Source", temp,columnVisibleSpinner, columnPositionSpinner);
		
		temp = referenceDataList.getStatus("status");
		columnVisibleSpinner = new Spinner(this);
		visibleAdapter = ArrayAdapter.createFromResource(            
				this, R.array.VisibleOptions, android.R.layout.simple_spinner_item);    
	    visibleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    positionAdapter = ArrayAdapter.createFromResource(            
				this, R.array.PositionOptions, android.R.layout.simple_spinner_item);    
	    positionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);    
		columnVisibleSpinner.setAdapter(visibleAdapter);
		columnVisibleSpinner.setSelection(getVisibleValue(temp));
		columnVisibleSpinner.setEnabled(false);
		columnPositionSpinner = new Spinner(this);
		columnPositionSpinner.setAdapter(positionAdapter);
		visibleListener = new VisibleSettingSpinnerListener(this,4,columnPositionSpinner,referenceDataList,"status");
	    orderListener = new OrderSettingSpinnerListener(this,4,columnVisibleSpinner,referenceDataList,"status");
		columnPositionSpinner.setOnItemSelectedListener(orderListener);
		columnVisibleSpinner.setOnItemSelectedListener(visibleListener);
		columnPositionSpinner.setSelection(getOrderValue(temp));
		addRow("Status", temp, columnVisibleSpinner, columnPositionSpinner);
		
		temp = referenceDataList.getStatus("target");
		columnVisibleSpinner = new Spinner(this);
		visibleAdapter = ArrayAdapter.createFromResource(            
				this, R.array.VisibleOptions, android.R.layout.simple_spinner_item);    
	    visibleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    positionAdapter = ArrayAdapter.createFromResource(            
				this, R.array.PositionOptions, android.R.layout.simple_spinner_item);    
	    positionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);    
		columnVisibleSpinner.setAdapter(visibleAdapter);
		columnVisibleSpinner.setSelection(getVisibleValue(temp));
		columnVisibleSpinner.setEnabled(false);
		columnPositionSpinner = new Spinner(this);
		columnPositionSpinner.setAdapter(positionAdapter);
		visibleListener = new VisibleSettingSpinnerListener(this,5,columnPositionSpinner,referenceDataList,"target");
	    orderListener = new OrderSettingSpinnerListener(this,5,columnVisibleSpinner,referenceDataList,"target");
		columnPositionSpinner.setOnItemSelectedListener(orderListener);
		columnVisibleSpinner.setOnItemSelectedListener(visibleListener);
		columnPositionSpinner.setSelection(getOrderValue(temp));
		addRow("Target", temp, columnVisibleSpinner, columnPositionSpinner);
		
		temp = referenceDataList.getStatus("location");
		columnVisibleSpinner = new Spinner(this);
		visibleAdapter = ArrayAdapter.createFromResource(            
				this, R.array.VisibleOptions, android.R.layout.simple_spinner_item);    
	    visibleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    positionAdapter = ArrayAdapter.createFromResource(            
				this, R.array.PositionOptions, android.R.layout.simple_spinner_item);    
	    positionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);    
		columnVisibleSpinner.setAdapter(visibleAdapter);
		columnVisibleSpinner.setSelection(getVisibleValue(temp));
		columnVisibleSpinner.setEnabled(false);
		columnPositionSpinner = new Spinner(this);
		columnPositionSpinner.setAdapter(positionAdapter);
		columnPositionSpinner.setSelection(getOrderValue(temp));
		visibleListener = new VisibleSettingSpinnerListener(this,6,columnPositionSpinner,referenceDataList,"location");
	    orderListener = new OrderSettingSpinnerListener(this,6,columnVisibleSpinner,referenceDataList,"location");
		columnPositionSpinner.setOnItemSelectedListener(orderListener);
		columnVisibleSpinner.setOnItemSelectedListener(visibleListener);
		addRow("Location", temp, columnVisibleSpinner, columnPositionSpinner);
		
		temp = referenceDataList.getStatus("priority");
		columnVisibleSpinner = new Spinner(this);
		visibleAdapter = ArrayAdapter.createFromResource(            
				this, R.array.VisibleOptions, android.R.layout.simple_spinner_item);    
	    visibleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    positionAdapter = ArrayAdapter.createFromResource(            
				this, R.array.PositionOptions, android.R.layout.simple_spinner_item);    
	    positionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);    
		columnVisibleSpinner.setAdapter(visibleAdapter);
		columnVisibleSpinner.setSelection(getVisibleValue(temp));
		columnVisibleSpinner.setEnabled(false);
		columnPositionSpinner = new Spinner(this);
		columnPositionSpinner.setAdapter(positionAdapter);
		visibleListener = new VisibleSettingSpinnerListener(this,7,columnPositionSpinner,referenceDataList,"priority");
	    orderListener = new OrderSettingSpinnerListener(this,7,columnVisibleSpinner,referenceDataList,"priority");
		columnPositionSpinner.setOnItemSelectedListener(orderListener);
		columnVisibleSpinner.setOnItemSelectedListener(visibleListener);
		columnPositionSpinner.setSelection(getOrderValue(temp));
		addRow("Priority", temp, columnVisibleSpinner, columnPositionSpinner);
		
		temp = referenceDataList.getStatus("building");
		columnVisibleSpinner = new Spinner(this);
		visibleAdapter = ArrayAdapter.createFromResource(            
				this, R.array.VisibleOptions, android.R.layout.simple_spinner_item);    
	    visibleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    positionAdapter = ArrayAdapter.createFromResource(            
				this, R.array.PositionOptions, android.R.layout.simple_spinner_item);    
	    positionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);    
		columnVisibleSpinner.setAdapter(visibleAdapter);
		columnVisibleSpinner.setSelection(getVisibleValue(temp));
		columnVisibleSpinner.setEnabled(false);
		columnPositionSpinner = new Spinner(this);
		columnPositionSpinner.setAdapter(positionAdapter);
		visibleListener = new VisibleSettingSpinnerListener(this,8,columnPositionSpinner,referenceDataList,"building");
	    orderListener = new OrderSettingSpinnerListener(this,8,columnVisibleSpinner,referenceDataList,"building");
		columnPositionSpinner.setOnItemSelectedListener(orderListener);
		columnVisibleSpinner.setOnItemSelectedListener(visibleListener);
		columnPositionSpinner.setSelection(getOrderValue(temp));
		addRow("Building", temp, columnVisibleSpinner, columnPositionSpinner);
	}
	
	public synchronized String alterField(String list, String value, int position) {
		int which = (position-1) * 2;
		int listLength=list.length();
		String alteredList=list;
		value = value.trim();
		if (value.length()>0) {
		     alteredList = list.substring(0, which)+value+list.substring(which+1,listLength);
		}
		return alteredList;
	}
	
	private int getVisibleValue(String setting) {
		int location = setting.indexOf(":");
		int value=0;
		String visible="";
		
		if (location>0) {
			visible = setting.substring(0,location);
			if (visible.equalsIgnoreCase("no")) {
				value=1;
			}
		}
		return value;
	}
	
	private int getOrderValue(String setting) {
		int location = setting.indexOf(":");
		int value=0;
		String order="";
		
		if (location>0) {
			order = setting.substring(location+1,setting.length());
			order = order.trim();
			if ((order==null) || (order.length()==0)) {
				order="0";
			}
			//Log.v("Quantarc","GetOrderValue:"+order);
			value = Integer.parseInt(order);
		}
		return value;
	}
	
	

	private void addTableRow(String rowTitle, String visible, String order, Spinner visibleSpinner, Spinner orderSpinner) {
		TableRow tableRow;
		int TEXT_HEIGHT = 25;
		TextView textView;
		rowId = rowId + 1;
		tableRow = new TableRow(this);
		tableRow.setId(Integer.valueOf(rowId));
		tableRow.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		/* Create a TextView to be the row-content. */
		textView = new TextView(this);
		// textView.setWidth(COLUMN_WIDTH);
		textView.setTextSize(TEXT_HEIGHT);
		textView.setText(rowTitle);
		textView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		/* Add Button to row. */
		tableRow.addView(textView);

	    /* Add Spinner to row. */
		tableRow.addView(visibleSpinner);

		/* Create a TextView to be the row-content. */
		 /* Add Spinner to row. */
		tableRow.addView(orderSpinner);		

		/* Add row to TableLayout. */
		tableLayout.addView(tableRow, new TableLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
	}

	private int getPosition(String list, int which) {
		int position=0;
		int index = (which-1)*2;
		//Log.v("GetPosition",list+":"+which);
		String value = list.substring(index,index+1);
		int listValue = Integer.valueOf(value);
		if (listValue>=0) {
			position=listValue;
		}
		return position;
	}
	
	private void addRow(String rowTitle, String value, Spinner visibleSpinner, Spinner orderSpinner) {
		int pos = value.indexOf(":");
		String visible = value.substring(0, pos);
		String order = value.substring(pos + 1, value.length());
		addTableRow(rowTitle, visible, order, visibleSpinner, orderSpinner);
	}
}