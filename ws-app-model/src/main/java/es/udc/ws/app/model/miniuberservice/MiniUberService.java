package es.udc.ws.app.model.miniuberservice;

import java.util.List;

import es.udc.ws.app.model.driver.Driver;
import es.udc.ws.app.model.miniuberservice.exceptions.AlreadyRatedTripException;
import es.udc.ws.app.model.miniuberservice.exceptions.InvalidUserLoginException;
import es.udc.ws.app.model.trip.Trip;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public interface MiniUberService {

	public Driver addDriver(Driver driver) throws InputValidationException;

	public void updateDriver(Driver driver) throws InputValidationException, InstanceNotFoundException;

	public List<Driver> findDriverByCity(String city);

	public Driver findDriver(Long driverId) throws InstanceNotFoundException;

	public Trip addTrip(Trip trip) throws InputValidationException, InstanceNotFoundException;

	public void rateTrip(Long tripId, Short score, String userLogin)
			throws InputValidationException, InstanceNotFoundException, AlreadyRatedTripException, InvalidUserLoginException;

	public List<Trip> findTripsByUser(String userLogin) throws InputValidationException;

	public List<Trip> findTripsByDriver(Long driverId) throws InputValidationException;
}
