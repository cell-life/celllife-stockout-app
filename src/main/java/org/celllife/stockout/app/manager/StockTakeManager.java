package org.celllife.stockout.app.manager;

import java.util.List;

import org.celllife.stockout.app.domain.Drug;
import org.celllife.stockout.app.domain.StockHistory;
import org.celllife.stockout.app.domain.StockReceived;
import org.celllife.stockout.app.domain.StockTake;
import org.celllife.stockout.app.integration.rest.framework.RestCommunicationException;

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
	 * (if that fails, a background job is started).
	 * 
	 * Once a StockTake has been performed, the related StockReceived(s) are also removed as
	 * they are no longer required (or relevant) - the latest StockTake is always the closest to the truth.
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

	/**
	 * Inserts the StockReceived into the database.
	 */
	void newStockReceived(StockReceived stockReceived);

	/**
	 * Submits the specified StockReceived to the Stock Management Service
	 * @return true if the StockReceived was submitted successfully (1 attempt only)
	 */
	boolean submitStockReceived(StockReceived stockReceived);

	/**
	 * Returns the latest stock received for display on the received tab
	 * @return List of StockReceived
	 */
	List<StockReceived> getLatestStockReceived();
	
	/**
	 * Synchronizes all StockTakes and StockReceived that are not submitted.
	 * @throws RestCommunicationException when the first communication error occurs
	 */
	void synch() throws RestCommunicationException;	
	
	/**
	 * Retrieves the last stock take details for a specific drug
	 * @param drug
	 * @return
	 */
	StockTake getDrugLastStockTake(Drug drug);
	
	/**
	 * Retrieves details of when stock of a specific drug was last received
	 * @param drug
	 * @return
	 */
	StockReceived getDrugLastStockReceived(Drug drug);
	
	/**
	 * Retrieves the stock history for a specific drug
	 * @param drug
	 * @return
	 */
	StockHistory getDrugStockHistory(Drug drug);
}
