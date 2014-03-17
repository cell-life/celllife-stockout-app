package org.celllife.stockout.app.database.framework;

import java.io.Serializable;
import java.util.List;

import android.content.ContentValues;

/**
 * Abstract class that defines the basic CRUD operations necessary to interact 
 * with the specific domain Entity (<T>).
 * 
 * Extend this for each table/domain entity. 
 */
public abstract class TableAdapter<T extends Serializable> implements TableHelper<T> {
	
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
}
