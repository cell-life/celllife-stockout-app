package org.celllife.stockout.app.test;

import java.util.Calendar;
import java.util.Date;

import junit.framework.Assert;

import org.celllife.stockout.app.database.ServerCommunicationLogTableAdapter;
import org.celllife.stockout.app.domain.ServerCommunicationLog;
import org.celllife.stockout.app.domain.ServerCommunicationType;
import org.celllife.stockout.app.manager.DatabaseManager;
import org.celllife.stockout.app.manager.ManagerFactory;
import org.celllife.stockout.app.manager.ServerCommunicationLogManager;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

public class ServerCommunicationLogManagerTest extends AndroidTestCase {
    private ServerCommunicationLogTableAdapter serverCommunicationLogDb;
    private ServerCommunicationLogManager serverCommunicationLogManager;

    public void setUp() {
        RenamingDelegatingContext context = new RenamingDelegatingContext(getContext(), "test_");
        ManagerFactory.initialise(context);
        serverCommunicationLogDb = DatabaseManager.getServerCommunicationLogTableAdapter();
        serverCommunicationLogManager = ManagerFactory.getServerCommunicationLogManager(); 
    }

    public void testDisplayNotificationFalse() {
        serverCommunicationLogDb.deleteAll();
        ServerCommunicationLog n = new ServerCommunicationLog();
        n.setDate(new Date());
        n.setSuccess(true);
        n.setType(ServerCommunicationType.ALERT);
        serverCommunicationLogDb.insert(n);
        Assert.assertFalse(serverCommunicationLogManager.displayOfflineNotification());
    }

    public void testDisplayNotificationTrueNoNotifications() {
        serverCommunicationLogDb.deleteAll();
        Assert.assertTrue(serverCommunicationLogManager.displayOfflineNotification());
    }

    public void testDisplayNotificationTrue() {
        serverCommunicationLogDb.deleteAll();
        ServerCommunicationLog n = new ServerCommunicationLog();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_YEAR, -20);
        n.setDate(cal.getTime());
        n.setSuccess(true);
        n.setType(ServerCommunicationType.ALERT);
        serverCommunicationLogDb.insert(n);
        Assert.assertTrue(serverCommunicationLogManager.displayOfflineNotification());
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }
}