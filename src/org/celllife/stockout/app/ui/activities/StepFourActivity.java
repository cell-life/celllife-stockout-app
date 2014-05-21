package org.celllife.stockout.app.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.celllife.stockout.app.R;

public class StepFourActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.step_four_reg);

		Button proceedButton = (Button) findViewById(R.id.confirm_button);
		Button cancelButton = (Button) findViewById(R.id.cancel_button);

		proceedButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent stepThree2 = new Intent(StepFourActivity.this, StepFourConfActivity.class);
				startActivity(stepThree2);
			}
		});

		cancelButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				StepFourActivity.this.finish();
			}
		});

	}
}