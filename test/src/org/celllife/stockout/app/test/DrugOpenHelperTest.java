package org.celllife.stockout.app.test;

import junit.framework.Assert;

import org.celllife.stockout.app.database.DrugOpenHelper;
import org.celllife.stockout.app.domain.Drug;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

public class DrugOpenHelperTest extends AndroidTestCase {
    private DrugOpenHelper db;

    public void setUp(){
        RenamingDelegatingContext context = new RenamingDelegatingContext(getContext(), "test_");
        db = new DrugOpenHelper(context);
    }

    public void testAddEntryAndFindByBarcode(){
    	Drug d = new Drug("112233454556","Dagmar");
    	db.addDrug(d);
    	Drug d2 = db.findByBarcode("112233454556");
    	Assert.assertNotNull(d2);
    	Assert.assertEquals(d.getBarcode(), d2.getBarcode());
    	Assert.assertEquals(d.getDescription(), d2.getDescription());
    }

    public void testFindById(){
    	Drug d = new Drug("112233454557","Brendan");
    	db.addDrug(d);
    	Drug d2 = db.findById(1l);
    	Assert.assertNotNull(d2);
    	Assert.assertEquals(d.getBarcode(), d2.getBarcode());
    }

    public void tearDown() throws Exception{
        db.close(); 
        super.tearDown();
    }
}