package org.celllife.stockout.app.test;

import java.net.MalformedURLException;
import java.net.URL;

import junit.framework.Assert;

import org.celllife.stockout.app.integration.rest.framework.RestClient;
import org.celllife.stockout.app.integration.rest.framework.RestClientImpl;
import org.celllife.stockout.app.integration.rest.framework.RestResponse;

import android.test.AndroidTestCase;

public class RestClientTest extends AndroidTestCase {

	public void setUp() {
	}

	public void testGetCall() throws MalformedURLException {
		RestClient restClient = new RestClientImpl("technical@cell-life.org", "swimqueengrowflow");

		URL url = new URL(
				"http://sol.cell-life.org/clinicservice/service/locateNearestClinic?longitude=-33.933782&latitude=18.417606");
		RestResponse response = restClient.doGet(url);

		Assert.assertNotNull(response);
		Assert.assertNotNull(response.getCode());
		Assert.assertEquals(200, response.getCode());
		Assert.assertNotNull(response.getData());
		Assert.assertEquals(
				"{\"id\":6882,\"name\":\"wc Robbie Nurock CDC\",\"code\":\"6878\",\"shortName\":\"R Nurock CDC\"" +
				",\"phoneNumber\":null,\"address\":\"89 Buitenkant Street, Cape Town 8000, South Africa\"" +
				",\"coordinates\":\"[18.42083,-33.93066]\"," +
				"\"subDistrictName\":\"CT Western SD\",\"districtName\":\"Cape Town MM\",\"provinceName\":\"Western Cape\"}",
				response.getData());
	}

	public void testGetCallStockOut() throws MalformedURLException {
		RestClient restClient = new RestClientImpl("27768198075", "1234");

		URL url = new URL(
				"http://sol.cell-life.org/stock/service/users?msisdn=27768198075");
		RestResponse response = restClient.doGet(url);

		Assert.assertNotNull(response);
		Assert.assertNotNull(response.getCode());
		Assert.assertEquals(200, response.getCode());
		Assert.assertNotNull(response.getData());
		Assert.assertEquals(
				"{\"id\":1,\"msisdn\":\"27768198075\",\"encryptedPassword\":\"a98f2b1c8e0618ffd0d764f8369015e45b71a1f129aa66881b4f25998807051a\"" +
				",\"password\":null,\"salt\":\"9b55660a88d00a61ac77937193acf6055e12fd02d1f187be57dcfe72bfffa1a6\"" +
				",\"clinicCode\":\"0000\",\"clinicName\":\"Demo Clinic 1\"}",
				response.getData());
	}

	/*public void testPost() throws MalformedURLException {
		RestClient restClient = new RestClientImpl("technical@cell-life.org", "swimqueengrowflow");

		URL url = new URL(
				"https://www.cell-life.org/clinicservice/service/locateNearestClinic?longitude=-33.933782&latitude=18.417606");
		RestResponse response = restClient.doPost(url, "");

		Assert.assertNotNull(response);
		Assert.assertNotNull(response.getCode());
		Assert.assertEquals(200, response.getCode());
		Assert.assertNotNull(response.getData());
		Assert.assertEquals(
				"{\"id\":6882,\"name\":\"wc Robbie Nurock CDC\",\"code\":\"6878\",\"shortName\":\"R Nurock CDC\"" +
				",\"phoneNumber\":null,\"address\":\"89 Buitenkant Street, Cape Town 8000, South Africa\"" +
				",\"coordinates\":\"[18.4208,-33.9307]\"," +
				"\"subDistrictName\":\"CT Western SD\",\"districtName\":\"Cape Town MM\",\"provinceName\":\"Western Cape\"}",
				response.getData());
	}*/

	public void testGet404() throws MalformedURLException {
		RestClient restClient = new RestClientImpl();
		URL url = new URL("http://sol.cell-life.org/test404");
		RestResponse response = restClient.doGet(url);

		Assert.assertNotNull(response);
		Assert.assertNotNull(response.getCode());
		Assert.assertEquals(404, response.getCode());
		Assert.assertNotNull(response.getErrorMessage());
		System.out.println("errorMessage="+response.getErrorMessage());
		Assert.assertEquals("Not Found", response.getErrorMessage());
	}

	public void tearDown() throws Exception {
		super.tearDown();
	}
}