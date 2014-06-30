package org.celllife.stockout.app.ui.services;

import java.util.ArrayList;
import java.util.List;

import org.celllife.stockout.app.R;
import org.celllife.stockout.app.domain.Alert;
import org.celllife.stockout.app.manager.ManagerFactory;
import org.celllife.stockout.app.ui.activities.MainActivity;

import com.qs.samsungbadger.Badge;

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
		
    	// Generate alerts from the phone ADC calculation and generate alerts
    	// FIXME: allow this to be turned off in the settings
    	List<Alert> phoneAlerts = ManagerFactory.getAlertManager().generateAlerts();
		
		// Get the alerts from the server
    	List<Alert> serverAlerts = ManagerFactory.getAlertManager().updateAlerts();
    	
    	// make a list of the final new alerts
    	List<Alert> allAlerts = ManagerFactory.getAlertManager().getAlerts();
    	List<Alert> alerts = new ArrayList<Alert>();
    	for (Alert a : allAlerts) {
    		if (phoneAlerts.contains(a) || serverAlerts.contains(a)) {
    			alerts.add(a);
    		} // else it is an existing alert, so don't need to notify the user
    	}
    	
    	Context context = this.getApplicationContext();
    	if (alerts != null && alerts.size() > 0) {
    		int notificationCount = alerts.size();
    		// Define the Notification Intent
    		Intent mainActivityIntent = new Intent(context, MainActivity.class);
        	PendingIntent mainActivityPendingIntent = PendingIntent.getActivity(context, 0,
    		mainActivityIntent, Intent.FLAG_ACTIVITY_CLEAR_TASK);
        	
        	// Build the notification
			String tickerText = context.getString(R.string.alertNotificationTicker);
			String contentTitle = context.getString(R.string.alertNotificationTitle);
			String subTitle = context.getString(R.string.alertNotificationSubTitle);
			if (notificationCount > 1) {
			    subTitle = context.getString(R.string.alertNotificationSubTitlePlural);
			}
			StringBuilder contentText = new StringBuilder();
			for (Alert a : alerts) {
				if (contentText.length() != 0) {
					contentText.append("\n");
				}
				contentText.append(a.getDrug().getDescription());
			}
	
			Notification.Builder notificationBuilder = new Notification.Builder(context)
			.setTicker(tickerText)
			.setSmallIcon(R.drawable.ic_alert_icon)
			.setAutoCancel(true).setContentTitle(contentTitle)
			.setContentIntent(mainActivityPendingIntent)
			.setStyle(new Notification.BigTextStyle().bigText(contentText))
			.setSubText(subTitle)
			.setNumber(notificationCount)
			.setSound(soundUri).setVibrate(mVibratePattern);
		
	
			// Pass the Notification to the NotificationManager:
			NotificationManager mNotificationManager = (NotificationManager) context
			.getSystemService(Context.NOTIFICATION_SERVICE);
			mNotificationManager.notify(ALERT_NOTIFICATION_ID,
			notificationBuilder.build());
			getBadge(notificationCount);
	        Log.i("AlarmNotificationReceiver", "Created an alert notification "+tickerText+" "+contentTitle+" "+contentText);
    	} 
    	
    	return START_STICKY;
	}
	
	private void getBadge(int notificationCount) {
	    Context context = this.getApplicationContext();
	    if (Badge.isBadgingSupported(context)) {
	        Badge badge = new Badge();
	        badge.mPackage = context.getPackageName();
	        badge.mClass = "org.celllife.stockout.app.ui.activities.MainActivity"; // This should point to Activity declared as android.intent.action.MAIN
	        badge.mBadgeCount = notificationCount;
	        badge.save(context);
	    }
    }
}