package org.celllife.stockout.app.ui.fragments;

import org.celllife.stockout.app.R;
import org.celllife.stockout.app.ui.activities.ScanActivity;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class ReceivedFragment extends Fragment {

	View receivedView = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        receivedView = inflater.inflate(R.layout.received, container, false);
        // Setup the view
        setupScanButton(receivedView);
        
        return receivedView;
    }

	private void setupScanButton(final View receivedView) {
        Button startScanBtn = (Button) receivedView.findViewById (R.id.scan_button);
        startScanBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startScanActivity();
            }
        });
	}

    private void startScanActivity() {
        Intent intent = new Intent(receivedView.getContext(), ScanActivity.class);
        startActivity(intent);
    }
}
