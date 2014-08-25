package com.webservice.android.xml;

import java.io.StringReader;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

public class SupervisorListTranslator {

	/** Create Object For SiteList Class */
	List<String> list = null;
	SupervisorListHandler supervisorListHandler=null;

	public SupervisorListTranslator() {
	}
	
	/** Called when the activity is first created. */
	public List<String> translate(String xmlInput) {

		try {

			/** Handling XML */
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();
			InputSource stringSource = new InputSource(new StringReader(xmlInput)); 
			supervisorListHandler = new SupervisorListHandler();
			xr.setContentHandler(supervisorListHandler);
			xr.parse(stringSource);
		} catch (Exception e) {
			System.out.println("Supervisor XML Pasing Excpetion = " + e);
		}

		/** Get result from MyXMLHandler SitlesList Object */
		list = supervisorListHandler.getSupervisorList();

        return list;
	}
}

