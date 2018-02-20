package es.udc.ws.app.dto;

public class ServiceDriverDto {

	private Long driverId;
	private String name;
	private String city;
	private String vehicleModel;
	private Short startHour;
	private Short endHour;
	private Double score;

	public ServiceDriverDto() {
	}

	public ServiceDriverDto(Long driverId, String name, String city, String vehicleModel, Short startHour,
			Short endHour) {
		this.driverId = driverId;
		this.name = name;
		this.city = city;
		this.vehicleModel = vehicleModel;
		this.startHour = startHour;
		this.endHour = endHour;
		this.score = 0d;
	}
	
	public ServiceDriverDto(Long driverId, String name, String city, String vehicleModel, Short startHour,
			Short endHour, Double score) {
		this.driverId = driverId;
		this.name = name;
		this.city = city;
		this.vehicleModel = vehicleModel;
		this.startHour = startHour;
		this.endHour = endHour;
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

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((driverId == null) ? 0 : driverId.hashCode());
		result = prime * result + ((endHour == null) ? 0 : endHour.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((score == null) ? 0 : score.hashCode());
		result = prime * result + ((startHour == null) ? 0 : startHour.hashCode());
		result = prime * result + ((vehicleModel == null) ? 0 : vehicleModel.hashCode());
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
		ServiceDriverDto other = (ServiceDriverDto) obj;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (driverId == null) {
			if (other.driverId != null)
				return false;
		} else if (!driverId.equals(other.driverId))
			return false;
		if (endHour == null) {
			if (other.endHour != null)
				return false;
		} else if (!endHour.equals(other.endHour))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (score == null) {
			if (other.score != null)
				return false;
		} else if (!score.equals(other.score))
			return false;
		if (startHour == null) {
			if (other.startHour != null)
				return false;
		} else if (!startHour.equals(other.startHour))
			return false;
		if (vehicleModel == null) {
			if (other.vehicleModel != null)
				return false;
		} else if (!vehicleModel.equals(other.vehicleModel))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ServiceDriverDto [driverId=" + driverId + ", name=" + name + ", city=" + city + ", vehicleModel="
				+ vehicleModel + ", startHour=" + startHour + ", endHour=" + endHour + ", score=" + score + "]";
	}

}
