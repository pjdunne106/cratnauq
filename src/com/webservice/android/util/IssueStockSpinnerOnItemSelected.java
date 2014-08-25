package com.webservice.android.util;

 import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.webservice.domain.StockToIssue;

public class IssueStockSpinnerOnItemSelected implements OnItemSelectedListener {

	  private StockToIssue stockToIssue;
      private String field;
      
	  public IssueStockSpinnerOnItemSelected(StockToIssue stockToIssue, String field) {
		  this.stockToIssue = stockToIssue;
		  this.field = field;
	  }
	
	  public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		  String value = (String)parent.getItemAtPosition(pos);
		  if (field.equalsIgnoreCase("stock")) {
			  stockToIssue.setCode(value);
		  } 
		  else if (field.equalsIgnoreCase("stockitem")) {
				  
		  }
		  else if (field.equalsIgnoreCase("storemen")) {
			  stockToIssue.setStoreman(value);		  
		   }
		  else if (field.equalsIgnoreCase("stocklocation")) {
			  stockToIssue.setLocation(value);		  
		   }
		  else if (field.equalsIgnoreCase("quantity")) {
			  stockToIssue.setQuantity(value);			 
			}
	  }
	  
	  public void onNothingSelected(AdapterView<?> parent) {
		  
	  }
}
