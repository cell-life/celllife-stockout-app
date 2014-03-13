package org.celllife.stockout.app.database;

import java.util.ArrayList;
import java.util.List;

import org.celllife.stockout.app.database.framework.TableAdapter;
import org.celllife.stockout.app.domain.Drug;
import org.celllife.stockout.app.domain.StockHistory;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * This is the Data Access Object (DAO) for the StockHistory table. All methods
 * required to save, update, or retrieve StockHistories from the database 
 * should be defined here.
 */
public class StockHistoryTableAdapter extends TableAdapter<StockHistory> {
	
	// StockHistory Table Name
	private static final String TABLE_STOCKHISTORY = "stockHistory";
	
	// StockHistory Table Column Names
	private static final String ID = "id";
	private static final String DRUG = "drug_id";
	private static final String AVERAGE_DAILY_CONSUMPTION = "average_daily_consumption";
			
	// StockHistory Queries
	private static final String QUERY_FINDBY_DRUG = "SELECT  * FROM " + TABLE_STOCKHISTORY + " WHERE " + DRUG + " = ?";

	String CREATE_STOCKHISTORY_TABLE = 
			"CREATE TABLE " + TABLE_STOCKHISTORY +" ("
			+ ID + " INTEGER PRIMARY KEY, " 
			+ DRUG + " NUMERIC, "
			+ AVERAGE_DAILY_CONSUMPTION + " INTEGER )";

	private DrugTableAdapter drugTableAdapter;
	
	public StockHistoryTableAdapter(DrugTableAdapter drugTableAdapter) {
		this.drugTableAdapter = drugTableAdapter;
	}

	@Override
	public String getCreateTableSql() {
		return CREATE_STOCKHISTORY_TABLE;
	}

	@Override
	public String getTableName() {
		return TABLE_STOCKHISTORY;
	}

	@Override
	public List<ContentValues> getInitialData() {
		List<ContentValues> initialData = new ArrayList<ContentValues>();
		return initialData;
	}
	
	@Override
	protected ContentValues createContentValues(StockHistory stockHistory) {
		ContentValues values = new ContentValues();
		if (stockHistory.getDrug() != null) {
			values.put(DRUG, stockHistory.getDrug().getId());
		}
		values.put(AVERAGE_DAILY_CONSUMPTION, stockHistory.getAverageDailyConsumption());
		return values;
	}

	@Override
	public StockHistory readFromCursor(Cursor c) {
	    if (c.getCount() > 0) {
		    StockHistory sh = new StockHistory();
		    sh.setId(c.getLong(c.getColumnIndex(ID)));
		    sh.setAverageDailyConsumption(c.getInt(c.getColumnIndex(AVERAGE_DAILY_CONSUMPTION)));
		    long drugId = c.getLong(c.getColumnIndex(DRUG));
		    Drug drug = drugTableAdapter.findById(drugId);
		    sh.setDrug(drug);
		    return sh;
	    }
	    return null;
	}

	// Add the table specific CRUD operations here

	public StockHistory findByDrug(Drug drug) {
		return db.find(this, QUERY_FINDBY_DRUG, new String[] { drug.getId().toString() });
	}	
}
