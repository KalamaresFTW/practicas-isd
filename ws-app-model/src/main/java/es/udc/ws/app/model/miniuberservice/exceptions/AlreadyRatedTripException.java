package es.udc.ws.app.model.miniuberservice.exceptions;

@SuppressWarnings("serial")
public class AlreadyRatedTripException extends Exception {

	private Long tripId;

	public AlreadyRatedTripException(Long tripId) {
		super("Trip with id=" + tripId + " has already been rated.");
		this.tripId = tripId;
	}

	public Long getTripId() {
		return tripId;
	}

	public void setTripId(Long tripId) {
		this.tripId = tripId;
	}

}
