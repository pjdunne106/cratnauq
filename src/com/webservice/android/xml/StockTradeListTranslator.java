package com.webservice.android.xml;

import java.io.StringReader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.webservice.domain.StockTradeList;

public class StockTradeListTranslator {

	/** Create Object For SiteList Class */
	StockTradeList list = null;
	StockTradeListHandler stockHandler=null;

	public StockTradeListTranslator() {
	}
	
	/** Called when the activity is first created. */
	public StockTradeList translate(String xmlInput) {

		try {

			/** Handling XML */
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();
			InputSource stringSource = new InputSource(new StringReader(xmlInput)); 
			stockHandler = new StockTradeListHandler();
			xr.setContentHandler(stockHandler);
			xr.parse(stringSource);
		} catch (Exception e) {
			System.out.println("Stock Trade XML Pasing Excpetion = " + e);
		}

		/** Get result from MyXMLHandler SitlesList Object */
		list = stockHandler.getStockList();

        return list;
	}
}

