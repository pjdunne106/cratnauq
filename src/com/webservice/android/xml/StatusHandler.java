package com.webservice.android.xml;

import java.util.HashMap;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.webservice.domain.StatusList;

public class StatusHandler extends DefaultHandler {

	Boolean currentElement = false;
	String currentValue = null;
	String id="";
	StatusList statusList = null;
	Map<String,String> statusMap;

	public StatusList getStatusList() {
		statusList.setStatusMap(statusMap);
		return statusList;
	}

	public void setStatusList(StatusList statusList) {
		this.statusList = statusList;
	}

	/**
	 * Called when tag starts ( ex:- <name>AndroidPeople</name> -- <name> )
	 */
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {

		currentElement = true;

		if (localName.equals("jobStatus")) {
			/** Start */
			statusList = new StatusList();
			statusMap = new HashMap<String, String>();
		}
		
		if (localName.equals("status")) {
			/** Start */
			id = attributes.getValue("id");
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
		if (localName.equalsIgnoreCase("status"))
			if (currentValue.equalsIgnoreCase("qsetting")) {
				statusList.setQsetting(id);
			} else {
			          statusMap.put(id, currentValue);
	          } 
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

