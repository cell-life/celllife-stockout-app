package org.celllife.stockout.app.manager.impl;

import java.util.Calendar;
import java.util.Date;

import org.celllife.stockout.app.database.ServerCommunicationLogTableAdapter;
import org.celllife.stockout.app.domain.ServerCommunicationLog;
import org.celllife.stockout.app.domain.ServerCommunicationType;
import org.celllife.stockout.app.manager.DatabaseManager;
import org.celllife.stockout.app.manager.ManagerFactory;
import org.celllife.stockout.app.manager.ServerCommunicationLogManager;

import android.util.Log;

public class ServerCommunicationLogManagerImpl implements ServerCommunicationLogManager {

    @Override
    public ServerCommunicationLog getLastSuccessfulServerInteraction() {
        ServerCommunicationLogTableAdapter logDb = DatabaseManager.getServerCommunicationLogTableAdapter();
        return logDb.findLastSucccess();
    }

    @Override
    public void createServerCommunicationLog(ServerCommunicationType type, boolean success) {
        ServerCommunicationLog log = new ServerCommunicationLog();
        log.setDate(new Date());
        log.setType(type);
        log.setSuccess(success);
        DatabaseManager.getServerCommunicationLogTableAdapter().insert(log);    
    }

    @Override
    public boolean displayOfflineNotification() {
        boolean displayNotification = true;
        ServerCommunicationLog log = getLastSuccessfulServerInteraction();
        if (log != null) {
            Calendar cal = Calendar.getInstance();
            int offlineDays = ManagerFactory.getSettingManager().getOfflineDays();
            cal.add(Calendar.DAY_OF_YEAR, -offlineDays);
            Date cutoffDate = cal.getTime();

            if (log.getDate().after(cutoffDate)) {
                displayNotification = false;
            } else {
                Log.e("OfflineService", "Last successful server communication was more than " + offlineDays
                        + " days ago. " + log);
            }
        } else {
            Log.e("OfflineService", "Last successful server communication was null");
        }
        return displayNotification;
    }
}