package org.celllife.stockout.app.ui.alarm;

import org.celllife.stockout.app.ui.services.OfflineService;
import org.celllife.stockout.app.ui.services.UpdateAlertService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")){
	    	// Get the alerts from the server
	    	Intent alertService = new Intent(context, UpdateAlertService.class);
	        context.startService(alertService);
	        Intent offlineService = new Intent(context, OfflineService.class);
	        context.startService(offlineService);
	    	}
		
	}

}
