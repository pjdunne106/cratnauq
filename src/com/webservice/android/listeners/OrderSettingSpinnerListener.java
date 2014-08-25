package com.webservice.android.listeners;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;

import com.webservice.android.activities.SetupPage2Activity;
import com.webservice.domain.ReferenceDataList;

public class OrderSettingSpinnerListener implements OnItemSelectedListener {

	private SetupPage2Activity page2Activity;
	private ReferenceDataList referenceDataList;
	private Spinner visibleSettingsSpinner;
	private String field;
	private int position;

	public OrderSettingSpinnerListener(SetupPage2Activity page2Activity,
			int position, Spinner visibleSettingsSpinner, ReferenceDataList dataList, String field) {
		this.page2Activity = page2Activity;
		this.position = position;
		this.referenceDataList = dataList;
		this.field = field;
		this.visibleSettingsSpinner=visibleSettingsSpinner;
	}

	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		String value = (String) parent.getItemAtPosition(pos);
		if ((value==null) || (value.length()==0)) {
			value="0";
		}
		String storedValue = referenceDataList.getStatus(field);
		Log.v("Quantarc","Order Setting Spinner, before:"+field+":"+storedValue);
		int location = storedValue.indexOf(":");
		storedValue = storedValue.substring(0, location+1)+value;
		referenceDataList.put(field, storedValue);
		if (value.equalsIgnoreCase("0")) {
			visibleSettingsSpinner.setEnabled(true);
			visibleSettingsSpinner.setSelection(1);
			visibleSettingsSpinner.setEnabled(false);
		} else {
			visibleSettingsSpinner.setEnabled(true);
			visibleSettingsSpinner.setSelection(0);
			visibleSettingsSpinner.setEnabled(false);
		}
		Log.v("Quantarc","Order Setting Spinner:"+field+":"+storedValue);
	}

	public void onNothingSelected(AdapterView<?> parent) {

	}
	
	
}