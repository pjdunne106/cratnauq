package com.webservice.android.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;

import com.webservice.android.common.QuantarcPopupDialog;
import com.webservice.android.listeners.OnTabListener;
import com.webservice.domain.Job;
import com.webservice.domain.JobDetailsList;
import com.webservice.domain.JobHistory;
import com.webservice.domain.JobList;
import com.webservice.domain.LabourTimeList;
import com.webservice.domain.LocationList;
import com.webservice.domain.StatusList;
import com.webservice.domain.StockItemList;
import com.webservice.domain.StockLevelList;
import com.webservice.domain.StockTradeList;
import com.webservice.domain.StoremanList;
import com.webservice.domain.TimesheetList;
import com.webservice.domain.UserList;

public class JobDetailsTabActivity extends DialogTabActivity {
	/** Called when the activity is first created. */
	Button okButton;
	boolean hasRisks;
	int tabCount=3;
	Intent historyDetailsIntent;
	JobHistory jobHistory;
	Intent riskHazardsIntent;
	TabWidget tabWidget;
	TabHost tabHost;
	TabSpec spec4;
	TextView jobText;
	LabourTimeList labourTimeList;
	LocationList locationList;
	OnTabListener changeListener;
	Job selectedJob;
	JobList jobList;
	JobDetailsList jobDetailsList;
	StatusList statusList;
	StoremanList storemanList;
	StockLevelList stockLevelList;
	StockItemList stockItemList;
	StockTradeList stockTradeList;
	TimesheetList timesheetList;
	UserList allUsers;
	QuantarcContext applicationContext;
    QuantarcPopupDialog popupDialog;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.job_tab);
		Bundle bundle = getIntent().getExtras();
		hasRisks=false;
		applicationContext = (QuantarcContext) this.getApplicationContext();
		selectedJob = bundle.getParcelable("com.webservice.domain.Job");
		statusList = bundle.getParcelable("com.webservice.domain.StatusList");
		allUsers = bundle.getParcelable("com.webservice.domain.UserList");
		labourTimeList = bundle.getParcelable("com.webservice.domain.LabourTimeList");
		locationList = bundle.getParcelable("com.webservice.domain.LocationList");
		jobDetailsList = bundle.getParcelable("com.webservice.domain.JobDetailsList");
		timesheetList = bundle.getParcelable("com.webservice.domain.TimesheetList");
		jobList = bundle.getParcelable("com.webservice.domain.JobList");
		tabHost = this.getTabHost();
		// no need to call TabHost.Setup()
		
		jobText = (TextView) findViewById(R.id.jt8);
		jobText.setText(selectedJob.getJobNumLong());
		
		okButton = (Button) findViewById(R.id.jt9);
		okButton.setOnClickListener(this);
		
		if (selectedJob.getRisks().size()>0) {
			hasRisks = true;
		}
		
		popupDialog = new QuantarcPopupDialog(this);
        jobHistory = new JobHistory();
		jobHistory.setFinishTime(applicationContext.getDefaultFinish());
		jobHistory.setStartTime(applicationContext.getDefaultStart());
		// First Tab
		TabSpec spec1 = tabHost.newTabSpec("Details");
		Intent in1 = new Intent(this, JobDetailsActivity.class);
		in1.putExtra("com.webservice.domain.Job", selectedJob);
		in1.putExtra("com.webservice.domain.StatusList", statusList);
		spec1.setContent(in1);
		spec1.setIndicator("Details");

		TabSpec spec2 = tabHost.newTabSpec("Re-assign");
		Intent in2 = new Intent(this, ReassignActivity.class);
		in2.putExtra("com.webservice.domain.Job", selectedJob);
		in2.putExtra("com.webservice.domain.UserList", allUsers);
		spec2.setContent(in2);
		spec2.setIndicator("Re-assign");

		TabSpec spec3 = tabHost.newTabSpec("History");
		historyDetailsIntent = new Intent(this, HistoryDetailsActivity.class);
		historyDetailsIntent.putExtra("com.webservice.domain.Job", selectedJob);
		historyDetailsIntent.putExtra("com.webservice.domain.StatusList", statusList);
		historyDetailsIntent.putExtra("com.webservice.domain.JobHistory", jobHistory);
		historyDetailsIntent.putExtra("com.webservice.domain.UserList", allUsers);
		historyDetailsIntent.putExtra("com.webservice.domain.LabourTimeList", labourTimeList);
		historyDetailsIntent.putExtra("com.webservice.domain.LocationList", locationList);
		historyDetailsIntent.putExtra("com.webservice.domain.JobDetailsList", jobDetailsList);
		historyDetailsIntent.putExtra("com.webservice.domain.TimesheetList", timesheetList);
		historyDetailsIntent.putExtra("com.webservice.domain.JobList", jobList);
		spec3.setContent(historyDetailsIntent);
		spec3.setIndicator("History");
        
		if (hasRisks) {
		   spec4 = tabHost.newTabSpec("Risk/Hazards");
		   riskHazardsIntent = new Intent(this, RiskHazardsActivity.class);
		   riskHazardsIntent.putExtra("com.webservice.domain.Job", selectedJob);
		   riskHazardsIntent.putExtra("com.webservice.domain.StatusList", statusList);
		   spec4.setContent(riskHazardsIntent);
		   spec4.setIndicator("Risk/Hazards");
		   tabCount=tabCount+1;
		}
		
		tabHost.addTab(spec1);
		tabHost.addTab(spec2);
		tabHost.addTab(spec3);
        if (hasRisks) {
		   tabHost.addTab(spec4);
        }
		tabWidget = (TabWidget) findViewById(android.R.id.tabs);
		changeListener = new OnTabListener(tabCount);
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
		Intent intent=null;
		if (v.getId() == okButton.getId()) {
			intent = new Intent();
			intent.putExtra("com.webservice.domain.Job", historyDetailsIntent.getParcelableExtra("com.webservice.domain.Job"));
			intent.putExtra("com.webservice.domain.TimesheetList", historyDetailsIntent.getParcelableExtra("com.webservice.domain.TimesheetList"));
			setResult(7000,intent);
			this.finish();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK
	            && event.getRepeatCount() == 0) {
	    	setResult(1080,null);
	    	this.finish();
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
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
		if (hasRisks) {
			tabHost.getTabWidget()
			.getChildAt(3)
			.setBackgroundColor(
					Color.parseColor(OnTabListener.TAB_UNSELECTED_COLOR));
	        tabHost.getTabWidget().getChildAt(3).getLayoutParams().height = OnTabListener.TAB_HEIGHT;
			
		}
	}

}