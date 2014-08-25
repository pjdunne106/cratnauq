package com.webservice.android.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

import com.webservice.android.util.FormSettingsTextWatcher;
import com.webservice.android.util.WeeksSettingsTextWatcher;
import com.webservice.domain.ReferenceDataList;


public class SetupPage3Activity extends Activity implements OnClickListener, OnCheckedChangeListener {
	EditText weeks;
	EditText quemisVersion;
	TextView dateTitle, historyTitle;
	CheckBox dateCheck;
	String response;
	Button saveButton;
	Button previous;
	int count;
	int peopleListSize;
	ReferenceDataList referenceDataList;
	FormSettingsTextWatcher watcher;
	WeeksSettingsTextWatcher weeksWatcher;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setuppage3);
		Bundle bundle = this.getIntent().getExtras();
		dateCheck=(CheckBox) findViewById(R.id.spa320);
		dateCheck.setOnCheckedChangeListener(this);
		weeks=(EditText) findViewById(R.id.spa350);
		quemisVersion=(EditText) findViewById(R.id.spa370);
		referenceDataList = bundle.getParcelable("com.webservice.domain.ReferenceDataList");
        watcher = new FormSettingsTextWatcher(referenceDataList,"numberOfWeeks");
        weeksWatcher = new WeeksSettingsTextWatcher(referenceDataList,"numberOfWeeks", 1, 12,weeks);
        weeks.addTextChangedListener(weeksWatcher);
		populateScreen(referenceDataList);
		quemisVersion.setEnabled(false);
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
		if (buttonView.getId()==dateCheck.getId()) {
			referenceDataList.put("shortTargetDate",clicked);			
		}
	}

	
	private void populateScreen(ReferenceDataList referenceDataList) {
		String temp = referenceDataList.getStatus("shortTargetDate");
		if ((temp != null) && (temp.equalsIgnoreCase("1"))) {
			dateCheck.setChecked(true);
		}
		temp = referenceDataList.getStatus("numberOfWeeks");
		if (temp != null) {
			weeks.setText(temp);
		}
		temp = referenceDataList.getStatus("quemisversion");
		if (temp != null) {
			quemisVersion.setText(temp);
		}
	}
}