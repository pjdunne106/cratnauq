package com.webservice.android.xml;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.webservice.domain.Timesheet;

public class TimesheetListHandler extends DefaultHandler {

	Boolean currentElement = false;
	String currentValue = null;
	String date="";
	String dateFormat="dd/MM/yyyy";
	StringBuilder xmlText = new StringBuilder();
	List<Timesheet> timesheetList = null;
	Timesheet timesheet = null;

	public TimesheetListHandler() {
		timesheetList = new ArrayList<Timesheet>();
	}
	
	public List<Timesheet> getTimesheetList() {
		return timesheetList;
	}

	public void setTimesheetList(List<Timesheet> jobList) {
		this.timesheetList = timesheetList;
	}

	/**
	 * Called when tag starts ( ex:- <name>AndroidPeople</name> -- <name> )
	 */
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {

		currentElement = true;

		if (localName.equals("TimeSheetEntry")) {
			/** Start */
			timesheet = new Timesheet();
			timesheet.setIsNew("0");
			timesheet.setSpent(" ");
			timesheetList.add(timesheet);
		} 
	}

	/**
	 * Called when tag closing ( ex:- <name>AndroidPeople</name> -- </name> )
	 */
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

		currentElement = false;
		currentValue = xmlText.toString().trim();
		currentValue = currentValue.replaceAll("(\\r|\\n)", ""); 

		/** set value */
		if (localName.equalsIgnoreCase("id"))
			timesheet.setWorkId(currentValue);
		else if (localName.equalsIgnoreCase("job_num"))
			timesheet.setJobNum(currentValue);
		else if (localName.equalsIgnoreCase("job_ref"))
			timesheet.setJobRef(currentValue);
		else if (localName.equalsIgnoreCase("start_time"))
			timesheet.setStartTime(currentValue);
		else if (localName.equalsIgnoreCase("finish_time"))
			timesheet.setFinishTime(currentValue);
		else if (localName.equalsIgnoreCase("action_date")) {
			date = currentValue;
			timesheet.setActionDate(currentValue);
		    }
		else if (localName.equalsIgnoreCase("time_code"))
			timesheet.setTimeCode(currentValue);
		 xmlText = new StringBuilder(); 
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
	
	private String toFormattedDate(String oldDate) {
		String toDate=oldDate;
	    return toDate;
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




