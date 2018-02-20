package es.udc.ws.app.model.trip;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Trip {

	private Long tripId;
	private Long driverId;
	private String userLogin;
	private String originLocation;
	private String destinationLocation;
	private Short score;
	private String cardNumber;
	private Calendar bookingDate;

	public Trip(Long driverId, String userLogin, String originLocation, String destinationLocation, String cardNumber) {
		this.driverId = driverId;
		this.userLogin = userLogin;
		this.originLocation = originLocation;
		this.destinationLocation = destinationLocation;
		this.cardNumber = cardNumber;
	}

	public Trip(Long driverId, String userLogin, String originLocation, String destinationLocation, Short score,
			String cardNumber) {
		this(driverId, userLogin, originLocation, destinationLocation, cardNumber);
		this.score = score;
	}

	public Trip(Long tripId, Long driverId, String userLogin, String originLocation, String destinationLocation,
			Short score, String cardNumber) {
		this(driverId, userLogin, originLocation, destinationLocation, score, cardNumber);
		this.tripId = tripId;
	}

	public Trip(Long tripId, Long driverId, String userLogin, String originLocation, String destinationLocation,
			Short score, String cardNumber, Calendar bookingDate) {
		this(tripId, driverId, userLogin, originLocation, destinationLocation, score, cardNumber);
		this.bookingDate = bookingDate;
		if (bookingDate != null) {
			this.bookingDate.set(Calendar.MILLISECOND, 0);
		}
	}
	
	public Trip(Long tripId, Long driverId, String userLogin, String originLocation, String destinationLocation,
			String cardNumber, Calendar bookingDate) {
		this(tripId, driverId, userLogin, originLocation, destinationLocation, null, cardNumber);
		this.bookingDate = bookingDate;
		if (bookingDate != null) {
			this.bookingDate.set(Calendar.MILLISECOND, 0);
		}
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

	@Override
	public String toString() {
		if (bookingDate != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return "Trip [tripId=" + tripId + ", driverId=" + driverId + ", userLogin=" + userLogin
					+ ", originLocation=" + originLocation + ", destinationLocation=" + destinationLocation + ", score="
					+ score + ", cardNumber=" + cardNumber + ", bookingDate="
					+ sdf.format(bookingDate.getTime().getTime()) + "]";
		} else {
			return "Trip [tripId=" + tripId + ", driverId=" + driverId + ", userLogin=" + userLogin
					+ ", originLocation=" + originLocation + ", destinationLocation=" + destinationLocation + ", score="
					+ score + ", cardNumber=" + cardNumber + ", bookingDate=" + bookingDate + "]";
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bookingDate == null) ? 0 : bookingDate.hashCode());
		result = prime * result + ((cardNumber == null) ? 0 : cardNumber.hashCode());
		result = prime * result + ((destinationLocation == null) ? 0 : destinationLocation.hashCode());
		result = prime * result + ((driverId == null) ? 0 : driverId.hashCode());
		result = prime * result + ((originLocation == null) ? 0 : originLocation.hashCode());
		result = prime * result + ((score == null) ? 0 : score.hashCode());
		result = prime * result + ((tripId == null) ? 0 : tripId.hashCode());
		result = prime * result + ((userLogin == null) ? 0 : userLogin.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Trip other = (Trip) obj;
		if (bookingDate == null) {
			if (other.bookingDate != null)
				return false;
		} else if (!bookingDate.equals(other.bookingDate))
			return false;
		if (cardNumber == null) {
			if (other.cardNumber != null)
				return false;
		} else if (!cardNumber.equals(other.cardNumber))
			return false;
		if (destinationLocation == null) {
			if (other.destinationLocation != null)
				return false;
		} else if (!destinationLocation.equals(other.destinationLocation))
			return false;
		if (driverId == null) {
			if (other.driverId != null)
				return false;
		} else if (!driverId.equals(other.driverId))
			return false;
		if (originLocation == null) {
			if (other.originLocation != null)
				return false;
		} else if (!originLocation.equals(other.originLocation))
			return false;
		if (score == null) {
			if (other.score != null)
				return false;
		} else if (!score.equals(other.score))
			return false;
		if (tripId == null) {
			if (other.tripId != null)
				return false;
		} else if (!tripId.equals(other.tripId))
			return false;
		if (userLogin == null) {
			if (other.userLogin != null)
				return false;
		} else if (!userLogin.equals(other.userLogin))
			return false;
		return true;
	}

}
