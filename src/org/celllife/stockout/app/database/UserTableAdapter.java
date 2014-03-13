package org.celllife.stockout.app.database;

import java.util.ArrayList;
import java.util.List;

import org.celllife.stockout.app.database.framework.TableAdapter;
import org.celllife.stockout.app.domain.User;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * This is the Data Access Object (DAO) for the User table. All methods
 * required to save, update, or retrieve Users from the database 
 * should be defined here.
 */
public class UserTableAdapter extends TableAdapter<User> {
	
	// User Table Name
	private static final String TABLE_USER = "user";
	
	// User Table Column Names
	private static final String ID = "id";
	private static final String MSISDN = "msisdn";
	private static final String ENCRYPTED_PASSWORD = "encrypted_password";
	private static final String SALT = "salt";
	private static final String CLINIC_CODE = "clinic_code";
	private static final String CLINIC_NAME = "clinic_name";
		
	// User Queries
	private static final String QUERY_FINDBY_MSISDN = "SELECT  * FROM " + TABLE_USER + " WHERE " + MSISDN + " = ?";

	String CREATE_USER_TABLE = 
			"CREATE TABLE " + TABLE_USER +" ("
			+ ID + " INTEGER PRIMARY KEY, " 
			+ MSISDN + " TEXT, "
			+ ENCRYPTED_PASSWORD + " TEXT, " 
			+ SALT + " TEXT, "
			+ CLINIC_CODE + " TEXT, "
			+ CLINIC_NAME + " TEXT )";

	
	public UserTableAdapter() {
	}

	@Override
	public String getCreateTableSql() {
		return CREATE_USER_TABLE;
	}

	@Override
	public String getTableName() {
		return TABLE_USER;
	}

	@Override
	public List<ContentValues> getInitialData() {
		List<ContentValues> initialData = new ArrayList<ContentValues>();
		return initialData;
	}
	
	@Override
	protected ContentValues createContentValues(User user) {
		ContentValues values = new ContentValues();
		values.put(MSISDN, user.getMsisdn());
		values.put(ENCRYPTED_PASSWORD, user.getEncryptedPassword());
		values.put(SALT, user.getSalt());
		values.put(CLINIC_CODE, user.getClinicCode());
		values.put(CLINIC_NAME, user.getClinicName());
		return values;
	}

	@Override
	public User readFromCursor(Cursor c) {
	    if (c.getCount() > 0) {
		    User u = new User();
		    u.setId(c.getLong(c.getColumnIndex(ID)));
		    u.setMsisdn((c.getString(c.getColumnIndex(MSISDN))));
		    u.setEncryptedPassword(c.getString(c.getColumnIndex(ENCRYPTED_PASSWORD)));
		    u.setSalt(c.getString(c.getColumnIndex(SALT)));
		    u.setClinicCode(c.getString(c.getColumnIndex(CLINIC_CODE)));
		    u.setClinicName(c.getString(c.getColumnIndex(CLINIC_NAME)));
		    return u;
	    }
	    return null;
	}

	// Add the table specific CRUD operations here

	public User findByMsisdn(String msisdn) {
		return db.find(this, QUERY_FINDBY_MSISDN, new String[] { msisdn });
	}	
}
