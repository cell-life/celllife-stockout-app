package org.celllife.stockout.app.manager;

import org.celllife.stockout.app.manager.impl.AlertManagerImpl;
import org.celllife.stockout.app.manager.impl.AuthenticationManagerImpl;
import org.celllife.stockout.app.manager.impl.CalculationManagerImpl;
import org.celllife.stockout.app.manager.impl.DrugManagerImpl;
import org.celllife.stockout.app.manager.impl.ServerCommunicationLogManagerImpl;
import org.celllife.stockout.app.manager.impl.SessionManagerImpl;
import org.celllife.stockout.app.manager.impl.SettingManagerImpl;
import org.celllife.stockout.app.manager.impl.SetupManagerImpl;
import org.celllife.stockout.app.manager.impl.StockTakeManagerImpl;

import android.content.Context;
import android.util.Log;

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
    private static SetupManager setupManager;
    private static ServerCommunicationLogManager serverCommunicationLogManager;
    private static DrugManager drugManager;
	
	public static void initialise(Context context) {
	    Log.d("ManagerFactory", "Initalise with application context.");
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
		getSessionManager().markInteraction();
		return authenticationManager;
	}

	public static SettingManager getSettingManager() {
		if (settingManager == null) {
			settingManager = new SettingManagerImpl(context);
		}
		getSessionManager().markInteraction();
		return settingManager;
	}
	
	public static CalculationManager getCalculationManager() {
		if (calculationManager == null) {
			calculationManager = new CalculationManagerImpl();
		}
		getSessionManager().markInteraction();
		return calculationManager;
	}

    public static SetupManager getSetupManager() {
        if (setupManager == null) {
            setupManager = new SetupManagerImpl();
        }
        return setupManager;
    }

    public static ServerCommunicationLogManager getServerCommunicationLogManager() {
        if (serverCommunicationLogManager == null) {
            serverCommunicationLogManager = new ServerCommunicationLogManagerImpl();
        }
        return serverCommunicationLogManager;
    }
    
    public static DrugManager getDrugManager() {
		if (drugManager == null) {
			drugManager = new DrugManagerImpl();
		}
		getSessionManager().markInteraction();
		return drugManager;
    }
}