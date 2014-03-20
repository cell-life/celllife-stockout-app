package org.celllife.stockout.app.test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import junit.framework.Assert;

import org.celllife.stockout.app.domain.Drug;
import org.celllife.stockout.app.domain.StockTake;
import org.celllife.stockout.app.domain.comparator.StockTakeComparator;

import android.test.AndroidTestCase;

public class StockTakeTest extends AndroidTestCase {

    public void setUp(){
    }

    public void testComparator(){
    	Drug d = new Drug("111111112222222222222", "Some drug");
    	Calendar cal = Calendar.getInstance(); // e.g. today 20 Jan
    	List<StockTake> stocks = new ArrayList<StockTake>();
    	cal.add(Calendar.DAY_OF_YEAR, -2); // e.g. today 18 Jan
    	StockTake st1 = new StockTake(cal.getTime(), d, 22, false);
    	cal.add(Calendar.DAY_OF_YEAR, -4); // e.g. today 14 Jan
    	StockTake st2 = new StockTake(cal.getTime(), d, 44, false);
    	cal.add(Calendar.DAY_OF_YEAR, 5); // e.g. today 19 Jan
    	StockTake st3 = new StockTake(cal.getTime(), d, 66, false);
    	stocks.add(st1);
    	stocks.add(st2);
    	stocks.add(st3);
    	Collections.sort(stocks, new StockTakeComparator());
    	for (StockTake stock : stocks) {
    		System.out.println("stock="+stock);
    	}
    	Assert.assertEquals(Integer.valueOf(66), stocks.get(0).getQuantity());
    	Assert.assertEquals(Integer.valueOf(22), stocks.get(1).getQuantity());
    	Assert.assertEquals(Integer.valueOf(44), stocks.get(2).getQuantity());
    }
    
    public void tearDown() throws Exception{
        super.tearDown();
    }
}