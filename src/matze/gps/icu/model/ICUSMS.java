package matze.gps.icu.model;

import matze.gps.icu.MainActivity;
import matze.gps.icu.R;

import org.osmdroid.util.GeoPoint;

import android.telephony.SmsManager;

public class ICUSMS {

	private String request;
	private String receivedFrom;
	private String sendTo;
	private GeoPoint location;
	private static final String div = " ";

//	MainActivity mainActivity; 
	// Aufbau einer SMS: REQ div [LATI div LONG div]

	public void setLocation(GeoPoint location) {
		this.location = location;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public void setReceivedFrom(String receivedFrom) {
		this.receivedFrom = receivedFrom;
	}

	public void setSendTo(String sendTo) {
		this.sendTo = sendTo;
	}

	public GeoPoint getLocation() {
		return location;
	}

	public String getRequest() {
		return request;
	}

	public String getReceivedFrom() {
		return receivedFrom;
	}

	public String getSendTo() {
		return sendTo;
	}

	/**
	 * Parse SMS content
	 * @param body
	 * @param sender
	 * @return
	 */
	public ICUSMS parseSMS(String body, String sender) {

		this.receivedFrom = sender;

		String frac[] = body.split(div);

		if (null != frac[0])
			request = frac[0];

		
		switch (request) {
		case   Requests.LOCATION_REQUEST:
			break;
		case Requests.LOCATION:
			if (null != frac[1] && null != frac[2])
				location = new GeoPoint(Double.parseDouble(frac[1]), Double.parseDouble(frac[2]));
			break;
		}
		return this;
	}

	/**
	 * Check SMS and send SMS
	 */
	public void send() {

		if (null == request || null == sendTo)
			return;

		StringBuilder message = new StringBuilder();
		message = message.append(request);
		message = message.append(div);

		if (null != location && request.equals(Requests.LOCATION)) {
			message = message.append(location.getLatitude());
			message = message.append(div);
			message = message.append(location.getLongitude());
			message = message.append(div);
		}
		
		// send
		SmsManager sm = SmsManager.getDefault();
		sm.sendTextMessage(sendTo, null, message.toString(), null, null);
	}

	
	/**
	 * 
	 * @param sendTo Target number
	 * @param request Request type
	 * @param location Optionally
	 */
	public ICUSMS(String sendTo, String request, GeoPoint location) {
		this.request = request;
		this.location = location;
		this.sendTo = sendTo;
	}
	
	public ICUSMS() {
		// TODO Auto-generated constructor stub
	}
	
	
	

}
