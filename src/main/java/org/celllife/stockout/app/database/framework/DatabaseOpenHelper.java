package org.celllife.stockout.app.database.framework;

import java.io.Serializable;
import java.util.ArrayList;
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
	
	private SQLiteDatabase database;

	
	public DatabaseOpenHelper(Context context, List<TableHelper<?>> tables) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.tables = tables;
		for (TableHelper<?> table : tables) {
			table.setDatabaseOpenHelper(this);
		}
		open();
	}
	
	public void open() {
		if (database == null) {
			database = this.getWritableDatabase();
		}
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		for (TableHelper<?> table : tables) {
			db.execSQL(table.getCreateTableSql());
	        initialise(db, table);
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
	        initialise(db, table);
		}
	}
	
	
	/**
	 * Inserts the specified data (row), for the specified table, into the specified database
	 */
	public <T extends Serializable> void addContent(SQLiteDatabase db, TableHelper<T> table, ContentValues values) {
		try {
			db.insert(table.getTableName(), null, values);
		} finally {
			//db.close();
		}
	}

	/**
	 * Inserts the specified data (row), for the specified table, into the known database (if it is available)
	 */
	public <T extends Serializable> void addContent(TableHelper<T> table, ContentValues values) {
		if (database != null) {
			addContent(database, table, values);
		}
	}

	/**
	 * Deletes the specified content from the specified table
	 */
	public <T extends Serializable> void deleteContent(SQLiteDatabase db, TableHelper<T> table, Long id) {
		db.beginTransaction();
	    try {
			db.delete(table.getTableName(), "id = ?", new String[] { id.toString() } );
			db.setTransactionSuccessful();
		} finally {
			//db.close();
			db.endTransaction();
		}
	}
	
	/**
	 * Deletes everything in the table. Please note that this should only be used in a test situation!
	 */
	public <T extends Serializable> void deleteAll(TableHelper<T> table) {
	    if (database != null) {
	        database.delete(table.getTableName(), "", new String[] {});
	    }
	}

	/**
	 * Deletes the specified content from the specified table, into the known database (if it is available)
	 */
	public <T extends Serializable> void deleteContent(TableHelper<T> table, Long id) {
		if (database != null) {
			deleteContent(database, table, id);
		}
	}

	/**
	 * Deletes the specified content from the specified table
	 */
	public <T extends Serializable> void updateContent(SQLiteDatabase db, TableHelper<T> table, Long id, ContentValues values) {
		db.beginTransaction();
		try {
			db.update(table.getTableName(), values, "id = ?", new String[] { id.toString() } );
			db.setTransactionSuccessful();
		} finally {
			//db.close();
			db.endTransaction();
		}
	}

	/**
	 * Deletes the specified content from the specified table, into the known database (if it is available)
	 */
	public <T extends Serializable> void updateContent(TableHelper<T> table, Long id, ContentValues values) {
		if (database != null) {
			updateContent(database, table, id, values);
		}
	}

	/**
	 * Finds some data from the specified table, given a query and the parameters
	 */
	public <T extends Serializable> T find(SQLiteDatabase db, TableHelper<T> table, String query, String[] values) {
	    Cursor c = null;
		try {
			c = db.rawQuery(query, values);
		    if (c != null) {
		        c.moveToFirst();
		        return table.readFromCursor(c);
		    } else {
		    	return null;
		    }
		} finally {
		    if (c != null) {
		        c.close();
		    }
		}		
	}

	/**
	 * Finds some data from the specified table, given a query and the parameters, into the known database (if it is available)
	 */
	public <T extends Serializable> T find(TableHelper<T> table, String query, String[] values) {
		if (database != null) {
			return find(database, table, query, values);
		}
		return null;
	}

	/**
	 * Finds some data from the specified table, given a query and the parameters
	 */
	public <T extends Serializable> List<T> findMany(SQLiteDatabase db, TableHelper<T> table, String query, String[] values) {
		List<T> entities = new ArrayList<T>();
		Cursor c = null;
		try {
			c = db.rawQuery(query, values);
		    if (c != null) {
		        if (c.moveToFirst()) {
			        do {
			        	T entity = table.readFromCursor(c);
			        	entities.add(entity);
			        } while (c.moveToNext());
		        }
		    }
		} finally {
		    if (c != null) {
                c.close();
            }
		}
		return entities;
	}

	/**
	 * Finds some data from the specified table, given a query and the parameters, into the known database (if it is available)
	 */
	public <T extends Serializable> List<T> findMany(TableHelper<T> table, String query, String[] values) {
		List<T> entities = new ArrayList<T>();
		if (database != null) {
			entities = findMany(database, table, query, values);
		}
		return entities;
	}

	@Override
    public String toString() {
        return "DatabaseOpenHelper [tables=" + tables + ", database=" + database + "]";
    }

    // initialises the data for all the tables in the database
	private <T extends Serializable> void initialise(SQLiteDatabase db, TableHelper<T> table) {
		List<ContentValues> initialData = table.getInitialData();
		for (ContentValues values : initialData) {
			addContent(db, table, values);
		}
	}
}

