package org.celllife.stockout.app.integration.rest;

import java.text.SimpleDateFormat;

import org.celllife.stockout.app.domain.Phone;
import org.celllife.stockout.app.domain.StockTake;
import org.celllife.stockout.app.integration.rest.framework.RestAuthenticationException;
import org.celllife.stockout.app.integration.rest.framework.RestClientRunner;
import org.celllife.stockout.app.integration.rest.framework.RestCommunicationException;
import org.celllife.stockout.app.integration.rest.framework.RestResponse;
import org.celllife.stockout.app.manager.ManagerFactory;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;

@SuppressLint("SimpleDateFormat")
/**
 * Handles sending of StockTakes to the server
 */
public class PostStockTakeMethod {

	public static boolean submitStockTake(StockTake stockTake) throws RestCommunicationException,
			RestAuthenticationException {

		// setup method + RestClient
        Phone phone = ManagerFactory.getAuthenticationManager().getPhone();
        if (phone == null) {
            return false;
        }
        String username = phone.getMsisdn();
        String password = phone.getPassword();
		RestClientRunner restClientRunner = new RestClientRunner(username, password);

		String url = ManagerFactory.getSettingManager().getServerBaseUrl();
        if (!url.endsWith("/")) {
            url = url + "/"; 
        }
        url = url + "service/stocks/stocktake";

		// NOTE: expected JSON payload below
		//{
		//    "date": "2014-02-08",
		//    "quantity": "22",
		//    "user": {
		//        "msisdn": "277681980752"
		//    },
		//    "drug": {
		//        "barcode": "111111222222333333"
		//    }
		//}
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("date", new SimpleDateFormat("yyyy-MM-dd").format(stockTake.getDate()));
			jsonObject.put("quantity", String.valueOf(stockTake.getQuantity()));
			JSONObject userJsonObject = new JSONObject();
			userJsonObject.put("msisdn", username);
			jsonObject.put("user", userJsonObject);
			JSONObject drugJsonObject = new JSONObject();
			drugJsonObject.put("barcode", stockTake.getDrug().getBarcode());
			jsonObject.put("drug", drugJsonObject);
		} catch (JSONException e) {
			throw new RestCommunicationException("Error while communicating with the server " + url 
					+ ". Error: " + e.getMessage(), e);
		}

		// check response and return
		RestResponse response = restClientRunner.doPost(url, jsonObject.toString());
		if (response.getCode() == 201) {
			return true;
		}
		return false;
	}
}
