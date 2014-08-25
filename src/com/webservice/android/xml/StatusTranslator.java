package com.webservice.android.xml;

import java.io.StringReader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.webservice.domain.StatusList;

public class StatusTranslator {

	/** Create Object For SiteList Class */
	StatusList list = null;
	StatusHandler statusHandler=null;

	public StatusTranslator() {
	}
	
	/** Called when the activity is first created. */
	public StatusList translate(String xmlInput) {

		try {

			/** Handling XML */
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();
			InputSource stringSource = new InputSource(new StringReader(xmlInput)); 
			statusHandler = new StatusHandler();
			xr.setContentHandler(statusHandler);
			xr.parse(stringSource);
		} catch (Exception e) {
			System.out.println("XML Pasing Excpetion = " + e);
		}

		/** Get result from MyXMLHandler SitlesList Object */
		list = statusHandler.getStatusList();

        return list;
	}
}

