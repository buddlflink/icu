package matze.gps.icu;

import matze.gps.icu.control.BatteryReveiver;
import matze.gps.icu.control.GPSLocationManager;
import matze.gps.icu.control.ICUSMSManager;
import matze.gps.icu.control.PhoneNumberManager;
import matze.gps.icu.model.Requests;
import matze.gps.icu.model.Requests.OperatingMode;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

	private OperatingMode operatingMode = Requests.OperatingMode.MONITOR;
	private boolean debug = true;
	// private final String PREFERENCES_AUT_NUMBERS = "ICU_AUTHORIZED_NUMBERS";
	// private final String PREFERENCES_TARGET_NUMBER = "ICU_REMOTE_NUMBER";

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this
	 * becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v13.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter sectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	ICUSMSManager smsManager;
	LocationManager locationManager;
	GPSLocationManager gpsLocationManager;
	BatteryReveiver batteryReveiver;
	static PhoneNumberManager phoneNumberManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		sectionsPagerAdapter = new SectionsPagerAdapter(this, getFragmentManager());
		

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(sectionsPagerAdapter);

		phoneNumberManager = new PhoneNumberManager();
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		gpsLocationManager = new GPSLocationManager(locationManager);
		gpsLocationManager.setMainActivity(this);
		// locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
		// 0, 0, gpsLocationListener);
		batteryReveiver = new BatteryReveiver(gpsLocationManager);

		locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 1000, 1, gpsLocationManager);

		registerReceiver(batteryReveiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

		smsManager = ICUSMSManager.getInstance();
		smsManager.setMainActivity(this);
		smsManager.setGpsLocationListener(gpsLocationManager);
		smsManager.setBatteryReveiver(batteryReveiver);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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

	public GPSLocationManager getGpsLocationListener() {
		return gpsLocationManager;
	}

	public static PhoneNumberManager getPhoneNumberManager() {
		return phoneNumberManager;
	}

	public boolean isDebug() {
		return debug;
	}

	public OperatingMode getOperatingMode() {
		return operatingMode;
	}

}
