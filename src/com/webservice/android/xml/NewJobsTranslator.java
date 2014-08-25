package com.webservice.android.xml;

import java.io.StringReader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.webservice.domain.NewJob;

public class NewJobsTranslator {

	/** Create Object For SiteList Class */
	NewJob newJob = null;
	NewJobsHandler newJobsHandler=null;

	public NewJobsTranslator() {
	}
	
	/** Called when the activity is first created. */
	public NewJob translate(String xmlInput) {

		try {

			/** Handling XML */
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();
			InputSource stringSource = new InputSource(new StringReader(xmlInput)); 
			newJobsHandler = new NewJobsHandler();
			xr.setContentHandler(newJobsHandler);
			xr.parse(stringSource);
		} catch (Exception e) {
			System.out.println("XML Pasing Excpetion = " + e);
		}

		/** Get result from MyXMLHandler SitlesList Object */
		newJob = newJobsHandler.getNewJob();

        return newJob;
	}
}

