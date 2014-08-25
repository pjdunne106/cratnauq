package com.webservice.android.xml;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.webservice.domain.StockLevel;

public class StockLevelHandler extends DefaultHandler {

	Boolean currentElement = false;
	String currentValue = null;
	String id;
	List<StockLevel> stockList = null;
	StockLevel stockLevel = null;

	public List<StockLevel> getStockLevelList() {
		return stockList;
	}

	public void setStockLevelList(List<StockLevel> stockList) {
		this.stockList = stockList;
	}

	/**
	 * Called when tag starts ( ex:- <name>AndroidPeople</name> -- <name> )
	 */
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {

		currentElement = true;

		if (localName.equals("stockLevels")) {
			/** Start */
			stockList = new ArrayList<StockLevel>();
		}
		
		if (localName.equals("stockLevel")) {
			/** Start */
			stockLevel = new StockLevel();
			stockList.add(stockLevel);
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
		if (localName.equalsIgnoreCase("part_num"))
			stockLevel.setPartNumber(currentValue);
		else if (localName.equalsIgnoreCase("SLoc_Id"))
			stockLevel.setPartId(currentValue);
		else if (localName.equalsIgnoreCase("quantity"))
			stockLevel.setQuantity(currentValue);
		else if (localName.equalsIgnoreCase("refreshed"))
			stockLevel.setRefreshed(currentValue);
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

