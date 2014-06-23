package org.celllife.stockout.app.database;

import java.util.ArrayList;
import java.util.List;

import org.celllife.stockout.app.database.framework.TableAdapter;
import org.celllife.stockout.app.domain.Drug;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

/**
 * This is the Data Access Object (DAO) for the Drug table. All methods
 * required to save, update, or retrieve Drugs from the database 
 * should be defined here.
 */
public class DrugTableAdapter extends TableAdapter<Drug> {
	
	// Drug Table Name
	private static final String TABLE_DRUG = "drug";
	
	// Drug Table Column Names
	private static final String ID = "id";
	private static final String DESCRIPTION = "description";
	private static final String BARCODE = "barcode";
	
	// Drug Queries
	private static final String QUERY_FINDBY_BARCODE = "SELECT  * FROM " + TABLE_DRUG + " WHERE " + BARCODE + " = ?";

	String CREATE_DRUG_TABLE = 
			"CREATE TABLE " + TABLE_DRUG +" ("
			+ ID + " INTEGER PRIMARY KEY, " 
			+ DESCRIPTION + " TEXT, "
			+ BARCODE + " TEXT )";

	
	public DrugTableAdapter() {
	}

	@Override
	public String getCreateTableSql() {
		return CREATE_DRUG_TABLE;
	}

	@Override
	public String getTableName() {
		return TABLE_DRUG;
	}

	@Override
	public List<ContentValues> getInitialData() {
		Log.w("DrugTableAdapter", "Initialising Drugs");
		List<ContentValues> initialData = new ArrayList<ContentValues>();
		Drug grandpa = new Drug("60015204", "Grandpa 24 tablets");
		Drug panado = new Drug("60011053", "Panado 500mg 24 tablets");
		Drug zidovudine = new Drug("6006352019459", "Cipla-Zidovudine 100 mg 100 capsules");
		Drug diflucan = new Drug("6001137100797", "Diflucan 35 ml powder for oral suspension");
		Drug kaletra = new Drug("6001127043738","Lopinavir/Ritonavir oral solution 5X60 ml");
		Drug aluvia = new Drug("6001127044858","Aluvia 100 mg/25mg 56 tablets");
		Drug norvirSec = new Drug("6001127043318","Norvir Sec 100 mg  84 capsules");
		Drug efavirenz = new Drug("6006352024804","Cipla-Efavirenz 600 mg 28 tablets");
		Drug dumiva = new Drug("6009628332856","Abacavir 600 mg/Lamivudine 300 mg 28 tablets");
		Drug adcoLamivudine  = new Drug("6004405009518","Adco-Lamivudine Solution 10mg/ml 240 ml");
		Drug erige  = new Drug("6009695242614","Erige 50 mg 30 capsules");
		Drug auroStavudine = new Drug("6009695242683","Auro-Stavudine  20mg  60 capsules");
		Drug adcoZidovudine = new Drug("6004405009525","Adco-Zidovudine Syrup 50 mg/ 5ml 240 ml");
		Drug kavimunPaed = new Drug("6009628332788","Abacavir 60 mg 56 tablets");
		Drug lamivudine = new Drug("6006352003052","Cipla-Lamivudine 150 mg 60 tablets");
		Drug legram = new Drug("6009695243024","Legram 10mg/ml oral solution 240 ml");
		Drug viropon  = new Drug("6009695242591","Viropon 50mg/ 5ml Oral Suspension 240 ml");
		Drug abacavir  = new Drug("6001390116696","Aspen Abacavir Oral Suspension 20mg/ml");
		Drug ziagen = new Drug("6001076209209","Ziagen  60 tablets");
		Drug zidomat = new Drug("6009628332801","Zidomat 300 mg 56 tablets");
		Drug deladex = new Drug("6009695243703","Deladex 250 mg capsules");
		Drug norvirSolution = new Drug("6001127043325","Norvir 80 mg/ml 1X90ml");
		Drug ciplaLamivudine300 = new Drug("6006352008583","Cipla-Lamivudine 300 mg 150 tablets");
		Drug norstanIsoniazid= new Drug("6009609471031","Norstan-isoniazid 100 mg 84 tablets");
		Drug sandozPyrazinamide = new Drug("6006498011638","Sandoz Pyrazinamide 500 mg 84 tablets");
		Drug rimactane = new Drug("6006498008003","Rimactane 150 mg 100 capsules");
		Drug ethatyl = new Drug("6005317012900","Ethatyl 250 mg 84 tablets");
		Drug rimactaneVial = new Drug("6006498009178","Rimactane Vial dry powder");
		Drug mycobutin = new Drug("6006673871552","Mycobutin 150 mg 30 capsules");
		Drug rCinSuspension = new Drug("6001390115637","R-Cin suspension 180 ml");
		Drug terivalidin = new Drug("6005317007357","Terivalidin 250 mg capsules");
		Drug rimactazidPaed = new Drug("6006498012703","Rimactazid Paed 56 tablets");
		Drug sandozEthambutol = new Drug("6006498009505","Sandoz Ethambutol HCI 400 mg 100 tablets");
		Drug rifafourE275 = new Drug("6005317017103","Rifafour e-275 100 tablets");
		Drug RCin600 = new Drug("6009657981742","R-CIN 600 mg 100 capsules");

		initialData.add(createContentValues(grandpa));
		initialData.add(createContentValues(panado));
		initialData.add(createContentValues(zidovudine));
		initialData.add(createContentValues(diflucan));
		initialData.add(createContentValues(kaletra));
		initialData.add(createContentValues(aluvia));
		initialData.add(createContentValues(norvirSec));
		initialData.add(createContentValues(efavirenz));
		initialData.add(createContentValues(dumiva));
		initialData.add(createContentValues(adcoLamivudine));
		initialData.add(createContentValues(erige));
		initialData.add(createContentValues(auroStavudine));
		initialData.add(createContentValues(adcoZidovudine));
		initialData.add(createContentValues(kavimunPaed));
		initialData.add(createContentValues(lamivudine));
		initialData.add(createContentValues(legram));
		initialData.add(createContentValues(viropon));
		initialData.add(createContentValues(abacavir));
		initialData.add(createContentValues(ziagen));
		initialData.add(createContentValues(zidomat));
		initialData.add(createContentValues(deladex));
		initialData.add(createContentValues(norvirSolution));
		initialData.add(createContentValues(ciplaLamivudine300));
		initialData.add(createContentValues(norstanIsoniazid));
		initialData.add(createContentValues(sandozPyrazinamide));
		initialData.add(createContentValues(rimactane));
		initialData.add(createContentValues(ethatyl));
		initialData.add(createContentValues(rimactaneVial));
		initialData.add(createContentValues(mycobutin));
		initialData.add(createContentValues(rCinSuspension));
		initialData.add(createContentValues(terivalidin));
		initialData.add(createContentValues(rimactazidPaed));
		initialData.add(createContentValues(sandozEthambutol));
		initialData.add(createContentValues(rifafourE275));
		initialData.add(createContentValues(RCin600));
	
		return initialData;
	}
	
	@Override
	protected ContentValues createContentValues(Drug drug) {
		ContentValues values = new ContentValues();
		values.put(DESCRIPTION, drug.getDescription());
		values.put(BARCODE, drug.getBarcode()); 
		return values;
	}

	@Override
	public Drug readFromCursor(Cursor c) {
	    if (c.getCount() > 0) {
		    Drug d = new Drug();
		    d.setId(c.getLong(c.getColumnIndex(ID)));
		    d.setDescription((c.getString(c.getColumnIndex(DESCRIPTION))));
		    d.setBarcode(c.getString(c.getColumnIndex(BARCODE)));
		    return d;
	    }
	    return null;
	}

	// Add the table specific CRUD operations here

	public Drug findByBarcode(String barcode) {
		if (barcode != null) {
			return db.find(this, QUERY_FINDBY_BARCODE, new String[] { barcode });
		}
		return null;
	}	
}
