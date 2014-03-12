package org.celllife.stockout.app.fragments;

import java.util.Date;

import org.celllife.stockout.app.R;
import org.celllife.stockout.app.activities.ScanActivity;
import org.celllife.stockout.app.adapters.AlertListViewAdapter;
import org.celllife.stockout.app.domain.Alert;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class OrderFragment extends Fragment {
	
	View orderView = null;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        orderView = inflater.inflate(R.layout.order, container, false);
        // setup the view
        setupOrder(orderView);
        setupScanButton(orderView);

        return orderView;
    }

	private void setupOrder(View orderView) {
	    final ListView listview = (ListView) orderView.findViewById(R.id.drug_alert_list);		    
	    Alert[] values = new Alert[] {
	    		new Alert(new Date(), 3, "Panado", null, null, null),
	    		new Alert(new Date(), 3, "Disprin", null, null, null),
	    		new Alert(new Date(), 2, "Grandpa", null, null, null),
	    		new Alert(new Date(), 2, "Myprodol", null, null, null),
	    		new Alert(new Date(), 1, "Allergex", null, null, null),
	    		new Alert(new Date(), 1, "Texa 10", null, null, null)
	    };
	    final AlertListViewAdapter adapter = new AlertListViewAdapter(orderView.getContext(), R.id.drug_alert_list, values);
	    listview.setAdapter(adapter);
	}
	
	private void setupScanButton(final View orderView) {
        Button startScanBtn = (Button) orderView.findViewById (R.id.scan_button);
        startScanBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	Toast.makeText(orderView.getContext(), R.string.loading_scanner, Toast.LENGTH_LONG).show();
                startScanActivity();
            }
        });
	}

    private void startScanActivity() {
        Intent intent = new Intent(orderView.getContext(), ScanActivity.class);
        startActivity(intent);
    }
}
