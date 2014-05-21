package org.celllife.stockout.app.ui.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.celllife.stockout.app.R;
import org.celllife.stockout.app.database.PhoneTableAdapter;
import org.celllife.stockout.app.database.framework.DatabaseOpenHelper;
import org.celllife.stockout.app.domain.Phone;
import org.celllife.stockout.app.manager.DatabaseManager;
import org.celllife.stockout.app.manager.ManagerFactory;


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
                    EditText leadtime = (EditText) findViewById(R.id.lead_text);
                    EditText safetytime = (EditText) findViewById(R.id.safety_text);
                    //EditText operatingdays = (EditText) findViewById(R.id.operating_text);

                    String leadTimeText = leadtime.getText().toString();
                    String safetyTimeText = safetytime.getText().toString();
                    //String operatingTimeText = operatingdays.getText().toString();

                    Integer leadTime = null;
                    Integer safetyTime = null;
                    //Integer operatingTime = null;

                    try {
                        leadTime = Integer.getInteger(leadTimeText);
                        safetyTime = Integer.getInteger(safetyTimeText);
                        //Fixme Not saving Operating time yet



                        Intent stepThree = new Intent (StepTwoActivity.this, StepThreeActivity.class);
                        startActivity(stepThree);
                    } catch (NumberFormatException e) {
                        displayErrorMessage();
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

    private void displayErrorMessage() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.number_error)
                .setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }

}
