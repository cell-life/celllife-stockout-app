package org.celllife.stockout.app.ui.fragments;

import java.util.List;

import org.celllife.stockout.app.R;
import org.celllife.stockout.app.domain.Alert;
import org.celllife.stockout.app.domain.StockTake;
import org.celllife.stockout.app.manager.AlertManager;
import org.celllife.stockout.app.manager.ManagerFactory;
import org.celllife.stockout.app.manager.SessionManager;
import org.celllife.stockout.app.manager.StockTakeManager;
import org.celllife.stockout.app.ui.activities.PinActivity;
import org.celllife.stockout.app.ui.activities.ScanActivity;
import org.celllife.stockout.app.ui.adapters.AlertListViewAdapter;
import org.celllife.stockout.app.ui.adapters.StockListViewAdapter;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This fragment manages the order tab view which contains a list of alerts and a list of stock takes done in
 * this session
 */
public class OrderFragment extends Fragment {
	
	private static final int SCAN_REQUEST_CODE = 23;
	private static final int PIN_REQUEST_CODE = 1;

	View orderView = null;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        orderView = inflater.inflate(R.layout.order, container, false);
        // setup the view
        setupAlert(orderView);
        setupStock(orderView);
        setupScanButton(orderView);

        return orderView;
    }

	private void setupAlert(View orderView) {
		// setup the list
	    final ListView listview = (ListView) orderView.findViewById(R.id.drug_alert_list);
	    AlertManager alertManager = ManagerFactory.getAlertManager();
	    List<Alert> values = alertManager.getAlerts();
	    Alert[] alerts = values.toArray(new Alert[values.size()]);
	    final AlertListViewAdapter adapter = new AlertListViewAdapter(orderView.getContext(), R.id.drug_alert_list, alerts);
	    listview.setAdapter(adapter);
	    
	    // setup alert count
		final TextView alertCount = (TextView) orderView.findViewById(R.id.alert_count);
		final TextView newAlerts = (TextView) orderView.findViewById(R.id.new_alerts);
		alertCount.setText("("+alerts.length+") ");
		newAlerts.setText(R.string.new_alerts);
	}

	private void setupStock(View stockView) {
	    final ListView listview = (ListView) stockView.findViewById(R.id.stock_alert_list);	
	    StockTakeManager stockManager = ManagerFactory.getStockTakeManager();
	    List<StockTake> values = stockManager.getLatestStockTakes();
	    StockTake[] stocks = values.toArray(new StockTake[values.size()]);
	    final StockListViewAdapter adapter = new StockListViewAdapter(stockView.getContext(), R.id.stock_alert_list, stocks);
	    listview.setAdapter(adapter);
	}
	
	private void setupScanButton(final View orderView) {
        Button startScanBtn = (Button) orderView.findViewById (R.id.scan_button);
        startScanBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startScanActivity();
            }
        });
	}

    private void startScanActivity() {
        SessionManager sessionManager = ManagerFactory.getSessionManager();
        if (sessionManager.isSessionExpired()) {
            Intent intent = new Intent(orderView.getContext(), PinActivity.class);
            startActivityForResult(intent, PIN_REQUEST_CODE);
        }
        else {
        	Toast.makeText(orderView.getContext(), R.string.loading_scanner, Toast.LENGTH_LONG).show();
            Intent intent = new Intent(orderView.getContext(), ScanActivity.class);
            startActivityForResult(intent, SCAN_REQUEST_CODE);
        }

    }
    
    @Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    	if (requestCode == SCAN_REQUEST_CODE) {
	        setupAlert(orderView);
	        setupStock(orderView);
    	}
    	if (requestCode == PIN_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Intent scanIntent = new Intent(orderView.getContext(), ScanActivity.class);
            startActivityForResult(scanIntent, SCAN_REQUEST_CODE);
    	}
    }
}
