package com.webservice.android.util;

import android.text.Editable;
import android.text.TextWatcher;

import com.webservice.domain.JobHistory;

public class JobHistoryTextWatcher implements TextWatcher {
     private JobHistory jobHistory;
     private String field;
	
	  public JobHistoryTextWatcher(JobHistory jobHistory, String field) {
		  this.jobHistory = jobHistory;
		  this.field = field;
	  }
     
	  // Method descriptor #5 (Ljava/lang/CharSequence;III)V
	  public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		  
	  }
	  
	  // Method descriptor #5 (Ljava/lang/CharSequence;III)V
	  public void onTextChanged(java.lang.CharSequence arg0, int arg1, int arg2, int arg3) {
		  
	  }
	  
	  // Method descriptor #8 (Landroid/text/Editable;)V
	  public void afterTextChanged(Editable editable) {
		  if (field.equalsIgnoreCase("date")) {
		     jobHistory.setHistoryDate(editable.toString().trim());
		  } else if (field.equalsIgnoreCase("details")) {
			  jobHistory.setDetails(editable.toString());
		  }
	  }
	
}
