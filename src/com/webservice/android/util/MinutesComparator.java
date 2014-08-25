package com.webservice.android.util;

import java.util.Comparator;

import com.webservice.domain.Timesheet;

public class MinutesComparator implements Comparator<Timesheet> {

	public int compare(Timesheet object1, Timesheet object2) {
		int comparator = 1;
		if ((object1.getStartTime().length() > 4)
				&& (object1.getStartTime().length() > 4)) {
			String start1 = object1.getStartTime();
			String start2 = object2.getStartTime();
			String finish1 = object1.getFinishTime();
			String finish2 = object2.getFinishTime();
			Integer startHours1 = Integer.valueOf(start1.substring(0, 2));
			Integer startHours2 = Integer.valueOf(start2.substring(0, 2));
			Integer startMinutes1 = Integer.valueOf(start1.substring(3, 5));
			Integer startMinutes2 = Integer.valueOf(start2.substring(3, 5));
			Integer finishHours1 = Integer.valueOf(finish1.substring(0, 2));
			Integer finishHours2 = Integer.valueOf(finish2.substring(0, 2));
			Integer finishMinutes1 = Integer.valueOf(finish1.substring(3, 5));
			Integer finishMinutes2 = Integer.valueOf(finish2.substring(3, 5));
			Integer time1 = startHours1 * 60 + startMinutes1;
			Integer time2 = startHours2 * 60 + startMinutes2;
			Integer time3 = finishHours1 * 60 + finishMinutes1;
			Integer time4 = finishHours2 * 60 + finishMinutes2;
			if (time1 < time2) {
				comparator = -1;
			} else if (time1 == time2) {
				if (time3 < time4) {
					comparator = -1;
				} else {
					comparator = 1;
				}
			}
		}
		return comparator;
	};
}