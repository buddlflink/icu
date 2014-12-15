package matze.gps.icu.control;

import java.util.Vector;

import matze.gps.icu.MainActivity;
import matze.gps.icu.R;
import matze.gps.icu.model.Requests.OperatingMode;
import matze.gps.icu.monitor.MapFragment;

import org.osmdroid.util.GeoPoint;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class GPSLocationManager implements LocationListener {

	Vector<GeoPoint> iPositions;
	Vector<GeoPoint> uPositions;
	GeoPoint iPosition;
	GeoPoint uPosition;
	String uBattery;
	private MainActivity mainActivity;
	LocationManager locationManager;

	public GPSLocationManager() {
		iPositions = new Vector<>();
		uPositions = new Vector<>();
		uBattery = new String();
	}

	public GPSLocationManager(LocationManager locationManager) {
		iPositions = new Vector<>();
		uPositions = new Vector<>();
		this.locationManager = locationManager;
		uBattery = new String();
	}

	/**
	 * 
	 * @param mainActivity
	 */
	public void setMainActivity(MainActivity mainActivity) {
		this.mainActivity = mainActivity;
	}

	@Override
	public void onLocationChanged(Location loc) {

		Log.i("debug", "loc " + loc.getLongitude());
		// @todo: add validation of position
		iPosition = new GeoPoint(loc.getLatitude(), loc.getLongitude());
		iPositions.add(iPosition);

		TextView coords = ((TextView) this.mainActivity.findViewById(R.id.textViewLocalCoord));
		if (null != coords)
			coords.setText("long " + iPosition.getLongitude() + "\nlati " + iPosition.getLatitude());

		if (null != MapFragment.getFragment() && (mainActivity.getOperatingMode() == OperatingMode.MONITOR || mainActivity.isDebug()))
			MapFragment.getFragment().drawPositions();
	}

	/**
	 * Return the last known location or null if no positions have been recorded
	 * yet.
	 * 
	 * @return
	 */
	public GeoPoint getLastIPosition() {

		if (0 < iPositions.size())
			return iPositions.lastElement();

		if (null != locationManager && null != locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER))
			return new GeoPoint(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));

		if (mainActivity.isDebug())
			return new GeoPoint(50.932626, 13.333345);
		return null;
	}

	/**
	 * Return the last known location or null if no positions have been recorded
	 * yet.
	 * 
	 * @return
	 */
	public GeoPoint getLastUPosition() {

		if (0 < uPositions.size())
			return uPositions.lastElement();

//		if (mainActivity.isDebug())
//			return new GeoPoint(50.93, 13.333);
		// return new GeoPoint(50.932626, 13.333345);
		return null;
	}

	public void addUPosition(GeoPoint loc) {
		uPositions.add(loc);
		uPosition = loc;
		// Draw
		if (null != MapFragment.getFragment())
			MapFragment.getFragment().drawPositions();
	}

	@Override
	public void onProviderDisabled(String provider)

	{
		Toast.makeText(this.mainActivity.getApplicationContext(), "GPS Disabled", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onProviderEnabled(String provider)

	{
		Toast.makeText(this.mainActivity.getApplicationContext(), "GPS Enabled", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	
	public void setBattery(String battery) {
		this.uBattery = battery;
	}
	
	public String getBattery() {
		return uBattery;
	}
}