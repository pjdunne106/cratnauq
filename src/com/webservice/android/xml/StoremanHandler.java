package com.webservice.android.xml;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.webservice.domain.Storemen;


public class StoremanHandler extends DefaultHandler {

	Boolean currentElement = false;
	String currentValue = null;
	String supervisor="";
	List<Storemen> storemenList = null;
	Storemen storemen;
	String name = null;

	public List<Storemen> getStoremenList() {
		return storemenList;
	}

	public void setStoremenList(List<Storemen> storemenList) {
		this.storemenList = storemenList;
	}

	/**
	 * Called when tag starts ( ex:- <name>AndroidPeople</name> -- <name> )
	 */
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
        String num="";
		currentElement = true;

		if (localName.equals("stockStoremen")) {
			/** Start */
			storemenList = new ArrayList<Storemen>();
		}
		
		if (localName.equals("stockStoreman")) {
			/** Start */
		    storemen=new Storemen();
		    num = attributes.getValue("storeman_num");
		    storemen.setStoremanNum(num);
			storemenList.add(storemen);
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
		if (localName.equalsIgnoreCase("storeman_name"))
			storemen.setStoremanName(currentValue);
		else if (localName.equalsIgnoreCase("staffID"))
			storemen.setStaffId(currentValue);
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

