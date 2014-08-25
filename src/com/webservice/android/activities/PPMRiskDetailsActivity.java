package com.webservice.android.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.webservice.android.common.QuantarcPopupDialog;
import com.webservice.android.listeners.LoginListener;
import com.webservice.domain.Job;
import com.webservice.domain.PPMRisk;
import com.webservice.domain.StatusList;

public class PPMRiskDetailsActivity extends DialogActivity {
	Button backButton;
	TextView error;
	TextView riskTV;
	TextView methodTV;
	PPMRisk ppmRisk;
	QuantarcPopupDialog popupDialog;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ppmriskdetails);
		Bundle bundle = getIntent().getExtras();
		ppmRisk = bundle.getParcelable("com.webservice.domain.PPMRisk");
		
		/* comment id not required */
		backButton = (Button) findViewById(R.id.ppmrd8);
		riskTV = (TextView) findViewById(R.id.ppmrd30);
		riskTV.setTypeface(Typeface.DEFAULT_BOLD);
		methodTV = (TextView) findViewById(R.id.ppmrd50);
		methodTV.setTypeface(Typeface.DEFAULT_BOLD);
		riskTV.setText(ppmRisk.getRiskassessment());
		methodTV.setText(ppmRisk.getMethodStatement());
		popupDialog = new QuantarcPopupDialog(this);
		backButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		int id = v.getId();
		this.finish();
	}
}

