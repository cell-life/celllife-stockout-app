package org.celllife.stockout.app.test;

import java.util.Date;

import junit.framework.Assert;

import org.celllife.stockout.app.database.DrugTableAdapter;
import org.celllife.stockout.app.domain.Drug;
import org.celllife.stockout.app.domain.Phone;
import org.celllife.stockout.app.domain.StockTake;
import org.celllife.stockout.app.manager.DatabaseManager;
import org.celllife.stockout.app.manager.ManagerFactory;
import org.celllife.stockout.app.manager.StockTakeManager;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

public class StockTakeManagerTest extends AndroidTestCase {

    public void setUp(){
        RenamingDelegatingContext context = new RenamingDelegatingContext(getContext(), "test_");
        ManagerFactory.initialise(context);
        Phone p = new Phone("27768198075", "1234");
        p.setId(1l); // ensure that we don't insert a new record
        DatabaseManager.getPhoneTableAdapter().insertOrUpdate(p);
    }

    public void testSendAStockTake() {
    	StockTakeManager stockTakeManager = ManagerFactory.getStockTakeManager();
    	StockTake stockTake = new StockTake();
    	stockTake.setDrug(createDrug("60015204", "Grandpa 24 tablets"));
    	stockTake.setQuantity(1);
    	stockTake.setDate(new Date());
    	stockTake.setSubmitted(false);
    	Phone p = DatabaseManager.getPhoneTableAdapter().findOne();
    	Assert.assertEquals("27768198075", p.getMsisdn());
    	Assert.assertEquals("1234", p.getPassword());
    	stockTakeManager.newStockTake(stockTake);
    	Assert.assertTrue(stockTake.getSubmitted());
    }
    
    private Drug createDrug(String barcode, String description) {
    	DrugTableAdapter drugDb = DatabaseManager.getDrugTableAdapter();
    	Drug d = new Drug(barcode, description);
    	drugDb.insert(d);
    	d = drugDb.findByBarcode(barcode);
    	return d;
    }

    public void tearDown() throws Exception{
        super.tearDown();
    }
}