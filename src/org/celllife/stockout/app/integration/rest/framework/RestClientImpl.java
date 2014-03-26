package org.celllife.stockout.app.integration.rest.framework;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Base64;
import android.util.Log;

public class RestClientImpl implements RestClient {

	String username;
	String password;

	/**
	 * RestClient without authentication
	 */
	public RestClientImpl() {
		
	}

	/**
	 * RestClient with authentication
	 */
	public RestClientImpl(String username, String password) {
		this.username = username;
		this.password = password;
	}

	@Override
	public RestResponse doPost(URL url, String content) {
		RestResponse response = null;
		HttpURLConnection conn = null;
		try {

			// connect
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);

			// send content, if it's available
			if (content != null && !content.trim().equals("")) {
				conn.setDoInput(true);
				OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
				wr.write(content);
				wr.flush();
			}

			doAuthentication(url, conn);

			// read response
			response = new RestResponse(conn.getResponseCode());
			if (response.isErrorCode()) {
				String errorMessage = getErrorMessageFromStream(conn);
				response.setErrorMessage(errorMessage);
			} else {
				String jsonResponse = getResponseDataFromStream(conn);
				response.setData(jsonResponse);
			}

		} catch (IOException e) {
			Log.e("RestClient", "Error while trying to connect to " + url.toString(), e);
			response = new RestResponse(500);
			response.setErrorMessage(e.getMessage());
		}

		return response;
	}

	@Override
	public RestResponse doGet(URL url) {
		RestResponse response = null;
		HttpURLConnection conn;
		try {
			// connect
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			doAuthentication(url, conn);
			// conn.connect();

			// read response
			response = new RestResponse(conn.getResponseCode());
			if (response.isErrorCode()) {
				String errorMessage = getErrorMessageFromStream(conn);
				response.setErrorMessage(errorMessage);
			} else {
				String jsonResponse = getResponseDataFromStream(conn);
				response.setData(jsonResponse);
			}

		} catch (IOException e) {
			Log.e("RestClient", "Error while trying to connect to " + url.toString(), e);
			response = new RestResponse(500);
			response.setErrorMessage(e.getMessage());
		}
		return response;
	}

	private void doAuthentication(URL url, HttpURLConnection conn) {
		if (username != null && !username.trim().equals("")) {
			String userpass = username + ":" + password;
			String basicAuth = "Basic " + Base64.encodeToString(userpass.getBytes(), Base64.DEFAULT);
			conn.setRequestProperty("Authorization", basicAuth);
		}
	}

	private String getResponseDataFromStream(HttpURLConnection conn) throws IOException {
		String jsonResponse = "";
		InputStream is = conn.getInputStream();
		jsonResponse = readFromInputStream(is);
		return jsonResponse;
	}

	private String getErrorMessageFromStream(HttpURLConnection conn) throws IOException {
		String errorResponse = "";
		InputStream is = conn.getErrorStream();
		errorResponse = readFromInputStream(is);
		return errorResponse;
	}

	private String readFromInputStream(InputStream is) throws IOException {
		String jsonResponse;
		try {
		    ByteArrayOutputStream baos = new ByteArrayOutputStream();
		    byte[] buffer = new byte[1024];
		    int length = 0;
		    while ((length = is.read(buffer)) != -1) {
		        baos.write(buffer, 0, length);
		    }
			jsonResponse = new String(baos.toByteArray());
		} finally {
			try {
				is.close();
			} catch (IOException e) {
			}
		}
		return jsonResponse;
	}
}