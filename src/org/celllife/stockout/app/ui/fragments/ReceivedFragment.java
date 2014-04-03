package org.celllife.stockout.app.ui.fragments;

import org.celllife.stockout.app.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ReceivedFragment extends ScanFragment {

	View receivedView = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        receivedView = inflater.inflate(R.layout.received, container, false);
        // Setup the view
        setupScanButton(receivedView);
        
        return receivedView;
    }

	@Override
	public void refresh(View view) {
		// TODO Add the stock received here
		
	}

}
