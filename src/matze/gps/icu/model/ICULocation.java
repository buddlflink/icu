package matze.gps.icu.model;

public class ICULocation {
	
	private String longitude;
	private String latitude;
	
	public ICULocation() {
	}
	
	public ICULocation(String latitude, String longitude){
		this.latitude = latitude;
		this.longitude = longitude;
	}
		
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	
	public String getLatitude() {
		return latitude;
	}
	public String getLongitude() {
		return longitude;
	}

}
