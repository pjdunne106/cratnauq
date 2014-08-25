package com.webservice.android.xml;

import java.io.StringReader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.webservice.domain.LabourGroupList;

public class LabourGroupTranslator {

	/** Create Object For SiteList Class */
	LabourGroupList list = null;
	LabourGroupHandler labourGroupHandler=null;

	public LabourGroupTranslator() {
	}
	
	/** Called when the activity is first created. */
	public LabourGroupList translate(String xmlInput) {

		try {

			/** Handling XML */
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();
			InputSource stringSource = new InputSource(new StringReader(xmlInput)); 
			labourGroupHandler = new LabourGroupHandler();
			xr.setContentHandler(labourGroupHandler);
			xr.parse(stringSource);
		} catch (Exception e) {
			System.out.println("XML Pasing Excpetion = " + e);
		}

		/** Get result from MyXMLHandler SitlesList Object */
		list = labourGroupHandler.getLabourGroupList();

        return list;
	}
}

