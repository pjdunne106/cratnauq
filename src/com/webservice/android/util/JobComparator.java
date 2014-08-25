package com.webservice.android.util;

import java.util.Comparator;

import com.webservice.android.activities.JobListActivity;
import com.webservice.domain.Job;

public class JobComparator implements Comparator<Job> {

	private String columnRef;
	private JobListActivity activity;

	public JobComparator(String column, JobListActivity activity) {
		columnRef = column;	
		this.activity = activity; 
	}

	public int compare(Job j1, Job j2)
	{  
		String field1;
		String field2;
		field1 = activity.getField(j1,columnRef);
		field2 = activity.getField(j2,columnRef);
		return field1.compareTo(field2);
	} 
}
