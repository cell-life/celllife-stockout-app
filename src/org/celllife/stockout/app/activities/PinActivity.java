package org.celllife.stockout.app.activities;


import org.celllife.stockout.app.R;
import org.celllife.stockout.app.activities.ScanActivity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Button;
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

    }

    private void setupScanButton(final View orderView) {
        Button confirmButton = (Button) orderView.findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startScanActivity();
            }
        });
    }

    private void setupCancelButton(final View orderView) {
        Button cancelButton = (Button) orderView.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startScanActivity();
            }
        });
    }

    private void startScanActivity() {
        Intent intent = new Intent(this, ScanActivity.class);
        startActivity(intent);
    }


}


