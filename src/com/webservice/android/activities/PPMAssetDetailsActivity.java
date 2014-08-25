package com.webservice.android.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.webservice.android.common.QuantarcPopupDialog;
import com.webservice.domain.PPMAsset;

public class PPMAssetDetailsActivity extends DialogActivity {
	Button backButton;
	TextView error;
	TextView informationTV;
	TextView descriptionTV;
	PPMAsset ppmAsset;
	QuantarcPopupDialog popupDialog;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ppmassetdetails);
		Bundle bundle = getIntent().getExtras();
		ppmAsset = bundle.getParcelable("com.webservice.domain.PPMAsset");
		
		/* comment id not required */
		backButton = (Button) findViewById(R.id.ppmad8);
		informationTV = (TextView) findViewById(R.id.ppmad30);
		informationTV.setTypeface(Typeface.DEFAULT_BOLD);
		descriptionTV = (TextView) findViewById(R.id.ppmad50);
		descriptionTV.setTypeface(Typeface.DEFAULT_BOLD);
		informationTV.setText(ppmAsset.getInformation());
		descriptionTV.setText(ppmAsset.getDescription());
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

