package org.celllife.stockout.app.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.celllife.stockout.app.R;
import org.celllife.stockout.app.domain.Drug;
import org.celllife.stockout.app.manager.CalculationManager;
import org.celllife.stockout.app.manager.ManagerFactory;

public class DrugStockListAdapter extends ArrayAdapter<Drug> {
    private Drug[] values;
    
	public DrugStockListAdapter(Context context, int resource, Drug[] values) {
		super(context, resource, values);
		this.values = values;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			LayoutInflater vi = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = vi.inflate(R.layout.drug_list_row, null);
		}
		TextView textView = (TextView) view.findViewById(R.id.drug_name);
		TextView quantityView = (TextView) view.findViewById(R.id.drug_amount);

		Drug drug = values[position];
		if (drug != null) {
			CalculationManager calcManager = ManagerFactory.getCalculationManager();
			int estimatedStock = calcManager.getEstimatedStock(drug);
			textView.setText(drug.getDescription());
			int invalidEstimatedStock = Integer.MAX_VALUE;
			if(estimatedStock == invalidEstimatedStock){
				quantityView.setText(this.getContext().getString(R.string.no_stock_estimate));
			}
			else{
				quantityView.setText(Integer.toString(estimatedStock));
			}
		}
		return view;

	}
	
}
