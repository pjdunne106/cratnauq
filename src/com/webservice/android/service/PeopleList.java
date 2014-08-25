package com.webservice.android.service;

import java.util.ArrayList;

/** Contains getter and setter method for varialbles */
public class PeopleList {

	/** Variables */
	private ArrayList<String> fullname = new ArrayList<String>();
	private ArrayList<String> title = new ArrayList<String>();
	private ArrayList<String> dob = new ArrayList<String>();
	private ArrayList<String> status = new ArrayList<String>();

	/**
	 * In Setter method default it will return arraylist change that to add
	 */

	public ArrayList<String> getFullName() {
		return fullname;
	}

	public void setFullName(String fullname) {
		this.fullname.add(fullname);
	}

	public ArrayList<String> getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title.add(title);
	}

	public ArrayList<String> getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob.add(dob);
	}

	public ArrayList<String> getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status.add(status);
	}
	
}
