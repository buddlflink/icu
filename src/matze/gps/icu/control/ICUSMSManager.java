/**
 * SMS manager singleton class.
 */

package matze.gps.icu.control;

import matze.gps.icu.GPSLocationListener;
import matze.gps.icu.MainActivity;
import matze.gps.icu.R;
import matze.gps.icu.model.ICULocation;
import matze.gps.icu.model.ICUSMS;
import matze.gps.icu.model.Requests;
import android.widget.TextView;


public class ICUSMSManager {

	
	private static ICUSMSManager instance;
	private MainActivity mainActivity;
	
	GPSLocationListener gpsLocationListener;
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
	 * @param receivedSMS
	 */
	public void SMSReceived(ICUSMS receivedSMS) {
		TextView tv=((TextView) mainActivity.findViewById(R.id.textViewRemoteCoord));
		
		if(null == receivedSMS.getRequest()){
			tv.setText("invalid message");
			return;
		}
		
		String message = "";
		
		switch (receivedSMS.getRequest()) {
		// Respond to request
		case Requests.LOCATION_REQUEST:
			ICULocation location = gpsLocationListener.getLastValidLocation();
			ICUSMS response = new ICUSMS(PhoneNumberManager.getTargetPhonenumber(), Requests.LOCATION, location);
			message = receivedSMS.getRequest();
			response.send();
			break;
		// Received response
		case Requests.LOCATION:
			ICULocation loc = receivedSMS.getLocation();
			message = receivedSMS.getRequest() + "\nlong " + loc.getLongitude() + "\nlati " + loc.getLatitude();
			break;
		default:
			break;
		}
        tv.setText(message);
	}
	
	
	
	
	
	
	public void setGpsLocationListener(GPSLocationListener gpsLocationListener) {
		this.gpsLocationListener = gpsLocationListener;
	}
}
