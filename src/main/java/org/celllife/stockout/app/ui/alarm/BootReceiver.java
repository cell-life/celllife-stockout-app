package org.celllife.stockout.app.ui.alarm;

import java.util.Date;

import org.celllife.stockout.app.manager.ManagerFactory;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            
            ManagerFactory.initialise(context);
            
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            
            Log.d("BootReceiver","Start Alert Alarm");
            Intent alertIntent = new Intent(context, AlarmNotificationReceiver.class);
            PendingIntent alertAlarmPendingIntent = PendingIntent.getBroadcast(context, 1, alertIntent, 
                    PendingIntent.FLAG_UPDATE_CURRENT);
            int mins = ManagerFactory.getSettingManager().getAutoSyncMinutes();
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, new Date().getTime(), mins*60*1000, alertAlarmPendingIntent);
            
            Log.d("BootReceiver","Start Offline Alarm");
            Intent offlineAlertIntent = new Intent(context, OfflineNotificationReceiver.class);
            PendingIntent offlineAlarmPendingIntent = PendingIntent.getBroadcast(context, 1, offlineAlertIntent, 
                    PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, new Date().getTime(), 
                    AlarmManager.INTERVAL_DAY, offlineAlarmPendingIntent);
        }

    }

}
