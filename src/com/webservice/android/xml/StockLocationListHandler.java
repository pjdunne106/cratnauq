package com.webservice.android.xml;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.webservice.domain.StockLocation;

public class StockLocationListHandler extends DefaultHandler {

	Boolean currentElement = false;
	String currentValue = null;
	String id;
	List<StockLocation> stockLocationList = null;
	StockLocation stockLocation = null;

	public List<StockLocation> getStockLocationList() {
		return stockLocationList;
	}

	public void setStockLocationList(List<StockLocation> stockLocationList) {
		this.stockLocationList = stockLocationList;
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
			stockLocationList = new ArrayList<StockLocation>();
		}
		
		if (localName.equals("stockLocation")) {
			/** Start */
			id = attributes.getValue("id");
			stockLocation = new StockLocation();
			stockLocation.setId(id);
			stockLocationList.add(stockLocation);
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
		if (localName.equalsIgnoreCase("loc_description"))
			stockLocation.setName(currentValue);
		else if (localName.equalsIgnoreCase("loc_ref"))
			stockLocation.setCode(currentValue);
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

