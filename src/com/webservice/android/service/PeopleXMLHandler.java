package com.webservice.android.service;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class PeopleXMLHandler extends DefaultHandler {

	Boolean currentElement = false;
	String currentValue = null;
	PeopleList peopleList = null;

	public PeopleList getPeopleList() {
		return peopleList;
	}

	public void setPeopleList(PeopleList peopleList) {
		this.peopleList = peopleList;
	}

	/**
	 * Called when tag starts ( ex:- <name>AndroidPeople</name> -- <name> )
	 */
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {

		currentElement = true;

		if (localName.equals("People")) {
			/** Start */
			peopleList = new PeopleList();
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
		if (localName.equalsIgnoreCase("fullname"))
			peopleList.setFullName(currentValue);
		else if (localName.equalsIgnoreCase("title"))
			peopleList.setTitle(currentValue);
		else if (localName.equalsIgnoreCase("dob"))
			peopleList.setDob(currentValue);
		else if (localName.equalsIgnoreCase("status"))
			peopleList.setStatus(currentValue);
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
