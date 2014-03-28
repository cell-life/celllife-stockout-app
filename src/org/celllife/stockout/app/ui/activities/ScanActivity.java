package org.celllife.stockout.app.ui.activities;

import java.util.Date;

import org.celllife.stockout.app.R;
import org.celllife.stockout.app.domain.Drug;
import org.celllife.stockout.app.domain.StockTake;
import org.celllife.stockout.app.integration.rest.framework.RestAuthenticationException;
import org.celllife.stockout.app.integration.rest.framework.RestCommunicationException;
import org.celllife.stockout.app.manager.ManagerFactory;
import org.celllife.stockout.app.manager.StockTakeManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ScanActivity extends Activity {

	private TextView contentTxt;

	private Drug drug;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scan);

		// instantiate UI items
		contentTxt = (TextView) findViewById(R.id.scan_content);
		
		final Button confirmButton = (Button) findViewById(R.id.confirm_button);
		confirmButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean success = createAndSaveStockTake();
				if (success) {
					ScanActivity.this.finish();
				}
				
			}
		});
		final Button cancelButton = (Button) findViewById(R.id.cancel_button);
		cancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ScanActivity.this.finish();
			}
		});

		// instantiate ZXing integration class
		IntentIntegrator scanIntegrator = new IntentIntegrator(ScanActivity.this);
		// start scanning
		scanIntegrator.initiateScan();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		// retrieve result of scanning - instantiate ZXing object
		IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
		// check we have a valid result
		if (scanningResult != null) {
			// get content from Intent Result
			String scanContent = scanningResult.getContents();
			// get format name of data scanned
			// String scanFormat = scanningResult.getFormatName();
			// output to UI
			drug = lookupDrug(scanContent);
			if (drug != null) {
				contentTxt.setText(drug.getDescription());
			} else if (scanContent != null) {
				// no drug found
				displayScanErrorMessage();
			} else {
				// they didn't scan anything, so abort silently
				ScanActivity.this.finish();
			}
			
		} else {
			// invalid scan data or scan canceled
			displayScanErrorMessage();
		}
	}
	
	private void displayScanErrorMessage() {
		new AlertDialog.Builder(this)
		.setMessage(R.string.scan_error)
	    .setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            dialog.cancel();
	            ScanActivity.this.finish();
	        }
	     })
	     .show();
	}
	
	private Drug lookupDrug(String barcode) {
		if (barcode != null) {
			StockTakeManager stockManager = ManagerFactory.getStockTakeManager();
			return stockManager.findDrugByBarcode(barcode.trim());
		}
		return null;
	}
	
	private boolean createAndSaveStockTake() {
		final EditText quantityField = (EditText) findViewById(R.id.quantity_text);
		if (quantityField.getText().toString() == null || quantityField.getText().toString().trim().equals("")) {
			displayQuantityErrorMessage(R.string.scan_quantity_error);
			return false;
		} else {
			try {
				Integer quantity = Integer.parseInt(quantityField.getText().toString());
				StockTake stockTake = new StockTake(new Date(), drug, quantity, false);
				StockTakeManager stockManager = ManagerFactory.getStockTakeManager();
				stockManager.newStockTake(stockTake);
			} catch (NumberFormatException e) {
				displayQuantityErrorMessage(R.string.scan_quantity_number_error);
				return false;
			} catch (RestAuthenticationException e) {
				displayErrorMessage(e.getMessage());
				return false;
			} catch (RestCommunicationException e) {
				displayErrorMessage(e.getMessage());
				return false;
			}
		}
		return true;
	}
	
	private void displayQuantityErrorMessage(int messageResource) {
		new AlertDialog.Builder(this)
		.setMessage(messageResource)
	    .setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            dialog.cancel();
	        }
	     })
	     .show();
	}
	
	private void displayErrorMessage(String message) {
		new AlertDialog.Builder(this)
		.setMessage(message)
	    .setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            dialog.cancel();
	            ScanActivity.this.finish();
	        }
	     })
	     .show();
	}
}