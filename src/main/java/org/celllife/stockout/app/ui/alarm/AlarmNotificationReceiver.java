package org.celllife.stockout.app.ui.alarm;

import org.celllife.stockout.app.ui.services.SendStockService;
import org.celllife.stockout.app.ui.services.UpdateAlertService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


/**
 * The StockApp Alarm - this is scheduled in the MainActivity. When it runs, it gets the latest
 * alerts from the server, sends the outstanding stocks and then displays a notification in 
 * the notification tray to inform the user if they need to scan some drugs.
 */
public class AlarmNotificationReceiver extends BroadcastReceiver {

    @Override
	public void onReceive(Context context, Intent intent) {   
        
        Log.i("AlarmNotificationReceiver","Received Alarm for synch.");
        
    	// Get the alerts from the server
    	Intent alertService = new Intent(context, UpdateAlertService.class);
        context.startService(alertService);

        // Send the unsent stocks to the server
        Intent stockService = new Intent(context, SendStockService.class);
        context.startService(stockService);
    }
}