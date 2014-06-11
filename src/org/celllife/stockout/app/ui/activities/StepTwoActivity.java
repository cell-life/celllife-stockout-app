package org.celllife.stockout.app.ui.activities;

import org.celllife.stockout.app.R;
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

public class StepTwoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.step_two_reg);

		Button proceedButton = (Button) findViewById(R.id.confirm_button);
		Button cancelButton = (Button) findViewById(R.id.cancel_button);

		proceedButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				EditText leadTime = (EditText) findViewById(R.id.lead_text);
				EditText safetyTime = (EditText) findViewById(R.id.safety_text);
				// EditText operatingDays = (EditText)findViewById(R.id.operating_text);

				String leadTimeText = leadTime.getText().toString();
				String safetyTimeText = safetyTime.getText().toString();
				Log.i("StepTwoActivity", "Got leadTime="+leadTimeText+", safetyTime="+safetyTimeText);
				// String operatingTimeText = operatingdays.getText().toString();

				try {
					Integer leadTimeInt = Integer.parseInt(leadTimeText);
					Integer safetyTimeInt = Integer.parseInt(safetyTimeText);
					// FIXME Not saving Operating time yet

				    ManagerFactory.getSetupManager().setSafetyLevelSettings(leadTimeInt, safetyTimeInt);
				    Log.i("StepTwoActivity", "Saving leadTime="+leadTimeInt+", safetyTime="+safetyTimeInt);

					Intent stepThree = new Intent(StepTwoActivity.this, StepThreeActivity.class);
					startActivity(stepThree);
				} catch (NumberFormatException e) {
					displayErrorMessage(getApplicationContext().getString(R.string.number_error));
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
				StepTwoActivity.this.finish();
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