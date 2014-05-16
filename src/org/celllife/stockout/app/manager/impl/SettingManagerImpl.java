package org.celllife.stockout.app.manager.impl;

import java.net.MalformedURLException;
import java.net.URL;

import org.celllife.stockout.app.manager.SettingManager;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingManagerImpl implements SettingManager {

	private static final String SERVER_PREFERENCES_KEY = "server";
	private static final String BASE_URL = "baseUrl";

	private Context context;

	public SettingManagerImpl(Context context) {
		this.context = context;
	}

	@Override
	public String getServerBaseUrl() {
		SharedPreferences settings = context.getSharedPreferences(SERVER_PREFERENCES_KEY, 0);
		String baseUrl = settings.getString(BASE_URL, "http://www.sol.cell-life.org/stock");
		return baseUrl;
	}

	@SuppressWarnings("unused")
	@Override
	public void setServerBaseUrl(String url) throws MalformedURLException {
		SharedPreferences settings = context.getSharedPreferences(SERVER_PREFERENCES_KEY, 0);
		SharedPreferences.Editor editor = settings.edit();
		// test the URL for correctness
		URL testUrl = new URL(url);
		// append slash at the end for ease of use in the rest of the application
		if (!url.endsWith("/")) {
			url = url + "/";
		}
		editor.putString(BASE_URL, url);
		editor.commit();
	}
}
