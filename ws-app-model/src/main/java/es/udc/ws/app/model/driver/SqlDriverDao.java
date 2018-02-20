package es.udc.ws.app.model.driver;

import java.sql.Connection;
import java.util.List;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

public interface SqlDriverDao {

	public Driver create(Connection connection, Driver driver);
	
	public void remove(Connection connection, Long driverId) throws InstanceNotFoundException;

	public void update(Connection connection, Driver sale) throws InstanceNotFoundException;

	public Driver find(Connection connection, Long driverId) throws InstanceNotFoundException;

	public List<Driver> findByCity(Connection connection, String city);
}
