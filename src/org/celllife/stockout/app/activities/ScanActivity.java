package org.celllife.stockout.app.activities;

import org.celllife.stockout.app.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ScanActivity extends Activity {

	private TextView contentTxt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scan);

		// instantiate UI items
		contentTxt = (TextView) findViewById(R.id.scan_content);

		// instantiate ZXing integration class
		IntentIntegrator scanIntegrator = new IntentIntegrator(ScanActivity.this);
		// start scanning
		scanIntegrator.initiateScan();
	}

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
			contentTxt.setText("CONTENT: " + scanContent);
		} else {
			// invalid scan data or scan canceled
			Toast toast = Toast.makeText(getApplicationContext(), R.string.no_barcode, Toast.LENGTH_SHORT);
			toast.show();
		}
	}
}