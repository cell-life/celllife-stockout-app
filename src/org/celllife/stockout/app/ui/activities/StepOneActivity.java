package org.celllife.stockout.app.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import org.celllife.stockout.app.R;

/**
 * Created by achmat on 2014/04/24.
 */
public class StepOneActivity extends Activity {
        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step_one_reg);

        EditText msisdn = (EditText) findViewById(R.id.quantity_text);
        EditText password = (EditText) findViewById(R.id.quantity_text);
        Button registerButton = (Button) findViewById(R.id.confirm_button);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent stepTwo = new Intent (StepOneActivity.this, StepTwoActivity.class);
                startActivity(stepTwo);
            }
        });



        }
}


