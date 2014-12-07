package matze.gps.icu;

import matze.gps.icu.control.ICUSMSManager;
import matze.gps.icu.control.PhoneNumberManager;
import matze.gps.icu.model.ICUSMS;
import matze.gps.icu.model.Requests;
import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class MainActivity extends Activity {

//    private SMSReceiver smsReceiver;
    
    
    
	ICUSMSManager smsManager;
    LocationManager locationManager;
    GPSLocationListener gpsLocationListener;
    PhoneNumberManager phoneNumberManager;
    
	@Override
    public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		phoneNumberManager = new PhoneNumberManager();
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		gpsLocationListener = new GPSLocationListener();
		gpsLocationListener.setMainActivity(this);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, gpsLocationListener);
		
		smsManager = ICUSMSManager.getInstance();
		smsManager.setMainActivity(this);
		smsManager.setGpsLocationListener(gpsLocationListener);
		
		Button b = ((Button) findViewById(R.id.buttonRequestLocation));
		b.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ICUSMS icuSMS = new ICUSMS(phoneNumberManager.getTargetPhonenumber(), Requests.LOCATION_REQUEST, null);
				
				icuSMS.send();
			}
		});

	}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
