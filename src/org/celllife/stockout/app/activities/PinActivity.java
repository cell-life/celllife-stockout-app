package org.celllife.stockout.app.activities;


import org.celllife.stockout.app.R;
import org.celllife.stockout.app.activities.ScanActivity;
import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Intent;

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
        final Button cancelButton = (Button) findViewById(R.id.cancel_button);

	private void setupScanButton(final View orderView) {
        Button startScanBtn = (Button) orderView.findViewById (R.id.scan_button);
        startScanBtn.setOnClickListener(new OnClickListener() {
           @Override
          public void onClick(View v) {
            	Toast.makeText(orderView.getContext(), R.string.loading_scanner, Toast.LENGTH_LONG).show();
                startScanActivity();
          }
      });
	}

    private void startScanActivity() {
       Intent intent = new Intent(orderView.getContext(), ScanActivity.class);
        startActivity(intent);
    }



    }

}
