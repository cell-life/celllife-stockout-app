package org.celllife.stockout.app.manager;

import java.net.MalformedURLException;

import org.celllife.stockout.app.integration.rest.framework.RestAuthenticationException;
import org.celllife.stockout.app.integration.rest.framework.RestCommunicationException;

/**
 * Service to retrieve phone settings
 */
public interface SettingManager {

    static final String APP_PREFERENCES_KEY = "app";
    static final String BASE_URL = "baseUrl";
    static final String OFFLINE_DAYS = "offlineDays";

	/**
	 * Retrieves the server baseUrl - the url used for communications
	 * @return String containing the server baseUrl (starting with http and ending with /)
	 */
	String getServerBaseUrl();

	/**
	 * Sets the server baseURL. Will perform basic checks on the URL, not whether there is a server available.
	 * @param url String url
	 * @throws MalformedURLException if the url is not formed correctly
	 */
	void setServerBaseUrl(String url) throws MalformedURLException;

	/**
	 * Checks if the specified URL is actually pointing at a valid Stock App server 
	 * @param url String URL to test
	 * @return true if the URL is valid
	 * @throws MalformedURLException if the URL is not formatted correctly (e.g. is missing http)
	 * @throws RestCommunicationException if a connection could not be made with the server
	 * @throws RestAuthenticationException if there was an authentication exception when communicating with the server
	 */
	boolean testServerBaseUrl(String url) throws MalformedURLException, RestAuthenticationException, RestCommunicationException;

	
	/**
	 * Sets the number of days the app is allowed to be offline
	 * @param days int, 0 to ignore 
	 */
	void setOfflineDays(int days);
	
	/**
	 * Get the number of days the app is allowed to be offline before a notification is created. 
	 * @return int, if 0 then the check never happens
	 */
	int getOfflineDays();
}
