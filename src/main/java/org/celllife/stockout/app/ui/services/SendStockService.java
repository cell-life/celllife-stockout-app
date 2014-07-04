package org.celllife.stockout.app.ui.services;

import org.celllife.stockout.app.integration.rest.framework.RestCommunicationException;
import org.celllife.stockout.app.manager.ManagerFactory;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * This handles the background sending of stocks. If an error occurs, then a notification is displayed
 * to the user to prompt them to debug the issue.
 */
public class SendStockService extends Service {
	
	//private static final int STOCK_NOTIFICATION_ID = 2;
	
    //private static Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    //private static long[] mVibratePattern = { 0, 200, 200, 300 };

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		
		ManagerFactory.initialise(this.getApplicationContext());

		try {
			ManagerFactory.getStockTakeManager().synch();
		} catch (RestCommunicationException e) {
		    // leave this off for now because it is generating a lot of alerts...
		    Log.e("SendStockService", "Communication error while trying to synch stock", e);

		    // See: http://stackoverflow.com/questions/12865337/repeating-notifications-on-android-4
		    // we should take note of the date/time when the last notification was sent, and 
		    // ensure we only send 1 notification a day (instead of a notification each time the
		    // synch fails....

		    /*
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
					.setSmallIcon(R.drawable.ic_alert_icon)
					.setAutoCancel(true).setContentTitle(contentTitle)
					.setContentText(contentText).setContentIntent(mainActivityPendingIntent)
					.setSound(soundUri).setVibrate(mVibratePattern);
			NotificationManager mNotificationManager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
			mNotificationManager.notify(STOCK_NOTIFICATION_ID, notificationBuilder.build());
			*/
		}
    	
    	return START_STICKY;
	}
}
