package org.celllife.stockout.app.database.framework;

import java.util.List;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

/**
 * Abstract class that defines the basic CRUD operations necessary to interact 
 * with the specific domain Entity (<T>).
 * 
 * Extend this for each table/domain entity. 
 */
public abstract class TableAdapter<T extends Entity> implements TableHelper<T> {
	
	protected DatabaseOpenHelper db;

	public TableAdapter() {
		
	}

	@Override
	public void setDatabaseOpenHelper(DatabaseOpenHelper db) {
		this.db = db;
	}

	protected abstract ContentValues createContentValues(T table);

	/**
	 * Inserts the specified data into the database
	 */
	public void insert(T table){
		db.addContent(this, createContentValues(table));
	}

	/**
	 * Updates the specified data in the database
	 */
	public void update(Long id, T table){
		db.updateContent(this, id, createContentValues(table));
	}

	/**
	 * Checks the entity for a ID and then performs either an insert or an update
	 */
	public void insertOrUpdate(T table) {
		if (table.getId() == null || table.getId().equals(Long.valueOf(0))) {
			insert(table);
		} else {
			update(table.getId(), table);
		}
	}

	/**
	 * Retrieves the data from the database using the specified id
	 */
	public T findById(Long id) {
		String queryFindBy = "SELECT  * FROM " + getTableName() + " WHERE id = ?";
		return db.find(this, queryFindBy, new String[] { id.toString() });
	}

	/**
	 * Retrieves all the data from the database
	 */
	public List<T> findAll() {
		String queryFindBy = "SELECT  * FROM " + getTableName();
		return db.findMany(this, queryFindBy, null);
	}

	/**
	 * Deletes the specified data from the database using the specified id
	 */
	public void deleteById(Long id) {
		db.deleteContent(this, id);
	}

	/**
	 * Deletes all the content in the table. Warning (!!!) should only be used in a test situation
	 */
	public void deleteAll() {
	    db.deleteAll(this);
	}

    @Override
    public void upgrade1To2(SQLiteDatabase db) {
        // does nothing, please override in your table specific class to perform some magic when the database is upgraded

        // This is an example implementation for PhoneTableAdapter override of this method:
        //Log.i("PhoneTableAdapter","Adding activated column");
        //final String UPGRADE_ADD_ACTIVATED_COLUMN = "ALTER TABLE phone ADD activated INTEGER";
        //db.execSQL(UPGRADE_ADD_ACTIVATED_COLUMN);
        //Log.i("PhoneTableAdapter","Dropping activated column");
        //final String DROP_ACTIVATED_COLUMN = "ALTER TABLE phone DROP COLUMN activated";
        //db.execSQL(DROP_ACTIVATED_COLUMN);        
    }

    @Override
    public void upgrade2To3(SQLiteDatabase db) {
        // does nothing, please override in your table specific class to perform some magic when the database is upgraded
    }
}
