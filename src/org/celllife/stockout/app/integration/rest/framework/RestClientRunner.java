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

	public String doGet(String url) throws RestAuthenticationException, RestCommunicationException {
		startProgress();
		Log.d("RestClientRunner", "Starting REST call to "+url);
		RestClientGetAsyncTask task = new RestClientGetAsyncTask();
		task.execute(new String[] { url });
		String response = null;
		try {
			RestResponse restResponse = task.get();
			if (restResponse.getErrorMessage() != null) {
				if (restResponse.getCode() == 401) {
					throw new RestAuthenticationException("Invalid username or password.");
				} else {
					throw new RestCommunicationException("Error while communicating with the server " + url 
					+ ". Error: " + restResponse.getErrorMessage());
				}
			}
			Log.d("RestClientRunner", "Finishing REST call with response: "+restResponse);
			response = restResponse.getData();
		} catch (InterruptedException e) {
			throw new RestCommunicationException("Error while communicating with the server " + url 
					+ ". Error: " + e.getMessage(), e);
		} catch (ExecutionException e) {
			throw new RestCommunicationException("Error while communicating with the server " + url 
					+ ". Error: " + e.getMessage(), e);
		} finally {
			endProgress();
		}
		return response;
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
				response = new RestResponse(500);
				response.setErrorMessage("Invalid url '"+urls[0]+"'");
			}
			return response;
		}
	}
}
