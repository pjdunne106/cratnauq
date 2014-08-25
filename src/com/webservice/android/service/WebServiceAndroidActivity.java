package com.webservice.android.service;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.webservice.android.listeners.LoginListener;

public class WebServiceAndroidActivity extends Activity {
	EditText username, password, dob, st;
	TextView error;
	String response;
	Button loginButton;
	Button previous;
	PeopleList peopleList;
	XMLPeopleTranslator xmlPeopleTranslator;
	LoginListener loginListener;
	int count;
	int peopleListSize;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(  
	            this,  
	            android.R.layout.simple_spinner_item,  
	            new String[] { "1", "2", "3" });  
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
		loginListener = new LoginListener(username, password);
        loginButton.setOnClickListener(loginListener);
		//xmlPeopleTranslator = new XMLPeopleTranslator();
		count = 0;
		peopleListSize = 0;
		//try {
		//	response = CustomHttpClient
		//			.executeHttpGet("http://10.0.2.2:8080/quantarcdataservices/data/people");
		//	peopleList = xmlPeopleTranslator.translate(response);
		//	peopleListSize = peopleList.getFullName().size();
		//	fn.setText(peopleList.getFullName().get(0));
		//	ti.setText(peopleList.getTitle().get(0));
		//	dob.setText(peopleList.getTitle().get(0));
		//	st.setText(peopleList.getStatus().get(0));
		//} catch (Exception e) {
		//	fn.setText(e.toString());
		//}
		//ok.setOnClickListener(new View.OnClickListener() {
		//	@Override
		//	public void onClick(View v) {
				// TODO Auto-generated method stub
		//		if (count < peopleListSize) {
		//			fn.setText(peopleList.getFullName().get(count));
		//			ti.setText(peopleList.getTitle().get(count));
		//			dob.setText(peopleList.getTitle().get(count));
		//			st.setText(peopleList.getStatus().get(count));
		//			count = count + 1;
		//		}
		//	}
		//});

		//ok.setOnClickListener(new View.OnClickListener() {
		//	@Override
		//	public void onClick(View v) {
				// TODO Auto-generated method stub
		//		if (count < peopleListSize) {
		//			fn.setText(peopleList.getFullName().get(count));
		//			ti.setText(peopleList.getTitle().get(count));
		//			dob.setText(peopleList.getTitle().get(count));
		//			st.setText(peopleList.getStatus().get(count));
		//			count = count + 1;
		//		}
		//	}
		//});
	}
}