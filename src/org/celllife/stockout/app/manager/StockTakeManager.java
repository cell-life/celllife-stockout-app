package org.celllife.stockout.app.manager;

import java.util.List;

import org.celllife.stockout.app.domain.Drug;
import org.celllife.stockout.app.domain.StockTake;

/**
 * Stock related manager. This service provides the functionality used to process stock in the app.
 */
public interface StockTakeManager {
	
	/**
	 * Find a drug given a barcode.
	 * @param barcode String containing the barcode
	 * @return Drug, or null if not found
	 */
	Drug findDrugByBarcode(String barcode);

	/**
	 * Inserts the new StockTake into the database (replaces the existing StockTake for the drug),
	 * cancels any Alerts open on the Drug, and attempts to submit the stock take 
	 * (if that fails, a background job is started)
	 * 
	 * @param stockTake
	 */
	void newStockTake(StockTake stockTake);

	/**
	 * Submits the specified StockTake to the Stock Management Server.
	 * @return true if the StockTake was submitted successfully (1 attempt only)
	 */
	boolean submitStockTake(StockTake stockTake);

	/**
	 * Returns the latest stock takes for display on the order tab
	 * @return List of StockTake
	 */
	List<StockTake> getLatestStockTakes();
	
}
