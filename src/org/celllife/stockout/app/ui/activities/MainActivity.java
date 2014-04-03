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
import org.celllife.stockout.app.ui.alarm.AlarmActivity;
import org.celllife.stockout.app.ui.fragments.OrderFragment;
import org.celllife.stockout.app.ui.fragments.ReceivedFragment;
import org.celllife.stockout.app.ui.fragments.ScanFragment;

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

    private AlarmActivity alarm;

	private UncaughtExceptionHandler exceptionHandler = new StockApplicationCrashExceptionHandler();
	
	ScanFragment scanFrag;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Log.e("MainActivity", "onCreate");
		
		setContentView(R.layout.main);

        alarm = new AlarmActivity();

		ManagerFactory.initialise(getApplicationContext());
		insertAlerts();
		setupPhone();

		final ActionBar tabBar = getActionBar();
		tabBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		OrderFragment orderFrag = new OrderFragment();
		tabBar.addTab(tabBar.newTab().setText(R.string.orders).setTabListener(new ScanTabListener(orderFrag)));

		ReceivedFragment receivedFrag = new ReceivedFragment();
		tabBar.addTab(tabBar.newTab().setText(R.string.received).setTabListener(new ScanTabListener(receivedFrag)));


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

	// FIXME: this is used to test send all functionality
	/*private void insertStockTakes() {
		Drug panado = DatabaseManager.getDrugTableAdapter().findByBarcode("60011053");
		StockTake st1 = new StockTake(new Date(), panado, 10, false);
		DatabaseManager.getStockTakeTableAdapter().insert(st1);
		Drug grandpa = DatabaseManager.getDrugTableAdapter().findByBarcode("60015204");
		StockTake st2 = new StockTake(new Date(), grandpa, 13, false);
		DatabaseManager.getStockTakeTableAdapter().insert(st2);
	}*/

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
	public class ScanTabListener implements ActionBar.TabListener {
		private final Fragment mFragment;

		public ScanTabListener(Fragment fragment) {
			this.mFragment = fragment;
		}

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
		}

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			if (mFragment !=  null) {
				ft.replace(R.id.main_tabs, mFragment);
			}
			MainActivity.this.scanFrag = (ScanFragment)mFragment;
		}

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			if (mFragment !=  null)
				ft.remove(mFragment);
			MainActivity.this.scanFrag = null;
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
		case R.id.action_send:
			Toast.makeText(this, R.string.sending, Toast.LENGTH_LONG).show();
			ManagerFactory.getStockTakeManager().synch();
			ManagerFactory.getAlertManager().updateAlerts();
			scanFrag.refresh(scanFrag.getView());
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

    @Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    	Log.i("MainActivity","onActivityResult resultCode="+resultCode+" requestCode="+requestCode);
    	scanFrag.onActivityResult(requestCode, resultCode, intent);
    }
}
