package org.celllife.stockout.app.manager;

import java.util.List;

import org.celllife.stockout.app.domain.Drug;
import org.celllife.stockout.app.domain.StockHistory;
import org.celllife.stockout.app.domain.StockReceived;
import org.celllife.stockout.app.domain.StockTake;


public interface DrugManager {
	
	/**
	 * Retrieves all the Drugs known to the app
	 * @return List of Alert
	 */
	List<Drug> getDrugs();
	

}
