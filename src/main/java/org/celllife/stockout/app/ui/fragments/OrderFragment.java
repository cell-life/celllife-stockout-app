package org.celllife.stockout.app.ui.fragments;

import java.util.List;

import org.celllife.stockout.app.R;
import org.celllife.stockout.app.domain.Alert;
import org.celllife.stockout.app.domain.StockTake;
import org.celllife.stockout.app.manager.AlertManager;
import org.celllife.stockout.app.manager.ManagerFactory;
import org.celllife.stockout.app.manager.StockTakeManager;
import org.celllife.stockout.app.ui.activities.DrugDetailActivity;
import org.celllife.stockout.app.ui.adapters.AlertListViewAdapter;
import org.celllife.stockout.app.ui.adapters.StockListViewAdapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This fragment manages the order tab view which contains a list of alerts and a list of stock takes done in
 * this session
 */
public class OrderFragment extends ScanFragment {

	View orderView = null;
	
	public static final String TYPE = "StockTake"; 
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        orderView = inflater.inflate(R.layout.order, container, false);
        
        refresh(orderView); // setup the view
        setupScanButton(orderView);

        return orderView;
    }
    
    @Override
    public void refresh(View view) {
        setupAlert(orderView);
        setupStock(orderView);    	
    }

	@Override
	public String getType() {
		return TYPE;
	}

	private void setupAlert(View orderView) {
		// setup the list
	    final ListView listview = (ListView) orderView.findViewById(R.id.drug_alert_list);
	    AlertManager alertManager = ManagerFactory.getAlertManager();
	    List<Alert> values = alertManager.getAlerts();
	    final Alert[] alerts = values.toArray(new Alert[values.size()]);
	    final AlertListViewAdapter adapter = new AlertListViewAdapter(orderView.getContext(), R.id.drug_alert_list, alerts);
	    listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //selected item
            Alert a = alerts[position];

            //send data to new activity
            Intent drugDetail = new Intent(OrderFragment.this.getActivity().getApplicationContext(), DrugDetailActivity.class);
            drugDetail.putExtra("Alert",a);
            startActivity(drugDetail);
            }


        });
	    
	    // setup alert count
		final TextView alertCount = (TextView) orderView.findViewById(R.id.alert_count);
		final TextView newAlerts = (TextView) orderView.findViewById(R.id.new_alerts);
		alertCount.setText("("+alerts.length+") ");
		newAlerts.setText(R.string.new_alerts);



	}

	private void setupStock(View stockView) {
		// setup the list
	    final ListView listview = (ListView) stockView.findViewById(R.id.stock_alert_list);	
	    StockTakeManager stockManager = ManagerFactory.getStockTakeManager();
	    List<StockTake> values = stockManager.getLatestStockTakes();
	    StockTake[] stocks = values.toArray(new StockTake[values.size()]);
	    final StockListViewAdapter adapter = new StockListViewAdapter(stockView.getContext(), R.id.stock_alert_list, stocks);
	    listview.setAdapter(adapter);

	    // setup stock count
		final TextView stockCount = (TextView) orderView.findViewById(R.id.stock_count);
		final TextView completedStocks = (TextView) orderView.findViewById(R.id.completed_stocks);
		stockCount.setText("("+stocks.length+") ");
		completedStocks.setText(R.string.completed_stocks);
	}
}
