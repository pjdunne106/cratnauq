package com.webservice.android.util;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

public class CustomSpinnerOnItemSelected implements OnItemSelectedListener {

	  private String value;
	  
	  public CustomSpinnerOnItemSelected() {
	  }
	
	  public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		  value = (String)parent.getItemAtPosition(pos);
	  }
	  
	  public void onNothingSelected(AdapterView<?> parent) {
		  
	  }
	  
	  public String getValue() {
		  return value;
	  }
}
