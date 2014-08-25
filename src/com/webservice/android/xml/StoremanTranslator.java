package com.webservice.android.xml;

import java.io.StringReader;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.webservice.domain.StoremanList;
import com.webservice.domain.Storemen;

public class StoremanTranslator {

	/** Create Object For SiteList Class */
	List<Storemen> list = null;
	StoremanList storemanList;
	StoremanHandler storemanHandler=null;

	public StoremanTranslator() {
		storemanList = new StoremanList();
	}
	
	/** Called when the activity is first created. */
	public StoremanList translate(String xmlInput) {

		try {

			/** Handling XML */
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();
			InputSource stringSource = new InputSource(new StringReader(xmlInput)); 
			storemanHandler = new StoremanHandler();
			xr.setContentHandler(storemanHandler);
			xr.parse(stringSource);
		} catch (Exception e) {
			System.out.println("Storeman XML Pasing Excpetion = " + e);
		}

		/** Get result from MyXMLHandler SitlesList Object */
		list = storemanHandler.getStoremenList();
        storemanList.setStoremanList(list);
        return storemanList;
	}
}

