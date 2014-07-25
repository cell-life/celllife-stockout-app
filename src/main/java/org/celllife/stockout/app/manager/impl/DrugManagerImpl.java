package org.celllife.stockout.app.manager.impl;

import java.util.Collections;
import java.util.List;

import org.celllife.stockout.app.database.DrugTableAdapter;
import org.celllife.stockout.app.domain.Drug;
import org.celllife.stockout.app.domain.comparator.DrugComparator;
import org.celllife.stockout.app.manager.DatabaseManager;
import org.celllife.stockout.app.manager.DrugManager;

public class DrugManagerImpl implements DrugManager{

	@Override
	public List<Drug> getDrugs() {
		DrugTableAdapter drugDb = DatabaseManager.getDrugTableAdapter();
		List<Drug> drugs = drugDb.findAll();
        Collections.sort(drugs, new DrugComparator());
		return drugs;
	}

}
