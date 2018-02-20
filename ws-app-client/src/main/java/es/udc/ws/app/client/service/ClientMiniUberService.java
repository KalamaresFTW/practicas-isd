package es.udc.ws.app.client.service;

import java.util.List;

import es.udc.ws.app.client.exceptions.AlreadyRatedTripException;
import es.udc.ws.app.client.exceptions.InvalidUserLoginException;
import es.udc.ws.app.client.service.dto.ClientDriverDto;
import es.udc.ws.app.client.service.dto.ClientTripDto;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public interface ClientMiniUberService {
	
	public Long addDriver(ClientDriverDto driver) throws InputValidationException;
	
	public void updateDriver(ClientDriverDto driver) throws InputValidationException, InstanceNotFoundException;
	
	public ClientDriverDto findDriver(Long driverId) throws InstanceNotFoundException;
	
	public List<ClientDriverDto> findDriverByCity(String city);
	
	public ClientTripDto addTrip(ClientTripDto trip) throws InputValidationException, InstanceNotFoundException;
	
	public void rateTrip(Long tripId, Short score, String userLogin)
			throws InputValidationException, InstanceNotFoundException, AlreadyRatedTripException, InvalidUserLoginException;
	
	public List<ClientTripDto> findTripsByDriver(Long driverId) throws InputValidationException;
	
	public List<ClientTripDto> findTripsByUser(String userLogin) throws InputValidationException;
	
	public List<ClientDriverDto> findDriversByUser(String userLogin) throws InputValidationException;
	
}
