/**
 * SMS manager singleton class.
 */

package matze.gps.icu.control;

import matze.gps.icu.MainActivity;
import matze.gps.icu.R;
import matze.gps.icu.model.ICUSMS;
import matze.gps.icu.model.Requests;

import org.osmdroid.util.GeoPoint;

import android.widget.TextView;

public class ICUSMSManager {

	private static ICUSMSManager instance;
	private MainActivity mainActivity;

	GPSLocationManager gpsLocationListener;
	BatteryReveiver batteryReveiver;
	
	public void setMainActivity(MainActivity mainActivity) {
		this.mainActivity = mainActivity;
	}

	/**
	 * 
	 * @return
	 */
	public static ICUSMSManager getInstance() {
		if (null == instance)
			instance = new ICUSMSManager();
		return instance;
	}

	/**
	 * Callback from SMSReiver
	 * 
	 * @param receivedSMS
	 */
	public void SMSReceived(ICUSMS receivedSMS) {
		TextView tv = ((TextView) mainActivity.findViewById(R.id.textViewRemoteCoord));

		if (null == receivedSMS.getRequest()) {
			tv.setText("invalid message");
			return;
		}

		String message = "";
		
		// Todo: Location und battery nicht hier setzen. 

		switch (receivedSMS.getRequest()) {
		// Respond to request
		case Requests.LOCATION_REQUEST:
			GeoPoint location = gpsLocationListener.getLastIPosition();
			String battery = gpsLocationListener.getBattery();
			ICUSMS response = new ICUSMS(receivedSMS.getReceivedFrom(), Requests.LOCATION, location, battery);
			message = receivedSMS.getRequest();

			if (PhoneNumberManager.getInstance().getAuthorizedNumbers().contains(receivedSMS.getReceivedFrom()) || mainActivity.isDebug())
				response.send();
			break;
		// Received response
		case Requests.LOCATION:
			GeoPoint loc = receivedSMS.getLocation();
			message = receivedSMS.getRequest() + "\nlong " + loc.getLongitude() + "\nlati " + loc.getLatitude();
			gpsLocationListener.addUPosition(loc);
			gpsLocationListener.setUBattery(receivedSMS.getBattery());
			break;
		default:
			break;
		}
		if (null != tv)
			tv.setText(message);
	}

	public void setGpsLocationListener(GPSLocationManager gpsLocationListener) {
		this.gpsLocationListener = gpsLocationListener;
	}
	
	public void setBatteryReveiver(BatteryReveiver batteryReveiver) {
		this.batteryReveiver = batteryReveiver;
	}
}
