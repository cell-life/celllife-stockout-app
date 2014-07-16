package org.celllife.stockout.app.ui.alarm;

import org.celllife.stockout.app.ui.services.OfflineService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * The StockApp Offline Alarm - this is scheduled in the MainActivity. When it
 * runs, it checks to see when the last successsful interaction with the server
 * was and if it was longer than desired, displays a notification in the
 * notification tray to inform the user that they need to ensure the app can
 * communicate with the server.
 */
public class OfflineNotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("OfflineNotificationReceiver","Received Alarm to check if the phone is offline.");
        Intent offlineService = new Intent(context, OfflineService.class);
        context.startService(offlineService);
    }
}