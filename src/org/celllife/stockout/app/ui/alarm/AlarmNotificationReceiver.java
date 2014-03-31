package org.celllife.stockout.app.ui.alarm;

import java.text.DateFormat;
import java.util.Date;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.app.PendingIntent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import org.celllife.stockout.app.ui.activities.MainActivity;


/**
 * Created by achmat on 2014/03/31.
 */
public class AlarmNotificationReceiver extends BroadcastReceiver {
    private static final int MY_NOTIFICATION_ID = 1;
    private static final String TAG= "AlarmNotificaionReceiver";


    //Notification Text Elements
    private final CharSequence tickerText = "Stock Take Notification";
    private final CharSequence contentTitle = "Please Scan the Following Drugs";
    //To be changed
    private final CharSequence contentText = "Panado, Grandpa";

    //Notification Action Elements
    private Intent mNotificationIntent;
    private PendingIntent mContentIntent;

    //Notification Sound and Vibration
    private Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    private long[] mVibratePattern = { 0, 200, 200, 300 };

    @Override
	public void onReceive(Context context, Intent intent) {

		mNotificationIntent = new Intent(context, MainActivity.class);
		mContentIntent = PendingIntent.getActivity(context, 0,
				mNotificationIntent, Intent.FLAG_ACTIVITY_NEW_TASK);

		Notification.Builder notificationBuilder = new Notification.Builder(
				context).setTicker(tickerText)
				.setSmallIcon(android.R.drawable.stat_sys_warning)
				.setAutoCancel(true).setContentTitle(contentTitle)
				.setContentText(contentText).setContentIntent(mContentIntent)
				.setSound(soundUri).setVibrate(mVibratePattern);

		// Pass the Notification to the NotificationManager:
		NotificationManager mNotificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(MY_NOTIFICATION_ID,
				notificationBuilder.build());

        //To be Determined
        //Log.i(TAG, "Sending notification at:" + DateFormat.getDateTimeInstance().format(new Date()));
    }
}
