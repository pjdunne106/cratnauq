package com.webservice.android.listeners;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;


public class HistoryItemSelectedListener implements OnItemSelectedListener {

	  private EditText detailsText;
	  
	  public HistoryItemSelectedListener(EditText details) {
		  this.detailsText = details;
	  }
	
	  public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		  String value = (String)parent.getItemAtPosition(pos);
		  String text = detailsText.getText().toString(); 
          detailsText.setText(value+" "+text);
	  }
	  
	  public void onNothingSelected(AdapterView<?> parent) {
		  
	  }
}

