package org.celllife.stockout.app.test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.celllife.stockout.app.database.ServerCommunicationLogTableAdapter;
import org.celllife.stockout.app.database.framework.DatabaseOpenHelper;
import org.celllife.stockout.app.database.framework.TableHelper;
import org.celllife.stockout.app.domain.ServerCommunicationLog;
import org.celllife.stockout.app.domain.ServerCommunicationType;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

public class ServerCommunicationLogTableAdapterTest extends AndroidTestCase {
    private ServerCommunicationLogTableAdapter notificationDb;
    DatabaseOpenHelper db;

    public void setUp() {
        RenamingDelegatingContext context = new RenamingDelegatingContext(getContext(), "test_");
        List<TableHelper<?>> tables = new ArrayList<TableHelper<?>>();
        notificationDb = new ServerCommunicationLogTableAdapter();
        tables.add(notificationDb);
        db = new DatabaseOpenHelper(context, tables);
    }

    public void testAddEntrySuccessAndFind() {
        ServerCommunicationLog n = new ServerCommunicationLog();
        n.setDate(new Date());
        n.setSuccess(true);
        n.setType(ServerCommunicationType.ALERT);
        notificationDb.insert(n);
        ServerCommunicationLog latest = notificationDb.findLastSucccess();
        Assert.assertNotNull(latest);
        Assert.assertNotNull(latest.getId());
        Assert.assertEquals(n.getDate(), latest.getDate());
        Assert.assertEquals(n.getSuccess(), latest.getSuccess());
        Assert.assertEquals(n.getType(), latest.getType());
    }

    public void testAddMultipleEntrySuccessAndFind() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, -7);
        ServerCommunicationLog n1 = new ServerCommunicationLog();
        n1.setDate(new Date());
        n1.setSuccess(true);
        n1.setType(ServerCommunicationType.ALERT);
        notificationDb.insert(n1);
        ServerCommunicationLog n2 = new ServerCommunicationLog();
        n2.setDate(new Date());
        n2.setSuccess(true);
        n2.setType(ServerCommunicationType.ALERT);
        notificationDb.insert(n2);
        
        ServerCommunicationLog latest = notificationDb.findLastSucccess();
        Assert.assertNotNull(latest);
        Assert.assertNotNull(latest.getId());
        Assert.assertEquals(n2.getDate(), latest.getDate());
        Assert.assertEquals(n2.getSuccess(), latest.getSuccess());
        Assert.assertEquals(n2.getType(), latest.getType());
    }

    public void testAddEntryFailureAndNotFind() {
        ServerCommunicationLog n = new ServerCommunicationLog();
        n.setDate(new Date());
        n.setSuccess(false);
        n.setType(ServerCommunicationType.ALERT);
        notificationDb.insert(n);
        ServerCommunicationLog latest = notificationDb.findLastSucccess();
        Assert.assertNull(latest);
    }

    public void tearDown() throws Exception {
        db.close();
        super.tearDown();
    }
}