package com.webservice.android.xml;

import java.io.StringReader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.webservice.domain.LabourTimeList;

public class LabourTimeTranslator {

	/** Create Object For SiteList Class */
	LabourTimeList list = null;
	LabourTimeHandler labourTimeHandler=null;

	public LabourTimeTranslator() {
	}
	
	/** Called when the activity is first created. */
	public LabourTimeList translate(String xmlInput) {
        list = new LabourTimeList();
		try {

			/** Handling XML */
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();
			InputSource stringSource = new InputSource(new StringReader(xmlInput)); 
			labourTimeHandler = new LabourTimeHandler();
			xr.setContentHandler(labourTimeHandler);
			xr.parse(stringSource);
		} catch (Exception e) {
			System.out.println("Labour Time XML Pasing Excpetion = " + e);
		}

		/** Get result from MyXMLHandler SitlesList Object */
		list.setLabourTimeList(labourTimeHandler.getLabourTimeList());

        return list;
	}
}