package org.celllife.stockout.app.manager.impl;

import java.util.Collections;
import java.util.List;

import org.celllife.stockout.app.database.AlertTableAdapter;
import org.celllife.stockout.app.domain.Alert;
import org.celllife.stockout.app.domain.Drug;
import org.celllife.stockout.app.domain.comparator.AlertComparator;
import org.celllife.stockout.app.manager.AlertManager;
import org.celllife.stockout.app.manager.DatabaseManager;

public class AlertManagerImpl implements AlertManager {
	
	public AlertManagerImpl() {
	}

	@Override
	public List<Alert> getAlerts() {
		AlertTableAdapter alertDb = DatabaseManager.getAlertTableAdapter();
		List<Alert> alerts = alertDb.findAll();
		Collections.sort(alerts, new AlertComparator());
		return alerts;
	}

	@Override
	public void addAlert(Alert alert) {
		AlertTableAdapter alertDb = DatabaseManager.getAlertTableAdapter();
		// Cancel (delete) old alert
		cancelAlert(alert.getDrug());
		// Insert new alert
		alertDb.insert(alert);
	}

	@Override
	public void cancelAlert(Drug drug) {
		AlertTableAdapter alertDb = DatabaseManager.getAlertTableAdapter();
		// cancel any old alert (by deleting them)
		Alert oldAlert = alertDb.findByDrug(drug);
		if (oldAlert != null) {
			alertDb.deleteById(oldAlert.getId());
		}
	}

	@Override
	public void updateAlerts() {
		// Retrieve the latest alerts from the server

	}

	@Override
	public void generateAlerts() {
		// Go through all the drugs on the system and check if alerts should be generated (using the calculation service)
	}

}
