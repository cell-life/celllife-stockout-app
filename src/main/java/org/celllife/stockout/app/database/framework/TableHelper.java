package org.celllife.stockout.app.database.framework;

import java.io.Serializable;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Defines the interface of the Table specific database code.
 *
 * A basic implementation is provided - see TableAdapter which you extend when you
 * wish to create a new table.
 *
 * @param <T> the Serializable entity backing the table
 */
public interface TableHelper<T extends Serializable> {

	String getCreateTableSql();
	String getTableName();

	List<ContentValues> getInitialData();
	T readFromCursor(Cursor cursor);

	void setDatabaseOpenHelper(DatabaseOpenHelper db);

	/** upgrade specific code for the transition from database version 1 to 2 */
	void upgrade1To2(SQLiteDatabase db);
	
	/** upgrade specific code for the transition from database version 2 to 3 */
	void upgrade2To3(SQLiteDatabase db);
}
