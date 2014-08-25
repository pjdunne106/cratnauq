package com.webservice.android.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.webservice.android.common.QuantarcPopupDialog;
import com.webservice.domain.Risk;

public class ViewRiskHazardActivity extends DialogActivity {
	private static int UPDATE_TIME_CODE = 1020;
	Risk risk;
	TextView riskType;
	TextView furtherDetails;
	TextView riskDescription;
	TextView helpDeskAction;
	String response;
	Button quitButton;
	QuantarcPopupDialog popupDialog;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_riskhazard);
		Bundle bundle = getIntent().getExtras();
		risk = bundle.getParcelable("com.webservice.domain.Risk");
		riskType = (TextView) findViewById(R.id.vrh30);
		riskType.setTypeface(Typeface.DEFAULT_BOLD);
		riskType.setText(risk.getRiskType());
		furtherDetails = (TextView) findViewById(R.id.vrh50);
		furtherDetails.setTypeface(Typeface.DEFAULT_BOLD);
		furtherDetails.setText(risk.getFurtherDetails());
		furtherDetails.setClickable(true);
		furtherDetails.setFocusable(false);
		furtherDetails.setEnabled(true);
		furtherDetails.setOnClickListener(this);
		riskDescription = (TextView) findViewById(R.id.vrh70);
		riskDescription.setTypeface(Typeface.DEFAULT_BOLD);
		riskDescription.setText(risk.getDescription());
		riskDescription.setOnClickListener(this);
		riskDescription.setClickable(true);
		riskDescription.setFocusable(false);
		riskDescription.setEnabled(true);
		helpDeskAction = (TextView) findViewById(R.id.vrh78);
		helpDeskAction.setTypeface(Typeface.DEFAULT_BOLD);
		helpDeskAction.setText(risk.getHelpDeskAction());
		riskDescription.setOnClickListener(this);
		helpDeskAction.setClickable(true);
		helpDeskAction.setFocusable(false);
		helpDeskAction.setEnabled(true);
		quitButton = (Button) findViewById(R.id.vrh80);
		quitButton.setOnClickListener(this);
		popupDialog = new QuantarcPopupDialog(this);
	}

	
	
	
	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id==riskDescription.getId()) {
			 popupDialog.showPopupDialog(risk.getDescription(), "ok");
		}
		if (id==helpDeskAction.getId()) {
			 popupDialog.showPopupDialog(risk.getHelpDeskAction(), "ok");
		}
		if (id==furtherDetails.getId()) {
			 popupDialog.showPopupDialog(risk.getFurtherDetails(), "ok");
		}
		if (id == quitButton.getId()) {
			Intent returnIntent = new Intent();
			setResult(7000, returnIntent);
			this.finish();
		}
	}

}