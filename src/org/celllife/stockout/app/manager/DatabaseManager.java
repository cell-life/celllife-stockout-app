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
	
	public static DrugTableAdapter getDrugTableAdapter() {
		return drugDb;
	}

	public static UserTableAdapter getUserTableAdapter() {
		return userDb;
	}

	public static AlertTableAdapter getAlertTableAdapter() {
		return alertDb;
	}

	public static StockTakeTableAdapter getStockTakeTableAdapter() {
		return stockTakeDb;
	}

	public static StockHistoryTableAdapter getStockHistoryTableAdapter() {
		return stockHistoryDb;
	}
	
	public static void initialise(Context context) {
		if (db == null) {
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
}