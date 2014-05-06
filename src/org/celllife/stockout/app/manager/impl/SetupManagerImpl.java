package org.celllife.stockout.app.manager.impl;

import android.util.Log;

import org.celllife.stockout.app.database.PhoneTableAdapter;
import org.celllife.stockout.app.domain.Drug;
import org.celllife.stockout.app.domain.Phone;
import org.celllife.stockout.app.integration.rest.GetUserMethod;
import org.celllife.stockout.app.integration.rest.framework.RestCommunicationException;
import org.celllife.stockout.app.manager.DatabaseManager;
import org.celllife.stockout.app.manager.ManagerFactory;
import org.celllife.stockout.app.manager.SetupManager;

import java.util.List;


public class SetupManagerImpl implements SetupManager {

    private String msisdn;
    private String password;


    @Override
    public Phone save(Phone phone) {
        PhoneTableAdapter phoneDb = DatabaseManager.getPhoneTableAdapter();
        phoneDb.insert(phone);

        Phone savedPhone = phoneDb.findByMsisdn(phone.getMsisdn());

        return savedPhone;
    }

    @Override
    public Phone initialise (String msisdn, String password){
        SetupManager setupManager = ManagerFactory.getSetupManager();
        Phone newPhone = GetUserMethod.getUserDetails(msisdn, password);
        Phone savedPhone = null;
        if (newPhone != null) {
            savedPhone = setupManager.save(newPhone);
         }
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

}
