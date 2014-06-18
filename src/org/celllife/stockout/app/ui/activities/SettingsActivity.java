package org.celllife.stockout.app.ui.activities;

import java.net.MalformedURLException;

import org.celllife.stockout.app.R;
import org.celllife.stockout.app.integration.rest.framework.RestAuthenticationException;
import org.celllife.stockout.app.integration.rest.framework.RestCommunicationException;
import org.celllife.stockout.app.manager.ManagerFactory;
import org.celllife.stockout.app.manager.SettingManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;

public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        FragmentManager mFragmentManager = getFragmentManager();
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        PrefsFragment mPrefsFragment = new PrefsFragment();
        mFragmentTransaction.replace(android.R.id.content, mPrefsFragment);
        mFragmentTransaction.commit();
    }

    public static class PrefsFragment extends PreferenceFragment {

        private SettingsActivity activity;

        @Override
        public void onAttach(Activity activity) {
            if (activity instanceof SettingsActivity) {
                this.activity = (SettingsActivity) activity;
            }
            super.onAttach(activity);
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // load the existing preferences
            this.getPreferenceManager().setSharedPreferencesName(SettingManager.SERVER_PREFERENCES_KEY);
            // Load the preferences layout from an XML resource
            addPreferencesFromResource(R.xml.preference);

            // ensure that the URL is valid
            EditTextPreference urlPref = (EditTextPreference) getPreferenceScreen().findPreference(
                    SettingManager.BASE_URL);
            urlPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    try {
                        ManagerFactory.getSettingManager().testServerBaseUrl((String) newValue);
                    } catch (MalformedURLException e) {
                        activity.displayErrorMessage(activity.getApplicationContext()
                                .getString(R.string.server_preferences_url_error_message_invalid));
                        return false;
                    } catch (RestAuthenticationException e) {
                        String errorMessage = activity.getApplicationContext()
                                .getString(R.string.server_preferences_url_error_message_authentication)
                                + " Error: "+e.getResponse().getCode();
                        activity.displayErrorMessage(errorMessage);
                        return false;
                    } catch (RestCommunicationException e) {
                        String errorMessage = activity.getApplicationContext()
                                .getString(R.string.server_preferences_url_error_message_connection)
                                + " Error: "+e.getResponse().getCode();
                        activity.displayErrorMessage(errorMessage);
                        return false;
                    }
                    return true; // and save the newValue
                }
            });
        }
    }

    void displayErrorMessage(String message) {
        new AlertDialog.Builder(this).setMessage(message)
                .setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
    }
}
