package com.webservice.android.xml;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.webservice.domain.StockItem;

public class StockItemListHandler extends DefaultHandler {

	Boolean currentElement = false;
	String currentValue = null;
	String id;
	List<StockItem> stockList = null;
	StockItem stockItem = null;

	public List<StockItem> getStockList() {
		return stockList;
	}

	public void setStockList(List<StockItem> stockList) {
		this.stockList = stockList;
	}

	/**
	 * Called when tag starts ( ex:- <name>AndroidPeople</name> -- <name> )
	 */
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {

		currentElement = true;

		if (localName.equals("stockCodes")) {
			/** Start */
			stockList = new ArrayList<StockItem>();
		}
		
		if (localName.equals("stockCode")) {
			/** Start */
			id = attributes.getValue("part_num");
			stockItem = new StockItem();
			stockItem.setCode(id);
			stockList.add(stockItem);
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
		if (localName.equalsIgnoreCase("part_desc"))
			stockItem.setName(currentValue);
		else if (localName.equalsIgnoreCase("stock_type"))
			stockItem.setType(currentValue);
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

