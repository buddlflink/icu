package matze.gps.icu.control;

import java.util.Vector;

import matze.gps.icu.model.Observed;

public class ObserverManager {
	
//	MainActivity mainActivity;
	private Observed me;
	private Vector<Observed> allObserved;

	public ObserverManager() {
		allObserved = new Vector<Observed>();
	}
	
	
	
//	public static String getTargetPhonenumber() {
//		return phonenumber;
//	}
	
	public void setMe(Observed me) {
		this.me = me;
	}
	public Observed getMe() {
		return me;
	}
	
	
	
	
	public Vector<Observed> getAllObserved() {
		return allObserved;
	}
	
	public void addObserved(Observed o){
		allObserved.add(o);
	}

}
