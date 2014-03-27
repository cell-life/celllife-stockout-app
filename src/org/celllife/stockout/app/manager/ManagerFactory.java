package org.celllife.stockout.app.manager;

import android.content.Context;

/**
 * This class is used to retrieve instances of the various Managers. It handles
 * "dependency injection" in that it creates singleton instances of the various Managers
 * used by the Stock App.
 */
public class ManagerFactory {

	private static Context context;
	private static AlertManager alertManager;
	private static StockTakeManager stockTakeManager;
	private static SessionManager sessionManager;
	private static AuthenticationManager authenticationManager;
	private static SettingManager settingManager;
	private static CalculationManager calculationManager;
	
	public static void initialise(Context context) {
		ManagerFactory.context = context;
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

	public static AuthenticationManager getAuthenticationManager() {
		if (authenticationManager == null) {
			authenticationManager = new AuthenticationManagerImpl();
		}
		return authenticationManager;
	}

	public static SettingManager getSettingManager() {
		if (settingManager == null) {
			settingManager = new SettingManagerImpl(context);
		}
		return settingManager;
	}
	
	public static CalculationManager getCalculationManager() {
		if (calculationManager == null) {
			calculationManager = new CalculationManagerImpl();
		}
		return calculationManager;
	}
}