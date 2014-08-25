package com.webservice.android.xml;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.webservice.domain.LabourPerson;
import com.webservice.domain.User;
import com.webservice.domain.UserList;

public class ValidUsersHandler extends DefaultHandler {

	Boolean currentElement = false;
	String currentValue = null;
	String id = null;
	UserList userList = null;
	LabourPerson labourPerson;
    List<User> users;
	User user = null;

	public UserList getUserList() {
		return userList;
	}


	/**
	 * Called when tag starts ( ex:- <name>AndroidPeople</name> -- <name> )
	 */
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {

		currentElement = true;

		if (localName.equals("staffMembers")) {
			/** Start */
			userList = new UserList();
			users = new ArrayList<User>();
			userList.setUserList(users);
		}
		
		if (localName.equals("staffMember")) {
			/** Start */
			user = new User();
			users.add(user);
		} 
		
		if (localName.equals("labourPersons")) {
			/** Start */
		}
		
		if (localName.equals("labourPerson")) {
			/** Start */
			labourPerson = new LabourPerson();
			user.addLabourPerson(labourPerson);
			id = attributes.getValue("id");
			labourPerson.setId(id);
		}
		
	}

	/**
	 * Called when tag closing ( ex:- <name>AndroidPeople</name> -- </name> )
	 */
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {

		currentElement = false;

		/** set value */
		if (localName.equalsIgnoreCase("surname")) {
			user.setSurname(currentValue);
		} else if (localName.equalsIgnoreCase("forename")) {
				user.setFirstname(currentValue);
		 } else if (localName.equalsIgnoreCase("sectionNum")) {
				labourPerson.setSectionId(currentValue);
		 } else if (localName.equalsIgnoreCase("groupNum")) {
				labourPerson.setGroupId(currentValue);
		 } else if (localName.equalsIgnoreCase("individual_name")) {
				labourPerson.setName(currentValue);
	     } else if (localName.equalsIgnoreCase("supervisor")) {
				labourPerson.setSupervisor(currentValue);
		 } else if (localName.equalsIgnoreCase("hours")) {
				labourPerson.setHours(currentValue);
		 }
	}

	/**
	 * Called to get tag characters ( ex:- <name>AndroidPeople</name> -- to get
	 * AndroidPeople Character )
	 */
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {

		if (currentElement) {
			currentValue = new String(ch, start, length);
			currentElement = false;
		}

	}

}

