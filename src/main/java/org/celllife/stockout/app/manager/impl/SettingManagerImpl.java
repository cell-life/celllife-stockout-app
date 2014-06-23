package org.celllife.stockout.app.manager.impl;

import java.net.MalformedURLException;
import java.net.URL;

import org.celllife.stockout.app.domain.Phone;
import org.celllife.stockout.app.integration.rest.GetUserMethod;
import org.celllife.stockout.app.integration.rest.framework.RestAuthenticationException;
import org.celllife.stockout.app.integration.rest.framework.RestCommunicationException;
import org.celllife.stockout.app.manager.DatabaseManager;
import org.celllife.stockout.app.manager.SettingManager;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingManagerImpl implements SettingManager {

	private Context context;

	public SettingManagerImpl(Context context) {
		this.context = context;
	}

	@Override
	public String getServerBaseUrl() {
		SharedPreferences settings = context.getSharedPreferences(SERVER_PREFERENCES_KEY, Context.MODE_PRIVATE);
		String baseUrl = settings.getString(BASE_URL, "http://sol.cell-life.org/stock");
		return baseUrl;
	}

	@SuppressWarnings("unused")
	@Override
	public void setServerBaseUrl(String url) throws MalformedURLException {
		SharedPreferences settings = context.getSharedPreferences(SERVER_PREFERENCES_KEY, Context.MODE_PRIVATE);
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

    @Override
    public boolean testServerBaseUrl(String url) 
            throws MalformedURLException, RestAuthenticationException, RestCommunicationException {
        Phone phone = DatabaseManager.getPhoneTableAdapter().findOne();
        if (phone != null) {
            // test URL for basic formatting
            @SuppressWarnings("unused")
            URL testUrl = new URL(url);
            // test URL to see if it points to a Stock App server
            GetUserMethod.getUserDetails(url, phone.getMsisdn(), phone.getPassword());
            // if we get to this line then no exception has been thrown
            return true; 
        }
        return false; // not possible to test since there is no phone entity in the database (unlikely)
    }
}
