package org.celllife.stockout.app.ui.fragments;

import java.util.List;

import org.celllife.stockout.app.R;
import org.celllife.stockout.app.domain.StockReceived;
import org.celllife.stockout.app.manager.ManagerFactory;
import org.celllife.stockout.app.manager.StockTakeManager;
import org.celllife.stockout.app.ui.adapters.StockReceivedListViewAdapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class ReceivedFragment extends ScanFragment {

	View receivedView = null;

	public static final String TYPE = "StockReceived";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        receivedView = inflater.inflate(R.layout.received, container, false);
        refresh(receivedView); // Setup the view
        setupScanButton(receivedView);
        
        return receivedView;
    }

	@Override
	public void refresh(View view) {
		setupStock(view);
	}

	@Override
	public String getType() {
		return TYPE;
	}

	private void setupStock(View stockView) {
	    ListView listview = (ListView) stockView.findViewById(R.id.stock_received_list);
	    LinearLayout linearLayout = (LinearLayout) stockView.findViewById(R.id.stock_linear_layout);
	    TextView messageView = (TextView) stockView.findViewById(R.id.received_stock_message);
	    StockTakeManager stockManager = ManagerFactory.getStockTakeManager();
	    List<StockReceived> values = stockManager.getLatestStockReceived();
	    if (values != null && values.size() > 0) {
		    StockReceived[] stocks = values.toArray(new StockReceived[values.size()]);
		    final StockReceivedListViewAdapter adapter = new StockReceivedListViewAdapter(stockView.getContext(), R.id.stock_received_list, stocks);
		    listview.setAdapter(adapter);
		    linearLayout.setVisibility(View.VISIBLE);
		    messageView.setVisibility(View.INVISIBLE);
	    } else {
		    linearLayout.setVisibility(View.INVISIBLE);
		    messageView.setVisibility(View.VISIBLE);
	    }
	}

}
