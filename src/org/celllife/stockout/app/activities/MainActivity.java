package org.celllife.stockout.app.activities;

import java.util.Date;

import org.celllife.stockout.app.R;
import org.celllife.stockout.app.adapters.AlertListViewAdapter;
import org.celllife.stockout.app.domain.Alert;
import org.celllife.stockout.app.fragments.OrderFragment;
import org.celllife.stockout.app.fragments.ReceivedFragment;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.view.View;
import android.widget.ListView;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.widget.Toast;



public class MainActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		final ActionBar tabBar = getActionBar();
		tabBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		OrderFragment orderFrag = new OrderFragment();
		View orderView = findViewById(R.layout.order);
		tabBar.addTab(tabBar.newTab().setText(R.string.orders).setTabListener(new OrderListener(orderFrag, orderView)));

		ReceivedFragment receivedFrag = new ReceivedFragment();
		tabBar.addTab(tabBar.newTab().setText(R.string.received).setTabListener(new ReceivedListener(receivedFrag)));

        Button startScanBtn = (Button) findViewById (R.id.start_scan_activity);
        startScanBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                startScanActivity();
            }

        });
    }


	public static class OrderListener implements ActionBar.TabListener {
		private final Fragment mFragment;
		View view;

		public OrderListener(Fragment fragment, View view) {
			this.mFragment = fragment;
			this.view = view;
			//setupOrder();
		}

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
		}

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			if (null != mFragment) {
				ft.replace(R.id.main_tabs, mFragment);
			}
		}
		
		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			if (null != mFragment)
				ft.remove(mFragment);
		}

		private void setupOrder() {
			Log.w("MainActivity", "view="+view);
		    final ListView listview = (ListView) view.findViewById(R.id.drug_alert);		    
		    Alert[] values = new Alert[] {
		    		new Alert(new Date(), 3, "Panado", null, null, null),
		    		new Alert(new Date(), 3, "Disprin", null, null, null),
		    		new Alert(new Date(), 2, "Grandpa", null, null, null),
		    		new Alert(new Date(), 2, "Myprodol", null, null, null),
		    		new Alert(new Date(), 1, "Allergex", null, null, null),
		    		new Alert(new Date(), 1, "Texa 10", null, null, null)
		    };
		    final AlertListViewAdapter adapter = new AlertListViewAdapter(view.getContext(), R.id.drug_alert, values);
		    listview.setAdapter(adapter);
		}
	}

	public static class ReceivedListener implements ActionBar.TabListener {
		private final Fragment mFragment;

		public ReceivedListener(Fragment fragment) {
			mFragment = fragment;
		}

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
		}

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			if (null != mFragment) {
				ft.replace(R.id.main_tabs, mFragment);
			}
		}
		
		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			if (null != mFragment)
				ft.remove(mFragment);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_settings:
				Toast.makeText(this,  R.string.hello , Toast.LENGTH_LONG).show();
			default:
				return super.onOptionsItemSelected(item);
		}
	}

    private void startScanActivity() {

        Intent intent = new Intent(this,ScanActivity.class);
        startActivity(intent);
    }
}

