package matze.gps.icu.model;

import java.util.HashSet;
import java.util.Set;

import matze.gps.icu.MainActivity;
import matze.gps.icu.R;
import matze.gps.icu.control.ObserverManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.WindowManager;

public class Settings {

	private boolean debug = true;
	private boolean displayAlwaysOn = false;
	private static final String MONITORNUMBER = "monitorNumber";
	private static final String DISPLAYALWAYSON = "displayAlwaysOn";
	ObserverManager observerManager;
	


	public Settings() {
		
		
		SharedPreferences settings = MainActivity.getMainActivity().getSharedPreferences(MainActivity.getMainActivity().getString(R.string.PREFERENCES), Context.MODE_PRIVATE);

		if (null == observerManager){
			observerManager= MainActivity.getMainActivity().getPhoneNumberManager();
		}
		
		
		Set<String> s = new HashSet<>();
		
		s =  settings.getStringSet(MONITORNUMBER, s);
		for(String n:s){
			Observed other = new Observed(false, 'U',n);
			observerManager.addObserved(other);
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
			MainActivity.getMainActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		else
			// Display off
			MainActivity.getMainActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	public void save() {

		SharedPreferences settings = MainActivity.getMainActivity().getSharedPreferences(MainActivity.getMainActivity().getString(R.string.PREFERENCES), Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();

		
		HashSet<String> s = new HashSet<>();
		for(Observed o: observerManager.getAllObserved()){
			if(!o.isMe()){
				s.add(o.getNumber());
			}
		}
		
		editor.putStringSet(MONITORNUMBER, s);
		editor.putBoolean(DISPLAYALWAYSON, displayAlwaysOn);
		editor.commit();
	}

}
;