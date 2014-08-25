package com.webservice.android.xml;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.webservice.domain.Location;

public class LocationHandler extends DefaultHandler {

	Boolean currentElement = false;
	String currentValue = null;
	String id;
	List<Location> locationList = null;
	Location location = null;

	public List<Location> getLocationList() {
		return locationList;
	}

	public void setLocationList(List<Location> locationList) {
		this.locationList = locationList;
	}

	/**
	 * Called when tag starts ( ex:- <name>AndroidPeople</name> -- <name> )
	 */
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {

		currentElement = true;

		if (localName.equals("stockLocations")) {
			/** Start */
			locationList = new ArrayList<Location>();
		}
		
		if (localName.equals("stockLocation")) {
			/** Start */
			id = attributes.getValue("id");
			location = new Location();
			location.setId(id);
			locationList.add(location);
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
		if (localName.equalsIgnoreCase("loc_ref"))
			location.setReference(currentValue);
		else if (localName.equalsIgnoreCase("loc_description"))
			location.setDescription(currentValue);
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

