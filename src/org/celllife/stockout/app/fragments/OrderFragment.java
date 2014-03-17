package org.celllife.stockout.app.fragments;

import java.util.List;

import org.celllife.stockout.app.R;
import org.celllife.stockout.app.activities.ScanActivity;
import org.celllife.stockout.app.adapters.AlertListViewAdapter;
import org.celllife.stockout.app.adapters.StockListViewAdapter;
import org.celllife.stockout.app.domain.Alert;
import org.celllife.stockout.app.domain.StockTake;
import org.celllife.stockout.app.manager.AlertManager;
import org.celllife.stockout.app.manager.AlertManagerImpl;

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
	    AlertManager alertManager = new AlertManagerImpl(orderView.getContext());
	    //insertAlerts(alertManager);
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
	
	/*private void insertAlerts(AlertManager alertManager) {
		Drug panado = new Drug("60011053", "Panado 500mg 24 tablets");
		DatabaseManager.getDrugTableAdapter().insert(panado);
		Log.w("OrderFragment","inserted panado: "+panado);
		panado = DatabaseManager.getDrugTableAdapter().findByBarcode("60011053");
		Log.w("OrderFragment","panado is .... "+panado);
		Alert panadoAlert = new Alert(new Date(), 3, "Panado", AlertStatus.NEW, panado);
		alertManager.addAlert(panadoAlert);
		Drug grandpa = new Drug("60015204", "Grandpa 24 tablets");
		DatabaseManager.getDrugTableAdapter().insert(grandpa);
		grandpa = DatabaseManager.getDrugTableAdapter().findByBarcode("60015204");
		Alert grandpaAlert = new Alert(new Date(), 3, "Grandpa", AlertStatus.NEW, grandpa);
		alertManager.addAlert(grandpaAlert);
	}*/

	private void setupStock(View stockView) {
	    final ListView listview = (ListView) stockView.findViewById(R.id.stock_alert_list);	
	    //StockTakeManager stockManager = new StockTakeManagerImpl(orderView.getContext());
	    //List<StockTake> values = stockManager.getLatestStockTakes();
	    //StockTake[] stocks = values.toArray(new StockTake[values.size()]);
	    StockTake[] stocks = new StockTake[0];
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
        startActivity(intent);
    }
}
