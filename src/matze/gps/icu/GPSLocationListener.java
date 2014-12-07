package matze.gps.icu;

import java.util.Vector;

import matze.gps.icu.model.ICULocation;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class GPSLocationListener implements LocationListener {


	Vector<ICULocation> locations;
	ICULocation lastValidLocation;
	private MainActivity mainActivity;
	
	
	public GPSLocationListener(){
		locations = new Vector<>();
		lastValidLocation = new ICULocation();
	}
	
	/**
	 * 
	 * @param mainActivity
	 */
	public void setMainActivity(MainActivity mainActivity) {
		this.mainActivity = mainActivity;
	}
	
	@Override
	public void onLocationChanged(Location loc)
	{
		//@todo: add validation of position
		lastValidLocation = new ICULocation(Double.toString(loc.getLatitude()), Double.toString(loc.getLongitude()));
		locations.add(lastValidLocation);
		
		((TextView) this.mainActivity.findViewById(R.id.labelLocation)).setText("long " + lastValidLocation.getLongitude() + "\nlati " + lastValidLocation.getLatitude());

	}

	/**
	 * Return the last known location
	 * @return
	 */
	public ICULocation getLastValidLocation(){
		
		return lastValidLocation;
	}
	
	@Override
	public void onProviderDisabled(String provider)

	{
		Toast.makeText(this.mainActivity.getApplicationContext(), "GPS Disabled",
		Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onProviderEnabled(String provider)

	{
		Toast.makeText(this.mainActivity.getApplicationContext(),
		"GPS Enabled", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras)
	{}

	
}