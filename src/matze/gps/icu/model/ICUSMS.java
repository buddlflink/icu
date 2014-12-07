package matze.gps.icu.model;

import android.location.Location;
import android.telephony.SmsManager;

public class ICUSMS {

	private String request;
	private String receivedFrom;
	private String sendTo;
	private String receiver;
	private ICULocation location;
	private static final String div = " ";

	// Aufbau einer SMS:
	// REQ
	// LATI
	// LONG

	public void setLocation(ICULocation location) {
		this.location = location;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
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

	public ICULocation getLocation() {
		return location;
	}

	public String getReceiver() {
		return receiver;
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
	 * Get SMS data
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
		case Requests.LOCATION_REQUEST:

			break;

		case Requests.LOCATION:
			if (null != frac[1] && null != frac[2])
				location = new ICULocation(frac[1], frac[2]);
			break;
		}

		return this;
	}

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

		SmsManager sm = SmsManager.getDefault();
		sm.sendTextMessage(sendTo, null, message.toString(), null, null);
	}

	public ICUSMS() {
	}

	public ICUSMS(String sendTo, String request, ICULocation location) {

		this.request = request;
		this.location = location;
		this.sendTo = sendTo;

	}

}
