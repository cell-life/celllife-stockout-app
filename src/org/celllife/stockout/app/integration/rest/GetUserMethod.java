package org.celllife.stockout.app.integration.rest;

import org.celllife.stockout.app.domain.Phone;
import org.celllife.stockout.app.integration.rest.framework.RestAuthenticationException;
import org.celllife.stockout.app.integration.rest.framework.RestClientRunner;
import org.celllife.stockout.app.integration.rest.framework.RestCommunicationException;
import org.celllife.stockout.app.integration.rest.framework.RestResponse;
import org.celllife.stockout.app.manager.ManagerFactory;
import org.celllife.stockout.app.manager.SessionManager;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * Retrieves the user details from the server
 */
public class GetUserMethod {

	public static Phone getUserDetails(String username, String password) throws RestCommunicationException, RestAuthenticationException {
		RestClientRunner restClientRunner = new RestClientRunner(username, password);
		String url = ManagerFactory.getSettingManager().getServerBaseUrl() + "service/users?msisdn="+username;

		Phone userDetails = new Phone();
		userDetails.setMsisdn(username);
		RestResponse response = restClientRunner.doGet(url);
		if (response.getData() != null) {
			try {
				JSONObject user;
				user = new JSONObject(response.getData());
				userDetails.setEncryptedPassword(user.getString("encryptedPassword"));
				userDetails.setSalt(user.getString("salt"));
				userDetails.setClinicCode(user.getString("clinicCode"));
				userDetails.setClinicName(user.getString("clinicName"));
			} catch (JSONException e) {
				throw new RestCommunicationException("Error while reading the response from the server " + url 
						+ ". Error: " + e.getMessage(), e);
			}
		} else {
			Log.w("AuthenticationManager", "Did not get any response");
			throw new RestCommunicationException("Error because there was no response from the server on " + url);
		}
		return userDetails;		
	}

	public static Phone getUserDetails() throws RestCommunicationException, RestAuthenticationException {
		SessionManager sessionMgr = ManagerFactory.getSessionManager();
		String username = sessionMgr.getUsername();
		String password = sessionMgr.getPassword();
		return getUserDetails(username, password);
	}
}
