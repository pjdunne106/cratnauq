package com.webservice.android.listeners;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;

import com.webservice.android.activities.IssueStockActivity;
import com.webservice.domain.StockToIssue;

public class StockCodeItemSelectedListener implements OnItemSelectedListener {

	  private IssueStockActivity issueStockActivity;
	  private StockToIssue stockToIssue;
	  private String stockCodeDesc;
	  
	  public StockCodeItemSelectedListener(IssueStockActivity issueStockActivity, StockToIssue stockToIssue) {
		  this.stockToIssue = stockToIssue;
		  this.issueStockActivity = issueStockActivity;
	  }
	
	  public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		  String value = (String)parent.getItemAtPosition(pos);
          EditText stockCodeField;
          stockToIssue.setCode(value);
		  stockCodeDesc = issueStockActivity.getStockCodeDescription(value);
		  stockCodeField = issueStockActivity.getStockCodeField();
          stockCodeField.setText(stockCodeDesc);
	  }
	  
	  public void onNothingSelected(AdapterView<?> parent) {
		  
	  }
}
