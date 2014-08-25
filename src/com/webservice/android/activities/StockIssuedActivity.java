package com.webservice.android.activities;

import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import com.webservice.android.listeners.LoginListener;
import com.webservice.android.util.IssueStockSpinnerOnItemSelected;
import com.webservice.domain.Job;
import com.webservice.domain.LocationList;
import com.webservice.domain.StockIssue;
import com.webservice.domain.StockItem;
import com.webservice.domain.StockLocation;
import com.webservice.domain.StockToIssue;
import com.webservice.domain.StockTradeList;
import com.webservice.domain.StoremanList;

public class StockIssuedActivity extends DialogActivity {
	ArrayAdapter<String> stockAdapter;
	ArrayAdapter<String> stockLocationAdapter;
	ArrayAdapter<String> stockItemAdapter;
	ArrayAdapter<String> quantityAdapter;
	ArrayAdapter<String> storemenAdapter;
	IssueStockSpinnerOnItemSelected issueStockSpinnerOnItemSelected;
	Job selectedJob;
	List<String> stockItemNames;
	List<String> stockLocationNames;
	List<String> quantityList;
	List<String> storemenNames;
	LocationList locationList;
	EditText username, password, description, levels, refreshed;
	TableLayout tableLayout;
	TableRow tableRow;
	TextView error;
	List<StockItem> stockItemList;
	List<StockLocation> stockLocationList;
	String response;
	Spinner stockSpinner;
	Spinner stockItemSpinner;
	Spinner stockLocationSpinner;
	Spinner quantitySpinner;
	Spinner storemenSpinner;
	StockTradeList stockList;
	StockToIssue stockToIssue;
	StoremanList storemenList;
	Button previous;
	LoginListener loginListener;
	int count;
	int peopleListSize;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stock_issued);
		tableLayout = (TableLayout) findViewById(R.id.si20);
		tableRow = (TableRow) findViewById(R.id.si25);
		Bundle bundle = getIntent().getExtras();
		selectedJob = bundle.getParcelable("com.webservice.domain.Job");
		locationList = bundle
				.getParcelable("com.webservice.domain.LocationList");
		stockToIssue = bundle
				.getParcelable("com.webservice.domain.StockToIssue");
		tableLayout.removeAllViewsInLayout();
		tableLayout.removeAllViews();
		addRows(selectedJob.getStockIssues());
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	}

	private void addRows(List<StockIssue> stockList) {
        String[] headers={"Part Number", "Part Desc", "Quantity"};
		TableRow tableRow;
		TableRow headerTableRow;
		TextView textView;
		int TEXT_HEIGHT = 18; // Anna 2012-07-17 reduced from 28
		int index = 0;
		tableLayout.removeAllViewsInLayout();
		tableLayout.removeAllViews();
		headerTableRow = new TableRow(this);
		for (int i = 0; i < headers.length; i++) {
			/* Create a TextView to be the row-content. */
			textView = new TextView(this);
			textView.setTextSize(TEXT_HEIGHT);
			textView.setText(headers[i]);
			textView.setBackgroundColor(Color.parseColor("#6699FF"));
			textView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
			/* Add Button to row. */
			headerTableRow.addView(textView);
		}
		tableLayout.addView(headerTableRow, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		for (StockIssue issue : stockList) {
			index = index + 1;
			tableRow = new TableRow(this);
			tableRow.setId(Integer.valueOf(index));
			tableRow.setOnClickListener(this);
			tableRow.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT));
			/* Create a TextView to be the row-content. */
			textView = new TextView(this);
			textView.setWidth(100);
			textView.setText(issue.getPartNumber());
			textView.setTextSize(TEXT_HEIGHT);
			textView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT));
			/* Add Button to row. */
			tableRow.addView(textView);

			/* Create a TextView to be the row-content. */
			textView = new TextView(this);
			textView.setText(issue.getPartDescription()+"  ");
			textView.setTextSize(TEXT_HEIGHT);
			textView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT));
			/* Add Button to row. */
			tableRow.addView(textView);

			/* Create a TextView to be the row-content. */
			textView = new TextView(this);
			textView.setText(issue.getItemCount());
			textView.setTextSize(TEXT_HEIGHT);
			textView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT));
			/* Add Button to row. */
			tableRow.addView(textView);

			/* Add row to TableLayout. */
			tableLayout.addView(tableRow, new TableLayout.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		}
	}

}