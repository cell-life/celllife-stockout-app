package org.celllife.stockout.app.database;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.celllife.stockout.app.database.framework.TableAdapter;
import org.celllife.stockout.app.domain.Alert;
import org.celllife.stockout.app.domain.AlertStatus;
import org.celllife.stockout.app.domain.Drug;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * This is the Data Access Object (DAO) for the Alert table. All methods
 * required to save, update, or retrieve Alerts from the database 
 * should be defined here.
 */
public class AlertTableAdapter extends TableAdapter<Alert> {
	
	// Alert Table Name
	private static final String TABLE_ALERT = "alert";
	
	// Alert Table Column Names
	private static final String ID = "id";
	private static final String DATE = "date";
	private static final String LEVEL = "level";
	private static final String MESSAGE = "message";
	private static final String STATUS = "status";
	private static final String DRUG = "drug_id";
			
	// Alert Queries
	private static final String QUERY_FINDBY_DRUG = "SELECT  * FROM " + TABLE_ALERT + " WHERE " + DRUG + " = ?";

	String CREATE_ALERT_TABLE = 
			"CREATE TABLE " + TABLE_ALERT +" ("
			+ ID + " INTEGER PRIMARY KEY, " 
			+ DATE + " NUMERIC, "
			+ LEVEL + " INTEGER, "
			+ MESSAGE + " TEXT, " 
			+ STATUS + " TEXT, "
			+ DRUG + " NUMERIC )";

	private DrugTableAdapter drugTableAdapter;
	
	public AlertTableAdapter(DrugTableAdapter drugTableAdapter) {
		this.drugTableAdapter = drugTableAdapter;
	}

	@Override
	public String getCreateTableSql() {
		return CREATE_ALERT_TABLE;
	}

	@Override
	public String getTableName() {
		return TABLE_ALERT;
	}

	@Override
	public List<ContentValues> getInitialData() {
		List<ContentValues> initialData = new ArrayList<ContentValues>();
		return initialData;
	}
	
	@Override
	protected ContentValues createContentValues(Alert alert) {
		ContentValues values = new ContentValues();
		values.put(LEVEL, alert.getLevel());
		values.put(DATE, alert.getDate().getTime());
		values.put(MESSAGE, alert.getMessage());
		values.put(STATUS, alert.getStatus().toString());
		if (alert.getDrug() != null) {
			values.put(DRUG, alert.getDrug().getId());
		}
		return values;
	}

	@Override
	public Alert readFromCursor(Cursor c) {
	    if (c.getCount() > 0) {
		    Alert a = new Alert();
		    a.setId(c.getLong(c.getColumnIndex(ID)));
		    a.setDate(new Date(c.getLong(c.getColumnIndex(DATE))));
		    a.setLevel(c.getInt(c.getColumnIndex(LEVEL)));
		    a.setMessage(c.getString(c.getColumnIndex(MESSAGE)));
		    AlertStatus status = AlertStatus.valueOf(c.getString(c.getColumnIndex(STATUS)));
		    a.setStatus(status);
		    long drugId = c.getLong(c.getColumnIndex(DRUG));
		    Drug drug = drugTableAdapter.findById(drugId);
		    a.setDrug(drug);
		    return a;
	    }
	    return null;
	}

	// Add the table specific CRUD operations here

	public Alert findByDrug(Drug drug) {
		return db.find(this, QUERY_FINDBY_DRUG, new String[] { drug.getId().toString() });
	}	
}
