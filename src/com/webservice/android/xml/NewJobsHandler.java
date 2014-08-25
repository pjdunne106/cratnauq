package com.webservice.android.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.webservice.domain.NewJob;

public class NewJobsHandler extends DefaultHandler {
	Boolean currentElement = false;
	String currentValue = null;
	NewJob response = null;

	public NewJob getNewJob() {
		return response;
	}

	public void setResponse(NewJob response) {
		this.response = response;
	}

	/**
	 * Called when tag starts ( ex:- <name>AndroidPeople</name> -- <name> )
	 */
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {

		currentElement = true;

		if (localName.equals("RESPONSE")) {
			/** Start */
			response= new NewJob();
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
		if (localName.equalsIgnoreCase("INFO")) {
			response.setInfo(currentValue);
		} else if (localName.equalsIgnoreCase("DETAILS")) {
			response.setDetails(currentValue);
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

