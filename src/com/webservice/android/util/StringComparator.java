package com.webservice.android.util;

import java.util.Comparator;

public class StringComparator implements Comparator<String>  {

	public int compare(String object1, String object2) {
		return object1.compareTo(object2);
	};
}
