package org.celllife.stockout.app.ui.services;

import org.celllife.stockout.app.R;
import org.celllife.stockout.app.manager.ManagerFactory;
import org.celllife.stockout.app.ui.activities.MainActivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;

/**
 * This handles the background task to check when the last successful server
 * communication was, and generate an alert if it is longer than the specified
 * period
 */
public class OfflineService extends Service {

    private static final int OFFLINE_NOTIFICATION_ID = 3;

    private static Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    private static long[] mVibratePattern = { 0, 200, 200, 300 };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        ManagerFactory.initialise(this.getApplicationContext());

        if (ManagerFactory.getServerCommunicationLogManager().displayOfflineNotification()) {

            // notify the user that the stock synch was not successful
            Context context = this.getApplicationContext();
            Intent mainActivityIntent = new Intent(context, MainActivity.class);
            PendingIntent mainActivityPendingIntent = PendingIntent.getActivity(context, 0, mainActivityIntent,
                    Intent.FLAG_ACTIVITY_NEW_TASK);

            // get the notification information to be displayed
            String tickerText = context.getString(R.string.offlineNotificationTicker);
            String contentTitle = context.getString(R.string.offlineNotificationTitle);
            String contentText = context.getString(R.string.offlinekNotificationText);

            // display the notifications
            Notification.Builder notificationBuilder = new Notification.Builder(context).setTicker(tickerText)
                    .setSmallIcon(R.drawable.ic_alert_icon).setAutoCancel(true).setContentTitle(contentTitle)
                    .setStyle(new Notification.BigTextStyle().bigText(contentText))
                    .setContentIntent(mainActivityPendingIntent).setSound(soundUri).setVibrate(mVibratePattern);
            NotificationManager mNotificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(OFFLINE_NOTIFICATION_ID, notificationBuilder.build());
        }

        return START_STICKY;
    }
}
