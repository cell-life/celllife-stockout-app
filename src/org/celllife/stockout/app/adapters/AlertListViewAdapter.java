package org.celllife.stockout.app.adapters;

import org.celllife.stockout.app.R;
import org.celllife.stockout.app.domain.Alert;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AlertListViewAdapter extends ArrayAdapter<Alert> {
	
	private Alert[] values;

	public AlertListViewAdapter(Context context, int resource, Alert[] values) {
		super(context, resource, values);
		this.values = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			LayoutInflater vi = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = vi.inflate(R.layout.alert_row, null);
		}

		ImageView imageView = (ImageView) view.findViewById(R.id.alert_icon);
		TextView textView = (TextView) view.findViewById(R.id.alert_name);

		Alert alert = values[position];
		Log.w("AlertListViewAdapter", "postition="+position+" alert=" + alert);
		if (alert != null) {
			textView.setText(values[position].getMessage());
			Integer level = values[position].getLevel();
			// change the icon for the different alert
			switch (level) {
				case 1:
					imageView.setImageResource(R.drawable.alert_green);
					break;
				case 2:
					imageView.setImageResource(R.drawable.alert_yellow);
					break;
				case 3:
					imageView.setImageResource(R.drawable.alert_red);
					break;
			}
		}
		return view;
	}
}