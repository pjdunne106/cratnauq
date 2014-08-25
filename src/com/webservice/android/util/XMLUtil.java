package com.webservice.android.util;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XMLUtil {

	Document document;
	
	public XMLUtil(String data) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	        try {

			DocumentBuilder db = dbf.newDocumentBuilder();

			InputSource is = new InputSource();
		        is.setCharacterStream(new StringReader(data));
		        document = db.parse(is); 
			} catch (ParserConfigurationException e) {
				System.out.println("XML parse error: " + e.getMessage());
				document = null;
			} catch (SAXException e) {
				System.out.println("Wrong XML file structure: " + e.getMessage());
	            document = null;
			} catch (IOException e) {
				System.out.println("I/O exeption: " + e.getMessage());
				document = null;
			}
		}
	
	public String getNodeElement(String name) { 
	    NodeList nodes = document.getElementsByTagName(name);
        String data=null;
        Element dataElement=null;
	//fill in the list items from the XML document
	    for (int i = 0; i < nodes.getLength(); i++) {
		    dataElement = (Element)nodes.item(i);
	    }
	    data = dataElement.getOwnerDocument().toString();
	    return data;
	}
	
}
