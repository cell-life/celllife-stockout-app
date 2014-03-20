package org.celllife.stockout.app.test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import junit.framework.Assert;

import org.celllife.stockout.app.domain.Alert;
import org.celllife.stockout.app.domain.AlertStatus;
import org.celllife.stockout.app.domain.Drug;
import org.celllife.stockout.app.domain.comparator.AlertComparator;

import android.test.AndroidTestCase;

public class AlertTest extends AndroidTestCase {

	public void setUp() {
	}

	public void testComparator() {
		Drug d = new Drug("111111112222222222222", "Some drug");
		List<Alert> alerts = new ArrayList<Alert>();
		Calendar cal = Calendar.getInstance(); // e.g. Jan 10
		alerts.add(new Alert(cal.getTime(), 1, "sixth", AlertStatus.NEW, d));
		alerts.add(new Alert(cal.getTime(), 3, "second", AlertStatus.NEW, d));
		alerts.add(new Alert(cal.getTime(), 2, "third", AlertStatus.NEW, d));
		cal.add(Calendar.DAY_OF_YEAR, 10); // e.g. Jan 20
		alerts.add(new Alert(cal.getTime(), 3, "first", AlertStatus.NEW, d));
		alerts.add(new Alert(cal.getTime(), 1, "fifth", AlertStatus.NEW, d));
		cal.add(Calendar.DAY_OF_YEAR, -15); // e.g. Jan 05
		alerts.add(new Alert(cal.getTime(), 1, "seventh", AlertStatus.NEW, d));
		alerts.add(new Alert(cal.getTime(), 2, "fourth", AlertStatus.NEW, d));
		
		Collections.sort(alerts, new AlertComparator());
		Assert.assertEquals("first", alerts.get(0).getMessage());
		Assert.assertEquals("second", alerts.get(1).getMessage());
		Assert.assertEquals("third", alerts.get(2).getMessage());
		Assert.assertEquals("fourth", alerts.get(3).getMessage());
		Assert.assertEquals("fifth", alerts.get(4).getMessage());
		Assert.assertEquals("sixth", alerts.get(5).getMessage());
		Assert.assertEquals("seventh", alerts.get(6).getMessage());
	}

	public void tearDown() throws Exception {
		super.tearDown();
	}
}