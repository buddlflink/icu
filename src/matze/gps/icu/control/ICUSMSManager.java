package matze.gps.icu.control;

import matze.gps.icu.GPSLocationListener;
import matze.gps.icu.MainActivity;
import matze.gps.icu.R;
import matze.gps.icu.model.ICULocation;
import matze.gps.icu.model.ICUSMS;
import matze.gps.icu.model.Requests;
import android.util.Log;
import android.widget.TextView;


public class ICUSMSManager {

	
	private static ICUSMSManager instance;
	private MainActivity mainActivity;
	
	GPSLocationListener gpsLocationListener;
	public void setMainActivity(MainActivity mainActivity) {
		this.mainActivity = mainActivity;
	}
	
	

	public static ICUSMSManager getInstance() {

		if (null == instance)
			instance = new ICUSMSManager();
		return instance;
	}

	

	public void SMSReceived(ICUSMS receivedSMS) {
		TextView tv=((TextView) mainActivity.findViewById(R.id.labelReceivedLocation));
		
		if(null == receivedSMS.getRequest()){
			tv.setText("invalid message");
			return;
		}
			
		
		String message = "";
//		String message = SMSReceiver.getInstance().getMessage();
		
//		String event = parse
		
		switch (receivedSMS.getRequest()) {
		case Requests.LOCATION_REQUEST:
			ICULocation location = gpsLocationListener.getLastValidLocation();
			ICUSMS response = new ICUSMS(PhoneNumberManager.getTargetPhonenumber(), Requests.LOCATION, location);
			message = receivedSMS.getRequest();
			response.send();
			break;

		case Requests.LOCATION:
			ICULocation loc = receivedSMS.getLocation();
			
			message = receivedSMS.getRequest() + "\nlong " + loc.getLongitude() + "\nlati " + loc.getLatitude();
			
			Log.i("debug", message);

			break;
		default:
			break;
		}
		
		
		
        tv.setText(message);
	}
	
	
	private String getSMSRequest(String message){
		
		
		
		
		return "";
	}

	
	
	
	public void setGpsLocationListener(GPSLocationListener gpsLocationListener) {
		this.gpsLocationListener = gpsLocationListener;
	}
}
