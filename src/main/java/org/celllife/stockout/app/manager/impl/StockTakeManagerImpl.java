package org.celllife.stockout.app.manager.impl;

import java.util.Collections;
import java.util.List;

import org.celllife.stockout.app.database.AlertTableAdapter;
import org.celllife.stockout.app.database.DrugTableAdapter;
import org.celllife.stockout.app.database.StockHistoryTableAdapter;
import org.celllife.stockout.app.database.StockReceivedTableAdapter;
import org.celllife.stockout.app.database.StockTakeTableAdapter;
import org.celllife.stockout.app.domain.Alert;
import org.celllife.stockout.app.domain.Drug;
import org.celllife.stockout.app.domain.ServerCommunicationType;
import org.celllife.stockout.app.domain.StockHistory;
import org.celllife.stockout.app.domain.StockReceived;
import org.celllife.stockout.app.domain.StockTake;
import org.celllife.stockout.app.domain.comparator.StockReceivedComparator;
import org.celllife.stockout.app.domain.comparator.StockTakeComparator;
import org.celllife.stockout.app.integration.rest.PostStockReceivedMethod;
import org.celllife.stockout.app.integration.rest.PostStockTakeMethod;
import org.celllife.stockout.app.integration.rest.framework.RestCommunicationException;
import org.celllife.stockout.app.manager.DatabaseManager;
import org.celllife.stockout.app.manager.ManagerFactory;
import org.celllife.stockout.app.manager.ServerCommunicationLogManager;
import org.celllife.stockout.app.manager.StockTakeManager;

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
		// Retrieves the StockReceived for the drug
		StockReceivedTableAdapter stockReceivedAdapter = DatabaseManager.getStockReceivedTableAdapter();
		List<StockReceived> stockReceived = stockReceivedAdapter.findByDrug(stockTake.getDrug());
		if (lastStockTake != null) {
			// Calculates new ADC + updates StockHistory
			int adc = ManagerFactory.getCalculationManager().calculateAverageDailyConsumption(lastStockTake, stockTake, stockReceived);
			StockHistoryTableAdapter stockHistoryAdapter = DatabaseManager.getStockHistoryTableAdapter();
			StockHistory stockHistory = stockHistoryAdapter.findByDrug(stockTake.getDrug());
			if (stockHistory == null) {
				stockHistory = new StockHistory();
				stockHistory.setDrug(stockTake.getDrug());
			}
			stockHistory.setAverageDailyConsumption(adc);
			stockHistoryAdapter.insertOrUpdate(stockHistory);
			// Delete last StockTake
			stockAdapter.deleteById(lastStockTake.getId());
		}
		// Cancels previous Stock Arrivals
		for (StockReceived sr : stockReceived) {
			Log.d("StockTakeManager", "Cancelling StockReceived "+sr);
			stockReceivedAdapter.deleteById(sr.getId());
		}
		// Cancels Alerts
		AlertTableAdapter alertAdapter = DatabaseManager.getAlertTableAdapter();
		Alert alert = alertAdapter.findByDrug(stockTake.getDrug());
		if (alert != null) {
			Log.d("StockTakeManager", "Cancelling Alert "+alert);
			alertAdapter.deleteById(alert.getId());
		}
		// Send StockTake to the server, if it fails, an error should be displayed
		// because a RestCommunicationException is thrown, and the background task
		// should pick it up and try resend later.
		try {
			boolean submitted = postStockTake(stockTake);
			stockTake.setSubmitted(submitted);
		} finally {
			// Inserts new StockTake
			stockAdapter.insert(stockTake);
		}
	}
	
	@Override
	public void synch() throws RestCommunicationException {
		// submit stock takes
		StockTakeTableAdapter stockAdapter = DatabaseManager.getStockTakeTableAdapter();
		List<StockTake> stocks = stockAdapter.findUnsubmittedStockTakes();
		for (StockTake s : stocks) {
			boolean submitted = postStockTake(s);
			s.setSubmitted(submitted);
			stockAdapter.update(s.getId(), s);
		}
		// submit stock received
		StockReceivedTableAdapter arrivalAdapter = DatabaseManager.getStockReceivedTableAdapter();
		List<StockReceived> arrivals = arrivalAdapter.findUnsubmittedStockReceived();
		for (StockReceived a : arrivals) {
			boolean submitted = postStockReceived(a);
			a.setSubmitted(submitted);
			arrivalAdapter.update(a.getId(), a);
		}
	}

	@Override
	public boolean submitStockTake(StockTake stockTake) {
		// Sends the StockTake to the server (this method is to be used by the background job)
		boolean success = false;
		try {
			success = postStockTake(stockTake);
			if (success) {
				stockTake.setSubmitted(true);
				DatabaseManager.getStockTakeTableAdapter().update(stockTake.getId(), stockTake);
			}
		} catch (RestCommunicationException e) {
			// swallows exceptions because this method is being used by the background task
			Log.w("StockTakeManager", "Got an error while submitting a stock take.", e);
		}
		return success;
	}

	@Override
	public List<StockTake> getLatestStockTakes() {
		StockTakeTableAdapter stockAdapter = DatabaseManager.getStockTakeTableAdapter();
		List<StockTake> stocks = stockAdapter.findLatestStockTakes();
		Collections.sort(stocks, new StockTakeComparator());
		return stocks;
	}

	@Override
	public void newStockReceived(StockReceived stockReceived) {
		StockReceivedTableAdapter stockReceivedAdapter = DatabaseManager.getStockReceivedTableAdapter();
		try {
			// submits to the server (note: will sent RestCommunicationException on error which should be displayed)
			boolean submitted = postStockReceived(stockReceived);
			stockReceived.setSubmitted(submitted);
		} finally {
			// Inserts new StockTake
			stockReceivedAdapter.insert(stockReceived);
		}
	}

	@Override
	public boolean submitStockReceived(StockReceived stockReceived) {
		// Sends the StockTake to the server (this method is to be used by the background job)
		boolean success = false;
		try {
			success = postStockReceived(stockReceived);
			if (success) {
				stockReceived.setSubmitted(true);
				DatabaseManager.getStockReceivedTableAdapter().update(stockReceived.getId(), stockReceived);
			}
		} catch (RestCommunicationException e) {
			// swallows exceptions because this method is being used by the background task
			Log.w("StockTakeManager", "Got an error while submitting a stock received.", e);
		}
		return success;
	}

	@Override
	public List<StockReceived> getLatestStockReceived() {
		StockReceivedTableAdapter stockReceivedAdapter = DatabaseManager.getStockReceivedTableAdapter();
		List<StockReceived> stocks = stockReceivedAdapter.findLatestStockReceived();
		Collections.sort(stocks, new StockReceivedComparator());
		return stocks;
	}

    private boolean postStockTake(StockTake stockTake) {
        ServerCommunicationLogManager logManager = ManagerFactory.getServerCommunicationLogManager();
        boolean success = false;
        try {
            success = PostStockTakeMethod.submitStockTake(stockTake);
            logManager.createServerCommunicationLog(ServerCommunicationType.STOCK, success);
        } catch (RuntimeException e) {
            logManager.createServerCommunicationLog(ServerCommunicationType.STOCK, false);
            throw e;
        }
        return success;
    }

    private boolean postStockReceived(StockReceived stockReceived) {
        ServerCommunicationLogManager logManager = ManagerFactory.getServerCommunicationLogManager();
        boolean success = false;
        try {
            success = PostStockReceivedMethod.submitStockReceived(stockReceived);
            logManager.createServerCommunicationLog(ServerCommunicationType.STOCK, success);
        } catch (RuntimeException e) {
            logManager.createServerCommunicationLog(ServerCommunicationType.STOCK, false);
            throw e;
        }
        return success;
    }
    
    @Override
	public StockTake getDrugLastStockTake(Drug drug) {
		StockTakeTableAdapter stockTake = DatabaseManager.getStockTakeTableAdapter();
		StockTake drugStockTake = stockTake.findByDrug(drug);
		return drugStockTake;
	}

	@Override
	public StockReceived getDrugLastStockReceived(Drug drug) {
		StockReceivedTableAdapter stockReceived = DatabaseManager.getStockReceivedTableAdapter();
		StockReceived drugStockReceived = stockReceived.findBySingleDrug(drug);
		return drugStockReceived;
	}

	@Override
	public StockHistory getDrugStockHistory(Drug drug) {
		StockHistoryTableAdapter stockHistory = DatabaseManager.getStockHistoryTableAdapter();
		StockHistory drugStockHistory = stockHistory.findByDrug(drug);
		return drugStockHistory;
	}
}
	
	