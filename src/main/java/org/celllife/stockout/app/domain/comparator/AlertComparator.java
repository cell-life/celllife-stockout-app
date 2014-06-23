package org.celllife.stockout.app.domain.comparator;

import java.util.Comparator;
import java.util.Date;

import org.celllife.stockout.app.domain.Alert;

/**
 * Can be used to comparator two Alerts by their status.
 * These are usually used to sort a List - Collections.sort(listOfAlerts, new AlertComparator())
 */
public class AlertComparator implements Comparator<Alert> {
	@Override
	public int compare(Alert lhs, Alert rhs) {
		if (lhs == null && rhs == null) {
			return 0;
		}
		if (lhs == null) {
			return 1;
		}
		if (rhs == null) {
			return -1;
		}
		Integer level1 = lhs.getLevel();
		Integer level2 = rhs.getLevel();

		//return level2.compareTo(level1);
		if (level1 > level2) {
			return -1;
		} else if (level1 < level2) {
			return 1;
		} else {
			Date date1 = lhs.getDate();
			Date date2 = rhs.getDate();
			return date2.compareTo(date1);
		}
	}
}
