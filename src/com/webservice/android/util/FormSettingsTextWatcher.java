package com.webservice.android.util;

import android.text.Editable;
import android.text.TextWatcher;

import com.webservice.domain.ReferenceDataList;

public class FormSettingsTextWatcher implements TextWatcher {
     private ReferenceDataList referenceDataList;
     private String key;
	
	  public FormSettingsTextWatcher(ReferenceDataList referenceDataList, String key) {
		  this.referenceDataList = referenceDataList;
		  this.key = key;
	  }
     
	  // Method descriptor #5 (Ljava/lang/CharSequence;III)V
	  public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		  
	  }
	  
	  // Method descriptor #5 (Ljava/lang/CharSequence;III)V
	  public void onTextChanged(java.lang.CharSequence arg0, int arg1, int arg2, int arg3) {
		  
	  }
	  
	  // Method descriptor #8 (Landroid/text/Editable;)V
	  public void afterTextChanged(Editable editable) {
		  referenceDataList.put(key,editable.toString());
	  }
	
}
