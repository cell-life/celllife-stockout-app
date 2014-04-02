package org.celllife.stockout.app.ui.alarm;


import org.celllife.stockout.app.R;
import org.celllife.stockout.app.ui.alarm.AlarmNotificationReceiver;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;

import java.util.Calendar;


/**
 * Created by achmat on 2014/03/31.
 */
public class AlarmActivity extends Activity {

    private AlarmManager mAlarmManager;
    private Intent mNotificationReceiverIntent;
    private PendingIntent mNotificationReceiverPendingIntent;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        // Get the AlarmManager Service
        mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // Create PendingIntent to start the AlarmNotificationReceiver
        mNotificationReceiverIntent = new Intent(AlarmActivity.this,
                AlarmNotificationReceiver.class);
        mNotificationReceiverPendingIntent = PendingIntent.getBroadcast(
                AlarmActivity.this, 0, mNotificationReceiverIntent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 9);

        mAlarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY,
                mNotificationReceiverPendingIntent);

    }
}
