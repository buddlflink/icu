package matze.gps.icu.control;

import java.util.Vector;

import matze.gps.icu.model.Observed;

public class PhoneNumberManager {
	
//	MainActivity mainActivity;
	private Observed me;
	private Vector<Observed> allObserved;


	private String monitorNumber;
	

	public PhoneNumberManager() {
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
	
	
	public String getMonitorNumber() {
		return monitorNumber;
	}
	
	public void setMonitorNumber(String monitorNumber) {
		this.monitorNumber = monitorNumber;
	}
	
	public Vector<Observed> getAllObserved() {
		return allObserved;
	}
	
	public void addObserved(Observed o){
		allObserved.add(o);
	}

}
