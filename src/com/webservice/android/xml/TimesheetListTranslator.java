package com.webservice.android.xml;

import java.io.StringReader;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.webservice.domain.Timesheet;
import com.webservice.domain.TimesheetList;

public class TimesheetListTranslator {

	/** Create Object For SiteList Class */
	TimesheetList timesheetList = null;
	private String dateFormat="";
	TimesheetListHandler timesheetListHandler=null;

	public TimesheetListTranslator() {
	}
	
	/** Called when the activity is first created. */
	public TimesheetList translate(String xmlInput) {
        List<Timesheet> list;
		try {

			/** Handling XML */
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();
			InputSource stringSource = new InputSource(new StringReader(xmlInput)); 
			timesheetListHandler = new TimesheetListHandler();
			timesheetListHandler.setDateFormat(dateFormat);
			xr.setContentHandler(timesheetListHandler);
			xr.parse(stringSource);
		} catch (Exception e) {
			System.out.println("XML Pasing Excpetion = " + e);
		}

		/** Get result from MyXMLHandler SitlesList Object */
		timesheetList = new TimesheetList();
		list = timesheetListHandler.getTimesheetList();
        timesheetList.setTimesheetList(list);
        return timesheetList;
	}
	
	public void setDateFormat(String dateFormat) {
		this.dateFormat=dateFormat;
	}
}

