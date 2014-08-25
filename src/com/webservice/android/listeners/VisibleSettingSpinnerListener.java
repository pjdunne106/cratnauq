package com.webservice.android.listeners;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;

import com.webservice.android.activities.SetupPage2Activity;
import com.webservice.domain.ReferenceDataList;

public class VisibleSettingSpinnerListener implements OnItemSelectedListener {

	private SetupPage2Activity page2Activity;
	private ReferenceDataList referenceDataList;
	private String field;
	private int position;
	private Spinner orderSettingsSpinner;

	public VisibleSettingSpinnerListener(SetupPage2Activity page2Activity,
			int position, Spinner orderSettingsSpinner,ReferenceDataList dataList, String field) {
		this.page2Activity = page2Activity;
		this.position = position;
		this.referenceDataList = dataList;
		this.field=field;
		this.orderSettingsSpinner = orderSettingsSpinner;
	}

	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		String value = (String) parent.getItemAtPosition(pos);
		value = value.toLowerCase();
		String storedValue = referenceDataList.getStatus(field);
		int location = storedValue.indexOf(":");
		Log.v("Quantarc", "Visible Selected:" + value);
		if (value.equalsIgnoreCase("no")) {
			storedValue = value+storedValue.substring(location,storedValue.length());
			Log.v("Quantarc", "Visible Selected, altering order:" + value);
			Log.v("Quantarc", "Visible Selected, new value:" + storedValue);
		} else {
			storedValue = value+storedValue.substring(location,storedValue.length());
		}
		referenceDataList.put(field,storedValue);
	}

	public void onNothingSelected(AdapterView<?> parent) {

	}
}
