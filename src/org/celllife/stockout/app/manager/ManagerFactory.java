package org.celllife.stockout.app.manager;

import android.content.Context;

public class ManagerFactory {

	private static AlertManager alertManager;
	private static StockTakeManager stockTakeManager;
	private static SessionManager sessionManager;
	
	public static void initialise(Context context) {
		DatabaseManager.initialise(context);
	}

	public static AlertManager getAlertManager() {
		if (alertManager == null) {
			alertManager = new AlertManagerImpl();
		}
		getSessionManager().markInteraction();
		return alertManager;
	}

	public static StockTakeManager getStockTakeManager() {
		if (stockTakeManager == null) {
			stockTakeManager = new StockTakeManagerImpl();
		}
		getSessionManager().markInteraction();
		return stockTakeManager;
	}

	public static SessionManager getSessionManager() {
		if (sessionManager == null) {
			sessionManager = new SessionManagerImpl();
		}
		return sessionManager;
	}

}