package matze.gps.icu.monitor;

import java.util.ArrayList;

import matze.gps.icu.MainActivity;
import matze.gps.icu.R;
import matze.gps.icu.control.PhoneNumberManager;
import matze.gps.icu.model.ICUSMS;
import matze.gps.icu.model.Observed;

import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.ResourceProxy;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

/**
 * A placeholder fragment containing a simple view.
 */
public class MapFragment extends Fragment {

	private MapController mapController;
	private MapView mapView;

	static MapFragment fragment;
	PhoneNumberManager phoneNumberManager;

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

		if (null == phoneNumberManager) {
			phoneNumberManager = ((MainActivity) getActivity()).getPhoneNumberManager();
		}

		mapView = (MapView) rootView.findViewById(R.id.map);
		mapView.setTileSource(TileSourceFactory.MAPNIK);
		mapView.setBuiltInZoomControls(true);
		mapView.setMultiTouchControls(true);
		mapController = (MapController) mapView.getController();
		mapController.setZoom(16);
		// GeoPoint pos = gpsLocationListener.getLastIPosition();

		// mapController.setCenter(pos);

		resourceProxy = new DefaultResourceProxyImpl(((MainActivity) getActivity()).getApplicationContext());

		// iDrawable =
		// getActivity().getResources().getDrawable(R.drawable.marker_i);
		// iDrawable.setBounds(0, 0, iDrawable.getIntrinsicWidth(),
		// iDrawable.getIntrinsicHeight());

		ToggleButton buttonhideMe = (ToggleButton) rootView.findViewById(R.id.toggleButtonHideMe);
		buttonhideMe.setChecked(true);

		buttonhideMe.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				phoneNumberManager.getMe().setDraw(((ToggleButton) view).isChecked());
				drawPositions();
			}
		});

		Button buttonRequestLocationMap = ((Button) rootView.findViewById(R.id.buttonRequestLocationMap));
		buttonRequestLocationMap.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// StringBuilder sb = new
				// StringBuilder(textViewNumber.getText());

				if (null != phoneNumberManager.getMonitorNumber()) {
					ICUSMS icuSMS = new ICUSMS(phoneNumberManager.getMonitorNumber(), getString(R.string.LOCATION_REQUEST), null, "");

					icuSMS.send();
				}
			}
		});

		drawPositions();

		return rootView;
	}

	public void drawPositions() {

		mapView.getOverlays().clear();

		for (Observed o : phoneNumberManager.getAllObserved()) {
			if (null != o.getPosition() && o.isDraw()) {
				mapView.getController().setCenter(o.getPosition());
			}

		}
		Observed me = phoneNumberManager.getMe();

		// hier weiter
		if (me.getPosition() != null && me.isDraw()) {

			// iPositionItem = new OverlayItem(iPositionItem.getTitle(),
			// iPositionItem.getSnippet(), iPosition);
			// iPositionItem.setMarker(iDrawable);
			// items.add(iPositionItem);
		}

		// Draw my own position
		// if (iPosition != null && drawme) {

		// iPositionItem = new OverlayItem(iPositionItem.getTitle(),
		// iPositionItem.getSnippet(), iPosition);
		// iPositionItem.setMarker(iDrawable);
		// items.add(iPositionItem);
		// }

		// Draw remote position
		// if (uPosition != null) {
		// mapView.getController().setCenter(uPosition);
		//
		// itemUPosition = new OverlayItem(itemUPosition.getTitle(),
		// itemUPosition.getSnippet(), uPosition);
		// itemUPosition.setMarker(uDrawable);
		// items.add(itemUPosition);

		// Drawable markerU =
		// getResources().getDrawable(R.drawable.markerU);
		// icon1.setBounds(0, 0, icon1.getIntrinsicWidth(),
		// icon1.getIntrinsicHeight());
		// OverlayItem item1 = new OverlayItem(new Point(48858290, 2294450),
		// "Tour Eiffel", "La tour Eiffel");
		// OverlayItem item2 = new OverlayItem(new Point(48873830, 2294800),
		// "Arc de Triomphe", "L'arc de triomphe");
		// item1.setMarker(icon1);
		// item2.setMarker(icon2);

		// }

		myLocationOverlay = new ItemizedIconOverlay<OverlayItem>(items, new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
			@Override
			public boolean onItemSingleTapUp(final int index, final OverlayItem item) {

				for (Observed o : phoneNumberManager.getAllObserved()) {

					if (o.getiPositionItem() == item) {
						Toast.makeText(getActivity().getApplicationContext(), "Bat: " + o.getBattery() + " %", Toast.LENGTH_SHORT).show();
					}
				}

				return true; // We 'handled' this event.
			}

			@Override
			public boolean onItemLongPress(final int index, final OverlayItem item) {
				// Toast.makeText(DemoMap.this, "Item '" + item.mTitle,
				// Toast.LENGTH_LONG).show();
				return false;
			}
		}, (ResourceProxy) resourceProxy);
		mapView.getOverlays().add(this.myLocationOverlay);
		mapView.invalidate();

	}

	public ArrayList<OverlayItem> getItems() {
		return items;
	}

}