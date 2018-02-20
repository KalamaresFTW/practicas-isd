package es.udc.ws.app.serviceutil;

import java.util.ArrayList;
import java.util.List;

import es.udc.ws.app.dto.ServiceDriverDto;
import es.udc.ws.app.model.driver.Driver;

public class DriverToDriverDtoConversor {

	public static Driver toDriver(ServiceDriverDto driver) {
		return new Driver(driver.getDriverId(), driver.getName(), driver.getCity(), driver.getVehicleModel(),
				driver.getStartHour(), driver.getEndHour());
	}

	public static ServiceDriverDto toDriverDto(Driver driver) {
		if (driver.getScoreCount() == 0) {
			return new ServiceDriverDto(driver.getDriverId(), driver.getName(), driver.getCity(), driver.getVehicleModel(),
					driver.getStartHour(), driver.getEndHour());
		} else {
			Double avgScore = Double.valueOf(driver.getTotalScore()/driver.getScoreCount());
			return new ServiceDriverDto(driver.getDriverId(), driver.getName(), driver.getCity(), driver.getVehicleModel(),
					driver.getStartHour(), driver.getEndHour(), avgScore);
		}
	}

	public static List<ServiceDriverDto> toDriverDtos(List<Driver> drivers) {
		List<ServiceDriverDto> driverDtos = new ArrayList<>(drivers.size());
		for (int i = 0; i < drivers.size(); i++) {
			Driver driver = drivers.get(i);
			driverDtos.add(toDriverDto(driver));
		}
		return driverDtos;
	}
	
}
