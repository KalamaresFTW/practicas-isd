package es.udc.ws.app.client.service.dto;

public class ClientTripDto {

	private Long tripId;
	private Long driverId;
	private String userLogin;
	private String originLocation;
	private String destinationLocation;
	private String creditCardNumber;
	private String bookingDate;

	public ClientTripDto(Long tripId, Long driverId, String userLogin, String originLocation,
			String destinationLocation, String bookingDate) {
		this.tripId = tripId;
		this.driverId = driverId;
		this.userLogin = userLogin;
		this.originLocation = originLocation;
		this.destinationLocation = destinationLocation;
		this.bookingDate = bookingDate;
	}

	public ClientTripDto(Long driverId, String userLogin, String originLocation, String destinationLocation,
			String creditCardNumber) {
		this.driverId = driverId;
		this.userLogin = userLogin;
		this.originLocation = originLocation;
		this.destinationLocation = destinationLocation;
		this.creditCardNumber = creditCardNumber;
	}

	public Long getTripId() {
		return tripId;
	}

	public void setTripId(Long tripId) {
		this.tripId = tripId;
	}

	public Long getDriverId() {
		return driverId;
	}

	public void setDriverId(Long driverId) {
		this.driverId = driverId;
	}

	public String getUserLogin() {
		return userLogin;
	}

	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
	}

	public String getOriginLocation() {
		return originLocation;
	}

	public void setOriginLocation(String originLocation) {
		this.originLocation = originLocation;
	}

	public String getDestinationLocation() {
		return destinationLocation;
	}

	public void setDestinationLocation(String destinationLocation) {
		this.destinationLocation = destinationLocation;
	}

	public String getCreditCardNumber() {
		return creditCardNumber;
	}

	public void setCreditCardNumber(String creditCardNumber) {
		this.creditCardNumber = creditCardNumber;
	}

	public String getBookingDate() {
		return bookingDate;
	}

	public void setBookingDate(String bookingDate) {
		this.bookingDate = bookingDate;
	}

	@Override
	public String toString() {
		return "ClientTripDto [tripId=" + tripId + ", driverId=" + driverId + ", userLogin=" + userLogin
				+ ", originLocation=" + originLocation + ", destinationLocation=" + destinationLocation
				+ ", bookingDate=" + bookingDate + "]";
	}

}
