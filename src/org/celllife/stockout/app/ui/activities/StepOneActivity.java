package org.celllife.stockout.app.ui.activities;

import org.celllife.stockout.app.R;
import org.celllife.stockout.app.integration.rest.framework.RestAuthenticationException;
import org.celllife.stockout.app.integration.rest.framework.RestCommunicationException;
import org.celllife.stockout.app.manager.ManagerFactory;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

                try {
                    ManagerFactory.getSetupManager().initialise(msisdnText, pinText);

                    // only if all went fine, then move onto the next step
                    Intent stepTwo = new Intent(StepOneActivity.this, StepTwoActivity.class);
                    startActivity(stepTwo);

                } catch (RestAuthenticationException e) {
                    Log.e("StepOneActivity", "Could not authenticate the user.", e);
                    String errorMessage = getApplicationContext().getString(R.string.authentication_error);
                    if (e.getResponse() != null) {
                        errorMessage = errorMessage + " Error: " + e.getResponse().getCode();
                    }
                    displayErrorMessage(errorMessage);

                } catch (RestCommunicationException e) {
                    Log.e("StepOneActivity", "Server communication problem while authenticating the user.", e);
                    String errorMessage = getApplicationContext().getString(R.string.communication_error);
                    if (e.getResponse() != null) {
                        errorMessage = errorMessage + " Error: " + e.getResponse().getCode();
                    }
                    displayErrorMessage(errorMessage);
                }
			}
		});

		cancelButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				StepOneActivity.this.finish();
			}
		});

	}

    private void displayErrorMessage(String errorMessage) {
        new AlertDialog.Builder(this).setMessage(errorMessage)
                .setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
    }
}
