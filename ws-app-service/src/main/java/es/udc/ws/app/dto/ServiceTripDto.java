package es.udc.ws.app.dto;

import java.util.Calendar;

public class ServiceTripDto {

	private Long tripId;
	private Long driverId;
	private String userLogin;
	private String originLocation;
	private String destinationLocation;
	private Short score;
	private String cardNumber;
	private Calendar bookingDate;

	public ServiceTripDto(Long tripId, Long driverId, String userLogin, String originLocation,
			String destinationLocation, Short score, String cardNumber, Calendar bookingDate) {
		this.tripId = tripId;
		this.driverId = driverId;
		this.userLogin = userLogin;
		this.originLocation = originLocation;
		this.destinationLocation = destinationLocation;
		this.score = score;
		this.cardNumber = cardNumber;
		this.bookingDate = bookingDate;
	}
	
	public ServiceTripDto(Long driverId, String userLogin, String originLocation,
			String destinationLocation, String cardNumber) {
		this.driverId = driverId;
		this.userLogin = userLogin;
		this.originLocation = originLocation;
		this.destinationLocation = destinationLocation;
		this.cardNumber = cardNumber;
	}
	
	public ServiceTripDto(Long tripId, Long driverId, String userLogin, String originLocation,
			String destinationLocation, Calendar bookingDate) {
		this.tripId = tripId;
		this.driverId = driverId;
		this.userLogin = userLogin;
		this.originLocation = originLocation;
		this.destinationLocation = destinationLocation;
		this.bookingDate = bookingDate;
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

	public Short getScore() {
		return score;
	}

	public void setScore(Short score) {
		this.score = score;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public Calendar getBookingDate() {
		return bookingDate;
	}

	public void setBookingDate(Calendar bookingDate) {
		bookingDate.set(Calendar.MILLISECOND, 0);
		this.bookingDate = bookingDate;
	}

}
