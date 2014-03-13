package org.celllife.stockout.app.database.framework;

import java.io.Serializable;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * This is the Data Access Object (DAO) for the Alert table. All methods
 * required to save, update, or retrieve Alerts from the database 
 * should be defined here.
 */
public class DatabaseOpenHelper extends SQLiteOpenHelper {
	
	// Database Version
	private static final int DATABASE_VERSION = 1;
	
	// Database Name
	private static final String DATABASE_NAME = "stockManagement";

	// List of all the tables in this database
	private List<TableHelper<?>> tables;

	
	public DatabaseOpenHelper(Context context, List<TableHelper<?>> tables) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.tables = tables;
		for (TableHelper<?> table : tables) {
			table.setDatabaseOpenHelper(this);
		}
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		for (TableHelper<?> table : tables) {
			db.execSQL(table.getCreateTableSql());
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		for (TableHelper<?> table: tables) {
			// Drop older table if existed
	        db.execSQL("DROP TABLE IF EXISTS " + table.getTableName());
	        
	        // Create tables again
	        db.execSQL(table.getCreateTableSql());
			
	        // Initialise the database
	        initialise(table);
		}
	}
	
	
	/**
	 * Inserts the specified data (row), for the specified table, into the database
	 */
	public <T extends Serializable> void addContent(TableHelper<T> table, ContentValues values){
		SQLiteDatabase db = this.getWritableDatabase();
		try {
			db.insert(table.getTableName(), null, values);
		} finally {
			db.close();
		}
	}

	/**
	 * Deletes the specified content from the specified table
	 */
	public <T extends Serializable> void deleteContent(TableHelper<T> table, Long id) {
		SQLiteDatabase db = this.getWritableDatabase();
		try {
			db.delete(table.getTableName(), "WHERE id = ?", new String[] { id.toString() } );
		} finally {
			db.close();
		}
	}

	/**
	 * Deletes the specified content from the specified table
	 */
	public <T extends Serializable> void updateContent(TableHelper<T> table, Long id, ContentValues values) {
		SQLiteDatabase db = this.getWritableDatabase();
		try {
			db.update(table.getTableName(), values, "id = ?", new String[] { id.toString() } );
		} finally {
			db.close();
		}
	}

	/**
	 * Finds some data from the specified table, given a query and the parameters
	 */
	public <T extends Serializable> T find(TableHelper<T> table, String query, String[] values) {
		SQLiteDatabase db = this.getWritableDatabase();
		try {
			Cursor c = db.rawQuery(query, values);
		    if (c != null) {
		        c.moveToFirst();
		        return table.readFromCursor(c);
		    } else {
		    	return null;
		    }
		} finally {
			db.close();
		}		
	}

	// initialises the data for all the tables in the database
	private <T extends Serializable> void initialise(TableHelper<T> table) {
		List<ContentValues> initialData = table.getInitialData();
		for (ContentValues values : initialData) {
			addContent(table, values);
		}
	}
}

