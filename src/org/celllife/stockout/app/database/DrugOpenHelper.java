package org.celllife.stockout.app.database;

import org.celllife.stockout.app.domain.Drug;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * This is the Data Access Object (DAO) for the Drug table. All methods
 * required to save, update, or retrieve Drugs from the database 
 * should be defined here.
 */
public class DrugOpenHelper extends SQLiteOpenHelper {
	
	// Database Version
	private static final int DATABASE_VERSION = 1;
	
	// Database Name
	private static final String DATABASE_NAME = "stockOut";
	
	// Drug Table Name
	private static final String TABLE_DRUG = "drug";
	
	// Drug Table Column Namers
	private static final String ID = "id";
	private static final String DESCRIPTION = "description";
	private static final String BARCODE = "barcode";
	
	// Drug Queries
	// FIXME: aren't we concerned with SQL injection?
	private static final String QUERY_FINDBY_BARCODE = "SELECT  * FROM " + TABLE_DRUG + " WHERE " + BARCODE + " = ";
	private static final String QUERY_FINDBY_ID = "SELECT  * FROM " + TABLE_DRUG + " WHERE " + ID + " = ";

	String CREATE_DRUG_TABLE = 
			"CREATE TABLE " + TABLE_DRUG +" ("
			+ ID + " INTEGER PRIMARY KEY, " 
			+ DESCRIPTION + " TEXT, "
			+ BARCODE + " TEXT )";

	
	public DrugOpenHelper(Context context) {
		super (context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	//creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_DRUG_TABLE);
	}

	//Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DRUG);
        
        // Create tables again
        onCreate(db);
		
        // Initialise the database
        initialiseDrugList();
	}
	
	
	//All CRUD(Create, Read, Update, Delete) Operations below
	
	/**
	 * Inserts the specified drug into the database
	 */
	public void addDrug(Drug drug){
		SQLiteDatabase db = this.getWritableDatabase();
		try {
			ContentValues values = new ContentValues();
			values.put (DESCRIPTION, drug.getDescription());
			values.put (BARCODE, drug.getBarcode()); 
			
			//Inserting Row
			db.insert(TABLE_DRUG, null, values);
		} finally {
			db.close();
		}
	}

	public Drug findByBarcode(String barcode) {
		SQLiteDatabase db = this.getWritableDatabase();
		try {
			Cursor c = db.rawQuery(QUERY_FINDBY_BARCODE+barcode, null);
		    return readDrugFromCursor(c);
		} finally {
			db.close();
		}
	}

	public Drug findById(Long id) {
		SQLiteDatabase db = this.getWritableDatabase();
		try {
			Cursor c = db.rawQuery(QUERY_FINDBY_ID+id, null);
		    return readDrugFromCursor(c);
		} finally {
			db.close();
		}		
	}
	
	private Drug readDrugFromCursor(Cursor c) {
	    if (c != null) {
	        c.moveToFirst();
	    }
	 
	    if (c.getCount() > 0) {
		    Drug d = new Drug();
		    d.setId(c.getLong(c.getColumnIndex(ID)));
		    d.setDescription((c.getString(c.getColumnIndex(DESCRIPTION))));
		    d.setBarcode(c.getString(c.getColumnIndex(BARCODE)));
		    return d;
	    }

	    return null;
	}

	private void initialiseDrugList() {
		Drug grandpa = new Drug("Grandpa tablets", "122344556654");
		addDrug(grandpa);
	}
	
}
