package com.webservice.android.listeners;

import com.webservice.domain.ReferenceDataList;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

public class DropDownItemSelectedListener implements OnItemSelectedListener {

	  private ReferenceDataList referenceDataList;
	  
	  public DropDownItemSelectedListener() {
	  }
	
	  public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		  String value = (String)parent.getItemAtPosition(pos);
		  referenceDataList.put("timeband", value);
	  }
	  
	  public void onNothingSelected(AdapterView<?> parent) {
		  
	  }
 
	  public void setReferenceDataList(ReferenceDataList referenceDataList) {
		  this.referenceDataList = referenceDataList;
	  }

}
