package org.celllife.stockout.app.fragments;

import java.util.Date;

import org.celllife.stockout.app.R;
import org.celllife.stockout.app.adapters.AlertListViewAdapter;
import org.celllife.stockout.app.domain.Alert;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class OrderFragment extends Fragment {
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View orderView = inflater.inflate(R.layout.order, container, false);
        setupOrder(orderView);
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
}
