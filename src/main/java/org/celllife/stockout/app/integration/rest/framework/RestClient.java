package org.celllife.stockout.app.integration.rest.framework;

import java.net.URL;

public interface RestClient {

	RestResponse doPost(URL url, String content);
	RestResponse doGet(URL url);

}
