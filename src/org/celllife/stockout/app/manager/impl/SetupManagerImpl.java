package org.celllife.stockout.app.manager.impl;

import java.util.List;

import org.celllife.stockout.app.database.PhoneTableAdapter;
import org.celllife.stockout.app.domain.Drug;
import org.celllife.stockout.app.domain.Phone;
import org.celllife.stockout.app.integration.rest.GetUserMethod;
import org.celllife.stockout.app.manager.DatabaseManager;
import org.celllife.stockout.app.manager.ManagerFactory;
import org.celllife.stockout.app.manager.SetupManager;

import android.util.Log;


public class SetupManagerImpl implements SetupManager {

    @Override
    public Phone save(Phone phone) {
        PhoneTableAdapter phoneDb = DatabaseManager.getPhoneTableAdapter();
        phoneDb.insert(phone);

        Phone savedPhone = phoneDb.findByMsisdn(phone.getMsisdn());

        return savedPhone;
    }

    @Override
    public Phone initialise(String msisdn, String password){
		Phone newPhone = GetUserMethod.getUserDetails(msisdn, password);
		Phone savedPhone = null;
		if (newPhone != null) {
			savedPhone = save(newPhone);
		}
		ManagerFactory.getSessionManager().authenticated(msisdn, password);
		return savedPhone;
    }

    @Override
    public List<Drug> getDrugList() {
        return DatabaseManager.getDrugTableAdapter().findAll();
    }

    @Override
    public boolean isInitialised() {
        PhoneTableAdapter phoneDb = DatabaseManager.getPhoneTableAdapter();

        Phone phone = phoneDb.findOne();
        boolean initialised = false;

        if (phone != null) {
            initialised = true;
        }
        Log.d("SetupManager", "Phone =" + phone + " Initialised =" + initialised );
        return initialised;
    }

	@Override
	public Phone setSafetyLevelSettings(Integer leadTime, Integer safetyTime) {
        String msisdn = ManagerFactory.getSessionManager().getUsername();

        PhoneTableAdapter phoneDb = DatabaseManager.getPhoneTableAdapter();
        Phone p = phoneDb.findByMsisdn(msisdn);
        p.setDrugLeadTime(leadTime);
        p.setDrugSafetyLevel(safetyTime);
        phoneDb.insertOrUpdate(p);
        
        return p;
	}
}