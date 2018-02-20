package es.udc.ws.app.model.driver;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Driver {

	private Long driverId;
	private String name;
	private String city;
	private String vehicleModel;
	private Short startHour;
	private Short endHour;
	private Integer scoreCount;
	private Integer totalScore;
	private Calendar registrationDate;

	public Driver(String name, String city, String vehicleModel, Short startHour, Short endHour) {
		this.name = name;
		this.city = city;
		this.vehicleModel = vehicleModel;
		this.startHour = startHour;
		this.endHour = endHour;
		this.scoreCount = 0;
		this.totalScore = 0;
	}

	public Driver(Long driverId, String name, String city, String vehicleModel, Short startHour, Short endHour) {
		this(name, city, vehicleModel, startHour, endHour);
		this.driverId = driverId;
	}

	public Driver(Long driverId, String name, String city, String vehicleModel, Short startHour, Short endHour,
			Calendar registrationDate) {
		this(driverId, name, city, vehicleModel, startHour, endHour);
		this.registrationDate = registrationDate;
		if (registrationDate != null) {
			this.registrationDate.set(Calendar.MILLISECOND, 0);
		}
	}

	public Driver(Long driverId, String name, String city, String vehicleModel, Short startHour, Short endHour,
			Integer scoreCount, Integer totalScore, Calendar registrationDate) {
		this(driverId, name, city, vehicleModel, startHour, endHour, registrationDate);
		this.scoreCount = scoreCount;
		this.totalScore = totalScore;
	}
	
	public Driver(Long driverId, String name, String city, Integer scoreCount, Integer totalScore) {
		this.driverId = driverId;
		this.name = name;
		this.city = city;
		this.scoreCount = scoreCount;
		this.totalScore = totalScore;
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

	public Integer getScoreCount() {
		return scoreCount;
	}

	public void setScoreCount(Integer scoreCount) {
		this.scoreCount = scoreCount;
	}
	
	public void increaseScoreCount(){
		this.scoreCount++;
	}

	public Integer getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(Integer totalScore) {
		this.totalScore = totalScore;
	}
	
	public void increaseTotalScore(Short score) {
		this.totalScore = totalScore + score;
	}
	
	
	public Calendar getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Calendar registrationDate) {
		registrationDate.set(Calendar.MILLISECOND, 0);
		this.registrationDate = registrationDate;
	}

	@Override
	public String toString() {
		if (registrationDate != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return "Driver [driverId=" + driverId + ", name=" + name + ", city=" + city + ", vehicleModel="
					+ vehicleModel + ", startHour=" + startHour + ", endHour=" + endHour + ", scoreCount=" + scoreCount
					+ ", totalScore=" + totalScore + ", registrationDate="
					+ sdf.format(registrationDate.getTime().getTime()) + "]";
		} else {
			return "Driver [driverId=" + driverId + ", name=" + name + ", city=" + city + ", vehicleModel="
					+ vehicleModel + ", startHour=" + startHour + ", endHour=" + endHour + ", scoreCount=" + scoreCount
					+ ", totalScore=" + totalScore + ", registrationDate=null";
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((driverId == null) ? 0 : driverId.hashCode());
		result = prime * result + ((endHour == null) ? 0 : endHour.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((registrationDate == null) ? 0 : registrationDate.hashCode());
		result = prime * result + ((scoreCount == null) ? 0 : scoreCount.hashCode());
		result = prime * result + ((startHour == null) ? 0 : startHour.hashCode());
		result = prime * result + ((totalScore == null) ? 0 : totalScore.hashCode());
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
		Driver other = (Driver) obj;
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
		if (registrationDate == null) {
			if (other.registrationDate != null)
				return false;
		} else if (!registrationDate.equals(other.registrationDate))
			return false;
		if (scoreCount == null) {
			if (other.scoreCount != null)
				return false;
		} else if (!scoreCount.equals(other.scoreCount))
			return false;
		if (startHour == null) {
			if (other.startHour != null)
				return false;
		} else if (!startHour.equals(other.startHour))
			return false;
		if (totalScore == null) {
			if (other.totalScore != null)
				return false;
		} else if (!totalScore.equals(other.totalScore))
			return false;
		if (vehicleModel == null) {
			if (other.vehicleModel != null)
				return false;
		} else if (!vehicleModel.equals(other.vehicleModel))
			return false;
		return true;
	}

}
