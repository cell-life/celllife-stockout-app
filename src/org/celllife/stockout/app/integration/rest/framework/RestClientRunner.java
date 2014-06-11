package org.celllife.stockout.app.integration.rest.framework;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import android.os.AsyncTask;
import android.util.Log;

public class RestClientRunner {

	//private ProgressDialog dialog;

	protected String username;
	protected String password;

	//public RestClientRunner(Context context, String username, String password) {
	public RestClientRunner(String username, String password) {
		//dialog = new ProgressDialog(context);
		this.username = username;
		this.password = password;
	}

	public RestResponse doGet(String url) throws RestAuthenticationException, RestCommunicationException {
		startProgress();
		Log.d("RestClientRunner", "Starting GET REST call to "+url);
		RestClientGetAsyncTask task = new RestClientGetAsyncTask();
		task.execute(new String[] { url });
		RestResponse restResponse = new RestResponse(503);
		try {
			restResponse = task.get();
			if (restResponse.getErrorMessage() != null) {
				if (restResponse.getCode() == 401) {
					throw new RestAuthenticationException("Invalid username or password.", restResponse);
				} else {
					throw new RestCommunicationException("Error while communicating with the server " + url 
					+ ". Error: " + restResponse.getErrorMessage(), restResponse);
				}
			}
			Log.d("RestClientRunner", "Finishing GET REST call with response: "+restResponse);
		} catch (InterruptedException e) {
			throw new RestCommunicationException("Error while communicating with the server " + url 
					+ ". Error: " + e.getMessage(), e);
		} catch (ExecutionException e) {
			throw new RestCommunicationException("Error while communicating with the server " + url 
					+ ". Error: " + e.getMessage(), e);
		} finally {
			endProgress();
		}
		return restResponse;
	}

	public RestResponse doPost(String url, String string) throws RestAuthenticationException, RestCommunicationException {
		startProgress();
		Log.d("RestClientRunner", "Starting POST REST call to "+url+" with data "+string);
		RestClientPostAsyncTask task = new RestClientPostAsyncTask();
		task.execute(new String[] { url, string });
		RestResponse restResponse = new RestResponse(503);
		try {
			restResponse = task.get();
			if (restResponse.getErrorMessage() != null) {
				if (restResponse.getCode() == 401) {
					throw new RestAuthenticationException("Invalid username or password.", restResponse);
				} else {
					throw new RestCommunicationException("Error while communicating with the server " + url 
					+ ". Error: " + restResponse.getCode(), restResponse);
				}
			}
			Log.d("RestClientRunner", "Finishing POST REST call with response: "+restResponse);
		} catch (InterruptedException e) {
			throw new RestCommunicationException("Error while communicating with the server " + url 
					+ ". Error: " + e.getMessage(), e);
		} catch (ExecutionException e) {
			throw new RestCommunicationException("Error while communicating with the server " + url 
					+ ". Error: " + e.getMessage(), e);
		} finally {
			endProgress();
		}
		return restResponse;
	}

	private void startProgress() {
		//this.dialog.setMessage("Progress start");
		//this.dialog.show();
	}

	private void endProgress() {
		//if (dialog.isShowing()) {
		//	dialog.dismiss();
		//}
	}

	class RestClientGetAsyncTask extends AsyncTask<String, Void, RestResponse> {
		protected RestResponse doInBackground(String... urls) {
			RestResponse response = null;
			try {
				URL url = new URL(urls[0]);
				RestClient client = new RestClientImpl(username, password);
				response = client.doGet(url);
			} catch (MalformedURLException e) {
				response = new RestResponse(404);
				response.setErrorMessage("Invalid url '"+urls[0]+"'");
			}
			return response;
		}
	}

	class RestClientPostAsyncTask extends AsyncTask<String, Void, RestResponse> {
		protected RestResponse doInBackground(String... params) {
			String url = params[0];
			String data = params[1];
			RestResponse response = null;
			try {
				RestClient client = new RestClientImpl(username, password);
				response = client.doPost(new URL(url), data);
			} catch (MalformedURLException e) {
				response = new RestResponse(404);
				response.setErrorMessage("Invalid url '"+url+"'");
			}
			return response;
		}
	}
}