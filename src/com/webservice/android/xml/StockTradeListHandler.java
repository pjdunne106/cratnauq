package com.webservice.android.xml;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.webservice.domain.StockTrade;
import com.webservice.domain.StockTradeList;

public class StockTradeListHandler extends DefaultHandler {

	Boolean currentElement = false;
	String currentValue = null;
	String id="";
	StockTradeList stockList = null;
    List<StockTrade> list;
    StockTrade stockTrade;
    
	public StockTradeList getStockList() {
		stockList = new StockTradeList();
		stockList.setStockTradeList(list);
		return stockList;
	}

	public void setStockList(StockTradeList stockList) {
		this.stockList = stockList;
	}

	/**
	 * Called when tag starts ( ex:- <name>AndroidPeople</name> -- <name> )
	 */
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {

		currentElement = true;

		if (localName.equals("stockTrades")) {
			/** Start */
			list = new ArrayList<StockTrade>();
		}
		
		if (localName.equals("stockTrade")) {
			/** Start */
			stockTrade = new StockTrade();
			list.add(stockTrade);
			stockTrade.setId(attributes.getValue("id"));
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
		if (localName.equalsIgnoreCase("stockTrade"))
			stockTrade.setDesc(currentValue);
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

