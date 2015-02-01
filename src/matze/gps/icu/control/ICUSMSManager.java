/**
 * SMS manager singleton class.
 */

package matze.gps.icu.control;

import matze.gps.icu.MainActivity;
import matze.gps.icu.R;
import matze.gps.icu.model.ICUSMS;
import matze.gps.icu.model.Observed;
import matze.gps.icu.model.Requests;

import org.osmdroid.util.GeoPoint;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.TextView;

public class ICUSMSManager extends BroadcastReceiver{

	private static ICUSMSManager instance;
	private MainActivity mainActivity;
	private static final String SMS_EXTRA_NAME ="pdus";
	
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

		if (null == mainActivity)
			return;

		TextView tv = ((TextView) mainActivity.findViewById(R.id.textViewRemoteCoord));

		if (null == receivedSMS.getRequest()) {
			tv.setText("invalid message");
			return;
		}

		String message = "";

		// Todo: Location und battery nicht hier setzen.

		// switch (receivedSMS.getRequest()) {
		// // Respond to request
		// case Requests.LOCATION_REQUEST:
		// GeoPoint location = gpsLocationListener.getLastIPosition();
		// String battery = gpsLocationListener.getBattery();
		// ICUSMS response = new ICUSMS(receivedSMS.getReceivedFrom(),
		// Requests.LOCATION, location, battery);
		// message = receivedSMS.getRequest();
		//
		// if
		// (PhoneNumberManager.getInstance().getAuthorizedNumbers().contains(receivedSMS.getReceivedFrom())
		// || mainActivity.isDebug())
		// response.send();
		// break;
		// // Received response
		// case Requests.LOCATION:
		// GeoPoint loc = receivedSMS.getLocation();
		// message = receivedSMS.getRequest() + "\nlong " + loc.getLongitude() +
		// "\nlati " + loc.getLatitude();
		// gpsLocationListener.addUPosition(loc);
		// gpsLocationListener.setUBattery(receivedSMS.getBattery());
		// break;
		// default:
		// break;
		// }
		if (receivedSMS.getRequest().equals(Requests.LOCATION)) {
			GeoPoint loc = receivedSMS.getLocation();
			message = receivedSMS.getRequest() + "\nlong " + loc.getLongitude() + "\nlati " + loc.getLatitude();
			
			for(Observed o: mainActivity.getAllObserved()){
				if(o.getNumber().equals(receivedSMS.getReceivedFrom())){
					o.setPosition(loc);
					o.setBattery(receivedSMS.getBattery());
				}
			}
			
			
			
		}

		if (null != tv)
			tv.setText(message);
	}

	@Override
	public void onReceive( Context context, Intent intent ) 
    {
        // Get the SMS map from Intent
        Bundle extras = intent.getExtras();
       
        if ( extras != null )
        {
            // Get received SMS array
            Object[] smsExtra = (Object[]) extras.get( SMS_EXTRA_NAME );
            
            
            for ( int i = 0; i < smsExtra.length; ++i )
            {
                SmsMessage sms = SmsMessage.createFromPdu((byte[])smsExtra[i]);
                
                String body = sms.getMessageBody().toString();
                String sender = sms.getOriginatingAddress();
                
                ICUSMS icuSMS = new ICUSMS();
                icuSMS.parseSMS(body, sender);
                ICUSMSManager.getInstance().SMSReceived(icuSMS);
            }
        }
    }
	
	
}
