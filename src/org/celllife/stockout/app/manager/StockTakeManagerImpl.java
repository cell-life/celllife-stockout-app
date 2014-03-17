package org.celllife.stockout.app.manager;

import java.util.List;

import org.celllife.stockout.app.database.DrugTableAdapter;
import org.celllife.stockout.app.database.StockTakeTableAdapter;
import org.celllife.stockout.app.domain.Drug;
import org.celllife.stockout.app.domain.StockTake;

import android.content.Context;

public class StockTakeManagerImpl implements StockTakeManager {

	private Context context;
	
	public StockTakeManagerImpl(Context context) {
		this.context = context;
		DatabaseManager.initialise(this.context);
	}

	@Override
	public Drug findDrugByBarcode(String barcode) {
		DrugTableAdapter drugAdapter = DatabaseManager.getDrugTableAdapter();
		return drugAdapter.findByBarcode(barcode);
	}

	@Override
	public void newStockTake(StockTake stockTake) {
		// Retrieves last StockTake for the drug
		// Calculates new ADC + updates StockHistory
		// Cancels Alerts
		// Inserts new StockTake
		// Sends StockTake to the server (on fail, start a task)
	}

	@Override
	public boolean submitStockTake(StockTake stockTake) {
		// Sends the StockTake to the server (this method is to be used by the background job)
		return false;
	}

	@Override
	public List<StockTake> getLatestStockTakes() {
		// FIXME: only return stock takes from this session
		StockTakeTableAdapter stockAdapter = DatabaseManager.getStockTakeTableAdapter();
		return stockAdapter.findAll();
	}

}
