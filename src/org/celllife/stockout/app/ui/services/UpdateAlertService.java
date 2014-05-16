package org.celllife.stockout.app.ui.services;

import java.util.List;

import org.celllife.stockout.app.R;
import org.celllife.stockout.app.domain.Alert;
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
import android.util.Log;
import android.widget.Toast;

/**
 * This handles the creation of the new alerts from the server
 */
public class UpdateAlertService extends Service {
	
	private static final int ALERT_NOTIFICATION_ID = 1;
	
    private static Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    private static long[] mVibratePattern = { 0, 200, 200, 300 };

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);

    	List<Alert> alerts = ManagerFactory.getAlertManager().updateAlerts();
    	Context context = this.getApplicationContext();
    	
    	if (alerts != null && alerts.size() > 0) {
    		Intent mainActivityIntent = new Intent(context, MainActivity.class);
        	PendingIntent mainActivityPendingIntent = PendingIntent.getActivity(context, 0,
    				mainActivityIntent, Intent.FLAG_ACTIVITY_NEW_TASK);
			String tickerText = context.getString(R.string.alertNotificationTicker);
			String contentTitle = context.getString(R.string.alertNotificationTitle);
			StringBuilder contentText = new StringBuilder();
			for (Alert a : alerts) {
				if (contentText.length() != 0) {
					contentText.append(", ");
				}
				contentText.append(a.getDrug().getDescription());
			}
	
			Notification.Builder notificationBuilder = new Notification.Builder(
					context).setTicker(tickerText)
					.setSmallIcon(android.R.drawable.stat_sys_warning)
					.setAutoCancel(true).setContentTitle(contentTitle)
					.setContentText(contentText).setContentIntent(mainActivityPendingIntent)
					.setSound(soundUri).setVibrate(mVibratePattern);
	
			// Pass the Notification to the NotificationManager:
			NotificationManager mNotificationManager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
			mNotificationManager.notify(ALERT_NOTIFICATION_ID,
					notificationBuilder.build());

			Toast.makeText(context, "Created an alert notification "+tickerText+" "+contentTitle+" "+contentText, Toast.LENGTH_LONG).show();
	        Log.i("AlarmNotificationReceiver", "Created an alert notification "+tickerText+" "+contentTitle+" "+contentText);
    	} else {
    		// this is just a test for now so we can see something
    		// FIXME: remove this
    		Intent mainActivityIntent = new Intent(context, MainActivity.class);
        	PendingIntent mainActivityPendingIntent = PendingIntent.getActivity(context, 0,
    				mainActivityIntent, Intent.FLAG_ACTIVITY_NEW_TASK);
			Notification.Builder notificationBuilder = new Notification.Builder(
					context).setTicker("testing")
					.setSmallIcon(android.R.drawable.stat_sys_warning)
					.setAutoCancel(true).setContentTitle("dagmar says")
					.setContentText("do this").setContentIntent(mainActivityPendingIntent)
					.setSound(soundUri).setVibrate(mVibratePattern);
	
			// Pass the Notification to the NotificationManager:
			NotificationManager mNotificationManager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
			mNotificationManager.notify(ALERT_NOTIFICATION_ID,
					notificationBuilder.build());
			
			Toast.makeText(context, "No new alerts", Toast.LENGTH_LONG).show();
    		Log.i("AlarmNotificationReceiver", "No new alerts");
    	}
    	
    	return START_STICKY;
	}
}
