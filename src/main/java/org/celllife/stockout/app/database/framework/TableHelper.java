package org.celllife.stockout.app.database.framework;

import java.io.Serializable;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;

public interface TableHelper<T extends Serializable> {

	String getCreateTableSql();
	String getTableName();

	List<ContentValues> getInitialData();
	T readFromCursor(Cursor cursor);

	void setDatabaseOpenHelper(DatabaseOpenHelper db);

}
