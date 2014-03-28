package org.celllife.stockout.app.ui.activities;

import org.celllife.stockout.app.R;
import org.celllife.stockout.app.manager.ManagerFactory;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
				if (passwd.getText().toString().equals("1234")) {
					Toast.makeText(PinActivity.this.getApplicationContext(), R.string.loading_scanner,
							Toast.LENGTH_LONG).show();
					ManagerFactory.getSessionManager().authenticated("27768198075", "1234");
					PinActivity.this.setResult(RESULT_OK, PinActivity.this.getIntent());
					PinActivity.this.finish();
				} else {
					Toast.makeText(getApplicationContext(), "Please Enter 1234", Toast.LENGTH_LONG).show();
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
}