package com.webservice.android.listeners;

import java.util.List;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.webservice.android.activities.IssueStockActivity;
import com.webservice.domain.StockToIssue;

public class StockTradeItemSelectedListener implements OnItemSelectedListener {

	  private IssueStockActivity issueStockActivity;
	  private StockToIssue stockToIssue;
	  private ArrayAdapter<String> stockCodeAdapter;
	  private List<String> stockCodeList;
	  
	  public StockTradeItemSelectedListener(IssueStockActivity issueStockActivity, StockToIssue stockToIssue) {
		  this.stockToIssue = stockToIssue;
		  this.issueStockActivity = issueStockActivity;
	  }
	
	  public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		  String value = (String)parent.getItemAtPosition(pos);
          Spinner stockCodeSpinner;
		  stockCodeList = issueStockActivity.getStockCodeList(value);
		  stockCodeSpinner = issueStockActivity.getStockCodeSpinner();
		  stockCodeAdapter = new ArrayAdapter<String>(issueStockActivity,
					android.R.layout.simple_spinner_item, stockCodeList);
		  stockCodeSpinner.setAdapter(stockCodeAdapter);
		  stockCodeSpinner.refreshDrawableState();
	  }
	  
	  public void onNothingSelected(AdapterView<?> parent) {
		  
	  }
}
