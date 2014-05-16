package org.celllife.stockout.app.ui.services;

import org.celllife.stockout.app.R;
import org.celllife.stockout.app.integration.rest.framework.RestCommunicationException;
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
 * This handles the background sending of stocks. If an error occurs, then a notification is displayed
 * to the user to prompt them to debug the issue.
 */
public class SendStockService extends Service {
	
	private static final int STOCK_NOTIFICATION_ID = 2;
	
    private static Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    private static long[] mVibratePattern = { 0, 200, 200, 300 };

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);

		try {
			//ManagerFactory.getStockTakeManager().synch();
		} catch (RestCommunicationException e) {
			// notify the user that the stock synch was not successful
			Context context = this.getApplicationContext();
			Intent mainActivityIntent = new Intent(context, MainActivity.class);
        	PendingIntent mainActivityPendingIntent = PendingIntent.getActivity(context, 0,
    				mainActivityIntent, Intent.FLAG_ACTIVITY_NEW_TASK);
        	
        	// get the notification information to be displayed
			String tickerText = context.getString(R.string.stockNotificationTicker);
			String contentTitle = context.getString(R.string.stockNotificationTitle);
			String contentText = context.getString(R.string.stockNotificationText) + e.getMessage();
			if (e.getCause() != null) {
				contentText = contentText + ". " + e.getCause().getMessage();
			}
			
			// display the notifications
			Notification.Builder notificationBuilder = new Notification.Builder(
					context).setTicker(tickerText)
					.setSmallIcon(android.R.drawable.stat_sys_warning)
					.setAutoCancel(true).setContentTitle(contentTitle)
					.setContentText(contentText).setContentIntent(mainActivityPendingIntent)
					.setSound(soundUri).setVibrate(mVibratePattern);
			NotificationManager mNotificationManager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
			mNotificationManager.notify(STOCK_NOTIFICATION_ID, notificationBuilder.build());
		}
    	
    	return START_STICKY;
	}
}
