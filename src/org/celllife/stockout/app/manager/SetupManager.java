package org.celllife.stockout.app.manager;

import org.celllife.stockout.app.domain.Drug;
import org.celllife.stockout.app.domain.Phone;

import java.util.List;

public interface SetupManager {

    boolean isInitialised();

    Phone save(Phone phone);

    List<Drug> getDrugList();

    Phone initialise (String msisdn, String password);


}
