package org.celllife.stockout.app.adapters;

import org.celllife.stockout.app.R;
import org.celllife.stockout.app.domain.StockTake;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class StockListViewAdapter extends ArrayAdapter<StockTake> {
	
	private StockTake[] values;

	public StockListViewAdapter(Context context, int resource, StockTake[] values) {
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

		StockTake stock = values[position];
		if (stock != null) {
			textView.setText(values[position].getDrug().getDescription());
			Boolean submitted = values[position].isSubmitted();
			// change the icon if it hasn't been submitted
			if (submitted) {
				imageView.setImageResource(R.drawable.alert_green);
			} else {
				imageView.setImageResource(R.drawable.alert_red);
			}
		}
		return view;
	}
}