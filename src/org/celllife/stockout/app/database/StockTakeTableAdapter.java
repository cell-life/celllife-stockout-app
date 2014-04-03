package org.celllife.stockout.app.database;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.celllife.stockout.app.database.framework.TableAdapter;
import org.celllife.stockout.app.domain.Drug;
import org.celllife.stockout.app.domain.StockTake;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * This is the Data Access Object (DAO) for the StockTake table. All methods
 * required to save, update, or retrieve StockTakes from the database 
 * should be defined here.
 */
public class StockTakeTableAdapter extends TableAdapter<StockTake> {
	
	// StockTake Table Name
	private static final String TABLE_STOCKTAKE = "stockTake";
	
	// StockTake Table Column Names
	private static final String ID = "id";
	private static final String DATE = "date";
	private static final String DRUG = "drug_id";
	private static final String QUANTITY = "quantity";
	private static final String SUBMITTED = "submitted";
			
	// StockTake Queries
	private static final String QUERY_FINDBY_DRUG = "SELECT  * FROM " + TABLE_STOCKTAKE + " WHERE " + DRUG + " = ?";
	private static final String QUERY_FINDBY_DATE = "SELECT  * FROM " + TABLE_STOCKTAKE + " WHERE " + DATE + " >= ?";
	private static final String QUERY_FINDBY_SUBMITTED = "SELECT  * FROM " + TABLE_STOCKTAKE + " WHERE " + SUBMITTED + " = 0";

	String CREATE_STOCKTAKE_TABLE = 
			"CREATE TABLE " + TABLE_STOCKTAKE +" ("
			+ ID + " INTEGER PRIMARY KEY, " 
			+ DATE + " NUMERIC, "
			+ DRUG + " NUMERIC, "
			+ QUANTITY + " INTEGER, " 
			+ SUBMITTED + " INTEGER )";

	private DrugTableAdapter drugTableAdapter;
	
	public StockTakeTableAdapter(DrugTableAdapter drugTableAdapter) {
		this.drugTableAdapter = drugTableAdapter;
	}

	@Override
	public String getCreateTableSql() {
		return CREATE_STOCKTAKE_TABLE;
	}

	@Override
	public String getTableName() {
		return TABLE_STOCKTAKE;
	}

	@Override
	public List<ContentValues> getInitialData() {
		List<ContentValues> initialData = new ArrayList<ContentValues>();
		return initialData;
	}
	
	@Override
	protected ContentValues createContentValues(StockTake stockTake) {
		ContentValues values = new ContentValues();
		values.put(DATE, stockTake.getDate().getTime());
		if (stockTake.getDrug() != null) {
			values.put(DRUG, stockTake.getDrug().getId());
		}
		values.put(QUANTITY, stockTake.getQuantity());
		values.put(SUBMITTED, stockTake.isSubmitted());
		return values;
	}

	@Override
	public StockTake readFromCursor(Cursor c) {
	    if (c.getCount() > 0) {
		    StockTake st = new StockTake();
		    st.setId(c.getLong(c.getColumnIndex(ID)));
		    st.setDate(new Date(c.getLong(c.getColumnIndex(DATE))));
		    st.setQuantity(c.getInt(c.getColumnIndex(QUANTITY)));
		    int submitted = c.getInt(c.getColumnIndex(SUBMITTED));
		    if (submitted == 1) {
		    	st.setSubmitted(true);
		    } else {
		    	st.setSubmitted(false);
		    }
		    long drugId = c.getLong(c.getColumnIndex(DRUG));
		    Drug drug = drugTableAdapter.findById(drugId);
		    st.setDrug(drug);
		    return st;
	    }
	    return null;
	}

	// Add the table specific CRUD operations here

	public StockTake findByDrug(Drug drug) {
		return db.find(this, QUERY_FINDBY_DRUG, new String[] { drug.getId().toString() });
	}

	public List<StockTake> findLatestStockTakes() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		String date = String.valueOf(cal.getTime().getTime());
		return db.findMany(this, QUERY_FINDBY_DATE, new String[] { date });
	}

	public List<StockTake> findUnsubmittedStockTakes() {
		return db.findMany(this, QUERY_FINDBY_SUBMITTED, new String[] { });
	}
}