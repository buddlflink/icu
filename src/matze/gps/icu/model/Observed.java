package matze.gps.icu.model;

import java.util.ArrayList;

import matze.gps.icu.MainActivity;
import matze.gps.icu.R;
import matze.gps.icu.monitor.MapFragment;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.OverlayItem;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class Observed extends BroadcastReceiver implements LocationListener {

	private String battery = "";
	private GeoPoint position;
	private boolean me = false;
	private ArrayList<GeoPoint> points;
	private boolean draw = true;
	private OverlayItem overlay;
	private char icon;
	private Drawable drawable;
	private String number = "";

	// MainActivity mainActivity;

	public Observed(boolean me, char icon, String number) {
		this.me = me;
		this.icon = icon;
		this.number = number;

		points = new ArrayList<GeoPoint>();

		if (me) {
			drawable = MainActivity.getMainActivity().getResources().getDrawable(R.drawable.marker_i);
			if (null != MainActivity.getMainActivity().getSystemService(Context.LOCATION_SERVICE)
					&& null != ((LocationManager) MainActivity.getMainActivity().getSystemService(Context.LOCATION_SERVICE)).getLastKnownLocation(LocationManager.GPS_PROVIDER)) {
				position = new GeoPoint(((LocationManager) MainActivity.getMainActivity().getSystemService(Context.LOCATION_SERVICE)).getLastKnownLocation(LocationManager.GPS_PROVIDER));
				points.add(position);
			}
		}
		else
			drawable = MainActivity.getMainActivity().getResources().getDrawable(R.drawable.marker_u);

		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

		

		TextView coords = ((TextView) MainActivity.getMainActivity().findViewById(R.id.textViewLocalCoord));
		if (null != coords)
			coords.setText("long " + position.getLongitude() + "\nlati " + position.getLatitude());

		if (null != MapFragment.getFragment())
			MapFragment.getFragment().drawPositions();

	}

	public String getBattery() {
		return battery;
	}

	public GeoPoint getPosition() {
		return position;
	}

	public void setBattery(String battery) {
		this.battery = battery;
	}

	public void setPosition(GeoPoint position) {
		this.position = position;
	}

	@Override
	public void onLocationChanged(Location loc) {

		if (null == drawable)
			if (me)
				drawable = MainActivity.getMainActivity().getResources().getDrawable(R.drawable.marker_i);
			else
				drawable = MainActivity.getMainActivity().getResources().getDrawable(R.drawable.marker_u);

		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
//		GeoPoint position = new GeoPoint(50.933464, 13.329889);

		if (null != loc)
			position = new GeoPoint(loc.getLatitude(), loc.getLongitude());

		points.add(position);
//		this.position = position;

		

		TextView coords = ((TextView) MainActivity.getMainActivity().findViewById(R.id.textViewLocalCoord));
		if (null != coords)
			coords.setText("long " + position.getLongitude() + "\nlati " + position.getLatitude());

		if (null != MapFragment.getFragment())
			MapFragment.getFragment().drawPositions();
	}

	@Override
	public void onProviderDisabled(String arg0) {
		Toast.makeText(MainActivity.getMainActivity().getApplicationContext(), "GPS Disabled", Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onProviderEnabled(String arg0) {
		Toast.makeText(MainActivity.getMainActivity().getApplicationContext(), "GPS Enabled", Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub

	}

	public boolean isDraw() {
		return draw;
	}

	public void setDraw(boolean draw) {
		this.draw = draw;
	}

	public void setOverlay(OverlayItem overlay) {
		this.overlay = overlay;
	}

	public OverlayItem getOverlay() {
		return overlay;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		int level = intent.getIntExtra("level", 0);
		battery = Integer.toString(level);

	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public boolean isMe() {
		return me;
	}

	public Drawable getDrawable() {
		return drawable;
	}

}
