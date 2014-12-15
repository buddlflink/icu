/**
 * Singleton class to receive SMS messages
 * 
 */


package matze.gps.icu.control;

import matze.gps.icu.model.ICUSMS;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class SMSReceiver extends BroadcastReceiver {


	private static final String SMS_EXTRA_NAME ="pdus";
	private static SMSReceiver instance;

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
	
	/**
	 * 
	 * @return
	 */
	public static SMSReceiver getInstance() {
		if (null == instance)
			instance = new SMSReceiver();
		return instance;
	}
}