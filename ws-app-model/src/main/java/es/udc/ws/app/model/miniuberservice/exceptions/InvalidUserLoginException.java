package es.udc.ws.app.model.miniuberservice.exceptions;

@SuppressWarnings("serial")
public class InvalidUserLoginException extends Exception {

	private Long tripId;
	private String userLogin;

	public InvalidUserLoginException(Long tripId, String userLogin) {
		super("An attempt of rating a trip (tripId: " + tripId + ") has been made by the wrong user (userLogin: "
				+ userLogin + ")");
		this.tripId = tripId;
		this.userLogin = userLogin;
	}

	public Long getTripId() {
		return tripId;
	}

	public void setTripId(Long tripId) {
		this.tripId = tripId;
	}

	public String getUserLogin() {
		return userLogin;
	}

	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
	}

}
