package com.webservice.android.activities;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

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
import com.webservice.android.task.WebUploadWithStockTask;
import com.webservice.android.util.DateUtil;
import com.webservice.domain.Job;
import com.webservice.domain.JobDetailsList;
import com.webservice.domain.JobHistory;
import com.webservice.domain.JobList;
import com.webservice.domain.LabourTimeList;
import com.webservice.domain.LocationList;
import com.webservice.domain.StatusList;
import com.webservice.domain.StockItemList;
import com.webservice.domain.StockLevelList;
import com.webservice.domain.StockToIssue;
import com.webservice.domain.StockTradeList;
import com.webservice.domain.StoremanList;
import com.webservice.domain.UserList;

public class ScrollActivity extends DialogTabActivity {
	/** Called when the activity is first created. */
	private static int SCROLL_REQUEST_CODE = 1000;
	private static String STANDARD_DATE_FORMAT = "dd/MM/yyyy kk:mm:ss";
	private static String SIMPLE_DATE_FORMAT = "dd/MM/yyyy";
	StatusList statusList;
	UserList allUsers;
	Intent intent;
	Intent jobHistoryIntent;
	Intent issueStockIntent;
	Intent issuedStockIntent;
	JobHistory jobHistory;
	JobHistory newlyCreatedjobHistory;
	JobList jobList;
	JobDetailsList jobDetailsList;
	LabourTimeList labourTimeList;
	LocationList locationList;
	StockToIssue stockToIssue;
	StockLevelList stockLevelList;
	StockTradeList stockTradeList;
	StockItemList stockItemList;
	String confirmationStatus;
	TabWidget tabWidget;
	TabHost tabHost;
	Button submitButton;
	Button cancelButton;
	Job selectedJob;
	int popupIndicator;
	OnTabListener changeListener;
	StoremanList storemanList;
	QuantarcPopupDialog popupDialog;
	QuantarcContext applicationContext;
	WebUploadWithStockTask webUploadWithStock;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scrollable_view);
		Bundle bundle = getIntent().getExtras();
		applicationContext = (QuantarcContext) this.getApplicationContext();
		submitButton = (Button) findViewById(R.id.sc130);
		submitButton.setOnClickListener(this);
		cancelButton = (Button) findViewById(R.id.sc140);
		cancelButton.setOnClickListener(this);
		popupDialog = new QuantarcPopupDialog(this);
		stockToIssue = new StockToIssue();
		stockToIssue.setIsNew("0");
		stockToIssue.setUpdated("0");
		selectedJob = bundle.getParcelable("com.webservice.domain.Job");
		statusList = bundle.getParcelable("com.webservice.domain.StatusList");
		jobHistory = bundle.getParcelable("com.webservice.domain.JobHistory");
		allUsers = bundle.getParcelable("com.webservice.domain.UserList");
		labourTimeList = bundle
				.getParcelable("com.webservice.domain.LabourTimeList");
		locationList = bundle
				.getParcelable("com.webservice.domain.LocationList");
		jobDetailsList = bundle
				.getParcelable("com.webservice.domain.JobDetailsList");
		jobList = bundle
				.getParcelable("com.webservice.domain.JobList");
		tabHost = this.getTabHost();
		// no need to call TabHost.Setup()

		// First Tab
		TabSpec spec1 = tabHost.newTabSpec("Job");
		jobHistoryIntent = new Intent(this, JobHistoryActivity.class);
		jobHistoryIntent.putExtra("com.webservice.domain.Job", selectedJob);
		jobHistoryIntent.putExtra("com.webservice.domain.StatusList",
				statusList);
		jobHistoryIntent.putExtra("com.webservice.domain.JobHistory",
				jobHistory);
		jobHistoryIntent.putExtra("com.webservice.domain.UserList", allUsers);
		jobHistoryIntent.putExtra("com.webservice.domain.LabourTimeList",
				labourTimeList);
		jobHistoryIntent.putExtra("com.webservice.domain.JobDetailsList",
				jobDetailsList);
		jobHistoryIntent.putExtra("com.webservice.domain.JobList",
				jobList);
		spec1.setContent(jobHistoryIntent);
		spec1.setIndicator("Job");

		TabSpec spec2 = tabHost.newTabSpec("Issue Stock");
		issueStockIntent = new Intent(this, IssueStockActivity.class);
		issueStockIntent.putExtra("com.webservice.domain.Job", selectedJob);
		issueStockIntent.putExtra("com.webservice.domain.StockToIssue",
				stockToIssue);
		issueStockIntent.putExtra("com.webservice.domain.LocationList",
				locationList);
		// spec2.setContent(in2);
		spec2.setContent(issueStockIntent);
		spec2.setIndicator("Issue Stock");

		TabSpec spec3 = tabHost.newTabSpec("Stock Issued");
		issuedStockIntent = new Intent(this, StockIssuedActivity.class);
		issuedStockIntent.putExtra("com.webservice.domain.Job", selectedJob);
		issuedStockIntent.putExtra("com.webservice.domain.StockToIssue",
				stockToIssue);
		spec3.setContent(issuedStockIntent);
		spec3.setIndicator("Stock Issued");

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
	public void onClick(DialogInterface dialog, int which) {
		String stockWithHistories = applicationContext.getAllowHistories();
		StockToIssue stockToIssue = issueStockIntent
				.getParcelableExtra("com.webservice.domain.StockToIssue");
		if (popupIndicator == 1) {
			popupIndicator = 2;
			switch (which) {
			case DialogInterface.BUTTON_POSITIVE: { // Yes button clicked
				if (stockWithHistories.equalsIgnoreCase("1")) {
					popupDialog
							.showPopupTabDialog(
									"You have issued no stock. If this is intentional,press OK. If you would like to issue stock now, press 'Cancel'.",
									"OK", "Cancel");
				} else {
					Intent returnIntent = new Intent();
					returnIntent.putExtra("confirmstatus", "qsetting");
					returnIntent.putExtra("com.webservice.domain.StockToIssue",
							stockToIssue);
					returnIntent.putExtra("com.webservice.domain.JobHistory",newlyCreatedjobHistory);
					setResult(1550, returnIntent);
					this.finish();
				}
			}
				;
				break;
			case DialogInterface.BUTTON_NEGATIVE: { // No button clicked
		//		intent = new Intent(this, ConfirmStatusActivity.class);	// Anna 2012-07-07
				Intent intent = new Intent();
				intent.putExtra("com.webservice.domain.StockToIssue",stockToIssue);
		//		intent.putExtra("com.webservice.domain.StatusList", statusList);	// Anna 2012-07-07
		// 		startActivityForResult(intent, SCROLL_REQUEST_CODE);				// Anna 2012-07-07
		// Anna 2012-07-07
				Log.v("ScrollActivity",newlyCreatedjobHistory.getUpdateTimesheet());
				intent.putExtra("com.webservice.domain.JobHistory",newlyCreatedjobHistory);
				setResult(1000, intent);
				this.finish();
		// Anna end	
			}
				;
				break;
			}
		} else if (popupIndicator == 2) {
			Intent returnIntent = new Intent();
			JobHistory jobHistory = newlyCreatedjobHistory;
			switch (which) {
			case DialogInterface.BUTTON_POSITIVE: { // Yes button clicked
				returnIntent.putExtra("confirmstatus", "qsetting");
			}
				;
				break;
			case DialogInterface.BUTTON_NEGATIVE: { // No button clicked
				returnIntent.putExtra("confirmstatus", "0");
			}
				;
				break;
			}
			returnIntent.putExtra("com.webservice.domain.JobHistory",
					jobHistory);
			setResult(1550, returnIntent);
			this.finish();
		} else if (popupIndicator==5) {
			Intent returnIntent = new Intent();
			switch (which) {
			   case DialogInterface.BUTTON_POSITIVE: { // Yes button clicked
				    setResult(9990, returnIntent);
					this.finish();
			    }; break;
			} 
		}
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		String historyDate="";
		newlyCreatedjobHistory = jobHistoryIntent.getParcelableExtra("com.webservice.domain.JobHistory");
		String errorMessage = "";
		if (id == submitButton.getId()) {
			errorMessage = validateJobHistory(newlyCreatedjobHistory);
			if (errorMessage.length() > 0) {
				popupDialog.showPopupTabDialog(errorMessage, "OK");
			}	else {
			          popupIndicator = 1;
			          historyDate=newlyCreatedjobHistory.getHistoryDate();
			          if (historyDate.length()>10) {
			        	  historyDate = historyDate.substring(0,10);
			          } else if (historyDate.length()==10) {
			        	  newlyCreatedjobHistory.setHistoryDate(historyDate+" 00:00:00");
			          }
			          newlyCreatedjobHistory.setUpdateTimesheet(applicationContext.getDisableTimes());
			          newlyCreatedjobHistory.setFinishTime(historyDate+" "+newlyCreatedjobHistory.getFinishTime());
			          newlyCreatedjobHistory.setStartTime(historyDate+" "+newlyCreatedjobHistory.getStartTime());
			          popupDialog.showPopupTabDialog("Is this job complete ?", "Yes","No");
					}
		} else 	if (id == cancelButton.getId()) {
			popupIndicator = 5;
			popupDialog.showPopupTabDialog("Are you sure you want to cancel ?", "Yes","No");
		} else if (errorMessage.length() > 0) {
			popupDialog.showPopupTabDialog(errorMessage, "OK");
		}
	}

	public void update(String data) {
		popupIndicator = 5;
		popupDialog.showPopupTabDialog(
				"Stock successfully issued and jobs have been updated !", "OK");

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		String stockWithHistories = applicationContext.getAllowHistories();
		confirmationStatus = data.getStringExtra("confirmstatus");
		if (stockWithHistories.equalsIgnoreCase("1")) {
			popupIndicator = 4;
			popupDialog
					.showPopupTabDialog(
							"This history has stock attached, do you want to upload this history now ?",
							"Yes", "No");
		}
		Intent returnIntent = new Intent();
//		returnIntent.putExtra("confirmstatus", confirmationStatus);			// Anna 2012-07-07
		Log.v("ScrollActivity",newlyCreatedjobHistory.getUpdateTimesheet());
		returnIntent
				.putExtra("com.webservice.domain.JobHistory", newlyCreatedjobHistory);
		setResult(1000, returnIntent);
		this.finish();
	}

	private void initTabsAppearance(TabWidget tab_widget) {
		String stockWithHistories = applicationContext.getAllowHistories();
		String displayJobs = applicationContext.getDisplayJobs();
		if (stockWithHistories.equalsIgnoreCase("0")) {
			tabHost.getTabWidget().getChildAt(1).setVisibility(View.GONE);
		} else {
			tabHost.getTabWidget()
					.getChildAt(1)
					.setBackgroundColor(
							Color.parseColor(OnTabListener.TAB_UNSELECTED_COLOR));
			tabHost.getTabWidget().getChildAt(1).getLayoutParams().height = OnTabListener.TAB_HEIGHT;

		}
		if (displayJobs.equalsIgnoreCase("0")) {
			tabHost.getTabWidget().getChildAt(2).setVisibility(View.GONE);
		} else {
			// Change background
			tabHost.getTabWidget()
					.getChildAt(2)
					.setBackgroundColor(
							Color.parseColor(OnTabListener.TAB_UNSELECTED_COLOR));
			tabHost.getTabWidget().getChildAt(2).getLayoutParams().height = OnTabListener.TAB_HEIGHT;
		}
	}

	private String validateJobHistory(JobHistory jobHistory) {
		String message = "";
		String displayMessage = "The following errors must be corrected";
		String value;
		boolean fault = false;

		if (applicationContext.getDisableTimes().equalsIgnoreCase("0")) {
		  value = jobHistory.getTimeCode().trim();
		  if (!(value.length() > 0)) {
			displayMessage = displayMessage + ", Time code must be chosen";
			fault = true;
		  }
		}
		
		if (applicationContext.getDisableTimes().equalsIgnoreCase("0")) {
		  value = jobHistory.getTimeSpent().trim();
		  if (!(value.length() > 0)) {
			displayMessage = displayMessage + ", Time spent must be chosen";
			fault = true;
		  }
		}

		if ((jobHistory.getHistoryDate() == null)
				|| (jobHistory.getHistoryDate().length() == 0)) {
			displayMessage = displayMessage + ", History date must be entered";
			fault = true;
		}
		if ((!fault) && (invalidHistoryDate(jobHistory.getHistoryDate()))) {
			displayMessage = displayMessage + ", History date must be in the format DD/MM/YYYY";
			fault = true;
		}
		value = jobHistory.getDetails().trim();
		if (!(value.length() > 0)) {
			displayMessage = displayMessage
					+ ", History details must be entered";
			fault = true;
		}
		
		if (applicationContext.getDisableTimes().equalsIgnoreCase("0")) {
		   if ((!fault) && (!validJobHistoryTime(jobHistory.getStartTime(),jobHistory.getFinishTime()))) {
			  displayMessage = displayMessage
					+ ", Start time must be before the finish time";
			  fault = true;
		   }
		   if ((!fault)
				&& (!validJobHistoryOverlapTime(jobHistory.getStartTime(),
						jobHistory.getFinishTime()))) {
			   displayMessage = displayMessage
					+ ", From and To times overlap with another Job History";
			   fault = true;
		   }
		   if (fault) {
			   displayMessage = displayMessage + ".";
			   message = displayMessage;
		   }
	    }
		return message;
	}
	
	private boolean validJobHistoryOverlapTime(String startTime, String finishTime) {
		boolean validTime = true;
        String historyDate = jobHistory.getHistoryDate();
        if (historyDate.length()>10) {
        	historyDate = jobHistory.getHistoryDate().substring(0, 10);
        }
		Date newStartDate = DateUtil.toDate(historyDate+" "+startTime, STANDARD_DATE_FORMAT);
		Date newFinishDate = DateUtil.toDate(historyDate+" "+finishTime, STANDARD_DATE_FORMAT);
		String newActionDate = historyDate;
		String oldActionDate = "";
		String currentStartTime="";
		String currentFinishTime="";
		Date storedFinishDate;
		Date storedStartDate;
		JobList tempJobList = new JobList();
		Iterator<JobHistory> iter = null;
		tempJobList.setJobList(jobList.getJobList());
		tempJobList.getJobList().add(selectedJob);
		Iterator<Job> jobIterator = tempJobList.getJobList().iterator();
		List<JobHistory> jobHistoryList=null;
		JobHistory jobHistory = null;
		int count=0;
		int numberOfHistories;
		while ((jobIterator.hasNext()) && (validTime)) {
			jobHistoryList = jobIterator.next().getJobHistoryList();
			iter = jobHistoryList.iterator();
			numberOfHistories = jobHistoryList.size(); 
			count=0;
 		    while (iter.hasNext()) {
			    jobHistory = iter.next();
                oldActionDate = jobHistory.getStartTime();
                if ((oldActionDate!=null) && (oldActionDate.length()>9)) {
            	   oldActionDate = oldActionDate.substring(0,10);
                } else {
			        oldActionDate = "";
                  }
			    if (newActionDate.equalsIgnoreCase(oldActionDate)) {
			      currentStartTime = jobHistory.getStartTime();
			      currentFinishTime = jobHistory.getFinishTime();
			      if (currentStartTime.length()<11) 
			    	  currentStartTime = currentStartTime + " 00:00:00";
			      if (currentFinishTime.length()<11) 
			    	  currentFinishTime = currentFinishTime + " 00:00:00";
				  storedFinishDate = DateUtil.toDate(currentFinishTime, STANDARD_DATE_FORMAT);
				  storedStartDate = DateUtil.toDate(currentStartTime, STANDARD_DATE_FORMAT);
				  if (((newStartDate.after(storedFinishDate)) || (newStartDate.equals(storedFinishDate))) || 
				    ((newFinishDate.before(storedStartDate)) || (newFinishDate.equals(storedStartDate)))) {
							count=count+1;
				  } 
			    } else {
				        count=count+1;
			       }
		    }
		    if (count != numberOfHistories) {
		    	validTime = false;
		    }
		}
		return validTime;
	}

	private boolean validJobHistoryTime(String startTime, String finishTime) {
		String historyDate = jobHistory.getHistoryDate();
        if (historyDate.length()>10) {
        	historyDate = jobHistory.getHistoryDate().substring(0, 10);
        }
		Date startDate = DateUtil.toDate(historyDate+" "+startTime, STANDARD_DATE_FORMAT);
		Date finishDate = DateUtil.toDate(historyDate+" "+finishTime, STANDARD_DATE_FORMAT);
		boolean validTime = true;
		Log.v("Valid Job History Time", "StartTime:"+startDate);
		Log.v("Valid Job History Time", "FinishTime:"+finishDate);
		if (startDate.compareTo(finishDate)>=0) {
			validTime = false;
		}
		return validTime;
	}
	
	private boolean invalidHistoryDate(String historyDate) {
		Date startDate = DateUtil.toDate(historyDate, SIMPLE_DATE_FORMAT);
		boolean invalidDate = false;
		if (((historyDate.length()!=10) && (historyDate.length()!=19)) || (startDate==null)) {
			invalidDate = true;
		}
		return invalidDate;
	}
}