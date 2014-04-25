package org.celllife.stockout.app.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import org.celllife.stockout.app.R;

public class StepThreeActivity extends Activity {
        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step_three_reg);

        Button proceedButton = (Button) findViewById(R.id.confirm_button);

            proceedButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent stepThree2 = new Intent (StepThreeActivity.this, StepThreeActivity2.class);
                    startActivity(stepThree2);
                }
            });

        }
}
