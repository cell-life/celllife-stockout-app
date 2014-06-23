package org.celllife.stockout.app.manager.impl;

import java.util.Date;
import java.util.List;

import org.celllife.stockout.app.database.PhoneTableAdapter;
import org.celllife.stockout.app.database.StockHistoryTableAdapter;
import org.celllife.stockout.app.database.StockReceivedTableAdapter;
import org.celllife.stockout.app.database.StockTakeTableAdapter;
import org.celllife.stockout.app.domain.Drug;
import org.celllife.stockout.app.domain.Phone;
import org.celllife.stockout.app.domain.StockHistory;
import org.celllife.stockout.app.domain.StockReceived;
import org.celllife.stockout.app.domain.StockTake;
import org.celllife.stockout.app.manager.CalculationManager;
import org.celllife.stockout.app.manager.DatabaseManager;

import android.util.Log;

public class CalculationManagerImpl implements CalculationManager {
	
	private static final int DEFAULT_LEAD_TIME = 3;
	private static final int DEFAULT_SAFETY_LEVEL = 3;
	private static final int DEFAULT_ADC = 1;
	
	private static final int MILLISECONDS_IN_DAY = 1000*60*60*24;

	@Override
	public int calculateAverageDailyConsumption(StockTake oldStockTake, StockTake newStockTake, List<StockReceived> stockReceived) {
		long milliseconds = newStockTake.getDate().getTime() - oldStockTake.getDate().getTime();
		int days = (int) Math.round(milliseconds/MILLISECONDS_IN_DAY);
		Log.d("CalculationManager", "calculateAverageDailyConsumption days="+days);
		// note: if we take into account opening hours, we should * days_open / 7 and then 
		// round to the nearest whole number

		int stockUsed = oldStockTake.getQuantity() - newStockTake.getQuantity();
		for (StockReceived sr : stockReceived) {
			stockUsed = stockUsed + sr.getQuantity();
		}
		Log.d("CalculationManager", "calculateAverageDailyConsumption stockUsed="+stockUsed);
		
		int adc;
		if (days == 0) {
			adc = stockUsed;
		} else {
			adc = Math.round(stockUsed/days);
		}
		Log.d("CalculationManager", "calculateAverageDailyConsumption adc="+adc);

		return adc;
	}

	@Override
	public int getEstimatedStock(Drug drug) {
		Date now = new Date();
		
		StockTakeTableAdapter stockTakeDb = DatabaseManager.getStockTakeTableAdapter();
		StockTake lastStockTake = stockTakeDb.findByDrug(drug);
		
		if (lastStockTake != null) {
		
			StockHistoryTableAdapter stockHistoryDb = DatabaseManager.getStockHistoryTableAdapter();
			StockHistory stockHistory = stockHistoryDb.findByDrug(drug);
			
			// work out the number of days since the last stock take
			long milliseconds = now.getTime() - lastStockTake.getDate().getTime();
			int days = (int) Math.round(milliseconds/MILLISECONDS_IN_DAY);
			Log.d("CalculationManager", "getEstimatedStock for "+drug+" days="+days);
			
			// work out how much stock you think you should have used (given adc)
			int estimatedUsage = (int)days * stockHistory.getAverageDailyConsumption();
			Log.d("CalculationManager", "getEstimatedStock estimatedUsage="+estimatedUsage);
	
			// calculate how much stock you currently have
			int stock = lastStockTake.getQuantity();
			StockReceivedTableAdapter stockReceivedDb = DatabaseManager.getStockReceivedTableAdapter();
			List<StockReceived> stockReceived = stockReceivedDb.findByDrug(drug);
			for (StockReceived sr : stockReceived) {
				stock = stock + sr.getQuantity();
			}
			
			// calculate the estimated stock
			int estimatedStock = stock - estimatedUsage;
			Log.d("CalculationManager", "getEstimatedStock estimatedStock="+estimatedStock);
			return estimatedStock;
		}

		return Integer.MAX_VALUE;
	}

	@Override
	public int getThreshold(Drug drug) {
		StockHistoryTableAdapter stockHistoryDb = DatabaseManager.getStockHistoryTableAdapter();
		StockHistory stockHistory = stockHistoryDb.findByDrug(drug);
		
		PhoneTableAdapter phoneDb = DatabaseManager.getPhoneTableAdapter();
		Phone userDetails = phoneDb.findOne();
		
		int days = 0;
		if (userDetails != null) {
			if (userDetails.getDrugLeadTime() != null) {
				days = userDetails.getDrugLeadTime();
			} else {
				days = DEFAULT_LEAD_TIME;
			}
			if (userDetails.getDrugSafetyLevel() != null) {
				days = days + userDetails.getDrugSafetyLevel();
			} else {
				days = days + DEFAULT_SAFETY_LEVEL;
			}
		}
		Log.d("CalculationManager", "getThreshold for "+drug+" days="+days);
		
		int threshold = DEFAULT_ADC;
		if (stockHistory != null) {
			threshold = days * stockHistory.getAverageDailyConsumption();
		}
		Log.d("CalculationManager", "getThreshold threshold="+threshold);
		
		return threshold;
	}
}