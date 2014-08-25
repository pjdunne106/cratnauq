package com.webservice.android.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;

import com.webservice.android.common.QuantarcPopupDialog;
import com.webservice.android.listeners.OnTabListener;
import com.webservice.domain.Job;
import com.webservice.domain.JobHistory;
import com.webservice.domain.ReferenceDataList;
import com.webservice.domain.StatusList;
import com.webservice.domain.UserList;

public class PPMViewTabActivity extends DialogTabActivity {
	/** Called when the activity is first created. */
	private static int SCROLL_REQUEST_CODE=1000;
	StatusList statusList;
	UserList allUsers;
	Intent intent;
	Intent formSettingsIntent;
	Intent setupPage2Intent;
	Intent setupPage3Intent;
	JobHistory jobHistory;
	TabSpec spec1;
	TabSpec spec2;
	TabSpec spec3;
	TabWidget tabWidget;
	TabHost tabHost;
	Button backButton;
	Job selectedJob;
	int popupIndicator;
	OnTabListener changeListener;
	QuantarcPopupDialog popupDialog;
	QuantarcContext applicationContext;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ppm_view);
		Bundle bundle = getIntent().getExtras();
		selectedJob = bundle.getParcelable("com.webservice.domain.Job");
		backButton = (Button) findViewById(R.id.ppm9);
		backButton.setOnClickListener(this);
		applicationContext = (QuantarcContext) this.getApplicationContext();
		popupDialog = new QuantarcPopupDialog(this);
		tabHost = this.getTabHost();
		setup(selectedJob);
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		QuantarcPopupDialog popupDialog = new QuantarcPopupDialog(this);
		if (popupIndicator == 1) {
			popupIndicator = 2;
			switch (which) {
			case DialogInterface.BUTTON_POSITIVE: // Yes button clicked
				popupDialog.showPopupTabDialog("Is this job complete ?", "Yes",
						"No");
				break;
			case DialogInterface.BUTTON_NEGATIVE: // No button clicked
				break;
			}
		} else if (popupIndicator == 2) {
			popupIndicator = 0;
			switch (which) {
			case DialogInterface.BUTTON_POSITIVE: // Yes button clicked
				popupDialog.showPopupTabDialog("Completed", "Yes", "No");
				break;
			case DialogInterface.BUTTON_NEGATIVE: { // No button clicked
				intent = new Intent(this, ConfirmStatusActivity.class);
				intent.putExtra("com.webservice.domain.StatusList", statusList);
				startActivityForResult(intent,SCROLL_REQUEST_CODE);
			}
			}
		}
	}

	public void setup(Job selectedJob) {
			spec1 = tabHost.newTabSpec("Risks");
			formSettingsIntent = new Intent(this, PPMRisksActivity.class);
			formSettingsIntent.putExtra("com.webservice.domain.Job", selectedJob);
			spec1.setContent(formSettingsIntent);
			spec1.setIndicator("Risks");

			spec2 = tabHost.newTabSpec("Checks");
			setupPage2Intent = new Intent(this, PPMChecksActivity.class);
			setupPage2Intent.putExtra("com.webservice.domain.Job", selectedJob);
			spec2.setContent(setupPage2Intent);
			spec2.setIndicator("Checks");

			spec3 = tabHost.newTabSpec("Assets");
			setupPage3Intent = new Intent(this, PPMAssetsActivity.class);
			setupPage3Intent.putExtra("com.webservice.domain.Job", selectedJob);
			spec3.setContent(setupPage3Intent);
			spec3.setIndicator("Assets");

			tabHost.addTab(spec1);
			tabHost.addTab(spec2);
			tabHost.addTab(spec3);
			tabWidget = (TabWidget) findViewById(android.R.id.tabs);
			changeListener = new OnTabListener(3);
			changeListener.setTabHost(tabHost);
			tabHost.setOnTabChangedListener(changeListener);
			tabHost.getTabWidget().setCurrentTab(0);
			tabHost.getTabWidget()
					.getChildAt(0)
					.setBackgroundColor(
							Color.parseColor(OnTabListener.TAB_SELECTED_COLOR));
			tabHost.getTabWidget().getChildAt(0).getLayoutParams().height = OnTabListener.TAB_HEIGHT;
			initTabsAppearance(tabWidget);
	}
	
	@Override
	public void onClick(View v) {
		QuantarcPopupDialog popupDialog = new QuantarcPopupDialog(this);
		int id = v.getId();
		String message="";
		if (id==backButton.getId()) {
			finish();
		} 

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Intent returnIntent = new Intent();
		String confirmStatus = data.getStringExtra("confirmstatus");
		returnIntent.putExtra("confirmstatus", confirmStatus);
		returnIntent.putExtra("com.webservice.domain.JobHistory", formSettingsIntent.getParcelableExtra("com.webservice.domain.JobHistory"));
		setResult(1000,returnIntent);
		this.finish();
	}

	private void initTabsAppearance(TabWidget tab_widget) {
		// Change background
		tabHost.getTabWidget()
				.getChildAt(1)
				.setBackgroundColor(
						Color.parseColor(OnTabListener.TAB_UNSELECTED_COLOR));
		tabHost.getTabWidget().getChildAt(1).getLayoutParams().height = OnTabListener.TAB_HEIGHT;
		tabHost.getTabWidget()
				.getChildAt(2)
				.setBackgroundColor(
						Color.parseColor(OnTabListener.TAB_UNSELECTED_COLOR));
		tabHost.getTabWidget().getChildAt(2).getLayoutParams().height = OnTabListener.TAB_HEIGHT;
	}	
	
}