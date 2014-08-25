package com.webservice.android.xml;

import java.util.HashMap;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.webservice.domain.LabourGroupList;

public class LabourGroupHandler extends DefaultHandler {

	Boolean currentElement = false;
	String currentValue = null;
	String id="";
	LabourGroupList labourGroupList = null;
	Map<String,String> labourGroupMap;

	public LabourGroupList getLabourGroupList() {
		labourGroupList.setLabourGroupMap(labourGroupMap);
		return labourGroupList;
	}

	public void setLabourGroupList(LabourGroupList labourGroupList) {
		this.labourGroupList = labourGroupList;
	}

	/**
	 * Called when tag starts ( ex:- <name>AndroidPeople</name> -- <name> )
	 */
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {

		currentElement = true;

		if (localName.equals("labourGroups")) {
			/** Start */
			labourGroupList = new LabourGroupList();
			labourGroupMap = new HashMap<String, String>();
		}
		
		if (localName.equals("labourGroup")) {
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
		if (localName.equalsIgnoreCase("labourGroup"))
			    labourGroupMap.put(id, currentValue);
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

