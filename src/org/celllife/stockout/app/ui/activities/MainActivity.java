package org.celllife.stockout.app.ui.activities;

import java.lang.Thread.UncaughtExceptionHandler;
import java.net.MalformedURLException;
import java.util.Date;

import org.celllife.stockout.app.R;
import org.celllife.stockout.app.domain.Alert;
import org.celllife.stockout.app.domain.AlertStatus;
import org.celllife.stockout.app.domain.Drug;
import org.celllife.stockout.app.manager.AlertManager;
import org.celllife.stockout.app.manager.DatabaseManager;
import org.celllife.stockout.app.manager.ManagerFactory;
import org.celllife.stockout.app.ui.fragments.OrderFragment;
import org.celllife.stockout.app.ui.fragments.ReceivedFragment;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends Activity {

	private UncaughtExceptionHandler exceptionHandler = new StockApplicationCrashExceptionHandler();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		ManagerFactory.initialise(getApplicationContext());
		insertAlerts();
		setupPhone();

		final ActionBar tabBar = getActionBar();
		tabBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		OrderFragment orderFrag = new OrderFragment();
		tabBar.addTab(tabBar.newTab().setText(R.string.orders).setTabListener(new ATabListener(orderFrag)));

		ReceivedFragment receivedFrag = new ReceivedFragment();
		tabBar.addTab(tabBar.newTab().setText(R.string.received).setTabListener(new ATabListener(receivedFrag)));

		// setup exception handling
		Thread.setDefaultUncaughtExceptionHandler(exceptionHandler);
	}

	// FIXME: remove this once we have alerts coming from the server
	private void insertAlerts() {
		AlertManager alertManager = ManagerFactory.getAlertManager();
		Drug panado = DatabaseManager.getDrugTableAdapter().findByBarcode("60011053");
		Alert panadoAlert = new Alert(new Date(), 1, panado.getDescription(), AlertStatus.NEW, panado);
		alertManager.addAlert(panadoAlert);
		Drug grandpa = DatabaseManager.getDrugTableAdapter().findByBarcode("60015204");
		Alert grandpaAlert = new Alert(new Date(), 3, grandpa.getDescription(), AlertStatus.NEW, grandpa);
		alertManager.addAlert(grandpaAlert);
	}

	// FIXME: remove this once we have the setup wizard
	private void setupPhone() {
		try {
			ManagerFactory.getSettingManager().setServerBaseUrl("http://sol.cell-life.org/stock");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	// FIXME: testing authentication
	/*private void testAuthentication() {
		Toast.makeText(MainActivity.this, "authenticating...", Toast.LENGTH_LONG).show(); 
		boolean authentication = ManagerFactory.getAuthenticationManager().authenticate("27768198075", "1234");
		Toast.makeText(MainActivity.this, "authentication="+authentication, Toast.LENGTH_LONG).show();
	}*/

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
			Toast.makeText(this, R.string.hello, Toast.LENGTH_LONG).show();
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Default handling of exceptions
	 */
	private class StockApplicationCrashExceptionHandler implements UncaughtExceptionHandler {
		@Override
		public void uncaughtException(Thread thread, Throwable ex) {
			Log.e("MainActivity", "Main Application exeption handler");
			try {
				Log.e("MainActivity", "Starting crashedIntent...");
				Intent crashedIntent = new Intent(MainActivity.this, CrashHandlerActivity.class);
				crashedIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				crashedIntent.putExtra("exception", ex);
				startActivity(crashedIntent);
			} catch (Throwable t) {
				Log.e("MainActivity", "Exception caught while handling exception: ", t);
			} finally {
				android.os.Process.killProcess(android.os.Process.myPid());
				System.exit(1);
			}
		}
	}
}
