package com.webservice.android.listeners;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class LoginListener implements OnClickListener {

	EditText username;
	EditText password;
	
	public LoginListener(EditText username, EditText password) {
	   this.username = username;
	   this.password = password;
	}
	
	@Override
	public void onClick(View v) {
		// Check Login
		String enteredUsername = username.getText().toString();
		String enteredPassword = password.getText().toString();
	}

	public boolean isUserValid() {
       return true;
	}
}
