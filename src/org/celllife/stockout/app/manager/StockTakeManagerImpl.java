package org.celllife.stockout.app.manager;

import java.util.Collections;
import java.util.List;

import org.celllife.stockout.app.database.AlertTableAdapter;
import org.celllife.stockout.app.database.DrugTableAdapter;
import org.celllife.stockout.app.database.StockTakeTableAdapter;
import org.celllife.stockout.app.domain.Alert;
import org.celllife.stockout.app.domain.Drug;
import org.celllife.stockout.app.domain.StockTake;
import org.celllife.stockout.app.domain.comparator.StockTakeComparator;

import android.util.Log;

public class StockTakeManagerImpl implements StockTakeManager {
	
	public StockTakeManagerImpl() {
	}

	@Override
	public Drug findDrugByBarcode(String barcode) {
		DrugTableAdapter drugAdapter = DatabaseManager.getDrugTableAdapter();
		return drugAdapter.findByBarcode(barcode);
	}

	@Override
	public void newStockTake(StockTake stockTake) {
		// Retrieves last StockTake for the drug
		StockTakeTableAdapter stockAdapter = DatabaseManager.getStockTakeTableAdapter();
		StockTake lastStockTake = stockAdapter.findByDrug(stockTake.getDrug());
		if (lastStockTake != null) {
			// TODO: Calculates new ADC + updates StockHistory
			// Delete last StockTake
			stockAdapter.deleteById(lastStockTake.getId());
			
		}
		// Cancels Alerts
		AlertTableAdapter alertAdapter = DatabaseManager.getAlertTableAdapter();
		Alert alert = alertAdapter.findByDrug(stockTake.getDrug());
		if (alert != null) {
			Log.d("StockTakeManager", "Cancelling Alert "+alert);
			alertAdapter.deleteById(alert.getId());
		}
		// Inserts new StockTake
		stockAdapter.insert(stockTake);
		// TODO: Sends StockTake to the server (on fail, start a task)
	}

	@Override
	public boolean submitStockTake(StockTake stockTake) {
		// Sends the StockTake to the server (this method is to be used by the background job)
		return false;
	}

	@Override
	public List<StockTake> getLatestStockTakes() {
		StockTakeTableAdapter stockAdapter = DatabaseManager.getStockTakeTableAdapter();
		List<StockTake> stocks = stockAdapter.findLatestStockTakes();
		Collections.sort(stocks, new StockTakeComparator());
		return stocks;
	}

}
