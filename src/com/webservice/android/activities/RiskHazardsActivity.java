package com.webservice.android.activities;

import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import com.webservice.android.listeners.LoginListener;
import com.webservice.domain.Job;
import com.webservice.domain.Risk;
import com.webservice.domain.StatusList;

public class RiskHazardsActivity extends BaseActivity implements OnClickListener {
	EditText username, password, dob, st;
	StatusList statusList;
	Risk risk;
	TableLayout tableLayout;
	TextView error;
	TextView jobRefTV;
	TextView locTV;
	TextView contactTV;
	TextView targetTV;
	TextView typeTV;
	TextView actionTV;
	EditText jobDetailsTV;
	EditText otherTV;
	String response;
	String statusName;
	String statusId;
	String jobDetails;
	String otherDetails; // Anna 2012-07-12
	String[] headerList={"Type","Tradesman action"};
	Button issueButton;
	Button previous;
	LoginListener loginListener;
	int count;
	String actionId;
	int peopleListSize;
	Job selectedJob;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.risk_hazard);
		Bundle bundle = getIntent().getExtras();
		selectedJob = bundle.getParcelable("com.webservice.domain.Job");
		statusList = bundle.getParcelable("com.webservice.domain.StatusList");
		tableLayout = (TableLayout) findViewById(R.id.rh80);
		jobRefTV = (TextView) findViewById(R.id.rh40);
		jobRefTV.setTypeface(Typeface.DEFAULT_BOLD);
		locTV = (TextView) findViewById(R.id.rh60);
		locTV.setTypeface(Typeface.DEFAULT_BOLD);
		jobRefTV.setText(selectedJob.getJobNumLong());
		locTV.setText(selectedJob.getLocDesc());
		if ( selectedJob.getRisks().size()>0) {
			populateTable(selectedJob.getRisks());
		}
		
	}
	
	
	private void populateTable(List<Risk> risks) {
		TableRow tableRow;
		String[] headers=headerList;
		TableRow headerRow;
		int TEXT_HEIGHT = 18; 
		TextView textView;
		int index = 0;
		tableLayout.removeAllViewsInLayout();
		tableLayout.removeAllViews();
		headerRow = new TableRow(this);
		for (int i = 0; i < headers.length; i++) {
			textView = new TextView(this);
			textView.setTextSize(TEXT_HEIGHT);
			textView.setText(headers[i]);
			textView.setBackgroundColor(Color.parseColor("#6699FF"));
			if (i == 0) {
				textView.setLayoutParams(new LayoutParams(170,
						LayoutParams.WRAP_CONTENT));
			} else if (i == 1) {
				textView.setLayoutParams(new LayoutParams(70,
						LayoutParams.WRAP_CONTENT));
			} else {
				textView.setLayoutParams(new LayoutParams(110,
						LayoutParams.WRAP_CONTENT));
			}
			headerRow.addView(textView);
		}
		/* Add row to TableLayout. */
		tableLayout.addView(headerRow, new TableLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		if (risks.size() > 0) {
			for (Risk risk : risks) {
				index = index + 1;
				tableRow = new TableRow(this);
				tableRow.setId(index);
				tableRow.setOnClickListener(this);
				tableRow.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			    
				textView = new TextView(this);
			    textView.setTextSize(TEXT_HEIGHT);
			    textView.setText(risk.getRiskType());
			    textView.setLayoutParams(new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			    tableRow.addView(textView);
					        
				textView = new TextView(this);
				textView.setTextSize(TEXT_HEIGHT);
				textView.setText(risk.getRiskAction());
			    textView.setLayoutParams(new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			    tableRow.addView(textView);
				/* Add row to TableLayout. */
				tableLayout.addView(tableRow, new TableLayout.LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			}
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	public void onClick(View v) {
		int riskId = v.getId();
		risk = getRisk(riskId);
		Intent viewRiskHazardIntent = new Intent(this, ViewRiskHazardActivity.class);
		viewRiskHazardIntent.putExtra("com.webservice.domain.Risk", risk);
		startActivityForResult(viewRiskHazardIntent, 5000);
		
	}
	
	private Risk getRisk(int id) {
		return selectedJob.getRisks().get(id-1);
	}
}
