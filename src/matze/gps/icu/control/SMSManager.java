/**
 * SMS manager singleton class.
 */

package matze.gps.icu.control;

import matze.gps.icu.MainActivity;
import matze.gps.icu.R;
import matze.gps.icu.model.ICUSMS;
import matze.gps.icu.model.Observed;
import matze.gps.icu.model.Requests;
import matze.gps.icu.monitor.MapFragment;

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
	private ObserverManager observerManager;
	
	

	@Override
	public void onReceive( Context context, Intent intent ) 
    {
        // Get the SMS map from Intent
        Bundle extras = intent.getExtras();
        if (null == observerManager){
			observerManager= MainActivity.getMainActivity().getPhoneNumberManager();
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
        			
        			for(Observed o: observerManager.getAllObserved()){
        				
        				Log.i("debug" , "receivedFrom " + icuSMS.getReceivedFrom());
        				Log.i("debug" , "o.getNumber() " + o.getNumber());
        				
        				if(!o.isMe() && 
        						cleanNumber(o.getNumber()).equals(cleanNumber(icuSMS.getReceivedFrom()))){
        					o.setPosition(loc);
        					o.setBattery(icuSMS.getBattery());
        				}
        			}
        			
        			
        			
        		}


         		TextView tv = ((TextView) MainActivity.getMainActivity().findViewById(R.id.textViewRemoteCoord));

        		if (null != tv)
        			tv.setText(message);
        		
            }
            
            MapFragment.getFragment().drawPositions();
            
            
            
            
        }
    }
	
	private String cleanNumber(String number) {
		
		number = number.replaceAll(" ", "");
		if(number.startsWith("+")){
			number=number.substring(3);
		}
		if(number.startsWith("00")){
			number=number.substring(4);
		}
		
		if(number.startsWith("0")){
			number=number.substring(1);
		}
		
		return number;

	}
	
}
