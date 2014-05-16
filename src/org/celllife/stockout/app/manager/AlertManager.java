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
	 * Checks to see if there is an alert already for the specified Drug
	 * If there is, and more than 24 hours have passed then escalate the Alert (if it is level 1 or 2).
	 * If there isn't, then create a new level 1 Alert.
	 * @param drug
	 */
	void escalateAlert(Drug drug);

	/**
	 * Cancels any Alert on the specified Drug
	 * @param drug
	 */
	void cancelAlert(Drug drug);
	/**
	 * Retrieves the latest Alerts from the Stock Management Server
	 * @return List of Alerts that were just created
	 */
	List<Alert> updateAlerts();

	/**
	 * Runs through the calculations and generates phone specific Alerts
	 */
	void generateAlerts();
}
