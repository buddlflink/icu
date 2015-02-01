package matze.gps.icu.model;

import java.util.ArrayList;

import matze.gps.icu.MainActivity;
import matze.gps.icu.R;
import matze.gps.icu.monitor.MapFragment;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.OverlayItem.HotspotPlace;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class Observed extends BroadcastReceiver implements LocationListener {

	private String battery = "";
	private GeoPoint position;
	private boolean me = false;
	private ArrayList<GeoPoint> points;
	private boolean draw = true;
	private OverlayItem positionItem;
	private char icon;
	private Drawable drawable;
	private String number = "";

	MainActivity mainActivity;

	public Observed(boolean me, MainActivity mainActivity, char icon) {
		this.me = me;
		this.mainActivity = mainActivity;
		this.icon = icon;

		points = new ArrayList<GeoPoint>();
		positionItem = new OverlayItem(Character.toString(icon), "", null);

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

		if (me)
			drawable = mainActivity.getResources().getDrawable(R.drawable.marker_i);
		else
			drawable = mainActivity.getResources().getDrawable(R.drawable.marker_u);

		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
		GeoPoint position = new GeoPoint(50.933464, 13.329889);

		if (null != loc)
			position = new GeoPoint(loc.getLatitude(), loc.getLongitude());

		
		points.add(position);
		this.position = position;

		MapFragment.getFragment().getItems().remove(positionItem);

		positionItem = new OverlayItem(positionItem.getTitle(), positionItem.getSnippet(), position);

		positionItem.setMarker(drawable);

		MapFragment.getFragment().getItems().add(positionItem);

		TextView coords = ((TextView) this.mainActivity.findViewById(R.id.textViewLocalCoord));
		if (null != coords)
			coords.setText("long " + position.getLongitude() + "\nlati " + position.getLatitude());

		if (null != MapFragment.getFragment())
			MapFragment.getFragment().drawPositions();

	}

	@Override
	public void onProviderDisabled(String arg0) {
		Toast.makeText(this.mainActivity.getApplicationContext(), "GPS Disabled", Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onProviderEnabled(String arg0) {
		Toast.makeText(this.mainActivity.getApplicationContext(), "GPS Enabled", Toast.LENGTH_SHORT).show();

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

	public OverlayItem getiPositionItem() {
		return positionItem;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		int level = intent.getIntExtra("level", 0);
		battery= Integer.toString(level);
		
	}

	public String getNumber() {
		return number;
	}
	
	public void setNumber(String number) {
		this.number = number;
	}
	
}
