package org.celllife.stockout.app.database;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.celllife.stockout.app.database.framework.TableAdapter;
import org.celllife.stockout.app.domain.Drug;
import org.celllife.stockout.app.domain.StockReceived;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * This is the Data Access Object (DAO) for the StockReceived table. All methods
 * required to save, update, or retrieve StockReceiveds from the database 
 * should be defined here.
 */
public class StockReceivedTableAdapter extends TableAdapter<StockReceived> {
	
	// StockReceived Table Name
	private static final String TABLE_STOCKRECEIVED = "stockReceived";
	
	// StockReceived Table Column Names
	private static final String ID = "id";
	private static final String DATE = "date";
	private static final String DRUG = "drug_id";
	private static final String QUANTITY = "quantity";
	private static final String SUBMITTED = "submitted";
			
	// StockReceived Queries
	private static final String QUERY_FINDBY_DRUG = "SELECT  * FROM " + TABLE_STOCKRECEIVED + " WHERE " + DRUG + " = ?";
	private static final String QUERY_FINDBY_DATE = "SELECT  * FROM " + TABLE_STOCKRECEIVED + " WHERE " + DATE + " >= ?";

	String CREATE_STOCKRECEIVED_TABLE = 
			"CREATE TABLE " + TABLE_STOCKRECEIVED +" ("
			+ ID + " INTEGER PRIMARY KEY, " 
			+ DATE + " NUMERIC, "
			+ DRUG + " NUMERIC, "
			+ QUANTITY + " INTEGER, " 
			+ SUBMITTED + " INTEGER )";

	private DrugTableAdapter drugTableAdapter;
	
	public StockReceivedTableAdapter(DrugTableAdapter drugTableAdapter) {
		this.drugTableAdapter = drugTableAdapter;
	}

	@Override
	public String getCreateTableSql() {
		return CREATE_STOCKRECEIVED_TABLE;
	}

	@Override
	public String getTableName() {
		return TABLE_STOCKRECEIVED;
	}

	@Override
	public List<ContentValues> getInitialData() {
		List<ContentValues> initialData = new ArrayList<ContentValues>();
		return initialData;
	}
	
	@Override
	protected ContentValues createContentValues(StockReceived stockReceived) {
		ContentValues values = new ContentValues();
		values.put(DATE, stockReceived.getDate().getTime());
		if (stockReceived.getDrug() != null) {
			values.put(DRUG, stockReceived.getDrug().getId());
		}
		values.put(QUANTITY, stockReceived.getQuantity());
		values.put(SUBMITTED, stockReceived.isSubmitted());
		return values;
	}

	@Override
	public StockReceived readFromCursor(Cursor c) {
	    if (c.getCount() > 0) {
		    StockReceived sr = new StockReceived();
		    sr.setId(c.getLong(c.getColumnIndex(ID)));
		    sr.setDate(new Date(c.getLong(c.getColumnIndex(DATE))));
		    sr.setQuantity(c.getInt(c.getColumnIndex(QUANTITY)));
		    int submitted = c.getInt(c.getColumnIndex(SUBMITTED));
		    if (submitted == 1) {
		    	sr.setSubmitted(true);
		    } else {
		    	sr.setSubmitted(false);
		    }
		    long drugId = c.getLong(c.getColumnIndex(DRUG));
		    Drug drug = drugTableAdapter.findById(drugId);
		    sr.setDrug(drug);
		    return sr;
	    }
	    return null;
	}

	// Add the table specific CRUD operations here

	public List<StockReceived> findByDrug(Drug drug) {
		return db.findMany(this, QUERY_FINDBY_DRUG, new String[] { drug.getId().toString() });
	}

	public List<StockReceived> findLatestStockReceived() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		String date = String.valueOf(cal.getTime().getTime());
		return db.findMany(this, QUERY_FINDBY_DATE, new String[] { date });
	}
}