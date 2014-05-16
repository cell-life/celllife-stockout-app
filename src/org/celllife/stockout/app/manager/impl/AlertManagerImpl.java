package org.celllife.stockout.app.manager.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.celllife.stockout.app.database.AlertTableAdapter;
import org.celllife.stockout.app.domain.Alert;
import org.celllife.stockout.app.domain.AlertStatus;
import org.celllife.stockout.app.domain.Drug;
import org.celllife.stockout.app.domain.comparator.AlertComparator;
import org.celllife.stockout.app.integration.rest.GetAlertMethod;
import org.celllife.stockout.app.integration.rest.framework.RestCommunicationException;
import org.celllife.stockout.app.manager.AlertManager;
import org.celllife.stockout.app.manager.CalculationManager;
import org.celllife.stockout.app.manager.DatabaseManager;
import org.celllife.stockout.app.manager.ManagerFactory;

import android.util.Log;

public class AlertManagerImpl implements AlertManager {

	public static final long ONE_DAY = 1 * 24 * 60 * 60 * 1000;

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
	public List<Alert> updateAlerts() {
		// Retrieves the latest alerts from the server and saves them to the database
		List<Alert> alerts = null;
		try {
			alerts = GetAlertMethod.getLatestAlerts();
			for (Alert a : alerts) {
				addAlert(a);
			}
		} catch (RestCommunicationException e) {
			// swallows exceptions because this method is being used by the background task
			Log.w("AlertManager", "Got an error while retrieving the latest alerts.", e);
		}
		return alerts;
	}

	@Override
	public void generateAlerts() {
		// Go through all the drugs on the system and check if alerts should be generated (using the calculation service)
		List<Drug> drugs = DatabaseManager.getDrugTableAdapter().findAll();
		for (Drug d : drugs) {
			CalculationManager calc = ManagerFactory.getCalculationManager();
			int estimatedStock = calc.getEstimatedStock(d);
			int threshold = calc.getThreshold(d);
			if (estimatedStock <= threshold) {
				escalateAlert(d);
			}
		}
	}

	@Override
	public void escalateAlert(Drug drug) {
		AlertTableAdapter alertDb = DatabaseManager.getAlertTableAdapter();
		Alert oldAlert = alertDb.findByDrug(drug);
		Alert newAlert = null;
		if (oldAlert == null) {
			newAlert = new Alert(new Date(), 1, drug.getDescription(), AlertStatus.NEW, drug);
		} else {
			Date now = new Date();
			if ((oldAlert.getDate().getTime() + ONE_DAY) <= now.getTime()) {
				int level = Math.min(3, (oldAlert.getLevel() + 1)); // ensure that the level doesn't go over 3
				alertDb.deleteById(oldAlert.getId());
				newAlert = new Alert(new Date(), level, drug.getDescription(), AlertStatus.NEW, drug);
			}
		}
		if (newAlert != null) {
			alertDb.insert(newAlert);
		}
	}
}