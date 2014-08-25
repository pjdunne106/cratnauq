package com.webservice.android.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.webservice.domain.ReferenceDataList;

public class WeeksSettingsTextWatcher implements TextWatcher {
    private ReferenceDataList referenceDataList;
    private String key;
    private EditText weeksText;
    private int min;
    private int max;
	
	  public WeeksSettingsTextWatcher(ReferenceDataList referenceDataList, String key, int min, int max, EditText field) {
		  this.referenceDataList = referenceDataList;
		  this.key = key;
		  this.min = min;
		  this.max = max;
		  weeksText = field;
	  }
    
	  // Method descriptor #5 (Ljava/lang/CharSequence;III)V
	  public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		  
	  }
	  
	  // Method descriptor #5 (Ljava/lang/CharSequence;III)V
	  public void onTextChanged(java.lang.CharSequence arg0, int arg1, int arg2, int arg3) {
		  
	  }
	  
	  // Method descriptor #8 (Landroid/text/Editable;)V
	  public void afterTextChanged(Editable editable) {
		  String value = editable.toString();
		  boolean valid = false;
		  int num=0;
         value = value.trim();
         if (value.length()>0) {
		    try {
			      num = Integer.parseInt(value);
			      if ((num>=min) && (num<=max)) {
			    	  valid = true;
			      }
		     } catch (NumberFormatException nfe) {
		    	 valid = false;
		     }
		     if (valid) {  
		           referenceDataList.put(key,editable.toString());
		     } else {
			       weeksText.setText("     ");
		        }
          }
	  }
	
}

