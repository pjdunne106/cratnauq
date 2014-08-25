package com.webservice.android.xml;

import java.io.StringReader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.webservice.domain.UserList;

public class ValidUsersTranslator {

	/** Create Object For SiteList Class */
	UserList userList = null;
	ValidUsersHandler validUsersHandler=null;

	public ValidUsersTranslator() {
	}
	
	/** Called when the activity is first created. */
	public UserList translate(String xmlInput) {

		try {

			/** Handling XML */
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();
			InputSource stringSource = new InputSource(new StringReader(xmlInput)); 
			validUsersHandler = new ValidUsersHandler();
			xr.setContentHandler(validUsersHandler);
			xr.parse(stringSource);
		} catch (Exception e) {
			System.out.println("XML Pasing Excpetion = " + e);
		}

		/** Get result from MyXMLHandler SitlesList Object */
		userList = validUsersHandler.getUserList();

        return userList;
	}
}

