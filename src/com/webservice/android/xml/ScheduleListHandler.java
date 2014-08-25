package com.webservice.android.xml;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.webservice.domain.LabourGroup;
import com.webservice.domain.StaffMember;
import com.webservice.domain.User;

public class ScheduleListHandler extends DefaultHandler {

	Boolean currentElement = false;
	String currentValue = null;
	List<User> userList = null;
	List<LabourGroup> labourList=null;
	List<StaffMember> staffList=null;
	
	User user = null;
	LabourGroup labourGroup = null;
    StaffMember staff = null;
	
	public List<User> getUserList() {
		return userList;
	}

	public List<LabourGroup> getLabourList() {
		return labourList;
	}
	
	public List<StaffMember> getStaffList() {
		return staffList;
	}

	
	public void setUserList(List<User> userList) {
		this.userList = userList;
	}

	/**
	 * Called when tag starts ( ex:- <name>AndroidPeople</name> -- <name> )
	 */
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {

		currentElement = true;

		if (localName.equals("users")) {
			/** Start */
			userList = new ArrayList<User>();
		}
		
		if (localName.equals("labourGroups")) {
			/** Start */
			labourList = new ArrayList<LabourGroup>();
		}	
		
		if (localName.equals("staffMembers")) {
			/** Start */
			staffList = new ArrayList<StaffMember>();
		}
		
		if (localName.equals("labourGroup")) {
			/** Start */
			labourGroup = new LabourGroup();
			labourList.add(labourGroup);
			labourGroup.setId(attributes.getValue("id"));
		} 
		
		
		if (localName.equals("staffMember")) {
			/** Start */
			staff = new StaffMember();
			staffList.add(staff);
		} 
		
		if (localName.equals("user")) {
			/** Start */
			user = new User();
			userList.add(user);
		} 
	}

	/**
	 * Called when tag closing ( ex:- <name>AndroidPeople</name> -- </name> )
	 */
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {

		currentElement = false;

		if (localName.equalsIgnoreCase("labourGroup")) {
			labourGroup.setName(currentValue);
	    }
		if (localName.equalsIgnoreCase("surname")) {
			staff.setSurname(currentValue);
	    }
		if (localName.equalsIgnoreCase("forename")) {
			staff.setForename(currentValue);
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

