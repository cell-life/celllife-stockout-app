package org.celllife.stockout.app.manager;

import java.util.ArrayList;
import java.util.List;

import org.celllife.stockout.app.database.AlertTableAdapter;
import org.celllife.stockout.app.database.DrugTableAdapter;
import org.celllife.stockout.app.database.PhoneTableAdapter;
import org.celllife.stockout.app.database.ServerCommunicationLogTableAdapter;
import org.celllife.stockout.app.database.StockHistoryTableAdapter;
import org.celllife.stockout.app.database.StockReceivedTableAdapter;
import org.celllife.stockout.app.database.StockTakeTableAdapter;
import org.celllife.stockout.app.database.framework.DatabaseOpenHelper;
import org.celllife.stockout.app.database.framework.TableHelper;

import android.content.Context;
import android.util.Log;

/**
 * Use this to get access to the various data access objects.
 * This class should be used by other managers only.
 */
public class DatabaseManager {

	private static DatabaseOpenHelper db;
	
	private static PhoneTableAdapter phoneDb;
	private static DrugTableAdapter drugDb;
	private static AlertTableAdapter alertDb;
	private static StockTakeTableAdapter stockTakeDb;
	private static StockReceivedTableAdapter stockReceivedDb;
	private static StockHistoryTableAdapter stockHistoryDb;
	private static ServerCommunicationLogTableAdapter serverCommunicationLogDb;
	
	public static DrugTableAdapter getDrugTableAdapter() {
		return drugDb;
	}

	public static PhoneTableAdapter getPhoneTableAdapter() {
		return phoneDb;
	}

	public static AlertTableAdapter getAlertTableAdapter() {
		return alertDb;
	}

	public static StockTakeTableAdapter getStockTakeTableAdapter() {
		return stockTakeDb;
	}

	public static StockReceivedTableAdapter getStockReceivedTableAdapter() {
		return stockReceivedDb;
	}

	public static StockHistoryTableAdapter getStockHistoryTableAdapter() {
		return stockHistoryDb;
	}

	public static ServerCommunicationLogTableAdapter getServerCommunicationLogTableAdapter() {
	    return serverCommunicationLogDb;
	}
	
	public static void initialise(Context context) {
	    if (db == null) {
	        Log.d("DatabaseManager", "Initalise with application context");
    		List<TableHelper<?>> tables = new ArrayList<TableHelper<?>>();
    		phoneDb = new PhoneTableAdapter();
    		tables.add(phoneDb);
    		drugDb = new DrugTableAdapter();
    		tables.add(drugDb);
    		alertDb = new AlertTableAdapter(drugDb);
    		tables.add(alertDb);
    		stockTakeDb = new StockTakeTableAdapter(drugDb);
    		tables.add(stockTakeDb);
    		stockReceivedDb = new StockReceivedTableAdapter(drugDb);
    		tables.add(stockReceivedDb);
    		stockHistoryDb = new StockHistoryTableAdapter(drugDb);
    		tables.add(stockHistoryDb);
    		serverCommunicationLogDb = new ServerCommunicationLogTableAdapter();
    		tables.add(serverCommunicationLogDb);
    		db = new DatabaseOpenHelper(context, tables);
    		Log.d("DatabaseManager", "Finished initalise with application context");
	    } else {
	        Log.d("DatabaseManager", "Already initialised db="+db);
	    }
	}

	public static void shutdown() {
	    if (db != null) {
	        db.close();
	    }
	}
}