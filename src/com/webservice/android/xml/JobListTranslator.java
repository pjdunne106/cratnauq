package com.webservice.android.xml;

import java.io.StringReader;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.webservice.domain.Job;
import com.webservice.domain.JobList;

public class JobListTranslator {

	/** Create Object For SiteList Class */
	JobList jobList = null;
	List<Job> list;
	JobListHandler jobListHandler=null;

	public JobListTranslator() {
	}
	
	/** Called when the activity is first created. */
	public JobList translate(String xmlInput) {

		try {

			/** Handling XML */
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();
			InputSource stringSource = new InputSource(new StringReader(xmlInput)); 
			jobListHandler = new JobListHandler();
			xr.setContentHandler(jobListHandler);
			xr.parse(stringSource);
		} catch (Exception e) {
			System.out.println("XML Pasing Excpetion = " + e);
		}

		/** Get result from MyXMLHandler SitlesList Object */
		list = jobListHandler.getJobList();
        jobList = new JobList();
        jobList.setJobList(list);
        return jobList;
	}
}

