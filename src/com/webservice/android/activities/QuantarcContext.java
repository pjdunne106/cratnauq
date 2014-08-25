package com.webservice.android.activities;

import android.app.Application;
import android.content.SharedPreferences;

import com.webservice.domain.ReferenceDataList;

public class QuantarcContext extends Application {
	    private static String QUEMIS_VERSION="1.67";
	    private static String DEFAULT_SUPERVISOR_NAME="Supervisor";
	    private static String DEFAULT_SUPERVISOR_PASSWORD="quantarc123";
		private SharedPreferences qPrefs;

		@Override
		public void onCreate() {
			super.onCreate();
			qPrefs = this.getPreferences();
		}
		
		public void saveServerType(String value) {
			SharedPreferences.Editor editor = qPrefs.edit();
			editor.putString("servertype", value);
			editor.commit();
		}
		
		public String getDefaultServerType() {
			return "php";
		}
		
		public void setNewHistories(String value) {
			SharedPreferences.Editor editor = qPrefs.edit();
			editor.putString("newhistories", value);
			editor.commit();
		}
		
		
		public String getNewHistories() {
			String value = qPrefs.getString("newhistories", null);
			if ((value==null) || (value.length()==0)) {
				value="false";
			}
			return value;
		}
		public String getServerType() {
			String value = qPrefs.getString("servertype", null);
			if ((value==null) || (value.length()==0)) {
				value="asp";
			}
			return value;
		}
		
		public void saveDisableJobsOnTimesheet(String value) {
			SharedPreferences.Editor editor = qPrefs.edit();
			editor.putString("disablejobsontimesheet", value);
			editor.commit();
		}
		
		public String getDisableJobsOnTimesheet() {
			String value = qPrefs.getString("disablejobsontimesheet", null);
			if ((value==null) || (value.length()==0)) {
				value="0";
			}
			return value;
		}
		
		public void saveIssueStockStatus(String value) {
			SharedPreferences.Editor editor = qPrefs.edit();
			editor.putString("issuestock", value);
			editor.commit();
		}
		
		public String getQuemisVersion() {
			return QUEMIS_VERSION;
		}
		
		public String getIssueStockStatus() {
			String value = qPrefs.getString("issuestock", null);
			if ((value==null) || (value.length()==0)) {
				value="0";
			}
			return value;
		}
		
		public void saveTimeBand(String value) {
			SharedPreferences.Editor editor = qPrefs.edit();
			editor.putString("timeband", value);
			editor.commit();
		}
		
		public String getTimeBand() {
			String value = qPrefs.getString("timeband", null);
			if ((value==null) || (value.length()==0)) {
				value="0";
			}
			return value;
		}
		
		public void saveJobData(String value) {
			SharedPreferences.Editor editor = qPrefs.edit();
			editor.putString("jobdata", value);
			editor.commit();
		}
		
		public String getJobData() {
			String value = qPrefs.getString("jobdata", null);
			if ((value==null) || (value.length()==0)) {
				value="";
			}
			return value;
		}
		
		public void saveDefaultFinish(String value) {
			SharedPreferences.Editor editor = qPrefs.edit();
			editor.putString("defaultfinish", value);
			editor.commit();
		}
		
		public String getDefaultFinish() {
			String value = qPrefs.getString("defaultfinish", null);
			if ((value==null) || (value.length()==0)) {
				value="00:00";
			}
			return value;
		}

		public void saveDefaultStart(String value) {
			SharedPreferences.Editor editor = qPrefs.edit();
			editor.putString("defaultstart", value);
			editor.commit();
		}
		
		public String getDefaultStart() {
			String value = qPrefs.getString("defaultstart", null);
			if ((value==null) || (value.length()==0)) {
				value="00:00";
			}
			return value;
		}
		
		public void saveSchedule(String value) {
			SharedPreferences.Editor editor = qPrefs.edit();
			editor.putString("schedule", value);
			editor.commit();
		}
		
		public String getSchedule() {
			String value = qPrefs.getString("schedule", null);
			if ((value==null) || (value.length()==0)) {
				value=null;
			}
			return value;
		}
		
		public void saveAutoCheckRunning(String value) {
			SharedPreferences.Editor editor = qPrefs.edit();
			editor.putString("autocheckactive", value);
			editor.commit();
		}
		
		public String getAutoCheckRunning() {
			String value = qPrefs.getString("autocheckactive", null);
			if ((value==null) || (value.length()==0)) {
				value="0";
			}
			return value;
		}
		
		public void saveAutoCheck(String value) {
			SharedPreferences.Editor editor = qPrefs.edit();
			editor.putString("autocheck", value);
			editor.commit();
		}
		
		public String getAutoCheck() {
			String value = qPrefs.getString("autocheck", null);
			if ((value==null) || (value.length()==0)) {
				value="0";
			}
			return value;
		}
		
		public void saveDisableTimes(String value) {
			SharedPreferences.Editor editor = qPrefs.edit();
			editor.putString("disabletimes", value);
			editor.commit();
		}
		
		public String getDisableTimes() {
			String value = qPrefs.getString("disabletimes", null);
			if ((value==null) || (value.length()==0)) {
				value="0";
			}
			return value;
		}
		
		public void saveRememberUser(String value) {
			SharedPreferences.Editor editor = qPrefs.edit();
			editor.putString("rememberuser", value);
			editor.commit();
		}
		
		public String getDefaultRememberUser() {
			return "1";
		}
		
		public String getRememberUser() {
			String value = qPrefs.getString("rememberuser", null);
			if ((value==null) || (value.length()==0)) {
				value="0";
			}
			return value;
		}
		
		public String getFromActivity() {
			String name = qPrefs.getString("fromactivity", null);
			if ((name==null) || (name.length()==0)) {
				name="login";
			}
			return name;
		}

		public void saveFromActivity(String activityName) {
			SharedPreferences.Editor editor = qPrefs.edit();
			editor.putString("fromactivity", activityName);
			editor.commit();
		}
		
		public String getUserId() {
			return qPrefs.getString("userid", null);
		}

		public void saveUserId(String userId) {
			SharedPreferences.Editor editor = qPrefs.edit();
			editor.putString("userid", userId);
			editor.commit();
		}
		
		public String getUpdateInterval() {
			return qPrefs.getString("updateinterval", null);
		}
		
		public void saveUpdateInterval(String interval) {
		    SharedPreferences.Editor editor = qPrefs.edit();
			editor.putString("updateinterval", interval);
			editor.commit();
		}
		
		public String getSectionId() {
			return qPrefs.getString("sectionid", null);
		}
		
		public void saveSectionId(String sectionId) {
		    SharedPreferences.Editor editor = qPrefs.edit();
			editor.putString("sectionid", sectionId);
			editor.commit();
		}
		
		public String getUsername() {
			return qPrefs.getString("username", null);
		}
		
		public void saveUsername(String userName) {
		    SharedPreferences.Editor editor = qPrefs.edit();
			editor.putString("username", userName);
			editor.commit();
		}
		
		public String getGroupId() {
			return qPrefs.getString("groupid", null);
		}
		
		public void saveGroupId(String groupId) {
		    SharedPreferences.Editor editor = qPrefs.edit();
			editor.putString("groupid", groupId);
			editor.commit();
		}
		
		public String getUserPassword() {
			return qPrefs.getString("password", null);
		}
		
		public void saveUserPassword(String password) {
			SharedPreferences.Editor editor = qPrefs.edit();
			editor.putString("password", password);
			editor.commit();
		}
		
		public String getDefaultShortTargetDate() {
			return "1";
		}
		
		public String getShortTargetDate() {
			return qPrefs.getString("shorttargetdate", null);
		}
		
		public void saveShortTargetDate(String shortdate) {
			SharedPreferences.Editor editor = qPrefs.edit();
			editor.putString("shorttargetdate", shortdate);
			editor.commit();
		}
		
		public String getDefaultNumberOfWeeks() {
			return "8";
		}

		public String getNumberOfWeeks() {
			String value = qPrefs.getString("numberofweeks", null);
			if (value==null) {
				value = getDefaultNumberOfWeeks();
			}
			return value;
		}
		
		public void saveNumberOfWeeks(String password) {
			SharedPreferences.Editor editor = qPrefs.edit();
			editor.putString("numberofweeks", password);
			editor.commit();
		}

		public String getDefaultLoginStaff() {
			String value="0";
			return value;
		}
		
		public String getLoginStaff() {
			String value = qPrefs.getString("loginstaff", null);
			if (value==null) {
				value="0";
			}
			return value;
		}
		
		public void saveLoginStaff(String password) {
			SharedPreferences.Editor editor = qPrefs.edit();
			editor.putString("loginstaff", password);
			editor.commit();
		}
		
		public String getDefaultSettingsVisible() {
			String value="0,0,0,0,0,0,0,0";
			return value;
		}
		
		public String getSettingsVisible() {
			String value = null; //qPrefs.getString("visible", null);
			if (value==null) {
				value=getDefaultSettingsVisible();
			}
			return value;
		}
		
		public void saveSettingsVisible(String settings) {
			SharedPreferences.Editor editor = qPrefs.edit();
			editor.putString("visible", settings);
			editor.commit();
		}

		public String getDefaultSettingsOrder() {
			String value="1,2,3,4,5,6,7,8";
			return value;
		}
		
		public String getSettingsOrder() {
			String value = null; // qPrefs.getString("order", null);
			if (value==null) {
				value=getDefaultSettingsOrder();
			}
			return value;
		}
		
		public void saveSettingsOrder(String settings) {
			SharedPreferences.Editor editor = qPrefs.edit();
			editor.putString("order", settings);
			editor.commit();
		}

		public String getDefaultDisplayJobs() {
			String value="0";
			return value;
		}
		
		public String getDisplayJobs() {
			String value = qPrefs.getString("displayjobs", null);
			if (value==null) {
				value="0";
			}
			return value;
		}
		
		public void saveDisplayJobs(String password) {
			SharedPreferences.Editor editor = qPrefs.edit();
			editor.putString("displayjobs", password);
			editor.commit();
		}
		
		public String getDefaultAllowHistories() {
			String value="0";
			return value;
		}

		public String getAllowHistories() {
			String value = qPrefs.getString("allowhistories", null);
			if (value==null) {
				value="0";
			}
			return value;
		}
		
		public void saveAllowHistories(String allowhistories) {
			SharedPreferences.Editor editor = qPrefs.edit();
			editor.putString("allowhistories", allowhistories);
			editor.commit();
		}

		public String getDefaultSettingsRisk() {
			return "no:1";
		}
		
		public String getSettingsRisk() {
			String value = qPrefs.getString("settingsrisk", null);
			if (value==null) {
				value = getDefaultSettingsRisk();
			}
			return value;
		}
		
		public String getDefaultSettingsJobNum() {
			return "yes:1";
		}
		
		public String getSettingsJobNum() {
			String value = qPrefs.getString("settingsjobnum", null);
			if (value==null) {
				value = getDefaultSettingsJobNum();
			}
			return value;
		}
		
		public void saveSettingsRisk(String risk) {
			SharedPreferences.Editor editor = qPrefs.edit();
			editor.putString("settingsrisk", risk);
			editor.commit();
		}
		
		
		public void saveSettingsJobNum(String jobnum) {
			SharedPreferences.Editor editor = qPrefs.edit();
			editor.putString("settingsjobnum", jobnum);
			editor.commit();
		}

		public String getDefaultSettingsSource() {
			return "yes:2";
		}
		
		public String getSettingsSource() {
			String value = qPrefs.getString("settingssource", null);
			if (value==null) {
				value = getDefaultSettingsSource();
			}
			return value;
		}
		
		public void saveSettingsSource(String source) {
			SharedPreferences.Editor editor = qPrefs.edit();
			editor.putString("settingssource", source);
			editor.commit();
		}

		public String getDefaultSettingsStatus() {
			return ("yes:3");
		}
		
		public String getSettingsStatus() {
			String value = qPrefs.getString("settingsstatus", null);
			if (value==null) {
				value = getDefaultSettingsStatus();
			}
			return value;
		}
		
		public void saveSettingsStatus(String status) {
			SharedPreferences.Editor editor = qPrefs.edit();
			editor.putString("settingsstatus", status);
			editor.commit();
		}

		public String getDefaultSettingsTarget() {
			return ("yes:4");
		}
		
		public String getSettingsTarget() {
			String value = qPrefs.getString("settingstarget", null);
			if (value==null) {
				value = getDefaultSettingsTarget();
			}
			return value;

		}
		
		public void saveSettingsTarget(String target) {
			SharedPreferences.Editor editor = qPrefs.edit();
			editor.putString("settingstarget", target);
			editor.commit();
		}

		public String getDefaultSettingsLocation() {
			return ("yes:5");
		}
		
		public String getSettingsLocation() {
			String value = qPrefs.getString("settingslocation", null);
			if (value==null) {
				value = getDefaultSettingsLocation();
				//Log.v("Quantarc","Getting Default Settings Location:"+value);
			} 
			return value;
		}
		
		public void saveSettingsLocation(String location) {
			SharedPreferences.Editor editor = qPrefs.edit();
			editor.putString("settingslocation", location);
			//Log.v("Quantarc","QuantarcContext,Saving Location:"+location);
			editor.commit();
		}

		public String getDefaultSettingsBuilding() {
			return ("yes:7");
		}
		
		public String getSettingsBuilding() {
			String value = qPrefs.getString("settingsbuilding", null);
			if (value==null) {
				value = getDefaultSettingsBuilding();
			}
			return value;
		}
		
		public void saveSettingsBuilding(String building) {
			SharedPreferences.Editor editor = qPrefs.edit();
			editor.putString("settingsbuilding", building);
			editor.commit();
		}

		
		
		
		public String getDefaultSettingsPriority() {
			return ("yes:6");
		}
		
		public String getSettingsPriority() {
			String value = qPrefs.getString("settingspriority", null);
			if (value==null) {
				value = getDefaultSettingsPriority();
			}
			return value;
		}
		
		public void saveSettingsPriority(String priority) {
			SharedPreferences.Editor editor = qPrefs.edit();
			editor.putString("settingspriority", priority);
			editor.commit();
		}
		
		public String getSupervisorName() {
			String name = qPrefs.getString("supervisorname", null);
			if ((name==null) || (name.length()==0)) {
				name=DEFAULT_SUPERVISOR_NAME;
			}
			return name;
		}
		
		public String getSupervisorPassword() {
			String password = qPrefs.getString("supervisorpassword", null);
			if ((password==null) || (password.length()==0)) {
				password=DEFAULT_SUPERVISOR_PASSWORD;
			}
			return password;
		}
		
		public void saveSupervisorPassword(String password) {
			SharedPreferences.Editor editor = qPrefs.edit();
			editor.putString("supervisorpassword", password);
			editor.commit();
		}
		
		public String getServiceUrl() {
			return qPrefs.getString("serviceURL", "");
		}
		
		public void saveServiceURL(String urlText) {
			SharedPreferences.Editor editor = qPrefs.edit();
			editor.putString("serviceURL", urlText);
			editor.commit();
		}
		
		public void populatePage2(ReferenceDataList referenceDataList) {
			referenceDataList.put("loginstaff",this.getLoginStaff());
			referenceDataList.put("disabletimes", this.getDisableTimes());
			referenceDataList.put("displayjobs", this.getDisplayJobs());
			referenceDataList.put("allowhistories", this.getAllowHistories());
			referenceDataList.put("disablejobnumber", this.getDisableJobsOnTimesheet());
			referenceDataList.put("risk", this.getSettingsRisk());
			referenceDataList.put("jobnum", this.getSettingsJobNum());
			referenceDataList.put("source", this.getSettingsSource());
			referenceDataList.put("status", this.getSettingsStatus());
			referenceDataList.put("target", this.getSettingsTarget());
			referenceDataList.put("location", this.getSettingsLocation());
			referenceDataList.put("building", this.getSettingsBuilding());
			referenceDataList.put("priority", this.getSettingsPriority());
		}
		
		private SharedPreferences getPreferences() {
			SharedPreferences preferences=null;
			String quantarcFile = getResources().getString(R.string.quantarcFilename);
			preferences =getSharedPreferences(quantarcFile,MODE_PRIVATE);
			return preferences;
		}
}
