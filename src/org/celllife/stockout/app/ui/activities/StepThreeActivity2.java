package org.celllife.stockout.app.ui.activities;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import org.celllife.stockout.app.R;
import org.celllife.stockout.app.domain.Drug;
import org.celllife.stockout.app.manager.ManagerFactory;
import org.celllife.stockout.app.manager.SetupManager;

import java.lang.reflect.Array;
import java.util.List;

public class StepThreeActivity2 extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step_three_pt2);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Fragment scanFrag = getFragmentManager().findFragmentById(R.id.scan_fragment);
        Log.i("MainActivity", "onActivityResult resultCode=" + resultCode + " requestCode=" + requestCode + " scanFrag=" + scanFrag.getClass());
        scanFrag.onActivityResult(requestCode, resultCode, intent);
    }


}


