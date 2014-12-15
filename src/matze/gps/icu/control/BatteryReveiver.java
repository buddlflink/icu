package matze.gps.icu.control;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BatteryReveiver extends BroadcastReceiver {

	
	GPSLocationManager gpsLocationManager;
	
	public BatteryReveiver(GPSLocationManager gpsLocationManager) {
		this.gpsLocationManager = gpsLocationManager;
	}
	
	@Override
	public void onReceive(Context arg0, Intent intent) {

		int level = intent.getIntExtra("level", 0);
		gpsLocationManager.setBattery(Integer.toString(level));

	}

}
