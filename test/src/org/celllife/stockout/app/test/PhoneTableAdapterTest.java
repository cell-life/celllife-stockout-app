package org.celllife.stockout.app.test;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.celllife.stockout.app.database.PhoneTableAdapter;
import org.celllife.stockout.app.database.framework.DatabaseOpenHelper;
import org.celllife.stockout.app.database.framework.TableHelper;
import org.celllife.stockout.app.domain.Phone;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

public class PhoneTableAdapterTest extends AndroidTestCase {
    private PhoneTableAdapter phoneDb;
    DatabaseOpenHelper db;

    public void setUp(){
    	RenamingDelegatingContext context = new RenamingDelegatingContext(getContext(), "test_");
        List<TableHelper<?>> tables = new ArrayList<TableHelper<?>>();
        phoneDb = new PhoneTableAdapter();
        tables.add(phoneDb);
        db = new DatabaseOpenHelper(context, tables);
    }

    public void testAddEntryAndFindByMsisdn(){
    	Phone p = new Phone("27768198075", "1234", "0000", "Demo Clinic 1");
    	phoneDb.insert(p);
    	Phone p2 = phoneDb.findByMsisdn("27768198075");
    	Assert.assertNotNull(p2);
    	Assert.assertEquals(p.getMsisdn(), p2.getMsisdn());
    }

    public void testFindById(){
    	Phone p = new Phone("27768198076", "1234", "0000", "Demo Clinic 1");
    	phoneDb.insert(p);
    	Phone p2 = phoneDb.findById(1l);
    	Assert.assertNotNull(p2);
    	Assert.assertEquals(p.getMsisdn(), p2.getMsisdn());
    }

    public void tearDown() throws Exception{
        db.close(); 
        super.tearDown();
    }
}