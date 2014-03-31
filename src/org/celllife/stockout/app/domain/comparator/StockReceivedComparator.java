package org.celllife.stockout.app.domain.comparator;

import java.util.Comparator;
import java.util.Date;

import org.celllife.stockout.app.domain.StockReceived;

/**
 * Compares two StockReceived by their date.
 */
public class StockReceivedComparator implements Comparator<StockReceived> {

	@Override
	public int compare(StockReceived lhs, StockReceived rhs) {
		// no check for nulls because you can't compare a null - therefore this method cannot accept them.
		Date date1 = lhs.getDate();
		Date date2 = rhs.getDate();

		return date2.compareTo(date1); // we want the latest alerts first
	}
}
