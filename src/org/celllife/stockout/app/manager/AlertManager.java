package org.celllife.stockout.app.manager;

import java.util.List;

import org.celllife.stockout.app.domain.Alert;
import org.celllife.stockout.app.domain.Drug;

public interface AlertManager {

	/**
	 * Retrieves all the Alerts known to the app
	 * @return List of Alert
	 */
	List<Alert> getAlerts();

	/**
	 * Adds the specified Alert to the database (cancelling any other Alerts on the same Drug)
	 * @param alert
	 */
	void addAlert(Alert alert);

	/**
	 * Cancels any Alert on the specified Drug
	 * @param drug
	 */
	void cancelAlert(Drug drug);

	/**
	 * Retrieves the latest Alerts from the Stock Management Server
	 */
	void updateAlerts();

	/**
	 * Runs through the calculations and generates phone specific Alerts
	 */
	void generateAlerts();
}
