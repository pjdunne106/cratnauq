package com.webservice.android.xml;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.webservice.domain.LabourTime;


public class LabourTimeHandler extends DefaultHandler {

	Boolean currentElement = false;
	String currentValue = null;
	String supervisor="";
	List<LabourTime> labourTimeList = null;
	LabourTime labourTime;
	String name = null;

	public List<LabourTime> getLabourTimeList() {
		return labourTimeList;
	}

	public void setLabourTimeList(List<LabourTime> labourTimeList) {
		this.labourTimeList = labourTimeList;
	}

	/**
	 * Called when tag starts ( ex:- <name>AndroidPeople</name> -- <name> )
	 */
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {

		currentElement = true;

		if (localName.equals("labourTimeCodes")) {
			/** Start */
			labourTimeList = new ArrayList<LabourTime>();
		}
		
		if (localName.equals("labourTimeCode")) {
			/** Start */
			labourTime=new LabourTime();
			labourTimeList.add(labourTime);
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
		if (localName.equalsIgnoreCase("timeCode"))
			labourTime.setCode(currentValue);
		else if (localName.equalsIgnoreCase("timeDesc"))
			labourTime.setDescription(currentValue);
		else if (localName.equalsIgnoreCase("timeSpent"))
			labourTime.setSpent(currentValue);
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
			currentElement = false;
		}

	}

}

