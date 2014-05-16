package org.celllife.stockout.app.database;

import java.util.ArrayList;
import java.util.List;

import org.celllife.stockout.app.database.framework.TableAdapter;
import org.celllife.stockout.app.domain.Drug;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

/**
 * This is the Data Access Object (DAO) for the Drug table. All methods
 * required to save, update, or retrieve Drugs from the database 
 * should be defined here.
 */
public class DrugTableAdapter extends TableAdapter<Drug> {
	
	// Drug Table Name
	private static final String TABLE_DRUG = "drug";
	
	// Drug Table Column Names
	private static final String ID = "id";
	private static final String DESCRIPTION = "description";
	private static final String BARCODE = "barcode";
	
	// Drug Queries
	private static final String QUERY_FINDBY_BARCODE = "SELECT  * FROM " + TABLE_DRUG + " WHERE " + BARCODE + " = ?";

	String CREATE_DRUG_TABLE = 
			"CREATE TABLE " + TABLE_DRUG +" ("
			+ ID + " INTEGER PRIMARY KEY, " 
			+ DESCRIPTION + " TEXT, "
			+ BARCODE + " TEXT )";

	
	public DrugTableAdapter() {
	}

	@Override
	public String getCreateTableSql() {
		return CREATE_DRUG_TABLE;
	}

	@Override
	public String getTableName() {
		return TABLE_DRUG;
	}

	@Override
	public List<ContentValues> getInitialData() {
		Log.w("DrugTableAdapter", "Initialising Drugs");
		List<ContentValues> initialData = new ArrayList<ContentValues>();
		Drug grandpa = new Drug("60015204", "Grandpa 24 tablets");
		Drug panado = new Drug("60011053", "Panado 500mg 24 tablets");
		initialData.add(createContentValues(grandpa));
		initialData.add(createContentValues(panado));
		return initialData;
	}
	
	@Override
	protected ContentValues createContentValues(Drug drug) {
		ContentValues values = new ContentValues();
		values.put(DESCRIPTION, drug.getDescription());
		values.put(BARCODE, drug.getBarcode()); 
		return values;
	}

	@Override
	public Drug readFromCursor(Cursor c) {
	    if (c.getCount() > 0) {
		    Drug d = new Drug();
		    d.setId(c.getLong(c.getColumnIndex(ID)));
		    d.setDescription((c.getString(c.getColumnIndex(DESCRIPTION))));
		    d.setBarcode(c.getString(c.getColumnIndex(BARCODE)));
		    return d;
	    }
	    return null;
	}

	// Add the table specific CRUD operations here

	public Drug findByBarcode(String barcode) {
		if (barcode != null) {
			return db.find(this, QUERY_FINDBY_BARCODE, new String[] { barcode });
		}
		return null;
	}	
}
