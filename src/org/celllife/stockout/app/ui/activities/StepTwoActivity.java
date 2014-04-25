package org.celllife.stockout.app.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.celllife.stockout.app.R;


public class StepTwoActivity extends Activity {
        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step_two_reg);

        EditText leadtime = (EditText) findViewById(R.id.quantity_text_two);
        EditText safetytime = (EditText) findViewById(R.id.quantity_text_two);
        EditText operatingdays = (EditText) findViewById(R.id.quantity_text_two);
        Button proceedButton = (Button) findViewById(R.id.confirm_button);

         proceedButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent stepThree = new Intent (StepTwoActivity.this, StepThreeActivity.class);
                    startActivity(stepThree);
                }
            });

        }

}
