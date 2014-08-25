package com.webservice.android.xml;

import java.io.StringReader;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.webservice.domain.JobDetails;
import com.webservice.domain.JobDetailsList;

public class JobDetailsTranslator {

	/** Create Object For SiteList Class */
	List<JobDetails> list = null;
	JobDetailsHandler jobDetailsHandler=null;
	JobDetailsList jobDetailsList;

	public JobDetailsTranslator() {
	}
	
	/** Called when the activity is first created. */
	public JobDetailsList translate(String xmlInput) {

		try {

			/** Handling XML */
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();
			InputSource stringSource = new InputSource(new StringReader(xmlInput)); 
			jobDetailsHandler = new JobDetailsHandler();
			xr.setContentHandler(jobDetailsHandler);
			xr.parse(stringSource);
		} catch (Exception e) {
			System.out.println("XML Pasing Excpetion = " + e);
		}

		/** Get result from MyXMLHandler SitlesList Object */
		list = jobDetailsHandler.getJobDetailsList();
        jobDetailsList = new JobDetailsList();
        jobDetailsList.setJobDetails(list);
        return jobDetailsList;
	}
}
