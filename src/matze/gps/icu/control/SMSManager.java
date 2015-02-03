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
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.TextView;

public class SMSManager extends BroadcastReceiver{

	
	
	private static final String SMS_EXTRA_NAME ="pdus";
	private PhoneNumberManager phoneNumberManager;
	
	

	

	

	@Override
	public void onReceive( Context context, Intent intent ) 
    {
        // Get the SMS map from Intent
        Bundle extras = intent.getExtras();
        if (null == phoneNumberManager){
			phoneNumberManager= MainActivity.getMainActivity().getPhoneNumberManager();
		}
       
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
                
                
                
                

        		String message = "";
        		if (icuSMS.getRequest().equals(Requests.LOCATION)) {
        			GeoPoint loc = icuSMS.getLocation();
        			message = icuSMS.getRequest() + "\nlong " + loc.getLongitude() + "\nlati " + loc.getLatitude();
        			
        			for(Observed o: phoneNumberManager.getAllObserved()){
        				
        				Log.i("debug" , "receivedFrom " + icuSMS.getReceivedFrom());
        				Log.i("debug" , "o.getNumber() " + o.getNumber());
        				
        				if(!o.isMe() && 
        						o.getNumber().equals(icuSMS.getReceivedFrom())){
        					o.setPosition(loc);
        					o.setBattery(icuSMS.getBattery());
        				}
        			}
        			
        			
        			
        		}


         		TextView tv = ((TextView) MainActivity.getMainActivity().findViewById(R.id.textViewRemoteCoord));

        		if (null != tv)
        			tv.setText(message);
        		
            }
            
            MainActivity.getMainActivity().getM
            
            
            
            
        }
    }
	
	
}
