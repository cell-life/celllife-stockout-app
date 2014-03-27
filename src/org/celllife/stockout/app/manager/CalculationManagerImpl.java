package org.celllife.stockout.app.manager;

import java.util.Date;

import org.celllife.stockout.app.database.PhoneTableAdapter;
import org.celllife.stockout.app.database.StockHistoryTableAdapter;
import org.celllife.stockout.app.database.StockTakeTableAdapter;
import org.celllife.stockout.app.domain.Drug;
import org.celllife.stockout.app.domain.Phone;
import org.celllife.stockout.app.domain.StockHistory;
import org.celllife.stockout.app.domain.StockTake;

import android.util.Log;

public class CalculationManagerImpl implements CalculationManager {
	
	private static final int DEFAULT_LEAD_TIME = 3;
	private static final int DEFAULT_SAFETY_LEVEL = 3;
	private static final int DEFAULT_ADC = 1;
	
	private static final int MILLISECONDS_IN_DAY = 1000*60*60*24;

	@Override
	public int calculateAverageDailyConsumption(StockTake oldStockTake, StockTake newStockTake) {
		long milliseconds = newStockTake.getDate().getTime() - oldStockTake.getDate().getTime();
		int days = (int) Math.round(milliseconds/MILLISECONDS_IN_DAY);
		Log.d("CalculationManager", "calculateAverageDailyConsumption days="+days);
		// note: if we take into account opening hours, we should * days_open / 7 and then 
		// round to the nearest whole number

		int stockUsed = oldStockTake.getQuantity() - newStockTake.getQuantity();
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
		
		StockHistoryTableAdapter stockHistoryDb = DatabaseManager.getStockHistoryTableAdapter();
		StockHistory stockHistory = stockHistoryDb.findByDrug(drug);
		
		long milliseconds = now.getTime() - lastStockTake.getDate().getTime();
		int days = (int) Math.round(milliseconds/MILLISECONDS_IN_DAY);
		Log.d("CalculationManager", "getEstimatedStock for "+drug+" days="+days);
		
		int estimatedUsage = (int)days * stockHistory.getAverageDailyConsumption();
		Log.d("CalculationManager", "getEstimatedStock estimatedUsage="+estimatedUsage);

		int estimatedStock = lastStockTake.getQuantity() - estimatedUsage;
		Log.d("CalculationManager", "getEstimatedStock estimatedStock="+estimatedStock);

		return estimatedStock;
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