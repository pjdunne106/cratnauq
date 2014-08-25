package com.webservice.android.xml;

import java.io.StringReader;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.webservice.domain.StockLocation;

public class StockLocationListTranslator {

	/** Create Object For SiteList Class */
	List<StockLocation> list = null;
	StockLocationListHandler stockLocationListHandler=null;

	public StockLocationListTranslator() {
	}
	
	/** Called when the activity is first created. */
	public List<StockLocation> translate(String xmlInput) {

		try {

			/** Handling XML */
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();
			InputSource stringSource = new InputSource(new StringReader(xmlInput)); 
			stockLocationListHandler = new StockLocationListHandler();
			xr.setContentHandler(stockLocationListHandler);
			xr.parse(stringSource);
		} catch (Exception e) {
			System.out.println("Stock Location XML Pasing Excpetion = " + e);
		}

		/** Get result from MyXMLHandler SitlesList Object */
		list = stockLocationListHandler.getStockLocationList();

        return list;
	}
}

