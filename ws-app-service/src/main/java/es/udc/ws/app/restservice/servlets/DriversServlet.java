package es.udc.ws.app.restservice.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.udc.ws.app.dto.ServiceDriverDto;

import es.udc.ws.app.model.driver.Driver;
import es.udc.ws.app.model.miniuberservice.MiniUberService;
import es.udc.ws.app.model.miniuberservice.MiniUberServiceFactory;
import es.udc.ws.app.model.trip.Trip;
import es.udc.ws.app.restservice.xml.XmlServiceDriverDtoConversor;
import es.udc.ws.app.restservice.xml.XmlServiceExceptionConversor;

import es.udc.ws.app.serviceutil.DriverToDriverDtoConversor;

import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.servlet.ServletUtils;
import es.udc.ws.util.xml.exceptions.ParsingException;

@SuppressWarnings("serial")
public class DriversServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ServiceDriverDto xmlDriver;
		try {
			xmlDriver = XmlServiceDriverDtoConversor.toServiceDriverDto(req.getInputStream());
		} catch (ParsingException ex) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST, XmlServiceExceptionConversor
					.toInputValidationExceptionXml(new InputValidationException(ex.getMessage())), null);

			return;
		}
		Driver driver = DriverToDriverDtoConversor.toDriver(xmlDriver);
		try {
			driver = MiniUberServiceFactory.getService().addDriver(driver);
		} catch (InputValidationException ex) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
					XmlServiceExceptionConversor.toInputValidationExceptionXml(ex), null);
			return;
		}
		ServiceDriverDto driverDto = DriverToDriverDtoConversor.toDriverDto(driver);
		String driverURL = ServletUtils.normalizePath(req.getRequestURL().toString()) + "/" + driver.getDriverId();
		Map<String, String> headers = new HashMap<>(1);
		headers.put("Location", driverURL);
		ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_CREATED,
				XmlServiceDriverDtoConversor.toXml(driverDto), headers);
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = ServletUtils.normalizePath(req.getPathInfo());
		if (path == null || path.length() == 0) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST, XmlServiceExceptionConversor
					.toInputValidationExceptionXml(new InputValidationException("Invalid Request: invalid driver id")),
					null);
			return;
		}
		String driverIdAsString = path.substring(1);
		Long driverId;
		try {
			driverId = Long.valueOf(driverIdAsString);
		} catch (NumberFormatException ex) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
					XmlServiceExceptionConversor.toInputValidationExceptionXml(new InputValidationException(
							"Invalid Request: invalid driver id '" + driverIdAsString + "'")),
					null);
			return;
		}
		ServiceDriverDto driverDto;
		try {
			driverDto = XmlServiceDriverDtoConversor.toServiceDriverDto(req.getInputStream());
		} catch (ParsingException ex) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST, XmlServiceExceptionConversor
					.toInputValidationExceptionXml(new InputValidationException(ex.getMessage())), null);
			return;

		}
		if (!driverId.equals(driverDto.getDriverId())) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST, XmlServiceExceptionConversor
					.toInputValidationExceptionXml(new InputValidationException("Invalid Request: invalid driverId")),
					null);
			return;
		}
		Driver driver = DriverToDriverDtoConversor.toDriver(driverDto);
		try {
			Driver foundDriver = MiniUberServiceFactory.getService().findDriver(driverId);
			driver.setTotalScore(foundDriver.getTotalScore());
			driver.setScoreCount(foundDriver.getScoreCount());
			MiniUberServiceFactory.getService().updateDriver(driver);
		} catch (InputValidationException ex) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
					XmlServiceExceptionConversor.toInputValidationExceptionXml(ex), null);
			return;
		} catch (InstanceNotFoundException ex) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND,
					XmlServiceExceptionConversor.toInstanceNotFoundException(ex), null);
			return;
		}
		ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NO_CONTENT, null, null);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String userLoginParameter = req.getParameter("userLogin");

		if (userLoginParameter != null && !userLoginParameter.isEmpty()) {
			try {
				MiniUberService service = MiniUberServiceFactory.getService();
				ArrayList<Trip> trips;
				trips = (ArrayList<Trip>) service.findTripsByUser(userLoginParameter);

				if (!trips.isEmpty()) {
					ArrayList<Long> foundDriversIds = new ArrayList<>();
					for (Trip trip : trips) {
						if (!foundDriversIds.contains((Long) trip.getDriverId())) {
							foundDriversIds.add(trip.getDriverId());
						}
					}
					Integer totalScore = 0;
					Integer tripCount = 0;
					Driver driver;
					ArrayList<Driver> resultDrivers = new ArrayList<>();
					for (Long driverId : foundDriversIds) {
						for (Trip trip : trips) {
							if (driverId == trip.getDriverId()) {
								tripCount++;
								totalScore += trip.getScore();
							}
						}
						try {
							driver = service.findDriver(driverId);
							driver.setStartHour(null);
							driver.setEndHour(null);
							driver.setVehicleModel(null);
							driver.setTotalScore(totalScore);
							driver.setScoreCount(tripCount);
							resultDrivers.add(driver);
						} catch (InstanceNotFoundException e) {
							System.err.println("No se ha encontrado el conductor con el Id: " + driverId);
						}
						totalScore = 0;
						tripCount = 0;
					}
					List<ServiceDriverDto> driverDtos = DriverToDriverDtoConversor.toDriverDtos(resultDrivers);
					ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
							XmlServiceDriverDtoConversor.toXml(driverDtos), null);
					return;

				}
			} catch (NumberFormatException | InputValidationException ex) {
				ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
						XmlServiceExceptionConversor.toInputValidationExceptionXml(new InputValidationException(
								"Invalid Request: parameter 'userLogin' is invalid '" + userLoginParameter + "'")),
						null);
				return;

			}
		} 
		String path = ServletUtils.normalizePath(req.getPathInfo());
		if (path == null || path.length() == 0) {
			String city = req.getParameter("city");
			List<Driver> drivers = MiniUberServiceFactory.getService().findDriverByCity(city);
			List<ServiceDriverDto> driverDtos = DriverToDriverDtoConversor.toDriverDtos(drivers);
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
					XmlServiceDriverDtoConversor.toXml(driverDtos), null);
		} else {
			String driverIdAsString = path.substring(1);
			Long driverId;
			try {
				driverId = Long.valueOf(driverIdAsString);
			} catch (NumberFormatException ex) {
				ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
						XmlServiceExceptionConversor.toInputValidationExceptionXml(new InputValidationException(
								"Invalid Request: " + "invalid driver id'" + driverIdAsString + "'")),
						null);
				return;
			}
			Driver driver;
			try {
				driver = MiniUberServiceFactory.getService().findDriver(driverId);
			} catch (InstanceNotFoundException ex) {
				ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND,
						XmlServiceExceptionConversor.toInstanceNotFoundException(ex), null);
				return;
			}
			ServiceDriverDto driverDto = DriverToDriverDtoConversor.toDriverDto(driver);
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
					XmlServiceDriverDtoConversor.toXml(driverDto), null);
		} 
		ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST, null, null);
		return;
	}
}
