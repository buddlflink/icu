package matze.gps.icu.model;

import matze.gps.icu.MainActivity;
import matze.gps.icu.R;
import matze.gps.icu.control.PhoneNumberManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.WindowManager;

public class Settings {

	private boolean debug = true;
	private boolean displayAlwaysOn = false;
	private static final String MONITORNUMBER = "monitorNumber";
	private static final String DISPLAYALWAYSON = "displayAlwaysOn";
	PhoneNumberManager phoneNumberManager;
	

	MainActivity mainActivity;

	public Settings(MainActivity mainActivity) {
		
		this.mainActivity = mainActivity;
		SharedPreferences settings = mainActivity.getSharedPreferences(mainActivity.getString(R.string.PREFERENCES), Context.MODE_PRIVATE);

		if (null == phoneNumberManager){
			phoneNumberManager= mainActivity.getPhoneNumberManager();
		}
		
		phoneNumberManager.setMonitorNumber(settings.getString(MONITORNUMBER, ""));
		
		String number = settings.getString(MONITORNUMBER, "");
		boolean inList = false;
		for (Observed o : phoneNumberManager.getAllObserved()) {
			inList = inList || (o.getNumber().equals(number));
		}

		if (!inList) {
			Observed other = new Observed(false, mainActivity, 'U');
			phoneNumberManager.addObserved(other);

		}
		
		

		setDisplayAlwaysOn(settings.getBoolean(DISPLAYALWAYSON, false));

	}

	public boolean isDebug() {
		return debug;
	}

	public boolean isDisplayAlwaysOn() {
		return displayAlwaysOn;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public void setDisplayAlwaysOn(boolean displayAlwaysOn) {
		this.displayAlwaysOn = displayAlwaysOn;

		// Display on
		if (displayAlwaysOn)
			mainActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		else
			// Display off
			mainActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	public void save() {

		SharedPreferences settings = mainActivity.getSharedPreferences(mainActivity.getString(R.string.PREFERENCES), Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();

		editor.putString(MONITORNUMBER, phoneNumberManager.getMonitorNumber());
		editor.putBoolean(DISPLAYALWAYSON, displayAlwaysOn);
		editor.commit();
	}

}
