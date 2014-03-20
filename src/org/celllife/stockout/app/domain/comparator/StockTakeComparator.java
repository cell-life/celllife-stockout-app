package org.celllife.stockout.app.domain.comparator;

import java.util.Comparator;
import java.util.Date;

import org.celllife.stockout.app.domain.StockTake;

/**
 * Compares two StockTakes by their date.
 */
public class StockTakeComparator implements Comparator<StockTake> {

	@Override
	public int compare(StockTake lhs, StockTake rhs) {
		// no check for nulls because you can't compare a null - therefore this method cannot accept them.
		Date date1 = lhs.getDate();
		Date date2 = rhs.getDate();

		return date2.compareTo(date1); // we want the latest alerts first
	}
}
