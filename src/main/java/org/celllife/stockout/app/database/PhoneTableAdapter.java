package org.celllife.stockout.app.database;

import java.util.ArrayList;
import java.util.List;

import org.celllife.stockout.app.database.framework.TableAdapter;
import org.celllife.stockout.app.domain.Phone;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * This is the Data Access Object (DAO) for the Phone table. All methods
 * required to save, update, or retrieve Users from the database 
 * should be defined here.
 */
public class PhoneTableAdapter extends TableAdapter<Phone> {
	
	// Phone Table Name
	private static final String TABLE_PHONE = "phone";
	
	// Phone Table Column Names
	private static final String ID = "id";
	private static final String MSISDN = "msisdn";
	private static final String PASSWORD = "password";
	private static final String CLINIC_CODE = "clinic_code";
	private static final String CLINIC_NAME = "clinic_name";
	private static final String DRUG_LEAD_TIME = "drug_lead_time";
	private static final String DRUG_SAFETY_LEVEL = "drug_safety_level";
	private static final String ACTIVATED = "activated";
		
	// Phone Queries
	private static final String QUERY_FINDBY_MSISDN = "SELECT  * FROM " + TABLE_PHONE + " WHERE " + MSISDN + " = ?";

	String CREATE_PHONE_TABLE = 
			"CREATE TABLE " + TABLE_PHONE +" ("
			+ ID + " INTEGER PRIMARY KEY, " 
			+ MSISDN + " TEXT, "
			+ PASSWORD + " TEXT, " 
			+ CLINIC_CODE + " TEXT, "
			+ CLINIC_NAME + " TEXT, "
			+ DRUG_LEAD_TIME + " INTEGER, "
			+ DRUG_SAFETY_LEVEL + " INTEGER, "
			+ ACTIVATED + " INTEGER )";

	
	public PhoneTableAdapter() {
	}

	@Override
	public String getCreateTableSql() {
		return CREATE_PHONE_TABLE;
	}

	@Override
	public String getTableName() {
		return TABLE_PHONE;
	}

	@Override
	public List<ContentValues> getInitialData() {
		List<ContentValues> initialData = new ArrayList<ContentValues>();
		return initialData;
	}
	
	@Override
	protected ContentValues createContentValues(Phone phone) {
		ContentValues values = new ContentValues();
		values.put(MSISDN, phone.getMsisdn());
		values.put(PASSWORD, phone.getPassword());
		values.put(CLINIC_CODE, phone.getClinicCode());
		values.put(CLINIC_NAME, phone.getClinicName());
		values.put(DRUG_LEAD_TIME, phone.getDrugLeadTime());
		values.put(DRUG_SAFETY_LEVEL, phone.getDrugSafetyLevel());
		values.put(ACTIVATED, (phone.isActivated()) ? 1 : 0);
		return values;
	}

	@Override
	public Phone readFromCursor(Cursor c) {
	    if (c.getCount() > 0) {
		    Phone p = new Phone();
		    p.setId(c.getLong(c.getColumnIndex(ID)));
		    p.setMsisdn((c.getString(c.getColumnIndex(MSISDN))));
		    p.setPassword(c.getString(c.getColumnIndex(PASSWORD)));
		    p.setClinicCode(c.getString(c.getColumnIndex(CLINIC_CODE)));
		    p.setClinicName(c.getString(c.getColumnIndex(CLINIC_NAME)));
		    p.setDrugLeadTime(c.getInt(c.getColumnIndex(DRUG_LEAD_TIME)));
		    p.setDrugSafetyLevel(c.getInt(c.getColumnIndex(DRUG_SAFETY_LEVEL)));
		    int activated = c.getInt(c.getColumnIndex(ACTIVATED));
		    p.setActivated((activated == 1) ? true : false);
		    return p;
	    }
	    return null;
	}

	// Add the table specific CRUD operations here

	public Phone findByMsisdn(String msisdn) {
		return db.find(this, QUERY_FINDBY_MSISDN, new String[] { msisdn });
	}

	public Phone findOne() {
		Phone theOne = null;
		List<Phone> allPhones = findAll();
		// there should only be one
		if (allPhones.size() > 0) {
			theOne = allPhones.get(0);
		}
		return theOne;
	}

	public boolean isActivated() {
	    Phone phone = findOne();
	    if (phone != null) {
	        return phone.isActivated();
	    }
	    return false;
	}
}
