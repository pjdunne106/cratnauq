package com.webservice.android.activities;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;

import com.webservice.android.listeners.OnTabListener;

public class PageTabActivity extends TabActivity {
	/** Called when the activity is first created. */
	TabWidget tabWidget;
	TabHost tabHost;
	OnTabListener changeListener;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_tab);

		tabHost = this.getTabHost();
		// no need to call TabHost.Setup()

		// First Tab
		TabSpec spec1 = tabHost.newTabSpec("Page 1");
		Intent in1 = new Intent(this, JobHistoryActivity.class);
		spec1.setContent(in1);
		spec1.setIndicator("Page 1");

		TabSpec spec2 = tabHost.newTabSpec("Page 2");
		Intent in2 = new Intent(this, SetupPage2Activity.class);
		//spec2.setContent(in2);
		spec2.setContent(in2);
		spec2.setIndicator("Page 2");

		TabSpec spec3 = tabHost.newTabSpec("Page 3");
		Intent in3 = new Intent(this, SetupPage3Activity.class);
		spec3.setContent(in3);
		spec3.setIndicator("Page 3");

		tabHost.addTab(spec1);
		tabHost.addTab(spec2);
		tabHost.addTab(spec3);
		tabWidget = (TabWidget) findViewById(android.R.id.tabs);
		changeListener = new OnTabListener(3);
		changeListener.setTabHost(tabHost);
		tabHost.setOnTabChangedListener(changeListener);
		tabHost.getTabWidget().setCurrentTab(1);
		tabHost.getTabWidget().getChildAt(0)
				.setBackgroundColor(Color.parseColor(OnTabListener.TAB_SELECTED_COLOR));
		tabHost.getTabWidget().getChildAt(0).getLayoutParams().height = OnTabListener.TAB_HEIGHT;
		initTabsAppearance(tabWidget);
	}

	private void initTabsAppearance(TabWidget tab_widget) {
		// Change background
		for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
			tabHost.getTabWidget().getChildAt(i)
					.setBackgroundColor(Color.parseColor(OnTabListener.TAB_UNSELECTED_COLOR));
			tabHost.getTabWidget().getChildAt(i).getLayoutParams().height = OnTabListener.TAB_HEIGHT;

		}
	}

}