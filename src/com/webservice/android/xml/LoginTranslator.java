package com.webservice.android.xml;

import java.io.StringReader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

public class LoginTranslator {

	/** Create Object For SiteList Class */
	String response = null;
	LoginHandler loginHandler=null;

	public LoginTranslator() {
	}
	
	/** Called when the activity is first created. */
	public String translate(String xmlInput) {

		try {

			/** Handling XML */
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();
			InputSource stringSource = new InputSource(new StringReader(xmlInput)); 
			loginHandler = new LoginHandler();
			xr.setContentHandler(loginHandler);
			xr.parse(stringSource);
		} catch (Exception e) {
			System.out.println("XML Pasing Excpetion = " + e);
		}

		/** Get result from MyXMLHandler SitlesList Object */
		response = loginHandler.getResponse();

        return response;
	}
}

