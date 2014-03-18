package org.celllife.stockout.app.activities;

import java.util.Date;

import org.celllife.stockout.app.R;
import org.celllife.stockout.app.domain.Alert;
import org.celllife.stockout.app.domain.AlertStatus;
import org.celllife.stockout.app.domain.Drug;
import org.celllife.stockout.app.fragments.OrderFragment;
import org.celllife.stockout.app.fragments.ReceivedFragment;
import org.celllife.stockout.app.manager.AlertManager;
import org.celllife.stockout.app.manager.AlertManagerImpl;
import org.celllife.stockout.app.manager.DatabaseManager;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;



public class MainActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		insertAlerts();

		final ActionBar tabBar = getActionBar();
		tabBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		OrderFragment orderFrag = new OrderFragment();
		tabBar.addTab(tabBar.newTab().setText(R.string.orders).setTabListener(new ATabListener(orderFrag)));

		ReceivedFragment receivedFrag = new ReceivedFragment();
		tabBar.addTab(tabBar.newTab().setText(R.string.received).setTabListener(new ATabListener(receivedFrag)));
    }

	// FIXME: remove this once we have alerts coming from the server
	private void insertAlerts() {
		AlertManager alertManager = new AlertManagerImpl(this.getApplicationContext());
		Drug panado = DatabaseManager.getDrugTableAdapter().findByBarcode("60011053");
		Alert panadoAlert = new Alert(new Date(), 3, panado.getDescription(), AlertStatus.NEW, panado);
		alertManager.addAlert(panadoAlert);
		Drug grandpa = DatabaseManager.getDrugTableAdapter().findByBarcode("60015204");
		Alert grandpaAlert = new Alert(new Date(), 1, grandpa.getDescription(), AlertStatus.NEW, grandpa);
		alertManager.addAlert(grandpaAlert);
	}

	/**
	 * Listener to handle tab switching
	 */
	public class ATabListener implements ActionBar.TabListener {
		private final Fragment mFragment;

		public ATabListener(Fragment fragment) {
			this.mFragment = fragment;
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
}

