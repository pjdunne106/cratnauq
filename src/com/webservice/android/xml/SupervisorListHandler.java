package com.webservice.android.xml;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SupervisorListHandler extends DefaultHandler {

	Boolean currentElement = false;
	String currentValue = null;
	String supervisor="";
	List<String> supervisorList = null;
	String name = null;

	public List<String> getSupervisorList() {
		return supervisorList;
	}

	public void setSupervisorList(List<String> supervisorList) {
		this.supervisorList = supervisorList;
	}

	/**
	 * Called when tag starts ( ex:- <name>AndroidPeople</name> -- <name> )
	 */
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {

		currentElement = true;

		if (localName.equals("staffMembers")) {
			/** Start */
			supervisorList = new ArrayList<String>();
		}

		
		if (localName.equals("labourPerson")) {
			/** Start */
			if ((supervisor.length()>0) && (supervisor.equalsIgnoreCase("1"))) {
				supervisorList.add(name);
			}
			supervisor="";
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
		if (localName.equalsIgnoreCase("individual_name"))
			name = currentValue;
		else if (localName.equalsIgnoreCase("supervisor"))
			supervisor = currentValue;

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

