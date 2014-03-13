package org.celllife.stockout.app.manager;

import java.util.ArrayList;
import java.util.List;

import org.celllife.stockout.app.database.AlertTableAdapter;
import org.celllife.stockout.app.database.DrugTableAdapter;
import org.celllife.stockout.app.database.StockHistoryTableAdapter;
import org.celllife.stockout.app.database.StockTakeTableAdapter;
import org.celllife.stockout.app.database.UserTableAdapter;
import org.celllife.stockout.app.database.framework.DatabaseOpenHelper;
import org.celllife.stockout.app.database.framework.TableHelper;

import android.content.Context;

/**
 * Use this to get access to the various data access objects.
 * This class should be used by other managers only.
 */
public class DatabaseManager {

	private static DatabaseOpenHelper db;
	private static UserTableAdapter userDb;
	private static DrugTableAdapter drugDb;
	private static AlertTableAdapter alertDb;
	private static StockTakeTableAdapter stockTakeDb;
	private static StockHistoryTableAdapter stockHistoryDb;
	
	public static DrugTableAdapter getDrugTableAdapter(Context context) {
		if (db == null) {
			initialiseDatabase(context);
		}
		return drugDb;
	}

	public static UserTableAdapter getUserTableAdapter(Context context) {
		if (db == null) {
			initialiseDatabase(context);
		}
		return userDb;
	}

	public static AlertTableAdapter getAlertTableAdapter(Context context) {
		if (db == null) {
			initialiseDatabase(context);
		}
		return alertDb;
	}

	public static StockTakeTableAdapter getStockTakeTableAdapter(Context context) {
		if (db == null) {
			initialiseDatabase(context);
		}
		return stockTakeDb;
	}

	public static StockHistoryTableAdapter getStockHistoryTableAdapter(Context context) {
		if (db == null) {
			initialiseDatabase(context);
		}
		return stockHistoryDb;
	}
	
	private static void initialiseDatabase(Context context) {
		List<TableHelper<?>> tables = new ArrayList<TableHelper<?>>();
		userDb = new UserTableAdapter();
		tables.add(userDb);
		drugDb = new DrugTableAdapter();
		tables.add(drugDb);
		alertDb = new AlertTableAdapter(drugDb);
		tables.add(alertDb);
		stockTakeDb = new StockTakeTableAdapter(drugDb);
		tables.add(stockTakeDb);
		stockHistoryDb = new StockHistoryTableAdapter(drugDb);
		tables.add(stockHistoryDb);
		db = new DatabaseOpenHelper(context, tables);
	}
}
