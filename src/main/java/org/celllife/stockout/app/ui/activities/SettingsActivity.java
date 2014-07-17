package org.celllife.stockout.app.ui.activities;

import java.net.MalformedURLException;
import java.util.Date;

import org.celllife.stockout.app.R;
import org.celllife.stockout.app.integration.rest.framework.RestAuthenticationException;
import org.celllife.stockout.app.integration.rest.framework.RestCommunicationException;
import org.celllife.stockout.app.manager.ManagerFactory;
import org.celllife.stockout.app.manager.SettingManager;
import org.celllife.stockout.app.ui.alarm.AlarmNotificationReceiver;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;
import android.util.Log;

/**
 * Activity that manages the preferences/app settings.
 */
public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // setting defaults
        int offlineDays = ManagerFactory.getSettingManager().getOfflineDays();
        ManagerFactory.getSettingManager().setOfflineDays(offlineDays);
        Log.d("SettingsActivity","Setting offlineDays to "+offlineDays);
        int autoSyncPeriod = ManagerFactory.getSettingManager().getAutoSyncMinutes();
        ManagerFactory.getSettingManager().setAutoSyncMinutes(autoSyncPeriod);
        Log.d("SettingsActivity","Setting autoSyncPeriod to "+autoSyncPeriod);

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
            this.getPreferenceManager().setSharedPreferencesName(SettingManager.APP_PREFERENCES_KEY);
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
                        activity.displayErrorMessage(activity.getApplicationContext().getString(
                                R.string.server_preferences_url_error_message_invalid));
                        return false;
                    } catch (RestAuthenticationException e) {
                        String errorMessage = activity.getApplicationContext().getString(
                                R.string.server_preferences_url_error_message_authentication)
                                + " Error: " + e.getResponse().getCode();
                        activity.displayErrorMessage(errorMessage);
                        return false;
                    } catch (RestCommunicationException e) {
                        String errorMessage = activity.getApplicationContext().getString(
                                R.string.server_preferences_url_error_message_connection)
                                + " Error: " + e.getResponse().getCode();
                        activity.displayErrorMessage(errorMessage);
                        return false;
                    }
                    return true; // and save the newValue
                }
            });

            // ensure that offline days is a number and not null
            EditTextPreference offlineDaysPref = (EditTextPreference) getPreferenceScreen().findPreference(
                    SettingManager.OFFLINE_DAYS);
            offlineDaysPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    boolean invalid = false;
                    if (newValue != null && newValue instanceof String) {
                        String offlineDays = (String) newValue;
                        if (offlineDays.trim().equals("")) {
                            invalid = true;
                        } else {
                            try {
                                Integer.parseInt(offlineDays);
                            } catch (NumberFormatException e) {
                                invalid = true;
                            }
                        }
                        
                    } else {
                        invalid = true;
                    }
                    if (invalid) {
                        activity.displayErrorMessage(activity.getApplicationContext().getString(R.string.offline_days_preferences_error));
                        return false;
                    } else {
                        return true; // and save the newValue
                    }
                }
            });
            
            // Reset the Background synch alarm
            ListPreference autoSyncPref = (ListPreference) getPreferenceScreen().findPreference(
                    SettingManager.AUTO_SYNC_MINUTES);
            autoSyncPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    Context context = PrefsFragment.this.getActivity().getApplicationContext();
                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    Intent alertIntent = new Intent(context, AlarmNotificationReceiver.class);
                    PendingIntent alertAlarmPendingIntent = PendingIntent.getBroadcast(context, 1, alertIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    int mins = Integer.parseInt((String)newValue); // assuming since it's a list, it has to be a valid number
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, new Date().getTime(), mins*60*1000, alertAlarmPendingIntent);
                    Log.i("SettingsActivity","Reset the Alert Alarm to be triggered every "+mins+" minutes.");
                    return true; // not validating, so always accept newValue by returning true
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
