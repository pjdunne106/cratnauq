package com.webservice.android.util;

import android.text.Editable;
import android.text.TextWatcher;

import com.webservice.domain.StockToIssue;

public class IssueStockTextWatcher implements TextWatcher {
    private StockToIssue issueStock;
    private String field;
	
	  public IssueStockTextWatcher(StockToIssue issueStock, String field) {
		  this.issueStock = issueStock;
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
		  if (field.equalsIgnoreCase("level")) {
			  issueStock.setLevels(editable.toString());
		  } else if (field.equalsIgnoreCase("refresh")) {
		          issueStock.setRefreshed(editable.toString());
		     }
	  }
 }
