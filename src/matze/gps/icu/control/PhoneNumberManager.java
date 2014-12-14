package matze.gps.icu.control;

import java.util.ArrayList;

public class PhoneNumberManager {
	
//	MainActivity mainActivity;

//	private static final String phonenumber = "015755902299";
	private ArrayList<String> authorizedNumbers;
	private String monitorNumber;
	
	private static PhoneNumberManager instance;

	public PhoneNumberManager() {

		authorizedNumbers = new ArrayList<String>();
	}
	
	public static PhoneNumberManager getInstance() {
		if(null == instance)
			instance = new PhoneNumberManager();
		return instance;
	}
	
//	public static String getTargetPhonenumber() {
//		return phonenumber;
//	}
	
	public void addAuthorizedNumber(String authorizedNumber) {
		this.authorizedNumbers.add(authorizedNumber);
	}
	
	public ArrayList<String> getAuthorizedNumbers() {
		return authorizedNumbers;
	}
	
	public String getMonitorNumber() {
		return monitorNumber;
	}
	
	public void setMonitorNumber(String monitorNumber) {
		this.monitorNumber = monitorNumber;
	}

}
