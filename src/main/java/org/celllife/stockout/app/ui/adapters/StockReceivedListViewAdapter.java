package org.celllife.stockout.app.ui.adapters;

import org.celllife.stockout.app.R;
import org.celllife.stockout.app.domain.StockReceived;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class StockReceivedListViewAdapter extends ArrayAdapter<StockReceived> {
	
	private StockReceived[] values;

	public StockReceivedListViewAdapter(Context context, int resource, StockReceived[] values) {
		super(context, resource, values);
		this.values = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			LayoutInflater vi = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = vi.inflate(R.layout.stock_row, null);
		}

		ImageView imageView = (ImageView) view.findViewById(R.id.stock_icon);
		TextView textView = (TextView) view.findViewById(R.id.drug_name);
		TextView quantityView = (TextView) view.findViewById(R.id.drug_amount);

		StockReceived stock = values[position];
		if (stock != null) {
			textView.setText(stock.getDrug().getDescription());
			quantityView.setText(stock.getQuantity().toString());
			Boolean submitted = stock.isSubmitted();
			// change the icon if it hasn't been submitted
			if (submitted) {
				imageView.setImageResource(R.drawable.stocktake_sent);
			} else {
				imageView.setImageResource(R.drawable.stocktake_saved);
			}
		}
		return view;
	}
}