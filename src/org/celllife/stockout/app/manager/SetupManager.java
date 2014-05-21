package org.celllife.stockout.app.manager;

import org.celllife.stockout.app.domain.Drug;
import org.celllife.stockout.app.domain.Phone;

import java.util.List;

/**
 * Manager for the setup wizard. This provides functionality used for creating your account
 * and changing your settings.
 */
public interface SetupManager {

	/**
	 * Determines if the app has been initialised.
	 * @return true if the app is initialised.
	 */
    boolean isInitialised();

    /**
     * Saves the phone entity in the app database
     * @param phone Phone to save
     * @return Phone saved entity
     */
    Phone save(Phone phone);

    /**
     * Retrieves the entire drug list which is used to prompt the user during setup
     * @return List of Drugs
     */
    List<Drug> getDrugList();

    /**
     * Creates the Phone entity in the app database
     * @param msisdn
     * @param password
     * @return Phone, updated entity
     */
    Phone initialise(String msisdn, String password);
    
    /**
     * Sets the app ADC related settings 
     * @param leadTime Integer the time it takes to deliver drugs (in days)
     * @param safetyTime Integer the number of days stock you would want to have in stock before re-ordering
     * @return Phone, updated entity
     */
    Phone setSafetyLevelSettings(Integer leadTime, Integer safetyTime);
}