package com.webservice.android.service;

import java.io.StringReader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

public class XMLPeopleTranslator {

	/** Create Object For SiteList Class */
	PeopleList peopleList = null;
	PeopleXMLHandler peopleXMLHandler=null;

	public XMLPeopleTranslator() {
	}
	
	/** Called when the activity is first created. */
	public PeopleList translate(String xmlInput) {

		try {

			/** Handling XML */
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();
			InputSource stringSource = new InputSource(new StringReader(xmlInput)); 
			peopleXMLHandler = new PeopleXMLHandler();
			xr.setContentHandler(peopleXMLHandler);
			xr.parse(stringSource);
		} catch (Exception e) {
			System.out.println("XML Pasing Excpetion = " + e);
		}

		/** Get result from MyXMLHandler SitlesList Object */
		peopleList = peopleXMLHandler.getPeopleList();

        return peopleList;
	}
}
