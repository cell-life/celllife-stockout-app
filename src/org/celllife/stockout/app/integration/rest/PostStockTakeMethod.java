package org.celllife.stockout.app.integration.rest;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.celllife.stockout.app.domain.StockTake;
import org.celllife.stockout.app.integration.rest.framework.RestAuthenticationException;
import org.celllife.stockout.app.integration.rest.framework.RestClientRunner;
import org.celllife.stockout.app.integration.rest.framework.RestCommunicationException;
import org.celllife.stockout.app.integration.rest.framework.RestResponse;
import org.celllife.stockout.app.manager.ManagerFactory;
import org.celllife.stockout.app.manager.SessionManager;
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
		SessionManager sessionMgr = ManagerFactory.getSessionManager();
		String username = sessionMgr.getUsername();
		String password = sessionMgr.getPassword();
		RestClientRunner restClientRunner = new RestClientRunner(username, password);
		String url = ManagerFactory.getSettingManager().getServerBaseUrl() + "service/stocks/stocktake";

		// build up json content
		Map<String, Object> stockTakeMap = new HashMap<String, Object>();
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
		stockTakeMap.put("date", new SimpleDateFormat("yyyy-MM-dd").format(stockTake.getDate()));
		stockTakeMap.put("quantity", String.valueOf(stockTake.getQuantity()));
		Map<String, Object> userMap = new HashMap<String, Object>();
		userMap.put("msisdn", username);
		stockTakeMap.put("user", userMap);
		Map<String, Object> drugMap = new HashMap<String, Object>();
		drugMap.put("barcode", stockTake.getDrug().getBarcode());
		stockTakeMap.put("drug", drugMap);
		JSONObject jsonObject = new JSONObject(stockTakeMap);

		// check response and return
		RestResponse response = restClientRunner.doPost(url, jsonObject.toString());
		if (response.getCode() == 201) {
			return true;
		}
		return false;
	}
}
