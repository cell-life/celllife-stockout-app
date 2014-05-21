package org.celllife.stockout.app.ui.activities;

import org.celllife.stockout.app.R;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class StepThreeActivity2 extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.step_three_pt2);

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		Fragment scanFrag = getFragmentManager().findFragmentById(R.id.scan_fragment);
		Log.i("StepThreeActivity2", "onActivityResult resultCode=" + resultCode + " requestCode=" + requestCode
				+ " scanFrag=" + scanFrag.getClass());
		scanFrag.onActivityResult(requestCode, resultCode, intent);
	}

}
