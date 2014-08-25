package com.webservice.android.xml;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.webservice.domain.JobDetails;

public class JobDetailsHandler extends DefaultHandler {

	Boolean currentElement = false;
	String currentValue = null;
	List<JobDetails> jobList = null;
	JobDetails jobDetails = null;

	public List<JobDetails> getJobDetailsList() {
		return jobList;
	}

	public void setJobDetailsList(List<JobDetails> jobDetailsList) {
		this.jobList = jobDetailsList;
	}

	/**
	 * Called when tag starts ( ex:- <name>AndroidPeople</name> -- <name> )
	 */
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {

		currentElement = true;

		if (localName.equals("jobDetails")) {
			/** Start */
			jobList = new ArrayList<JobDetails>();
		}
		
		if (localName.equals("details")) {
			/** Start */
			jobDetails = new JobDetails();
			jobDetails.setId(attributes.getValue("id"));
			jobList.add(jobDetails);
		} 
	}

	/**
	 * Called when tag closing ( ex:- <name>AndroidPeople</name> -- </name> )
	 */
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {

		currentElement = false;

		/** set value */
		if ((currentValue==null) || (currentValue.length()==0)) {
			currentValue=" ";
		}
		if (localName.equalsIgnoreCase("details"))
			jobDetails.setJobDetails(currentValue);

		currentValue="";
	}

	/**
	 * Called to get tag characters ( ex:- <name>AndroidPeople</name> -- to get
	 * AndroidPeople Character )
	 */
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {

		if (currentElement) {
			currentValue = new String(ch, start, length);
			if ((currentValue==null) || (currentValue.length()==0)) {
				currentValue=" ";
			}
			currentElement = false;
		}

	}

}

