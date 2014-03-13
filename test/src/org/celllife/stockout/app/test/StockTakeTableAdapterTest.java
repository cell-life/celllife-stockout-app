package org.celllife.stockout.app.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.celllife.stockout.app.database.DrugTableAdapter;
import org.celllife.stockout.app.database.StockTakeTableAdapter;
import org.celllife.stockout.app.database.framework.DatabaseOpenHelper;
import org.celllife.stockout.app.database.framework.TableHelper;
import org.celllife.stockout.app.domain.Drug;
import org.celllife.stockout.app.domain.StockTake;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

public class StockTakeTableAdapterTest extends AndroidTestCase {
	private StockTakeTableAdapter stockTakeDb;
	private DrugTableAdapter drugDb;
	DatabaseOpenHelper db;

    public void setUp(){
        RenamingDelegatingContext context = new RenamingDelegatingContext(getContext(), "test_");
        List<TableHelper<?>> tables = new ArrayList<TableHelper<?>>();
        drugDb = new DrugTableAdapter();
        stockTakeDb = new StockTakeTableAdapter(drugDb);
        tables.add(drugDb);
        tables.add(stockTakeDb);
        db = new DatabaseOpenHelper(context, tables);
    }

    public void testAddEntryAndFindByDrug(){
    	Drug d = new Drug("111111112222222222222", "Some drug");
    	drugDb.insert(d);
    	d = drugDb.findByBarcode(d.getBarcode());
    	StockTake st = new StockTake(new Date(), d, 1, false);
    	stockTakeDb.insert(st);
    	StockTake st2 = stockTakeDb.findByDrug(d);
    	Assert.assertNotNull(st2);
    	Assert.assertEquals(st.getQuantity(), st2.getQuantity());
    	Assert.assertEquals(st.isSubmitted(), st2.isSubmitted());
    	Assert.assertEquals(st.getDate(), st2.getDate());
    	Assert.assertEquals(st.getDrug().getId(), st2.getDrug().getId());
    }

    public void testFindById(){
    	Drug d = new Drug("111111112222222222222", "Some drug");
    	drugDb.insert(d);
    	d = drugDb.findByBarcode(d.getBarcode());
    	StockTake st = new StockTake(new Date(), d, 1, false);
    	stockTakeDb.insert(st);
    	StockTake st2 = stockTakeDb.findById(1l);
    	Assert.assertNotNull(st2);
    	Assert.assertEquals(st.getQuantity(), st2.getQuantity());
    	Assert.assertEquals(st.isSubmitted(), st2.isSubmitted());
    	Assert.assertEquals(st.getDate(), st2.getDate());
    	Assert.assertEquals(st.getDrug().getId(), st2.getDrug().getId());
    }

    public void testUpdate(){
    	Drug d = new Drug("111111112222222222222", "Some drug");
    	drugDb.insert(d);
    	d = drugDb.findByBarcode(d.getBarcode());
    	StockTake st = new StockTake(new Date(), d, 1, false);
    	stockTakeDb.insert(st);
    	StockTake st2 = stockTakeDb.findById(1l);
    	Assert.assertNotNull(st2);
    	st2.setSubmitted(true);
    	stockTakeDb.update(st2.getId(), st2);
    	StockTake st3 = stockTakeDb.findById(1l);
    	Assert.assertNotNull(st3);
    	Assert.assertTrue(st3.isSubmitted());
    }

    public void tearDown() throws Exception{
        db.close();
        super.tearDown();
    }
}