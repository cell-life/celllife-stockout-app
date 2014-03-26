package org.celllife.stockout.app.ui.fragments;

import java.util.List;

import org.celllife.stockout.app.R;
import org.celllife.stockout.app.domain.Alert;
import org.celllife.stockout.app.domain.StockTake;
import org.celllife.stockout.app.manager.AlertManager;
import org.celllife.stockout.app.manager.ManagerFactory;
import org.celllife.stockout.app.manager.StockTakeManager;
import org.celllife.stockout.app.ui.activities.ScanActivity;
import org.celllife.stockout.app.ui.adapters.AlertListViewAdapter;
import org.celllife.stockout.app.ui.adapters.StockListViewAdapter;

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

/**
 * This fragment manages the order tab view which contains a list of alerts and a list of stock takes done in
 * this session
 */
public class OrderFragment extends Fragment {
	
	private static final int SCAN_REQUEST_CODE = 23;

	View orderView = null;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        orderView = inflater.inflate(R.layout.order, container, false);
        // setup the view
        setupOrder(orderView);
        setupStock(orderView);
        setupScanButton(orderView);

        return orderView;
    }

	private void setupOrder(View orderView) {
	    final ListView listview = (ListView) orderView.findViewById(R.id.drug_alert_list);
	    AlertManager alertManager = ManagerFactory.getAlertManager();
	    List<Alert> values = alertManager.getAlerts();
	    Alert[] alerts = values.toArray(new Alert[values.size()]);
	    /*Alert[] alerts = new Alert[] {
	    		new Alert(new Date(), 3, "Panado", null, null),
	    		new Alert(new Date(), 3, "Disprin", null, null),
	    		new Alert(new Date(), 2, "Grandpa", null, null),
	    		new Alert(new Date(), 2, "Myprodol", null, null),
	    		new Alert(new Date(), 1, "Allergex", null, null),
	    		new Alert(new Date(), 1, "Texa 10", null, null)
	    };*/
	    final AlertListViewAdapter adapter = new AlertListViewAdapter(orderView.getContext(), R.id.drug_alert_list, alerts);
	    listview.setAdapter(adapter);
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
            	Toast.makeText(orderView.getContext(), R.string.loading_scanner, Toast.LENGTH_LONG).show();
                startScanActivity();
            }
        });
	}

    private void startScanActivity() {
        Intent intent = new Intent(orderView.getContext(), ScanActivity.class);
        this.startActivityForResult(intent, SCAN_REQUEST_CODE);
    }
    
    @Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        setupOrder(orderView);
        setupStock(orderView);
    }
}
