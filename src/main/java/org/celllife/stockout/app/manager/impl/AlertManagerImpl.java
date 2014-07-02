package org.celllife.stockout.app.manager.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.celllife.stockout.app.database.AlertTableAdapter;
import org.celllife.stockout.app.domain.Alert;
import org.celllife.stockout.app.domain.AlertStatus;
import org.celllife.stockout.app.domain.Drug;
import org.celllife.stockout.app.domain.ServerCommunicationType;
import org.celllife.stockout.app.domain.StockReceived;
import org.celllife.stockout.app.domain.StockTake;
import org.celllife.stockout.app.domain.comparator.AlertComparator;
import org.celllife.stockout.app.integration.rest.GetAlertMethod;
import org.celllife.stockout.app.integration.rest.framework.RestCommunicationException;
import org.celllife.stockout.app.manager.AlertManager;
import org.celllife.stockout.app.manager.CalculationManager;
import org.celllife.stockout.app.manager.DatabaseManager;
import org.celllife.stockout.app.manager.ManagerFactory;
import org.celllife.stockout.app.manager.ServerCommunicationLogManager;

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
	public Alert addAlert(Alert alert) {
		AlertTableAdapter alertDb = DatabaseManager.getAlertTableAdapter();
		// Cancel (delete) old alert
		cancelAlert(alert.getDrug());
		// Insert new alert
		alertDb.insert(alert);
		// Retrieve latest copy of new alert
		return alertDb.findByDrug(alert.getDrug());
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
		List<Alert> newAlerts = new ArrayList<Alert>();
		try {
			List<Alert> alerts = getLatestAlertsMethod();
			for (Alert a : alerts) {
				a.getDate();
				a.getLevel();
				a.getMessage();
				a.getStatus();
				Drug d = DatabaseManager.getDrugTableAdapter().findByBarcode(a.getDrug().getBarcode());
				if (d != null) {
					a.setDrug(d);
				} else {
				  Log.e("Alert", "Drug not found");
				}
				Alert newAlert = addAlert(a);
				newAlerts.add(newAlert);
				
			}
		} catch (RestCommunicationException e) {
			// swallows exceptions because this method is being used by the background task
			Log.w("AlertManager", "Got an error while retrieving the latest alerts.", e);
		}
		return newAlerts;
	}

	@Override
	public List<Alert> generateAlerts() {
		// Go through all the drugs on the system and check if alerts should be generated (using the calculation service)
		List<Alert> alerts = new ArrayList<Alert>();
		List<Drug> drugs = DatabaseManager.getDrugTableAdapter().findAll();
		for (Drug d : drugs) {
			CalculationManager calc = ManagerFactory.getCalculationManager();
			int estimatedStock = calc.getEstimatedStock(d);
			int threshold = calc.getThreshold(d);
			if (estimatedStock <= threshold) {
				Alert newAlert = escalateAlert(d);
				if (newAlert != null) {
					alerts.add(newAlert);
				}
			}
		}
		return alerts;
	}

	@Override
	public Alert escalateAlert(Drug drug) {
		AlertTableAdapter alertDb = DatabaseManager.getAlertTableAdapter();
		Alert oldAlert = alertDb.findByDrug(drug);
		Alert newAlert = null;
		if (oldAlert == null) {
			List<StockReceived> stockReceived = DatabaseManager.getStockReceivedTableAdapter().findByDrug(drug);
			StockTake stockTake = DatabaseManager.getStockTakeTableAdapter().findByDrug(drug);
			if (hasStockBeenReceived(stockReceived, stockTake)) {	
				newAlert = new Alert(new Date(), 1, drug.getDescription(), AlertStatus.NEW, drug);
			}
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
			newAlert = alertDb.findByDrug(drug);
		}
		return newAlert;
	}
	
	private boolean hasStockBeenReceived(List<StockReceived> stocksReceived, StockTake stockTake) {
		if (stockTake != null) {
			for (StockReceived stockReceived : stocksReceived) {
				if (stockReceived.getDate().after(stockTake.getDate())) {
					return true;
				}
			}
		}
		return false;
	}

    private List<Alert> getLatestAlertsMethod() {
        ServerCommunicationLogManager logManager = ManagerFactory.getServerCommunicationLogManager();
        try {
            List<Alert> alerts = GetAlertMethod.getLatestAlerts();
            logManager.createServerCommunicationLog(ServerCommunicationType.ALERT, true);
            return alerts;
        } catch (RuntimeException e) {
            logManager.createServerCommunicationLog(ServerCommunicationType.ALERT, false);
            throw e;
        }
    }
}