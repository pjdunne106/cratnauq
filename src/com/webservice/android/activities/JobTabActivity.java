package com.webservice.android.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;

import com.webservice.android.listeners.OnTabListener;

public class JobTabActivity extends DialogTabActivity {
	/** Called when the activity is first created. */
	TabWidget tabWidget;
	TabHost tabHost;
	Button okButton;
	OnTabListener changeListener;
	QuantarcContext applicationContext;
	String allowIssueStock;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		applicationContext = (QuantarcContext) this.getApplicationContext();
		allowIssueStock = applicationContext.getAllowHistories();
		setContentView(R.layout.job_tab);

		tabHost = this.getTabHost();
		// no need to call TabHost.Setup()

		// First Tab
		TabSpec spec1 = tabHost.newTabSpec("Job");
		Intent in1 = new Intent(this, JobHistoryActivity.class);
		spec1.setContent(in1);
		spec1.setIndicator("Job");
		tabHost.addTab(spec1);

		if (allowIssueStock.equalsIgnoreCase("1")) {
		    TabSpec spec2 = tabHost.newTabSpec("Issue Stock");
		    Intent in2 = new Intent(this, HistoryDetailsActivity.class);
		    // spec2.setContent(in2);
		    spec2.setContent(in2);
		    spec2.setIndicator("Issue Stock");
		    tabHost.addTab(spec2);
		}
		TabSpec spec3 = tabHost.newTabSpec("Stock Issued");
		Intent in3 = new Intent(this, IssueStockActivity.class);
		spec3.setContent(in3);
		spec3.setIndicator("Stock Issued");
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