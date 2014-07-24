package org.celllife.stockout.app.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.celllife.stockout.app.R;
import org.celllife.stockout.app.database.DrugTableAdapter;
import org.celllife.stockout.app.domain.Alert;
import org.celllife.stockout.app.domain.Drug;
import org.celllife.stockout.app.manager.DatabaseManager;

/**
 * Details Drug Information when Alert is clicked
 */
public class DrugDetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drug_detail);

        TextView drugName = (TextView) findViewById(R.id.drug_name);
        TextView barCode = (TextView) findViewById(R.id.code_no);
        TextView alertDate = (TextView) findViewById(R.id.alter_date_num);
        TextView alertLevel = (TextView) findViewById(R.id.alert_level_text);

        //getting attached intent data
        Alert alert = (Alert) getIntent().getSerializableExtra("Alert");

        //Display Details
        drugName.setText(alert.getDrug().getDescription());
        barCode.setText(alert.getDrug().getBarcode());
        alertDate.setText(alert.getDate().toString());
        alertLevel.setText(alert.getLevel().toString());

        final Button backButton = (Button) findViewById(R.id.cancel_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrugDetailActivity.this.finish();
            }
        });

    }
}

