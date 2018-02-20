package es.udc.ws.app.restservice.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.udc.ws.app.dto.ServiceTripDto;
import es.udc.ws.app.model.miniuberservice.MiniUberServiceFactory;
import es.udc.ws.app.model.miniuberservice.exceptions.AlreadyRatedTripException;
import es.udc.ws.app.model.miniuberservice.exceptions.InvalidUserLoginException;
import es.udc.ws.app.model.trip.Trip;
import es.udc.ws.app.restservice.xml.XmlServiceExceptionConversor;
import es.udc.ws.app.restservice.xml.XmlServiceTripDtoConversor;

import es.udc.ws.app.serviceutil.TripToTripDtoConversor;

import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.servlet.ServletUtils;
import es.udc.ws.util.xml.exceptions.ParsingException;

@SuppressWarnings("serial")
public class TripsServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String driverIdParameter = req.getParameter("driverId");
		if (driverIdParameter != null && !driverIdParameter.isEmpty()) {
			Long driverId;
			try {
				driverId = Long.valueOf(driverIdParameter);
				MiniUberServiceFactory.getService().findDriver(driverId);
				ArrayList<Trip> trips;
				trips = (ArrayList<Trip>) MiniUberServiceFactory.getService().findTripsByDriver(driverId);
				ArrayList<ServiceTripDto> tripsDto;
				tripsDto = (ArrayList<ServiceTripDto>) TripToTripDtoConversor.toTripsDto(trips);
				ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
						XmlServiceTripDtoConversor.toXml(tripsDto), null);
				return;
			} catch (NumberFormatException | InputValidationException ex) {
				ServletUtils
						.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
								XmlServiceExceptionConversor.toInputValidationExceptionXml(new InputValidationException(
										"Invalid Request: parameter 'tripId' is invalid '" + driverIdParameter + "'")),
								null);
				return;

			} catch (InstanceNotFoundException ex) {
				ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND,
						XmlServiceExceptionConversor.toInstanceNotFoundException(ex), null);
				return;
			}
		}

		String userLoginParameter = req.getParameter("userLogin");
		if (userLoginParameter != null && !userLoginParameter.isEmpty()) {
			try {
				ArrayList<Trip> trips;
				trips = (ArrayList<Trip>) MiniUberServiceFactory.getService().findTripsByUser(userLoginParameter);
				ArrayList<ServiceTripDto> tripsDto;
				tripsDto = (ArrayList<ServiceTripDto>) TripToTripDtoConversor.toTripsDto(trips);
				ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
						XmlServiceTripDtoConversor.toXml(tripsDto), null);
				return;
			} catch (NumberFormatException | InputValidationException ex) {
				ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
						XmlServiceExceptionConversor.toInputValidationExceptionXml(new InputValidationException(
								"Invalid Request: parameter 'userLogin' is invalid '" + userLoginParameter + "'")),
						null);
				return;
			}
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ServiceTripDto xmlTrip;
		try {
			xmlTrip = XmlServiceTripDtoConversor.toServiceTripDto(req.getInputStream());
		} catch (ParsingException ex) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST, XmlServiceExceptionConversor
					.toInputValidationExceptionXml(new InputValidationException(ex.getMessage())), null);

			return;
		}
		Trip trip = TripToTripDtoConversor.toTrip(xmlTrip);
		try {
			MiniUberServiceFactory.getService().findDriver(trip.getDriverId());
			trip = MiniUberServiceFactory.getService().addTrip(trip);
		} catch (InstanceNotFoundException ex) { // If the driver does not exist
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND,
					XmlServiceExceptionConversor.toInstanceNotFoundException(ex), null);
			return;
		} catch (InputValidationException ex) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
					XmlServiceExceptionConversor.toInputValidationExceptionXml(ex), null);
			return;
		}
		ServiceTripDto tripDto = TripToTripDtoConversor.toTripDto(trip);
		String tripURL = ServletUtils.normalizePath(req.getRequestURL().toString()) + "/" + trip.getTripId();
		Map<String, String> headers = new HashMap<>(1);
		headers.put("Location", tripURL);
		ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_CREATED,
				XmlServiceTripDtoConversor.toXml(tripDto), headers);
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = ServletUtils.normalizePath(req.getPathInfo());
		if (path == null || path.length() == 0) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST, XmlServiceExceptionConversor
					.toInputValidationExceptionXml(new InputValidationException("Invalid Request: invalid tripId")),
					null);
			return;
		}
		String tripIdAsString = path.substring(1);
		Long tripId;
		try {
			tripId = Long.valueOf(tripIdAsString);
		} catch (NumberFormatException ex) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
					XmlServiceExceptionConversor.toInputValidationExceptionXml(
							new InputValidationException("Invalid Request: invalid tripId '" + tripIdAsString + "'")),
					null);
			return;
		}

		String scoreParameter = req.getParameter("score");
		if (scoreParameter == null) {
			ServletUtils
					.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
							XmlServiceExceptionConversor.toInputValidationExceptionXml(
									new InputValidationException("Invalid Request: parameter 'score' is mandatory")),
							null);

			return;
		}

		Short score;
		try {
			score = Short.valueOf(scoreParameter);
		} catch (NumberFormatException ex) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
					XmlServiceExceptionConversor.toInputValidationExceptionXml(new InputValidationException(
							"Invalid Request: parameter 'score' is invalid '" + scoreParameter + "'")),
					null);

			return;
		}

		String userLogin = req.getParameter("userLogin");
		if (userLogin == null) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
					XmlServiceExceptionConversor.toInputValidationExceptionXml(
							new InputValidationException("Invalid Request: parameter 'userLogin' is mandatory")),
					null);
			return;
		}

		try {
			MiniUberServiceFactory.getService().rateTrip(tripId, score, userLogin);
		} catch (InstanceNotFoundException ex) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND,
					XmlServiceExceptionConversor.toInstanceNotFoundException(ex), null);
			return;
		} catch (InputValidationException ex) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
					XmlServiceExceptionConversor.toInputValidationExceptionXml(ex), null);
			return;
		} catch (AlreadyRatedTripException ex) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN,
					XmlServiceExceptionConversor.toAlreadyRatedTripExceptionXml(ex), null);
			return;
		} catch (InvalidUserLoginException ex) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_CONFLICT,
					XmlServiceExceptionConversor.toInvalidUserLoginExceptionXml(ex), null);
			return;
		}
		ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NO_CONTENT, null, null);
	}

}
