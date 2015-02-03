package matze.gps.icu;

import matze.gps.icu.control.PhoneNumberManager;
import matze.gps.icu.control.SMSManager;
import matze.gps.icu.model.Observed;
import matze.gps.icu.model.Requests;
import matze.gps.icu.model.Requests.OperatingMode;
import matze.gps.icu.model.Settings;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

	private OperatingMode operatingMode = Requests.OperatingMode.MONITOR;

	// private final String PREFERENCES_AUT_NUMBERS = "ICU_AUTHORIZED_NUMBERS";
	// private final String PREFERENCES_TARGET_NUMBER = "ICU_REMOTE_NUMBER";

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this
	 * becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v13.app.FragmentStatePagerAdapter}.
	 */
	private SectionsPagerAdapter sectionsPagerAdapter;
	private static MainActivity mainActivity = null;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	private ViewPager mViewPager;
//	private SMSManager smsManager;
	private LocationManager locationManager;
	

	private PhoneNumberManager phoneNumberManager;
	private Settings settings;
	private Observed me;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		sectionsPagerAdapter = new SectionsPagerAdapter(this, getFragmentManager());
		phoneNumberManager = new PhoneNumberManager();
		
		settings = new Settings(this);

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(sectionsPagerAdapter);

		
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		me = new Observed(true, this, 'I');
		
		phoneNumberManager.setMe(me);
		
		// locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
		// 0, 0, gpsLocationListener);

		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, me);

		registerReceiver(me, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

		mainActivity = this;
		
		registerReceiver(new SMSManager(),new IntentFilter("RECEIVED SMS"));
		
		
		


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onStop() {

		settings.save();
//		unregisterReceiver(me);
		super.onStop();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public Settings getSettings() {
		return settings;
	}
	
	

	

	public PhoneNumberManager getPhoneNumberManager() {
		return phoneNumberManager;
	}

	public boolean isDebug() {
		return settings.isDebug();
	}

	public OperatingMode getOperatingMode() {
		return operatingMode;
	}

	public static MainActivity getMainActivity() {
		return mainActivity;
	}

}
