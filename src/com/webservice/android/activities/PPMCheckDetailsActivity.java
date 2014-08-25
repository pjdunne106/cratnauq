package com.webservice.android.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.webservice.android.common.QuantarcPopupDialog;
import com.webservice.domain.PPMCheck;

public class PPMCheckDetailsActivity extends DialogActivity {
	Button backButton;
	TextView error;
	TextView taskIdTV;
	TextView taskTV;
	PPMCheck ppmCheck;
	QuantarcPopupDialog popupDialog;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ppmcheckdetails);
		Bundle bundle = getIntent().getExtras();
		ppmCheck = bundle.getParcelable("com.webservice.domain.PPMCheck");
		
		/* comment id not required */
		backButton = (Button) findViewById(R.id.ppmcd8);
		taskIdTV = (TextView) findViewById(R.id.ppmcd30);
		taskIdTV.setTypeface(Typeface.DEFAULT_BOLD);
		taskTV = (TextView) findViewById(R.id.ppmcd50);
		taskTV.setTypeface(Typeface.DEFAULT_BOLD);
		taskIdTV.setText(ppmCheck.getTaskId());
		taskTV.setText(ppmCheck.getTask());
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

