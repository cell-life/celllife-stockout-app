package org.celllife.stockout.app.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.celllife.stockout.app.database.AlertTableAdapter;
import org.celllife.stockout.app.database.DrugTableAdapter;
import org.celllife.stockout.app.database.framework.DatabaseOpenHelper;
import org.celllife.stockout.app.database.framework.TableHelper;
import org.celllife.stockout.app.domain.Alert;
import org.celllife.stockout.app.domain.AlertStatus;
import org.celllife.stockout.app.domain.Drug;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

public class AlertTableAdapterTest extends AndroidTestCase {
	private AlertTableAdapter alertDb;
	private DrugTableAdapter drugDb;
	DatabaseOpenHelper db;

    public void setUp(){
        RenamingDelegatingContext context = new RenamingDelegatingContext(getContext(), "test_");
        List<TableHelper<?>> tables = new ArrayList<TableHelper<?>>();
        drugDb = new DrugTableAdapter();
        alertDb = new AlertTableAdapter(drugDb);
        tables.add(drugDb);
        tables.add(alertDb);
        db = new DatabaseOpenHelper(context, tables);
    }

    public void testAddEntryAndFindByDrug(){
    	Drug d = new Drug("111111112222222222222", "Some drug");
    	drugDb.insert(d);
    	d = drugDb.findByBarcode(d.getBarcode());
    	Alert a = new Alert(new Date(), 1, "test alert", AlertStatus.NEW, d);
    	alertDb.insert(a);
    	Alert a2 = alertDb.findByDrug(d);
    	Assert.assertNotNull(a2);
    	Assert.assertEquals(a.getMessage(), a2.getMessage());
    	Assert.assertEquals(a.getLevel(), a2.getLevel());
    	Assert.assertEquals(a.getDate(), a2.getDate());
    	Assert.assertEquals(a.getStatus(), a2.getStatus());
    	Assert.assertEquals(a.getDrug().getId(), a2.getDrug().getId());
    }

    public void testFindById(){
    	Drug d = new Drug("111111112222222222222", "Some drug");
    	drugDb.insert(d);
    	d = drugDb.findByBarcode(d.getBarcode());
    	Alert a = new Alert(new Date(), 1, "test alert", AlertStatus.NEW, d);
    	alertDb.insert(a);
    	Alert a2 = alertDb.findById(1l);
    	Assert.assertNotNull(a2);
    	Assert.assertEquals(a.getMessage(), a2.getMessage());
    	Assert.assertEquals(a.getLevel(), a2.getLevel());
    	Assert.assertEquals(a.getDate(), a2.getDate());
    	Assert.assertEquals(a.getStatus(), a2.getStatus());
    	Assert.assertEquals(a.getDrug().getId(), a2.getDrug().getId());
    }

    public void tearDown() throws Exception{
        db.close();
        super.tearDown();
    }
}