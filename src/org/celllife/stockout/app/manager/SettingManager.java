package org.celllife.stockout.app.manager;

import java.net.MalformedURLException;

/**
 * Service to retrieve phone settings
 */
public interface SettingManager {

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
}
