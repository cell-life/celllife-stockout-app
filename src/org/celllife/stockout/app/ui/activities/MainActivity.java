package org.celllife.stockout.app.ui.activities;

import java.lang.Thread.UncaughtExceptionHandler;
import java.net.MalformedURLException;
import java.util.Calendar;

import org.celllife.stockout.app.R;
import org.celllife.stockout.app.manager.ManagerFactory;
import org.celllife.stockout.app.manager.SetupManager;
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
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends Activity {

	private UncaughtExceptionHandler exceptionHandler = new StockApplicationCrashExceptionHandler();
	
	private ScanFragment scanFrag;

	private PendingIntent alertAlarmPendingIntent;
	public static BroadcastReceiver receiver = new AlarmNotificationReceiver();
	public static LocalBroadcastManager broadcast;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Log.e("MainActivity", "onCreate");
		
		setContentView(R.layout.main);
		
		
        ManagerFactory.initialise(getApplicationContext());
		//setupManager();
		setupPhone();
		startAlertAlarm();

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

    private void setupManager() {
        SetupManager setupManager = ManagerFactory.getSetupManager();
        if (!setupManager.isInitialised()) {
            Intent intent = new Intent (MainActivity.this, SetupActivity.class);
            startActivity(intent);
        }

        else {
            Toast.makeText(this, "Already Setup", Toast.LENGTH_LONG).show();
        }

    }

   private void startAlertAlarm() {
    	if (alertAlarmPendingIntent == null) {
	        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
	        
	        Intent alertIntent = new Intent(MainActivity.this, AlarmNotificationReceiver.class);
	        alertAlarmPendingIntent = PendingIntent.getBroadcast(MainActivity.this, 1, alertIntent, PendingIntent.FLAG_UPDATE_CURRENT );
	
	        // FIXME: we need to think of a clever way to stagger the requests so they don't all try access the server at once
	        // See: http://developer.android.com/training/scheduling/alarms.html - Best practices section
	        Calendar calendar = Calendar.getInstance();
	        calendar.setTimeInMillis(System.currentTimeMillis());
//	        calendar.set(Calendar.HOUR_OF_DAY, 14);
//	        calendar.set(Calendar.MINUTE,20);
	        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1*60*1000, alertAlarmPendingIntent);
	        //alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_FIFTEEN_MINUTES,
	        //alertAlarmPendingIntent);
  
    	}
    }
    
	// FIXME: remove this once we have the setup wizard
	private void setupPhone() {
		try {
			ManagerFactory.getSettingManager().setServerBaseUrl("http://sol.cell-life.org/stock");
			ManagerFactory.getSessionManager().authenticated("27741486362", "1117");
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
		case R.id.action_sync:
			Toast.makeText(this, R.string.syncing, Toast.LENGTH_LONG).show(); 
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
    
    protected void onResume(){
    	super.onResume();
    	scanFrag.refresh(scanFrag.getView());
    }
    
    
    
    
    
}
