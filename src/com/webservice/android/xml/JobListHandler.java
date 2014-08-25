package com.webservice.android.xml;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.webservice.domain.Job;
import com.webservice.domain.JobHistory;
import com.webservice.domain.PPMAsset;
import com.webservice.domain.PPMCheck;
import com.webservice.domain.PPMRisk;
import com.webservice.domain.Risk;
import com.webservice.domain.StockIssue;

public class JobListHandler extends DefaultHandler {

	Boolean currentElement = false;
	boolean history;
	String currentValue = null;
	StringBuilder xmlText = new StringBuilder();
	List<Job> jobList = null;
	List<StockIssue> stockIssues;
	List<Risk> risks;
	List<PPMCheck> ppmchecks;
	List<PPMRisk> ppmrisks;
	List<PPMAsset> ppmassets;
	Job job = null;
	JobHistory jobHistory = null;
	StockIssue stockIssue=null;
	Risk risk=null;
	PPMCheck ppmcheck=null;
	Integer historyId=0;
	PPMRisk ppmrisk=null;
	PPMAsset ppmasset=null;

	
	public JobListHandler() {
		jobList = new ArrayList<Job>();
		
	}
	
	public List<Job> getJobList() {
		return jobList;
	}

	public void setJobList(List<Job> jobList) {
		this.jobList = jobList;
	}

	/**
	 * Called when tag starts ( ex:- <name>AndroidPeople</name> -- <name> )
	 */
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {

		currentElement = true;

		if (localName.equals("jobs")) {
			/** Start */
			history=false;
		}
		
		if (localName.equals("job")) {
			/** Start */
			job = new Job();
			job.setCostOfMaterial("0");
			stockIssues = new ArrayList<StockIssue>();
			risks = new ArrayList<Risk>();
			ppmchecks = new ArrayList<PPMCheck>();
			ppmrisks = new ArrayList<PPMRisk>();
			ppmassets = new ArrayList<PPMAsset>();
			job.setRisks(risks);
			job.setStockIssues(stockIssues);
			job.setPPMChecks(ppmchecks);
			job.setPPMAssets(ppmassets);
			job.setPPMRisks(ppmrisks);
			jobList.add(job);
		} 
		
		if (localName.equals("stockIssue")) {
			/** Start */
			stockIssue = new StockIssue();
			stockIssues.add(stockIssue);
		} 
		
		if (localName.equals("risk")) {
			/** Start */
			risk = new Risk();
			risks.add(risk);
		} 
		
		if (localName.equals("check")) {
			/** Start */
			ppmcheck = new PPMCheck();
			ppmchecks.add(ppmcheck);
		} 
		
		if (localName.equals("ppmrisk")) {
			/** Start */
			ppmrisk = new PPMRisk();
			ppmrisks.add(ppmrisk);
		} 
		
		if (localName.equals("ppmasset")) {
			/** Start */
			ppmasset = new PPMAsset();
			ppmassets.add(ppmasset);
		} 
		
		if (localName.equals("JOBHISTORY")) {
			/** Start */
			jobHistory = new JobHistory();
			historyId=historyId+1;
			jobHistory.setId(historyId);
			jobHistory.setReadOnly("false");
			jobHistory.setInTimesheet("true");
			jobHistory.setToBeRemoved("0");
			jobHistory.setUpdateTimesheet("0");
			
			jobHistory.setJobStatus(job.getJobStatus());
			history=true;
			job.addJobHistory(jobHistory);
		} 
	}

	/**
	 * Called when tag closing ( ex:- <name>AndroidPeople</name> -- </name> )
	 */
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {

		currentElement = false;
		currentValue = xmlText.toString().trim();
		currentValue = currentValue.replaceAll("(\\r|\\n)", ""); 

		/** set value */
		if ((currentValue==null) || (currentValue.length()==0)) {
			currentValue=" ";
		}
		if (localName.equalsIgnoreCase("site"))
			job.setSite(currentValue);
		else if (localName.equalsIgnoreCase("jobNum"))
			job.setJobNum(currentValue);
		else if (localName.equalsIgnoreCase("sourceId"))
			job.setSourceId(currentValue);
		else if (localName.equalsIgnoreCase("locDesc"))
			job.setLocDesc(currentValue);
		else if (localName.equalsIgnoreCase("locRef"))
			job.setLocRef(currentValue);
		else if (localName.equalsIgnoreCase("building"))		// Anna 2012-07-07 added building
			job.setBuilding(currentValue);
		else if ((localName.equalsIgnoreCase("labourPerson")) && (!history))
			job.setLabourPerson(currentValue);
		else if (localName.equalsIgnoreCase("jobStatus"))
			job.setJobStatus(currentValue);
		else if (localName.equalsIgnoreCase("jobDetails"))
			job.setJobDetails(currentValue);
		else if (localName.equalsIgnoreCase("otherDetails"))	// Anna 2012-07-12 added otherDetails
			job.setOtherDetails(currentValue);
		else if (localName.equalsIgnoreCase("requestDetails"))
			job.setRequestDetails(currentValue);
		else if (localName.equalsIgnoreCase("targetDate"))
			job.setTargetDate(currentValue);
		else if (localName.equalsIgnoreCase("tel_ext"))
			job.setTelExt(currentValue);
		else if (localName.equalsIgnoreCase("requestor"))
			job.setRequestor(currentValue);
		else if (localName.equalsIgnoreCase("jobType"))
			job.setJobType(currentValue);
		else if (localName.equalsIgnoreCase("jobNumLong"))
			job.setJobNumLong(currentValue);
		else if (localName.equalsIgnoreCase("source"))
			job.setSource(currentValue);
		else if (localName.equalsIgnoreCase("priorityCode"))
			job.setPriorityCode(currentValue);
		else if (localName.equalsIgnoreCase("historyDate"))
			jobHistory.setHistoryDate(currentValue);
		else if (localName.equalsIgnoreCase("details"))
			jobHistory.setDetails(currentValue);
		else if ((localName.equalsIgnoreCase("labourPerson")) && (history))
			jobHistory.setLabourPersonId(currentValue);
		else if (localName.equalsIgnoreCase("hoursWorked"))
			jobHistory.setHoursWorked(currentValue);
		else if (localName.equalsIgnoreCase("READONLY"))
			jobHistory.setReadOnly(currentValue);
		else if (localName.equalsIgnoreCase("startTime"))
			jobHistory.setStartTime(currentValue);
		else if (localName.equalsIgnoreCase("finishTime"))
			jobHistory.setFinishTime(currentValue);
		else if (localName.equalsIgnoreCase("partNum"))
			stockIssue.setPartNumber(currentValue);
		else if (localName.equalsIgnoreCase("partDesc")) 
			stockIssue.setPartDescription(currentValue);
		else if (localName.equalsIgnoreCase("itemCount"))
			stockIssue.setItemCount(currentValue);
		else if (localName.equalsIgnoreCase("type"))
			risk.setRiskType(currentValue);
		else if (localName.equalsIgnoreCase("action"))
			risk.setRiskAction(currentValue);
		else if (localName.equalsIgnoreCase("furtherdetails"))
			risk.setFurtherDetails(currentValue);
		else if (localName.equalsIgnoreCase("helpDeskAction"))
			risk.setHelpDeskAction(currentValue);
		else if (localName.equalsIgnoreCase("description"))
			risk.setDescription(currentValue);
		else if (localName.equalsIgnoreCase("taskID"))
			ppmcheck.setTaskId(currentValue);
		else if (localName.equalsIgnoreCase("task"))
			ppmcheck.setTask(currentValue);
		else if (localName.equalsIgnoreCase("ppmriskassessment"))
			ppmrisk.setRiskassessment(currentValue);
		else if (localName.equalsIgnoreCase("ppmmethodstatement"))
			ppmrisk.setMethodStatement(currentValue);
		else if (localName.equalsIgnoreCase("ppmassetinformation"))
			ppmasset.setInformation(currentValue);
		else if (localName.equalsIgnoreCase("ppmassetdescription"))
			ppmasset.setDescription(currentValue);
		currentValue="";
		xmlText = new StringBuilder();
	}

	/**
	 * Called to get tag characters ( ex:- <name>AndroidPeople</name> -- to get
	 * AndroidPeople Character )
	 */
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		 xmlText.append(ch, start, length);
	}

}

