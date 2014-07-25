package org.celllife.stockout.app.ui.fragments;

import java.util.List;

import org.celllife.stockout.app.R;
import org.celllife.stockout.app.domain.Drug;
import org.celllife.stockout.app.manager.DrugManager;
import org.celllife.stockout.app.manager.ManagerFactory;
import org.celllife.stockout.app.ui.adapters.DrugStockListAdapter;
import org.celllife.stockout.app.ui.activities.StockInfoActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * This fragment manages the stock tab view which contains a list of drugs and their estimated stock levels
 */
public class StockFragment extends ScanFragment {

    View stockView = null;

    //Allows scanning as order
    public static final String TYPE = "StockTake";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        stockView = inflater.inflate(R.layout.stock, container, false);
        refresh(stockView); // setup the view
        setupScanButton(stockView);

        return stockView;
    }

    public void refresh(View view) {
        setupDrugStockList(stockView);
    }

    public String getType() {
        return TYPE;
    }

    private void setupDrugStockList(View stockView) {
        final ListView list = (ListView) stockView.findViewById(R.id.drug_stock_list);
        DrugManager drugManager = ManagerFactory.getDrugManager();
        List<Drug> drugList = drugManager.getDrugs();
        Drug[] drugs = drugList.toArray(new Drug[drugList.size()]);
        final DrugStockListAdapter adapter = new DrugStockListAdapter(stockView.getContext(), R.id.drug_stock_list,
                drugs);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Drug d = adapter.getItem(position);
                // send drug name and barcode to information to StockInfoActivity
                Intent intent = new Intent(getActivity(), StockInfoActivity.class);
                intent.putExtra("Drug_barcode", d.getBarcode());
                intent.putExtra("Drug_name", d.getDescription());
                startActivity(intent);

            }
        });
    }

}
