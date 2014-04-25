package org.celllife.stockout.app.ui.activities;

import java.lang.Thread.UncaughtExceptionHandler;
import java.net.MalformedURLException;
import java.util.Calendar;
import java.util.Date;

import org.celllife.stockout.app.R;
import org.celllife.stockout.app.domain.Alert;
import org.celllife.stockout.app.domain.AlertStatus;
import org.celllife.stockout.app.domain.Drug;
import org.celllife.stockout.app.manager.AlertManager;
import org.celllife.stockout.app.manager.DatabaseManager;
import org.celllife.stockout.app.manager.ManagerFactory;
import org.celllife.stockout.app.ui.alarm.AlarmNotificationReceiver;
import org.celllife.stockout.app.ui.fragments.OrderFragment;
import org.celllife.stockout.app.ui.fragments.ReceivedFragment;
import org.celllife.stockout.app.ui.fragments.ScanFragment;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends Activity {


	private UncaughtExceptionHandler exceptionHandler = new StockApplicationCrashExceptionHandler();
	
	private ScanFragment scanFrag;
	
	private PendingIntent alertAlarmPendingIntent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Log.e("MainActivity", "onCreate");
		
		setContentView(R.layout.main);
		
        ManagerFactory.initialise(getApplicationContext());
		insertAlerts();
		setupPhone();

		final ActionBar tabBar = getActionBar();
		tabBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		String selectedTab = null;
		if (savedInstanceState != null) {
			selectedTab = savedInstanceState.getString("selectedTab");
		}

		OrderFragment orderFrag = new OrderFragment();
		tabBar.addTab(tabBar.newTab().setText(R.string.orders).setTabListener(new ScanTabListener(orderFrag)));

		ReceivedFragment receivedFrag = new ReceivedFragment();
		tabBar.addTab(tabBar.newTab().setText(R.string.received).setTabListener(new ScanTabListener(receivedFrag)));
		
		// restore previous selected tab
		if (OrderFragment.TYPE.equals(selectedTab)) {
			tabBar.setSelectedNavigationItem(0);
		} else if (ReceivedFragment.TYPE.equals(selectedTab)) {
			tabBar.setSelectedNavigationItem(1);
		}
		
		// restore previously set alarm, and set if it isn't known
		if (savedInstanceState != null) {
			alertAlarmPendingIntent = savedInstanceState.getParcelable("alertAlarmPendingIntent");
		}
		startAlertAlarm();

		// setup exception handling
		Thread.setDefaultUncaughtExceptionHandler(exceptionHandler);
    }

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
	  super.onSaveInstanceState(savedInstanceState);
	  // adds variables which are saved before an Activity is restarted 
	  savedInstanceState.putString("selectedTab", scanFrag.getType());
	  savedInstanceState.putParcelable("alertAlarmPendingIntent", alertAlarmPendingIntent);
	}

    private void startAlertAlarm() {
    	Toast.makeText(this, "method: startAlertAlarm()", Toast.LENGTH_LONG).show();
    	if (alertAlarmPendingIntent != null) {
	        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
	        
	        Intent alertIntent = new Intent(MainActivity.this, AlarmNotificationReceiver.class);
	        alertAlarmPendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, alertIntent, 
	        		PendingIntent.FLAG_CANCEL_CURRENT);
	
	        // FIXME: we need to think of a clever way to stagger the requests so they don't all try access the server at once
	        // See: http://developer.android.com/training/scheduling/alarms.html - Best practices section
	        Calendar calendar = Calendar.getInstance();
	        calendar.setTimeInMillis(System.currentTimeMillis());
	        calendar.set(Calendar.HOUR_OF_DAY, 9);
	
	        Toast.makeText(this, "scheduling", Toast.LENGTH_LONG).show();
	        
	        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 5*60*1000, alertAlarmPendingIntent);
	        
	        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY,
	        		alertAlarmPendingIntent);
    	}
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
			Log.i("ScanTabListener", "onTabReselected "+mFragment+" tab="+tab+" ft="+ft);
			MainActivity.this.scanFrag = (ScanFragment)mFragment;
		}

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			Log.i("ScanTabListener", "onTabSelected "+mFragment+" tab="+tab+" ft="+ft);
			if (mFragment !=  null) {
				ft.replace(R.id.main_tabs, mFragment);
			}
			MainActivity.this.scanFrag = (ScanFragment)mFragment;
		}

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			Log.i("ScanTabListener", "onTabUnselected "+mFragment+" tab="+tab+" ft="+ft);
			if (mFragment !=  null)
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
			return true;
		case R.id.action_send:
			Toast.makeText(this, R.string.sending, Toast.LENGTH_LONG).show();
			ManagerFactory.getStockTakeManager().synch();
			ManagerFactory.getAlertManager().updateAlerts();
			scanFrag.refresh(scanFrag.getView());
			return true;
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
    	Log.i("MainActivity","onActivityResult resultCode="+resultCode+" requestCode="+requestCode+" scanFrag="+scanFrag.getClass());
    	scanFrag.onActivityResult(requestCode, resultCode, intent);
    }
}
