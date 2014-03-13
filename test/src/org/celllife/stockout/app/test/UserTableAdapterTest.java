package org.celllife.stockout.app.test;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.celllife.stockout.app.database.UserTableAdapter;
import org.celllife.stockout.app.database.framework.DatabaseOpenHelper;
import org.celllife.stockout.app.database.framework.TableHelper;
import org.celllife.stockout.app.domain.User;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

public class UserTableAdapterTest extends AndroidTestCase {
    private UserTableAdapter userDb;
    DatabaseOpenHelper db;

    public void setUp(){
    	RenamingDelegatingContext context = new RenamingDelegatingContext(getContext(), "test_");
        List<TableHelper<?>> tables = new ArrayList<TableHelper<?>>();
        userDb = new UserTableAdapter();
        tables.add(userDb);
        db = new DatabaseOpenHelper(context, tables);
    }

    public void testAddEntryAndFindByMsisdn(){
    	User u = new User("27768198075", "sdffffffffffffff", "1234", "0000", "Demo Clinic 1");
    	userDb.insert(u);
    	User u2 = userDb.findByMsisdn("27768198075");
    	Assert.assertNotNull(u2);
    	Assert.assertEquals(u.getMsisdn(), u2.getMsisdn());
    }

    public void testFindById(){
    	User u = new User("27768198076", "sdffffffffffffff", "1234", "0000", "Demo Clinic 1");
    	userDb.insert(u);
    	User u2 = userDb.findById(1l);
    	Assert.assertNotNull(u2);
    	Assert.assertEquals(u.getMsisdn(), u2.getMsisdn());
    }

    public void tearDown() throws Exception{
        db.close(); 
        super.tearDown();
    }
}