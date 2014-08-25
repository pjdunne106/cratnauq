package com.webservice.android.xml;

import java.io.StringReader;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.webservice.domain.Location;
import com.webservice.domain.LocationList;

public class LocationTranslator {

	/** Create Object For SiteList Class */
	List<Location> list = null;
	LocationList locationList;
	LocationHandler locationHandler=null;

	public LocationTranslator() {
		locationList = new LocationList();
	}
	
	/** Called when the activity is first created. */
	public LocationList translate(String xmlInput) {

		try {

			/** Handling XML */
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();
			InputSource stringSource = new InputSource(new StringReader(xmlInput)); 
			locationHandler = new LocationHandler();
			xr.setContentHandler(locationHandler);
			xr.parse(stringSource);
		} catch (Exception e) {
			System.out.println("XML Pasing Excpetion = " + e);
		}

		/** Get result from MyXMLHandler SitlesList Object */
		list = locationHandler.getLocationList();
        locationList.setLocationList(list);
        return locationList;
	}
}