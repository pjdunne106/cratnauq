package com.webservice.android.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.webservice.android.listeners.LoginListener;

public class AddJobHistoryActivity extends Activity implements OnClickListener {
	EditText username, password, dob, st;
	TextView error;
	String response;
	Button issueButton;
	Button previous;
	LoginListener loginListener;
	int count;
	int peopleListSize;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_job_history);
		issueButton=(Button) findViewById(R.id.ajh44);
		issueButton.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, IssueStockActivity.class);
		 startActivity(intent);

	}
}