package org.celllife.stockout.app.ui.activities;

import org.celllife.stockout.app.R;
import org.celllife.stockout.app.domain.Phone;
import org.celllife.stockout.app.manager.AuthenticationManager;
import org.celllife.stockout.app.manager.ManagerFactory;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * The activity that handles the user entering their password/pin
 */
public class PinActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pin);

		Button confirmButton = (Button) findViewById(R.id.confirm_button);
		confirmButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				AuthenticationManager authManager = ManagerFactory.getAuthenticationManager();
				
				Phone phone = authManager.getPhone();
				if (phone != null) {
					String username = phone.getMsisdn();
					String password = ((EditText) findViewById(R.id.pin)).getText().toString();
					
					boolean success = authManager.authenticate(username, password);
					if (success) {
						Toast.makeText(PinActivity.this.getApplicationContext(), R.string.loading_scanner,
								Toast.LENGTH_LONG).show();
						ManagerFactory.getSessionManager().authenticated(username, password);
						PinActivity.this.setResult(RESULT_OK, PinActivity.this.getIntent());
						PinActivity.this.finish();
					} else {
						Toast.makeText(getApplicationContext(), R.string.pin_error, Toast.LENGTH_LONG).show();
					}
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