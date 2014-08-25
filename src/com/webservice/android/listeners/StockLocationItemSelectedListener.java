package com.webservice.android.listeners;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.webservice.android.activities.IssueStockActivity;
import com.webservice.domain.StockToIssue;

public class StockLocationItemSelectedListener implements OnItemSelectedListener {

	  private IssueStockActivity issueStockActivity;
      private StockToIssue stockToIssue;	  
	  public StockLocationItemSelectedListener(IssueStockActivity issueStockActivity, StockToIssue stockToIssue) {
		  this.stockToIssue = stockToIssue;
		  this.issueStockActivity = issueStockActivity;
	  }
	
	  public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		  String value = (String)parent.getItemAtPosition(pos);
		  stockToIssue.setLocation(value);
		  issueStockActivity.setStockLevelAndRefreshed(stockToIssue.getCode());
	  }
	  
	  public void onNothingSelected(AdapterView<?> parent) {
		  
	  }
}
