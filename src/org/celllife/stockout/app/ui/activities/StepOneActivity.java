package org.celllife.stockout.app.ui.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import org.celllife.stockout.app.R;
import org.celllife.stockout.app.database.PhoneTableAdapter;
import org.celllife.stockout.app.domain.Phone;
import org.celllife.stockout.app.integration.rest.GetUserMethod;
import org.celllife.stockout.app.manager.DatabaseManager;
import org.celllife.stockout.app.manager.ManagerFactory;

public class StepOneActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.step_one_reg);

		Button registerButton = (Button) findViewById(R.id.confirm_button);
		Button cancelButton = (Button) findViewById(R.id.cancel_button);

		registerButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				EditText msisdn = (EditText) findViewById(R.id.msisdn_text);
				EditText pin = (EditText) findViewById(R.id.pin_text);

				String msisdnText = msisdn.getText().toString();
				String pinText = pin.getText().toString();

				ManagerFactory.getSetupManager().initialise(msisdnText, pinText);

				Intent stepTwo = new Intent(StepOneActivity.this, StepTwoActivity.class);
				startActivity(stepTwo);
			}
		});

		cancelButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				StepOneActivity.this.finish();
			}
		});

	}

}
