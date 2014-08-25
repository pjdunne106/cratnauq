package com.webservice.android.util;

import android.text.Editable;
import android.text.TextWatcher;

import com.webservice.domain.Job;
import com.webservice.domain.JobHistory;

public class CostOfMaterialsTextWatcher implements TextWatcher {
     private JobHistory jobHistory;
	
	  public CostOfMaterialsTextWatcher(JobHistory jobHistory) {
		  this.jobHistory = jobHistory;
	  }
     
	  // Method descriptor #5 (Ljava/lang/CharSequence;III)V
	  public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		  
	  }
	  
	  // Method descriptor #5 (Ljava/lang/CharSequence;III)V
	  public void onTextChanged(java.lang.CharSequence arg0, int arg1, int arg2, int arg3) {
		  
	  }
	  
	  // Method descriptor #8 (Landroid/text/Editable;)V
	  public void afterTextChanged(Editable editable) {
		  jobHistory.setCostOfMaterial(editable.toString());
	  }
}