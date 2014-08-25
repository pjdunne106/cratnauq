package com.webservice.android.listeners;

import android.graphics.Color;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

public class OnTabListener implements OnTabChangeListener {
	TabHost tabHost;
	public static String TAB_UNSELECTED_COLOR="#6699FF";
	public static String TAB_SELECTED_COLOR="#FFFFFF";
	public static String TAB_TEXT_COLOR="#000000";
    public static int TAB_HEIGHT=40;
    public int numberOfTabs;
    
    public OnTabListener(int tabNumber) {
    	numberOfTabs = tabNumber;
    }
    
	@Override
	public void onTabChanged(String tabId) {
		// TODO Auto-generated method stub
		for (int i = 0; i < numberOfTabs; i++) {
			tabHost.getTabWidget().getChildAt(i)
					.setBackgroundColor(Color.parseColor(TAB_UNSELECTED_COLOR));
			tabHost.getTabWidget().getChildAt(0).getLayoutParams().height = TAB_HEIGHT;

		}

		tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab())
				.setBackgroundColor(Color.parseColor(TAB_SELECTED_COLOR));
		tabHost.getTabWidget().getChildAt(0).getLayoutParams().height = TAB_HEIGHT;

	}

   public void setTabHost(TabHost tabHost) {
	   this.tabHost = tabHost;
   }
}
