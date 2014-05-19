package org.celllife.stockout.app.test;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.celllife.stockout.app.database.DrugTableAdapter;
import org.celllife.stockout.app.database.framework.DatabaseOpenHelper;
import org.celllife.stockout.app.database.framework.TableHelper;
import org.celllife.stockout.app.domain.Drug;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

public class DrugTableAdapterTest extends AndroidTestCase {
	private DrugTableAdapter drugDb;
	DatabaseOpenHelper db;

    public void setUp(){
        RenamingDelegatingContext context = new RenamingDelegatingContext(getContext(), "test_");
        List<TableHelper<?>> tables = new ArrayList<TableHelper<?>>();
        drugDb = new DrugTableAdapter();
        tables.add(drugDb);
        db = new DatabaseOpenHelper(context, tables);
    }

    public void testAddEntryAndFindByBarcode(){
    	Drug d = new Drug("112233454556","Dagmar");
    	drugDb.insert(d);
    	Drug d2 = drugDb.findByBarcode("112233454556");
    	Assert.assertNotNull(d2);
    	Assert.assertEquals(d.getBarcode(), d2.getBarcode());
    	Assert.assertEquals(d.getDescription(), d2.getDescription());
    }

    public void testFindById(){
    	Drug d = new Drug("112233454557","Brendan");
    	drugDb.insert(d);
    	Drug savedD = drugDb.findByBarcode("112233454557");
    	Drug d2 = drugDb.findById(savedD.getId());
    	Assert.assertNotNull(d2);
    	Assert.assertEquals(d.getBarcode(), d2.getBarcode());
    }

    public void tearDown() throws Exception{
        db.close(); 
        super.tearDown();
    }
}