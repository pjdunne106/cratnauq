package com.webservice.android.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.webservice.android.activities.LoginActivity;
import com.webservice.android.activities.R;
import com.webservice.domain.LabourPerson;
import com.webservice.domain.User;
import com.webservice.domain.UserList;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class LoginSpinnerOnItemSelected implements OnItemSelectedListener {

		  private UserList userList;
		  private ArrayAdapter<String> labourPersonAdapter;
		  private String loginName;
		  private String labourId;
		  private List<String> personIds;
		  private LoginActivity loginActivity;
		  
		  public LoginSpinnerOnItemSelected(LoginActivity loginActivity) {
		     this.loginActivity = loginActivity;
		  }
		
		  public void setAllUsers(UserList userList) {
			  this.userList = userList;
		  }
		  
		  public void setFirstUser() {
			  loginName = userList.getAllUsers().get(0).getFirstname()+" "+userList.getAllUsers().get(0).getSurname() ;
		  }
		  
		  public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
			  String value = (String)parent.getItemAtPosition(pos);
			  loginName = value;
		  }
		  
		  private List<String> getLabourPersonIds(String userName) {
				List<String> idList = new ArrayList<String>();
				String storedName = "";
				User user=null;
				boolean found=false;
				Iterator<User> iter = userList.getAllUsers().iterator();
				while ((!found) && (iter.hasNext())) {
				    user = iter.next();
					storedName = user.getFirstname() + " " + user.getSurname();
					if (storedName.equalsIgnoreCase(userName)) {
						found = true;
		                for (LabourPerson labourPerson:user.getLabourPersonList()) {
						    idList.add(labourPerson.getId());
		                }
					}
				}
				return idList;
			}
		  
		  public void onNothingSelected(AdapterView<?> parent) {
			  
		  }
		  
		  public String getLoginName() {
			  return loginName;
		  }
		  
		  public String getLabourId() {
			  return labourId;
		  }
	}

