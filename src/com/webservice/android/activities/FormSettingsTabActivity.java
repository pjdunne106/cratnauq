package com.webservice.android.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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

public class FormSettingsTabActivity extends DialogTabActivity {
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
	Button saveButton, defaultsButton, cancelButton;
	Job selectedJob;
	int popupIndicator;
	OnTabListener changeListener;
	QuantarcPopupDialog popupDialog;
	QuantarcContext applicationContext;
	ReferenceDataList page1ReferenceDataList, page2ReferenceDataList, page3ReferenceDataList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_tab_view);
		Bundle bundle = getIntent().getExtras();
		page1ReferenceDataList = new ReferenceDataList();
		page2ReferenceDataList = new ReferenceDataList();
		page3ReferenceDataList = new ReferenceDataList();
		applicationContext = (QuantarcContext) this.getApplicationContext();
		populatePage1(page1ReferenceDataList);
		populatePage2(page2ReferenceDataList);
		populatePage3(page3ReferenceDataList);
		saveButton = (Button) findViewById(R.id.stv130);
		saveButton.setOnClickListener(this);
		defaultsButton = (Button) findViewById(R.id.stv140);
		defaultsButton.setOnClickListener(this);
		cancelButton = (Button) findViewById(R.id.stv150);
		cancelButton.setOnClickListener(this);
		popupDialog = new QuantarcPopupDialog(this);
		tabHost = this.getTabHost();
		setup(page1ReferenceDataList, page2ReferenceDataList, page3ReferenceDataList);
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

	public void setup(ReferenceDataList page1DataList, ReferenceDataList page2DataList, ReferenceDataList page3DataList) {
			spec1 = tabHost.newTabSpec("Page 1");
			formSettingsIntent = new Intent(this, FormSettingsActivity.class);
			formSettingsIntent.putExtra("com.webservice.domain.ReferenceDataList", page1DataList);
			spec1.setContent(formSettingsIntent);
			spec1.setIndicator("Page 1");

			spec2 = tabHost.newTabSpec("Page 2");
			setupPage2Intent = new Intent(this, SetupPage2Activity.class);
			setupPage2Intent.putExtra("com.webservice.domain.ReferenceDataList", page2DataList);
			spec2.setContent(setupPage2Intent);
			spec2.setIndicator("Page 2");

			spec3 = tabHost.newTabSpec("Page 3");
			setupPage3Intent = new Intent(this, SetupPage3Activity.class);
			setupPage3Intent.putExtra("com.webservice.domain.ReferenceDataList", page3DataList);
			spec3.setContent(setupPage3Intent);
			spec3.setIndicator("Page 3");

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
		if (id==saveButton.getId()) {
			message = validateData();
			if (message.length()==0) {
			  savePage1DataReferenceList();
			  savePage2DataReferenceList();
			  savePage3DataReferenceList();
			  applicationContext.saveFromActivity("login");
			  popupDialog.showPopupTabDialog("Your changes have been saved", "Okay");
			} else {
				 popupDialog.showPopupTabDialog("The following need to be amended"+message+".", "Okay");
			}
				
		} else if (id==defaultsButton.getId()) {
			page1ReferenceDataList = new ReferenceDataList();
			page2ReferenceDataList = new ReferenceDataList();
			page3ReferenceDataList = new ReferenceDataList();
			populateDefaultPage1(page1ReferenceDataList);
			populateDefaultPage2(page2ReferenceDataList);
			populateDefaultPage3(page3ReferenceDataList);
			popupDialog.showPopupTabDialog("The default settings have been applied, press Save to commit those changes", "Okay");
		} else if (id==cancelButton.getId()) {
			this.finish();
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
	
	private String validateData() {
		String message="";
		String start = page1ReferenceDataList.getStatus("start");
		String finish = page1ReferenceDataList.getStatus("finish");
		int pos = start.indexOf(":");
		if ((start.length()!=5) || (pos<0)) {
			message=", the start time needs to be in the format 99:99";
		}
		 pos = finish.indexOf(":");
		if ((finish.length()!=5) || (pos<0)) {
			message=", the finish time needs to be in the format 99:99";
		}
		message = validateOrderSequence();
		return message;
	}
	
	private String validateOrderSequence() {
		String temp;
		String message="";
		int[] values=new int[9];
		int value;
		int index=0;
		int count=0;
		boolean error = false;
		for (int i=0;i<9;i++) {
			values[i]=0;
		}
		temp = page2ReferenceDataList.getStatus("risk");
		index = temp.indexOf(":");
		temp = temp.substring(index+1,temp.length());
		value = Integer.parseInt(temp);
		if (value!=0) {
		   if (values[value]!=1) {
			  values[value]=1;
			  count++;
		    } else {
			         error = true;
		       }
		}
		temp = page2ReferenceDataList.getStatus("jobnum");
		index = temp.indexOf(":");
		temp = temp.substring(index+1,temp.length());
		value = Integer.parseInt(temp);
		if (value!=0) {
			   if (values[value]!=1) {
				  values[value]=1;
				  count++;
			    } else {
				         error = true;
			       }
			}
		temp = page2ReferenceDataList.getStatus("source");
		index = temp.indexOf(":");
		temp = temp.substring(index+1,temp.length());
		value = Integer.parseInt(temp);
		if (value!=0) {
			   if (values[value]!=1) {
				  values[value]=1;
				  count++;
			    } else {
				         error = true;
			       }
			}
		temp = page2ReferenceDataList.getStatus("status");
		index = temp.indexOf(":");
		temp = temp.substring(index+1,temp.length());
		value = Integer.parseInt(temp);
		if (value!=0) {
			   if (values[value]!=1) {
				  values[value]=1;
				  count++;
			    } else {
				         error = true;
			       }
			}
		temp = page2ReferenceDataList.getStatus("target");
		index = temp.indexOf(":");
		temp = temp.substring(index+1,temp.length());
		value = Integer.parseInt(temp);
		if (value!=0) {
			   if (values[value]!=1) {
				  values[value]=1;
				  count++;
			    } else {
				         error = true;
			       }
			}
		temp = page2ReferenceDataList.getStatus("location");
		index = temp.indexOf(":");
		temp = temp.substring(index+1,temp.length());
		value = Integer.parseInt(temp);
		if (value!=0) {
			   if (values[value]!=1) {
				  values[value]=1;
				  count++;
			    } else {
				         error = true;
			       }
			}
		temp = page2ReferenceDataList.getStatus("building");
		index = temp.indexOf(":");
		temp = temp.substring(index+1,temp.length());
		value = Integer.parseInt(temp);
		if (value!=0) {
			   if (values[value]!=1) {
				  values[value]=1;
				  count++;
			    } else {
				         error = true;
			       }
			}
		temp = page2ReferenceDataList.getStatus("priority");
		index = temp.indexOf(":");
		temp = temp.substring(index+1,temp.length());
		value = Integer.parseInt(temp);
		if (value!=0) {
			   if (values[value]!=1) {
				  values[value]=1;
				  count++;
			    } else {
				         error = true;
			       }
			}
		if (!error) {
			for (int i=1;i<=count;i++) {
				if (values[i]!=1) {
					error = true;
				}
			}
		}
		if (error) {
			message=",any column setting which is set to 'yes' must start from 1 and follow in sequence";
		} else if (count==0) {
			message=",at least one column must be selected for viewing";
		}
		return message;
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
	
	private void populatePage1(ReferenceDataList page1ReferenceDataList) {
		page1ReferenceDataList.put("rememberuser", applicationContext.getRememberUser());
		page1ReferenceDataList.put("autocheck", applicationContext.getAutoCheck());
		page1ReferenceDataList.put("url", applicationContext.getServiceUrl());
		page1ReferenceDataList.put("servertype", applicationContext.getServerType());
		page1ReferenceDataList.put("timeband", applicationContext.getTimeBand());
		page1ReferenceDataList.put("start", applicationContext.getDefaultStart());
		page1ReferenceDataList.put("finish", applicationContext.getDefaultFinish());
		page1ReferenceDataList.put("updateinterval", applicationContext.getUpdateInterval());
	}
	
	private void populateDefaultPage1(ReferenceDataList page1ReferenceDataList) {
		page1ReferenceDataList.put("rememberuser", applicationContext.getDefaultRememberUser());
		page1ReferenceDataList.put("autocheck", "0");
		page1ReferenceDataList.put("url", "");
		page1ReferenceDataList.put("servertype", applicationContext.getDefaultServerType());
		page1ReferenceDataList.put("timeband", applicationContext.getTimeBand());
		page1ReferenceDataList.put("start", applicationContext.getDefaultStart());
		page1ReferenceDataList.put("finish", applicationContext.getDefaultFinish());
		page1ReferenceDataList.put("updateinterval", applicationContext.getUpdateInterval());
	}


	private void savePage1DataReferenceList() {
		applicationContext.saveRememberUser(page1ReferenceDataList.getStatus("rememberuser"));
		applicationContext.saveAutoCheck(page1ReferenceDataList.getStatus("autocheck"));
		applicationContext.saveServerType(page1ReferenceDataList.getStatus("servertype"));
		applicationContext.saveServiceURL(page1ReferenceDataList.getStatus("url"));
		applicationContext.saveTimeBand(page1ReferenceDataList.getStatus("timeband"));
		applicationContext.saveDefaultStart(page1ReferenceDataList.getStatus("start"));
		applicationContext.saveDefaultFinish(page1ReferenceDataList.getStatus("finish"));
		applicationContext.saveUpdateInterval(page1ReferenceDataList.getStatus("updateinterval"));
	}
	
	private void savePage2DataReferenceList() {
		Bundle bundle = setupPage2Intent.getExtras();
		page2ReferenceDataList = bundle.getParcelable("com.webservice.domain.ReferenceDataList");
		//Log.v("Quantarc","Save Page2, Location:"+page2ReferenceDataList.getStatus("location"));
		//Log.v("Quantarc","Save Page2, Risk:"+page2ReferenceDataList.getStatus("risk"));
		applicationContext.saveLoginStaff(page2ReferenceDataList.getStatus("loginstaff"));
		applicationContext.saveDisplayJobs(page2ReferenceDataList.getStatus("displayjobs"));
		applicationContext.saveDisableTimes(page2ReferenceDataList.getStatus("disabletimes"));
		applicationContext.saveAllowHistories(page2ReferenceDataList.getStatus("allowhistories"));
		applicationContext.saveDisableJobsOnTimesheet(page2ReferenceDataList.getStatus("disablejobnumber"));
		applicationContext.saveSettingsRisk(page2ReferenceDataList.getStatus("risk"));
		applicationContext.saveSettingsJobNum(page2ReferenceDataList.getStatus("jobnum"));
		applicationContext.saveSettingsSource(page2ReferenceDataList.getStatus("source"));
		applicationContext.saveSettingsStatus(page2ReferenceDataList.getStatus("status"));
		applicationContext.saveSettingsTarget(page2ReferenceDataList.getStatus("target"));
		applicationContext.saveSettingsLocation(page2ReferenceDataList.getStatus("location"));
		applicationContext.saveSettingsBuilding(page2ReferenceDataList.getStatus("building"));
		applicationContext.saveSettingsPriority(page2ReferenceDataList.getStatus("priority"));
	}
	
	private void savePage3DataReferenceList() {
		applicationContext.saveShortTargetDate(page3ReferenceDataList.getStatus("shortTargetDate"));
		applicationContext.saveNumberOfWeeks(page3ReferenceDataList.getStatus("numberOfWeeks"));
	}
	
	private void populateDefaultPage2(ReferenceDataList page2ReferenceDataList) {
		page2ReferenceDataList.put("loginstaff", applicationContext.getDefaultLoginStaff());
		page2ReferenceDataList.put("displayjobs", applicationContext.getDefaultDisplayJobs());
		page2ReferenceDataList.put("disabletimes", applicationContext.getDisableTimes());
		page2ReferenceDataList.put("allowhistories", applicationContext.getDefaultAllowHistories());
		page2ReferenceDataList.put("disablejobnumber", applicationContext.getDisableJobsOnTimesheet());
		page2ReferenceDataList.put("risk", applicationContext.getDefaultSettingsRisk());
		page2ReferenceDataList.put("jobnum", applicationContext.getDefaultSettingsJobNum());
		page2ReferenceDataList.put("source", applicationContext.getDefaultSettingsSource());
		page2ReferenceDataList.put("status", applicationContext.getDefaultSettingsStatus());
		page2ReferenceDataList.put("target", applicationContext.getDefaultSettingsTarget());
		page2ReferenceDataList.put("location", applicationContext.getDefaultSettingsLocation());
		page2ReferenceDataList.put("building", applicationContext.getDefaultSettingsBuilding());
		page2ReferenceDataList.put("priority", applicationContext.getDefaultSettingsPriority());
		page2ReferenceDataList.put("disablejobs", applicationContext.getDisableJobsOnTimesheet());
	}
	
	private void populatePage2(ReferenceDataList page2ReferenceDataList) {
		applicationContext.populatePage2(page2ReferenceDataList);
	}

	private void populatePage3(ReferenceDataList page3ReferenceDataList) {
		page3ReferenceDataList.put("shortTargetDate", applicationContext.getShortTargetDate());
		page3ReferenceDataList.put("numberOfWeeks", applicationContext.getNumberOfWeeks());
		page3ReferenceDataList.put("quemisversion", applicationContext.getQuemisVersion());
	}
	
	private void populateDefaultPage3(ReferenceDataList page3ReferenceDataList) {
		page3ReferenceDataList.put("shortTargetDate", applicationContext.getDefaultShortTargetDate());
		page3ReferenceDataList.put("numberOfWeeks", applicationContext.getDefaultNumberOfWeeks());
		page3ReferenceDataList.put("quemisversion", applicationContext.getQuemisVersion());
	}
}