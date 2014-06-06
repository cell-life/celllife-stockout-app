package org.celllife.stockout.app.integration.rest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.celllife.stockout.app.domain.Alert;
import org.celllife.stockout.app.domain.AlertStatus;
import org.celllife.stockout.app.domain.Drug;
import org.celllife.stockout.app.domain.Phone;
import org.celllife.stockout.app.integration.rest.framework.RestAuthenticationException;
import org.celllife.stockout.app.integration.rest.framework.RestClientRunner;
import org.celllife.stockout.app.integration.rest.framework.RestCommunicationException;
import org.celllife.stockout.app.integration.rest.framework.RestResponse;
import org.celllife.stockout.app.manager.ManagerFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * Retrieves the latest alert details from the server
 */
public class GetAlertMethod {

	public static List<Alert> getLatestAlerts() throws RestCommunicationException, RestAuthenticationException {
	    List<Alert> latestAlerts = new ArrayList<Alert>();

	    Phone phone = ManagerFactory.getAuthenticationManager().getPhone();
	    if (phone == null) {
	        return latestAlerts;
	    }
		String username = phone.getMsisdn();
		String password = phone.getPassword();
		RestClientRunner restClientRunner = new RestClientRunner(username, password);

		String url = ManagerFactory.getSettingManager().getServerBaseUrl();
		if (!url.endsWith("/")) {
		    url = url + "/"; 
		}
		url = url + "service/alerts?msisdn="+username;

		// FIXME: we need to confirm that we have received these alerts in order to properly mark them as sent
		RestResponse response = restClientRunner.doGet(url);
		if (response.getData() != null) {
			try {
				JSONArray alerts = new JSONArray(response.getData());
				for (int i=0, len=alerts.length(); i<len; i++) {
					JSONObject alertJSON = alerts.getJSONObject(i);
					Alert alert = new Alert();
					
					// this is the format of the response
//					[
//					  {
//					    "id": 2,
//					    "date": 1391731200000,
//					    "level": 1,
//					    "message": "This is an alert",
//					    "status": "NEW",
//					    "user": {
//					      "id": 1,
//					      "msisdn": "27768198075",
//					      "encryptedPassword": "a98f2b1c8e0618ffd0d764f8369015e45b71a1f129aa66881b4f25998807051a",
//					      "password": null,
//					      "salt": "9b55660a88d00a61ac77937193acf6055e12fd02d1f187be57dcfe72bfffa1a6",
//					      "clinicCode": "0000",
//					      "clinicName": "Demo Clinic 1"
//					    },
//					    "drug": {
//					      "id": 2,
//					      "barcode": "60011053",
//					      "description": "Panado 500mg 24 tablets"
//					    }
//					  }
//					]					
					
					alert.setDate(new Date(alertJSON.getLong("date")));
					alert.setLevel(alertJSON.getInt("level"));
					JSONObject drugJSON = alertJSON.getJSONObject("drug");
					Drug drug = new Drug();
					drug.setBarcode(drugJSON.getString("barcode"));
					alert.setDrug(drug);
					alert.setMessage(alertJSON.getString("message"));
					alert.setStatus(AlertStatus.valueOf(alertJSON.getString("status")));
					latestAlerts.add(alert);
				}
				
			} catch (JSONException e) {
				throw new RestCommunicationException("Error while reading the response from the server " + url 
						+ ". Error: " + e.getMessage(), e);
			}
		} else {
			Log.w("AuthenticationManager", "Did not get any response");
			throw new RestCommunicationException("Error because there was no response from the server on " + url);
		}
		return latestAlerts;
	}
}
