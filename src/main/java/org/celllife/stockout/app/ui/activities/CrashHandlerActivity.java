package org.celllife.stockout.app.ui.activities;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.celllife.stockout.app.R;
import org.celllife.stockout.app.integration.rest.framework.RestAuthenticationException;
import org.celllife.stockout.app.integration.rest.framework.RestCommunicationException;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class CrashHandlerActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crash);
		Throwable ex = (Throwable)getIntent().getSerializableExtra("exception");
		if (ex instanceof RestAuthenticationException) {
			setError(ex.getMessage(), null);
		} else  if (ex instanceof RestCommunicationException) {
			setError(ex.getMessage(), null);
		} else {
			String errorReport = getErrorReport(ex);
			Log.e("CrashHandlerActivity", errorReport);
			setError(null, errorReport);
		}
	}

	private void setError(String errorMessage, String errorReport) {
		TextView errorText = (TextView) findViewById(R.id.error);
		if (errorMessage != null) {
			errorText.setGravity(Gravity.CENTER);
			errorText.setText(errorMessage);
		} else {
			errorText.setText(errorReport);
		}
		Button button = (Button) findViewById(R.id.ok_button);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(CrashHandlerActivity.this, MainActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // clears the back stack
				startActivity(intent);
			}
		});
	}

	protected String getErrorReport(Throwable exception) {
		StringWriter stackTrace = new StringWriter();
		exception.printStackTrace(new PrintWriter(stackTrace));
		StringBuilder errorReport = new StringBuilder();
		errorReport.append("DEVICE INFORMATION").append(System.getProperty("line.separator"));
		errorReport.append("Brand: ").append(Build.BRAND).append(System.getProperty("line.separator"));
		errorReport.append("Device: ").append(Build.DEVICE).append(System.getProperty("line.separator"));
		errorReport.append("Model: ").append(Build.MODEL).append(System.getProperty("line.separator"));
		errorReport.append("Id: ").append(Build.ID).append(System.getProperty("line.separator"));
		errorReport.append("Product: ").append(Build.PRODUCT).append(System.getProperty("line.separator"));
		errorReport.append("SDK: ").append(Build.VERSION.SDK_INT).append(System.getProperty("line.separator"));
		errorReport.append("Release: ").append(Build.VERSION.RELEASE).append(System.getProperty("line.separator"));
		errorReport.append("Incremental: ").append(Build.VERSION.INCREMENTAL)
				.append(System.getProperty("line.separator"));
		errorReport.append(System.getProperty("line.separator"));
		errorReport.append("CAUSE OF ERROR").append(System.getProperty("line.separator"));
		errorReport.append(stackTrace.toString());
		return errorReport.toString();
	}
}
