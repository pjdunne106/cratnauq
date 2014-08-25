package com.webservice.android.xml;

import java.io.StringReader;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.webservice.domain.StockLevel;
import com.webservice.domain.StockLevelList;

public class StockLevelTranslator {

	/** Create Object For SiteList Class */
	List<StockLevel> list = null;
	StockLevelList stockLevelList;
	StockLevelHandler stockLevelHandler=null;

	public StockLevelTranslator() {
	}
	
	/** Called when the activity is first created. */
	public StockLevelList translate(String xmlInput) {

		try {

			/** Handling XML */
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();
			InputSource stringSource = new InputSource(new StringReader(xmlInput)); 
			stockLevelHandler = new StockLevelHandler();
			xr.setContentHandler(stockLevelHandler);
			xr.parse(stringSource);
		} catch (Exception e) {
			System.out.println("Stock Level XML Pasing Excpetion = " + e);
		}

		/** Get result from MyXMLHandler SitlesList Object */
		list = stockLevelHandler.getStockLevelList();
        stockLevelList = new StockLevelList();
        stockLevelList.setStockLevels(list);
        return stockLevelList;
	}
}

