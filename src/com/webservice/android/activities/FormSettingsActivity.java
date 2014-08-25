package com.webservice.android.activities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.webservice.android.listeners.DropDownItemSelectedListener;
import com.webservice.android.util.FormSettingsTextWatcher;
import com.webservice.domain.ReferenceDataList;


public class FormSettingsActivity extends BaseActivity implements OnClickListener, OnCheckedChangeListener {
    ArrayAdapter<String> adapter;
    DropDownItemSelectedListener dropDownItemSelectedListener;
	EditText username, password, dob, st, defaultStart, defaultFinish;
	TextView error;
	RadioButton aspButton, phpButton;
	String response;
	String urlStr="";
	String[] timeBands={"05","10","15","20","30"};
	Spinner timeBandList=null;
	List<String> defaultUrls = new ArrayList<String>();
	List<String> allValidTimes;
	Button previous;
	CheckBox rememberUser;
	CheckBox autoCheck;
	EditText urlText;
	EditText updateInterval;
	FormSettingsTextWatcher watcher1, watcher2, watcher3, watcher4;
	int count;
	int peopleListSize;
	ReferenceDataList referenceDataList;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.form_settings);
        Bundle bundle = this.getIntent().getExtras();
		allValidTimes = new ArrayList<String>();
		dropDownItemSelectedListener = new DropDownItemSelectedListener();
		referenceDataList = bundle.getParcelable("com.webservice.domain.ReferenceDataList");
		dropDownItemSelectedListener.setReferenceDataList(referenceDataList);
		watcher1 = new FormSettingsTextWatcher(referenceDataList,"start");
		watcher2 = new FormSettingsTextWatcher(referenceDataList,"finish");
		watcher3 = new FormSettingsTextWatcher(referenceDataList,"url");
		watcher4 = new FormSettingsTextWatcher(referenceDataList,"updateinterval");
		Collections.addAll(allValidTimes,timeBands);
		timeBandList = (Spinner)findViewById(R.id.fs47);
        rememberUser = (CheckBox)findViewById(R.id.fs39);
        rememberUser.setOnCheckedChangeListener(this);
        aspButton = (RadioButton) findViewById(R.id.fs50);
        aspButton.setOnClickListener(this);
		phpButton = (RadioButton) findViewById(R.id.fs60);
		phpButton.setOnClickListener(this);
        autoCheck = (CheckBox)findViewById(R.id.fs38);
        autoCheck.setOnCheckedChangeListener(this);
        adapter = new ArrayAdapter<String>(this,
	            android.R.layout.simple_spinner_item, allValidTimes);
        timeBandList.setAdapter(adapter);
        timeBandList.setOnItemSelectedListener(dropDownItemSelectedListener);
        defaultStart = (EditText)findViewById(R.id.fs32);
        defaultStart.addTextChangedListener(watcher1);
        defaultFinish = (EditText)findViewById(R.id.fs34);
        defaultFinish.addTextChangedListener(watcher2);
		urlText = (EditText)findViewById(R.id.fs30);
		urlText.addTextChangedListener(watcher3);
		updateInterval = (EditText)findViewById(R.id.fs42);
		updateInterval.addTextChangedListener(watcher4);
		populateScreen(referenceDataList);
	}
	
	@Override
	public void onClick(View v) {
		EditText editText;
		// TODO Auto-generated method stub
		int viewId = v.getId();
		if (viewId==aspButton.getId()) {
			referenceDataList.put("servertype","asp");
		} else if (viewId==phpButton.getId()) {
			referenceDataList.put("servertype","php");
		  }
	}
	
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		String clicked="0";
		if (buttonView.isChecked()) {
			clicked="1";
		}
		if (buttonView.getId()==rememberUser.getId()) {
			referenceDataList.put("rememberuser",clicked);			
		} else if (buttonView.getId()==autoCheck.getId()) {
			referenceDataList.put("autocheck",clicked);			
		}
	}

	
	private void populateScreen(ReferenceDataList referenceDataList) {
		String value = referenceDataList.getStatus("rememberuser");
		if (value.equalsIgnoreCase("1")) {
			rememberUser.setChecked(true);
		}
		value = referenceDataList.getStatus("servertype");
		if (value.equalsIgnoreCase("asp")) {
			aspButton.setChecked(true);
		} else {
			phpButton.setChecked(true);
		}
		value = referenceDataList.getStatus("autocheck");
		if (value.equalsIgnoreCase("1")) {
			autoCheck.setChecked(true);
		}
		urlText.setText(referenceDataList.getStatus("url"));
		value = referenceDataList.getStatus("timeband");
		String timeBand="";
        int position=0;
		for (int i=0;i<timeBands.length;i++) {
			timeBand = timeBands[i];
			if (value.equalsIgnoreCase(timeBand)) {
				position=i;
			}
		}
		if (position>0) {
		       timeBandList.setSelection(position);
		}
		value = referenceDataList.getStatus("start");
		if (value.equalsIgnoreCase("0")) {
			value="00:00";
		}
		defaultStart.setText(value);
		value = referenceDataList.getStatus("finish");
		if (value.equalsIgnoreCase("0")) {
		       value="00:00";
		}
		defaultFinish.setText(value);
		updateInterval.setText(referenceDataList.getStatus("updateinterval"));
	}
	
}