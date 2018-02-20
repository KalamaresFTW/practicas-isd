package es.udc.ws.app.serviceutil;

import java.util.ArrayList;
import java.util.List;

import es.udc.ws.app.dto.ServiceTripDto;
import es.udc.ws.app.model.trip.Trip;

public class TripToTripDtoConversor {

	public static List<ServiceTripDto> toTripsDto(List<Trip> trips) {
		List<ServiceTripDto> tripsDto = new ArrayList<>(trips.size());
		for (int i = 0; i < trips.size(); i++) {
			tripsDto.add(toTripDto((Trip) trips.get(i)));
		}
		return tripsDto;
	}

	/*
	 * Lo usamos para convertir los objetos Trip resultantes de crear un viaje a
	 * DTOs.
	 */
	public static ServiceTripDto toTripDto(Trip trip) {
		return new ServiceTripDto(trip.getTripId(), trip.getDriverId(), trip.getUserLogin(), trip.getOriginLocation(),
				trip.getDestinationLocation(), trip.getBookingDate());
	}

	/*
	 * Lo usamos para convertir el DTO resultado de parsear el XML de las
	 * peticiones de entrada
	 */
	public static Trip toTrip(ServiceTripDto serviceDto) {
		return new Trip(serviceDto.getDriverId(), serviceDto.getUserLogin(), serviceDto.getOriginLocation(),
				serviceDto.getDestinationLocation(), serviceDto.getCardNumber());
	}
}
