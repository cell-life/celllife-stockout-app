package org.celllife.stockout.app.integration.rest;

import org.celllife.stockout.app.domain.Phone;
import org.celllife.stockout.app.integration.rest.framework.RestAuthenticationException;
import org.celllife.stockout.app.integration.rest.framework.RestClientRunner;
import org.celllife.stockout.app.integration.rest.framework.RestCommunicationException;
import org.celllife.stockout.app.integration.rest.framework.RestResponse;
import org.celllife.stockout.app.manager.ManagerFactory;
import org.json.JSONException;
import org.json.JSONObject;

public class PostActivateClinicMethod {

    public static void activateClinic(Phone phone) throws RestCommunicationException, RestAuthenticationException {
        String username = phone.getMsisdn();
        String password = phone.getPassword();
        RestClientRunner restClientRunner = new RestClientRunner(username, password);

        String url = ManagerFactory.getSettingManager().getServerBaseUrl();
        if (!url.endsWith("/")) {
            url = url + "/";
        }
        url = url + "service/clinics/activate";

        // Expected JSON payload
        // {
        // "msisdn": "27768198075",
        // "safetyLevel": 3,
        // "leadTime": 14
        // }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("msisdn", phone.getMsisdn());
            jsonObject.put("safetyLevel", phone.getDrugSafetyLevel());
            jsonObject.put("leadTime", phone.getDrugLeadTime());
        } catch (JSONException e) {
            throw new RestCommunicationException(
                    "Could not activate clinic. Error while communicating with the server: " + e.getMessage(), e);
        }

        // check response and return
        RestResponse response = restClientRunner.doPost(url, jsonObject.toString());
        if (response.getCode() != 200) {
            throw new RestCommunicationException(
                    "Could not activate clinic. Error while communicating with the server: " + response.getCode() + " "
                            + response.getErrorMessage(), response);
        }
    }
}
