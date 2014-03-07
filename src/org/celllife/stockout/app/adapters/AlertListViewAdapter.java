package org.celllife.stockout.app.adapters;

import org.celllife.stockout.app.R;
import org.celllife.stockout.app.domain.Alert;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AlertListViewAdapter extends ArrayAdapter<Alert> {
  private final Context context;
  private final Alert[] values;

  public AlertListViewAdapter(Context context, int resource, Alert[] values) {
    super(context, resource, values);
    this.context = context;
    this.values = values;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    LayoutInflater inflater = (LayoutInflater) context
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View rowView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
    TextView textView = (TextView) rowView.findViewById(R.id.alert_heading);
    textView.setText(values[position].getMessage());
    // change the icon for the different alert
    Integer level = values[position].getLevel();
    switch (level) {
    	case 1:
    		// imageView.setImageResource(R.drawable.alert_green);
    	case 2:
    		// imageView.setImageResource(R.drawable.alert_yellow);
    	case 3:
    		// imageView.setImageResource(R.drawable.alert_red);
    }
    return rowView;
  }
}