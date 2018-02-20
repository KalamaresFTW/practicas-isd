package es.udc.ws.app.model.trip;

import java.sql.Connection;
import java.util.List;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

public interface SqlTripDao {

	public Trip create(Connection connection, Trip trip);
	
	public void remove(Connection connection, Long tripId) throws InstanceNotFoundException;

	public void update(Connection connection, Trip trip) throws InstanceNotFoundException;

	public Trip find(Connection connection, Long tripId) throws InstanceNotFoundException;

	public List<Trip> findByDriver(Connection connection, Long driverId);

	public List<Trip> findByUser(Connection connection, String userLogin);
}
