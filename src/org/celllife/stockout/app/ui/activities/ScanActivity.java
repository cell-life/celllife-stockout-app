package org.celllife.stockout.app.ui.activities;

import java.util.Date;

import org.celllife.stockout.app.R;
import org.celllife.stockout.app.domain.Drug;
import org.celllife.stockout.app.domain.StockHistory;
import org.celllife.stockout.app.domain.StockReceived;
import org.celllife.stockout.app.domain.StockTake;
import org.celllife.stockout.app.integration.rest.framework.RestAuthenticationException;
import org.celllife.stockout.app.integration.rest.framework.RestCommunicationException;
import org.celllife.stockout.app.manager.DatabaseManager;
import org.celllife.stockout.app.manager.ManagerFactory;
import org.celllife.stockout.app.manager.StockTakeManager;
import org.celllife.stockout.app.ui.fragments.OrderFragment;
import org.celllife.stockout.app.ui.fragments.ReceivedFragment;
import org.w3c.dom.Text;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class ScanActivity extends Activity {

	private TextView contentTxt;

	private Drug drug;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i("ScanActivity", "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scan);

		// instantiate UI items
		contentTxt = (TextView) findViewById(R.id.scan_content);
		
		final String scanType = this.getIntent().getStringExtra("type");
		final Button confirmButton = (Button) findViewById(R.id.confirm_button);
		confirmButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean success = false;
				if (scanType.equalsIgnoreCase(OrderFragment.TYPE)) {
					success = createAndSaveStockTake();
				}
				if (scanType.equalsIgnoreCase(ReceivedFragment.TYPE)) {
					success = createAndSaveStockReceived();
				}

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

		// ensure that the app always returns in portrait mode
		Log.i("ScanActivity", "onCreate - making orientation portrait");
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		String scanContent = this.getIntent().getStringExtra("barcode");
		drug = lookupDrug(scanContent);
		if (drug != null) {
			setupAvdViews();
			contentTxt.setText(drug.getDescription());
		} else if (scanContent != null) {
			// no drug found
			displayScanErrorMessage();
		} else {
			// they didn't scan anything, so abort silently
			ScanActivity.this.finish();
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

    private void setupAvdViews() {
        final TextView avdText = (TextView) findViewById(R.id.avd_msg);
        final EditText avdField = (EditText) findViewById(R.id.avd_text);
        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout2);
        StockHistory stockHistory = DatabaseManager.getStockHistoryTableAdapter().findByDrug(drug);
        if (stockHistory == null) {
            linearLayout.setVisibility(View.VISIBLE);
            avdText.setVisibility(View.VISIBLE);
            avdField.setVisibility(View.VISIBLE);
        } else {
            linearLayout.setVisibility(View.INVISIBLE);
            avdText.setVisibility(View.INVISIBLE);
            avdField.setVisibility(View.INVISIBLE);
        }
    }



	private boolean createAndSaveStockReceived() {
		final EditText quantityField = (EditText) findViewById(R.id.quantity_text);
        final EditText avdField = (EditText) findViewById(R.id.avd_text);
		if (quantityField.getText().toString() == null || quantityField.getText().toString().trim().equals("")) {
			displayQuantityErrorMessage(R.string.scan_quantity_error);
			return false;
      //  if (avdField.isShown() && (avdField.getText().toString() == null || avdField.getText().toString().trim().equals(""))) {
            //display error
		} else {
			try {
				Integer quantity = Integer.parseInt(quantityField.getText().toString());
				StockReceived stockReceived = new StockReceived(new Date(), drug, quantity, false);
				StockTakeManager stockManager = ManagerFactory.getStockTakeManager();
				stockManager.newStockReceived(stockReceived);
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