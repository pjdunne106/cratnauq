package com.webservice.android.xml;

import java.io.StringReader;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.webservice.domain.StockItem;
import com.webservice.domain.StockItemList;

public class StockItemListTranslator {

	/** Create Object For SiteList Class */
	List<StockItem> list = null;
	StockItemList stockItemList;
	StockItemListHandler stockItemListHandler=null;

	public StockItemListTranslator() {
	}
	
	/** Called when the activity is first created. */
	public StockItemList translate(String xmlInput) {

		try {

			/** Handling XML */
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();
			InputSource stringSource = new InputSource(new StringReader(xmlInput)); 
			stockItemListHandler = new StockItemListHandler();
			xr.setContentHandler(stockItemListHandler);
			xr.parse(stringSource);
		} catch (Exception e) {
			System.out.println("Stock Item XML Pasing Excpetion = " + e);
		}

		/** Get result from MyXMLHandler SitlesList Object */
		list = stockItemListHandler.getStockList();
        stockItemList = new StockItemList();
        stockItemList.setStockItemList(list);
        return stockItemList;
	}
}

