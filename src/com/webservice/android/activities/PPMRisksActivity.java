package com.webservice.android.activities;

import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import com.webservice.domain.Job;
import com.webservice.domain.PPMRisk;

public class PPMRisksActivity extends DialogActivity {

	private TableLayout tableLayout;
	private TableRow tableRow;
	private String[] headers;
	private Job selectedJob;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ppmrisks_view);
		Bundle bundle = getIntent().getExtras();
		tableLayout = (TableLayout) findViewById(R.id.ppmr50);
		tableRow = (TableRow) findViewById(R.id.ppmrl00);
		selectedJob = bundle.getParcelable("com.webservice.domain.Job");
		addRows(selectedJob.getPPMRisks());
	}
	
	@Override
	public void onClick(View v) {
		Intent intent=null;
		PPMRisk ppmRisk;
		int id = v.getId();
		Log.v("PPMRisksActivity","Clicked, Id:"+(new Integer(id)).toString());
		if (id <= selectedJob.getPPMRisks().size()) {
			ppmRisk = selectedJob.getPPMRisks().get(id-1);
			intent = new Intent(this, PPMRiskDetailsActivity.class);
			intent.putExtra("com.webservice.domain.PPMRisk",ppmRisk);
			startActivity(intent);
		}
    }
	
	private void addRows(List<PPMRisk> riskList) {
		TableRow headerTableRow;
		int TEXT_HEIGHT = 18; // Anna
		TextView textView;
		int index = 0;
		
		tableLayout.removeAllViewsInLayout();
		tableLayout.removeAllViews();
		headerTableRow = new TableRow(this);
		/* Create a TextView to be the row-content. */

		textView = new TextView(this);
	    textView.setTextSize(TEXT_HEIGHT);
	    textView.setText("Risk Assessment");
	    textView.setBackgroundColor(Color.parseColor("#6699FF"));
	    textView.setLayoutParams(new LayoutParams(
        LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
	    headerTableRow.addView(textView);
	    
	    textView = new TextView(this);
	    textView.setTextSize(TEXT_HEIGHT);
	    textView.setBackgroundColor(Color.parseColor("#6699FF"));
	    textView.setText("Method Statement");
	    textView.setLayoutParams(new LayoutParams(
        LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
	    headerTableRow.addView(textView);
	    
		/* Add row to TableLayout. */
		tableLayout.addView(headerTableRow, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		if (riskList.size() > 0) {
			for (PPMRisk risk : riskList) {
				index = index + 1;
				tableRow = new TableRow(this);
				tableRow.setId(Integer.valueOf(index));
				tableRow.setOnClickListener(this);
				tableRow.setLayoutParams(new LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
				
		/* Create a TextView to be the row-content. */
			    textView = new TextView(this);
			    textView.setTextSize(TEXT_HEIGHT);
			    textView.setText(risk.getRiskassessment());
			    textView.setLayoutParams(new LayoutParams(
                LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        /* Add Button to row. */
			    tableRow.addView(textView);
	
	    /* Create a TextView to be the row-content. */
			    textView = new TextView(this);
			    textView.setTextSize(TEXT_HEIGHT);
			    textView.setText(risk.getMethodStatement());
			    textView.setLayoutParams(new LayoutParams(
                LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        /* Add Button to row. */
			    tableRow.addView(textView);
				}
				/* Add row to TableLayout. */
				tableLayout.addView(tableRow, new TableLayout.LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			}
		}
}
