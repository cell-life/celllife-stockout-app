package org.celllife.stockout.app.test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.celllife.stockout.app.database.DrugTableAdapter;
import org.celllife.stockout.app.database.PhoneTableAdapter;
import org.celllife.stockout.app.database.StockHistoryTableAdapter;
import org.celllife.stockout.app.database.StockTakeTableAdapter;
import org.celllife.stockout.app.domain.Drug;
import org.celllife.stockout.app.domain.Phone;
import org.celllife.stockout.app.domain.StockHistory;
import org.celllife.stockout.app.domain.StockReceived;
import org.celllife.stockout.app.domain.StockTake;
import org.celllife.stockout.app.manager.CalculationManagerImpl;
import org.celllife.stockout.app.manager.DatabaseManager;
import org.celllife.stockout.app.manager.ManagerFactory;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

public class CalculationManagerTest extends AndroidTestCase {

	public void setUp() {
        RenamingDelegatingContext context = new RenamingDelegatingContext(getContext(), "test_");
        ManagerFactory.initialise(context);
	}

	public void testCalculateAverageDailyConsumption() {
		Calendar cal = Calendar.getInstance();
		StockTake newStockTake = new StockTake();
		StockTake oldStockTake = new StockTake();
		newStockTake.setQuantity(10);
		newStockTake.setDate(cal.getTime());
		cal.add(Calendar.DATE, -3);
		oldStockTake.setQuantity(40);
		oldStockTake.setDate(cal.getTime());
		int adc = new CalculationManagerImpl().calculateAverageDailyConsumption(oldStockTake, newStockTake, new ArrayList<StockReceived>());
		Assert.assertEquals(10, adc);
	}

	public void testCalculateAverageDailyConsumptionWithStockReceived() {
		Calendar cal = Calendar.getInstance();
		List<StockReceived> stockReceived = new ArrayList<StockReceived>();
		StockTake newStockTake = new StockTake();
		StockTake oldStockTake = new StockTake();
		newStockTake.setQuantity(20);
		newStockTake.setDate(cal.getTime());
		cal.add(Calendar.DATE, -1);
		stockReceived.add(new StockReceived(new Date(), null, 10, false));
		cal.add(Calendar.DATE, -1);
		stockReceived.add(new StockReceived(new Date(), null, 10, false));
		cal.add(Calendar.DATE, -3);
		oldStockTake.setQuantity(20);
		oldStockTake.setDate(cal.getTime());
		int adc = new CalculationManagerImpl().calculateAverageDailyConsumption(oldStockTake, newStockTake, stockReceived);
		Assert.assertEquals(4, adc);
	}

	public void testGetEstimatedStock() {
		Drug d = createDrug("60015204", "Grandpa 24 tablets");
		createStockTake(d);
		createStockHistory(d);
		int estimatedStock = new CalculationManagerImpl().getEstimatedStock(d);
		Assert.assertEquals(110, estimatedStock);
	}

	public void testGetThreshold() {
		Drug d = createDrug("60015204", "Grandpa 24 tablets");
		createStockHistory(d);
		createPhone();
		int threshold = new CalculationManagerImpl().getThreshold(d);
		Assert.assertEquals(100, threshold);
	}

    private Drug createDrug(String barcode, String description) {
    	DrugTableAdapter drugDb = DatabaseManager.getDrugTableAdapter();
    	Drug d = new Drug(barcode, description);
    	drugDb.insert(d);
    	d = drugDb.findByBarcode(barcode);
    	return d;
    }
    
    private StockTake createStockTake(Drug d) {
    	Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -3);
		StockTake stockTake = new StockTake();
		stockTake.setQuantity(140);
		stockTake.setDrug(d);
		stockTake.setDate(cal.getTime());
		StockTakeTableAdapter stockTakeDb = DatabaseManager.getStockTakeTableAdapter();
		stockTakeDb.insert(stockTake);
		stockTake = stockTakeDb.findByDrug(d);
		return stockTake;
    }
    
    private StockHistory createStockHistory(Drug d) {
    	StockHistory stockHistory = new StockHistory();
    	stockHistory.setDrug(d);
    	stockHistory.setAverageDailyConsumption(10);
    	StockHistoryTableAdapter stockHistoryDb = DatabaseManager.getStockHistoryTableAdapter();
		stockHistoryDb.insert(stockHistory);
		stockHistory = stockHistoryDb.findByDrug(d);
    	return stockHistory;
    }
    
    private Phone createPhone() {
    	Phone phone = new Phone();
    	phone.setDrugLeadTime(7);
    	phone.setDrugSafetyLevel(3);
    	PhoneTableAdapter phoneDb = DatabaseManager.getPhoneTableAdapter();
    	phoneDb.insert(phone);
    	phone = phoneDb.findOne();
    	return phone;
    }

	public void tearDown() throws Exception {
		super.tearDown();
	}
}