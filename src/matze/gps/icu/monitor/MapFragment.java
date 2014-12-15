package matze.gps.icu.monitor;

import java.util.ArrayList;

import matze.gps.icu.MainActivity;
import matze.gps.icu.R;
import matze.gps.icu.control.GPSLocationManager;
import matze.gps.icu.control.PhoneNumberManager;
import matze.gps.icu.model.ICUSMS;
import matze.gps.icu.model.Requests;

import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.ResourceProxy;
import org.osmdroid.api.Marker;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

/**
 * A placeholder fragment containing a simple view.
 */
public class MapFragment extends Fragment {

	private MapController mapController;
	private MapView mapView;
	private GPSLocationManager gpsLocationListener;
	static MapFragment fragment;
	static OverlayItem iPositionItem;
	static OverlayItem itemUPosition;
	
	Drawable iDrawable;
	Drawable uDrawable;
	
	private boolean drawme = true;
	
	

	ItemizedIconOverlay<OverlayItem> myLocationOverlay;
	private Object resourceProxy;
	static ArrayList<OverlayItem> items;
	// ArrayItemizedOverlay myLocationOverlay;

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static MapFragment newInstance() {
		fragment = new MapFragment();
		items = new ArrayList<OverlayItem>();
		iPositionItem = new OverlayItem("I", "My position", null);
		itemUPosition = new OverlayItem("U", "Remote position", null);
		
		return fragment;
	}

	public static MapFragment getFragment() {
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_map, container, false);

		gpsLocationListener = ((MainActivity) getActivity()).getGpsLocationListener();
		
		mapView = (MapView) rootView.findViewById(R.id.map);
		mapView.setTileSource(TileSourceFactory.MAPNIK);
		mapView.setBuiltInZoomControls(true);
		mapView.setMultiTouchControls(true);
		mapController = (MapController) mapView.getController();
		mapController.setZoom(16);
		GeoPoint pos = gpsLocationListener.getLastIPosition();
		
		mapController.setCenter(pos);

		resourceProxy = new DefaultResourceProxyImpl(((MainActivity) getActivity()).getApplicationContext());
		

		uDrawable = getActivity().getResources().getDrawable(R.drawable.marker_u);
		uDrawable.setBounds(0, 0, uDrawable.getIntrinsicWidth(), uDrawable.getIntrinsicHeight());
		
		iDrawable = getActivity().getResources().getDrawable(R.drawable.marker_i);
		iDrawable.setBounds(0, 0, iDrawable.getIntrinsicWidth(), iDrawable.getIntrinsicHeight());
		
		
		ToggleButton buttonhideMe = (ToggleButton) rootView.findViewById(R.id.toggleButtonHideMe);
		buttonhideMe.setChecked(true);

		buttonhideMe.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				
				drawme = ((ToggleButton) view).isChecked();
				drawPositions();
			}
		});
		
		
		Button buttonRequestLocationMap = ((Button) rootView.findViewById(R.id.buttonRequestLocationMap));
		buttonRequestLocationMap.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// StringBuilder sb = new
				// StringBuilder(textViewNumber.getText());

				if (null != PhoneNumberManager.getInstance().getMonitorNumber()) {
					ICUSMS icuSMS = new ICUSMS(PhoneNumberManager.getInstance().getMonitorNumber(), getString(R.string.LOCATION_REQUEST), null, "");
					
					icuSMS.send();
				}
			}
		});
		
		
		drawPositions();

		return rootView;
	}

	public void drawPositions() {
		
		GeoPoint iPosition = gpsLocationListener.getLastIPosition();
		GeoPoint uPosition = gpsLocationListener.getLastUPosition();
		
		mapView.getOverlays().clear();
		items.clear();
		
//		 Draw my own position
		if (iPosition != null && drawme) {
			mapView.getController().setCenter(iPosition);
			iPositionItem = new OverlayItem(iPositionItem.getTitle(), iPositionItem.getSnippet(), iPosition);
			iPositionItem.setMarker(iDrawable);
			items.add(iPositionItem);
		}

		// Draw remote position
		if (uPosition != null) {
			mapView.getController().setCenter(uPosition);
			
			itemUPosition = new OverlayItem(itemUPosition.getTitle(), itemUPosition.getSnippet(), uPosition);
			itemUPosition.setMarker(uDrawable);
			items.add(itemUPosition);

//			Drawable markerU = getResources().getDrawable(R.drawable.markerU);
//			icon1.setBounds(0, 0, icon1.getIntrinsicWidth(), icon1.getIntrinsicHeight());
//			OverlayItem item1 = new OverlayItem(new Point(48858290, 2294450), 
//			    "Tour Eiffel", "La tour Eiffel");
//			OverlayItem item2 = new OverlayItem(new Point(48873830, 2294800), 
//			    "Arc de Triomphe", "L'arc de triomphe");                        
//			item1.setMarker(icon1);
//			item2.setMarker(icon2);
			
		}
		
		myLocationOverlay = new ItemizedIconOverlay<OverlayItem>(items, new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
			@Override
			public boolean onItemSingleTapUp(final int index, final OverlayItem item) {

				Toast.makeText(getActivity().getApplicationContext(), "Bat: " + gpsLocationListener.getBattery() + "%", Toast.LENGTH_SHORT).show();
				return true; // We 'handled' this event.
			}

			@Override
			public boolean onItemLongPress(final int index, final OverlayItem item) {
//				Toast.makeText(DemoMap.this, "Item '" + item.mTitle, Toast.LENGTH_LONG).show();
				return false;
			}
		}, (ResourceProxy) resourceProxy);
		mapView.getOverlays().add(this.myLocationOverlay);
		mapView.invalidate();
		
		
		
		
		
		
		
		
	}
}