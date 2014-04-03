package org.celllife.stockout.app.ui.fragments;

import org.celllife.stockout.app.R;
import org.celllife.stockout.app.manager.ManagerFactory;
import org.celllife.stockout.app.manager.SessionManager;
import org.celllife.stockout.app.ui.activities.PinActivity;
import org.celllife.stockout.app.ui.activities.ScanActivity;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public abstract class ScanFragment extends Fragment  {

	public static final int SCAN_REQUEST_CODE = 23;
	public static final int PIN_REQUEST_CODE = 1;

	public void setupScanButton(View view) {
        Button startScanBtn = (Button) view.findViewById (R.id.scan_button);
        startScanBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startScanActivity(v);
            }
        });
	}
	
	public abstract void refresh(View view);

    private void startScanActivity(View view) {
        SessionManager sessionManager = ManagerFactory.getSessionManager();
        if (sessionManager.isSessionExpired()) {
            Intent intent = new Intent(view.getContext(), PinActivity.class);
            startActivityForResult(intent, PIN_REQUEST_CODE);
        }
        else {
        	runScanIntent();
        }

    }
    
    @Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    	Log.i("ScanFragment","onActivityResult resultCode="+resultCode+" requestCode="+requestCode);
    	if (requestCode == SCAN_REQUEST_CODE) {
	        refresh(this.getView());
    	}
    	if (requestCode == PIN_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
    		runScanIntent();
    	}
    	if (requestCode == IntentIntegrator.REQUEST_CODE) {
    		IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
    		if (scanningResult != null) {
    			Log.i("ScanFragment","onActivityResult scanningResult="+scanningResult.getContents());
    			Log.i("ScanFragment","onActivityResult this.Activity="+this.getActivity());
	    		Intent scanIntent = new Intent(this.getActivity(), ScanActivity.class);
	    		scanIntent.putExtra("barcode", scanningResult.getContents());
	    		scanIntent.putExtra("type", "StockTake");
	    		startActivityForResult(scanIntent, SCAN_REQUEST_CODE);
    		}
    	}
    }
    
    private void runScanIntent() {
    	Toast.makeText(this.getView().getContext(), R.string.loading_scanner, Toast.LENGTH_LONG).show();
    	Log.i("ScanFragment", "start scanning");

		IntentIntegrator scanIntegrator = new IntentIntegrator(this.getActivity());
		scanIntegrator.initiateScan();
    }
}
