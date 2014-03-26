package org.celllife.stockout.app.ui.activities;

import org.celllife.stockout.app.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class CrashHandlerActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crash);
		setError(getIntent().getStringExtra("error"));
	}

	private void setError(String errorReport) {
		TextView errorText = (TextView) findViewById(R.id.error);
		errorText.setText(errorReport);
		Button button = (Button) findViewById(R.id.cancel_button);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(CrashHandlerActivity.this, MainActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // clears the back stack
				startActivity(intent);
			}
		});
	}
}
