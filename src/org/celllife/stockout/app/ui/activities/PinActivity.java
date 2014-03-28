package org.celllife.stockout.app.ui.activities;

import org.celllife.stockout.app.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by achmat on 2014/03/12.
 */
public class PinActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pin);

		final EditText passwd = (EditText) findViewById(R.id.pin);

		final Button confirmButton = (Button) findViewById(R.id.confirm_button);
		confirmButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				if (passwd.getText().toString().equals("123456789")) {
					startScanActivity();
				} else {
					Toast.makeText(getApplicationContext(), "Please Enter Numeric Password", Toast.LENGTH_LONG).show();
				}
			}
		});

		final Button cancelButton = (Button) findViewById(R.id.cancel_button);
		cancelButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				PinActivity.this.finish();
			}
		});

	}

	private void startScanActivity() {
		Intent intent = new Intent(this, ScanActivity.class);
		startActivity(intent);
	}

}
