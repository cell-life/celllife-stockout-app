package org.celllife.stockout.app.test;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.celllife.stockout.app.database.DrugTableAdapter;
import org.celllife.stockout.app.database.StockHistoryTableAdapter;
import org.celllife.stockout.app.database.framework.DatabaseOpenHelper;
import org.celllife.stockout.app.database.framework.TableHelper;
import org.celllife.stockout.app.domain.Drug;
import org.celllife.stockout.app.domain.StockHistory;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

public class StockHistoryTableAdapterTest extends AndroidTestCase {
	private StockHistoryTableAdapter stockTakeDb;
	private DrugTableAdapter drugDb;
	DatabaseOpenHelper db;

    public void setUp(){
        RenamingDelegatingContext context = new RenamingDelegatingContext(getContext(), "test_");
        List<TableHelper<?>> tables = new ArrayList<TableHelper<?>>();
        drugDb = new DrugTableAdapter();
        stockTakeDb = new StockHistoryTableAdapter(drugDb);
        tables.add(drugDb);
        tables.add(stockTakeDb);
        db = new DatabaseOpenHelper(context, tables);
    }

    public void testAddEntryAndFindByDrug(){
    	Drug d = new Drug("111111112222222222222", "Some drug");
    	drugDb.insert(d);
    	d = drugDb.findByBarcode(d.getBarcode());
    	StockHistory sh = new StockHistory(d, 10);
    	stockTakeDb.insert(sh);
    	StockHistory sh2 = stockTakeDb.findByDrug(d);
    	Assert.assertNotNull(sh2);
    	Assert.assertEquals(sh.getAverageDailyConsumption(), sh2.getAverageDailyConsumption());
    	Assert.assertEquals(sh.getDrug().getId(), sh2.getDrug().getId());
    }

    public void testFindById(){
    	Drug d = new Drug("111111112222222222222", "Some drug");
    	drugDb.insert(d);
    	d = drugDb.findByBarcode(d.getBarcode());
    	StockHistory sh = new StockHistory(d, 10);
    	stockTakeDb.insert(sh);
    	StockHistory sh2 = stockTakeDb.findById(1l);
    	Assert.assertNotNull(sh2);
    	Assert.assertEquals(sh.getAverageDailyConsumption(), sh2.getAverageDailyConsumption());
    	Assert.assertEquals(sh.getDrug().getId(), sh2.getDrug().getId());
    }

    public void testUpdate(){
    	Drug d = new Drug("111111112222222222222", "Some drug");
    	drugDb.insert(d);
    	d = drugDb.findByBarcode(d.getBarcode());
    	StockHistory sh = new StockHistory(d, 10);
    	stockTakeDb.insert(sh);
    	StockHistory sh2 = stockTakeDb.findById(1l);
    	Assert.assertNotNull(sh2);
    	sh2.setAverageDailyConsumption(20);
    	stockTakeDb.update(sh2.getId(), sh2);
    	StockHistory sh3 = stockTakeDb.findById(1l);
    	Assert.assertNotNull(sh3);
    	Assert.assertEquals(Integer.valueOf(20), sh3.getAverageDailyConsumption());
    }

    public void tearDown() throws Exception{
        db.close();
        super.tearDown();
    }
}