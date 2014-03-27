package org.celllife.stockout.app.manager;

import org.celllife.stockout.app.domain.Drug;
import org.celllife.stockout.app.domain.StockTake;

public interface CalculationManager {

	/**
	 * Calculates the new Average Daily Consumption (ADC) for the specified Drug
	 * @param oldStockTake previous StockTake, must not be null
	 * @param newStockTake new StockTake, must not be null
	 */
	int calculateAverageDailyConsumption(StockTake oldStockTake, StockTake newStockTake);

	/**
	 * Calculates the estimated stock on hand for the specified Drug using the ADC
	 * @param drug Drug
	 * @return int quantity of the estimated stock
	 */
	int getEstimatedStock(Drug drug);

	/**
	 * Determines the level at which an alert should be generated for the specified Drug.
	 * This uses the last known ADC and a safety level.
	 * @param drug Drug
	 * @return int minimum quantity of stock required
	 */
	int getThreshold(Drug drug);

}
