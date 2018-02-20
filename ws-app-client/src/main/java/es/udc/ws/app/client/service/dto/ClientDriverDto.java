package es.udc.ws.app.client.service.dto;

public class ClientDriverDto {

	private Long driverId;
	private String name;
	private String city;
	private String vehicleModel;
	private Short startHour;
	private Short endHour;
	private Double score;

	public ClientDriverDto() {
	}

	public ClientDriverDto(String name, String city, String vehicleModel, Short startHour, Short endHour) {
		this.name = name;
		this.city = city;
		this.vehicleModel = vehicleModel;
		this.startHour = startHour;
		this.endHour = endHour;
	}

	public ClientDriverDto(Long driverId, String name, String city, String vehicleModel, Short startHour,
			Short endHour) {
		this.driverId = driverId;
		this.name = name;
		this.city = city;
		this.vehicleModel = vehicleModel;
		this.startHour = startHour;
		this.endHour = endHour;
	}

	public ClientDriverDto(Long driverId, String name, String city, String vehicleModel, Short startHour, Short endHour,
			Double score) {
		super();
		this.driverId = driverId;
		this.name = name;
		this.city = city;
		this.vehicleModel = vehicleModel;
		this.startHour = startHour;
		this.endHour = endHour;
		this.score = score;
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	public Long getDriverId() {
		return driverId;
	}

	public void setDriverId(Long driverId) {
		this.driverId = driverId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getVehicleModel() {
		return vehicleModel;
	}

	public void setVehicleModel(String vehicleModel) {
		this.vehicleModel = vehicleModel;
	}

	public Short getStartHour() {
		return startHour;
	}

	public void setStartHour(Short startHour) {
		this.startHour = startHour;
	}

	public Short getEndHour() {
		return endHour;
	}

	public void setEndHour(Short endHour) {
		this.endHour = endHour;
	}

	@Override
	public String toString() {
		return "ClientDriverDto [driverId=" + driverId + ", name=" + name + ", city=" + city + ", vehicleModel="
				+ vehicleModel + ", startHour=" + startHour + ", endHour=" + endHour + ", score=" + score + "]";
	}

}
