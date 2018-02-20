package es.udc.ws.app.client.service.rest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import es.udc.ws.app.client.exceptions.AlreadyRatedTripException;
import es.udc.ws.app.client.exceptions.InvalidUserLoginException;
import es.udc.ws.app.client.service.ClientMiniUberService;
import es.udc.ws.app.client.service.dto.ClientDriverDto;
import es.udc.ws.app.client.service.dto.ClientTripDto;
import es.udc.ws.app.client.service.rest.xml.XmlClientDriverDtoConversor;
import es.udc.ws.app.client.service.rest.xml.XmlClientExceptionConversor;
import es.udc.ws.app.client.service.rest.xml.XmlClientTripDtoConversor;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.xml.exceptions.ParsingException;

public class RestClientMiniUberService implements ClientMiniUberService {

	private final static String ENDPOINT_ADDRESS_PARAMETER = "RestClientMiniUberService.endpointAddress";
	private String endpointAddress;

	@Override
	public Long addDriver(ClientDriverDto driver) throws InputValidationException {
		try {

			HttpResponse response = Request.Post(getEndpointAddress() + "drivers")
					.bodyStream(toInputStream(driver), ContentType.create("application/xml")).execute()
					.returnResponse();

			validateStatusCode(HttpStatus.SC_CREATED, response);

			return XmlClientDriverDtoConversor.toClientDriverDto(response.getEntity().getContent()).getDriverId();

		} catch (InputValidationException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public void updateDriver(ClientDriverDto driver) throws InputValidationException, InstanceNotFoundException {
		try {

			HttpResponse response = Request.Put(getEndpointAddress() + "drivers/" + driver.getDriverId())
					.bodyStream(toInputStream(driver), ContentType.create("application/xml")).execute()
					.returnResponse();

			validateStatusCode(HttpStatus.SC_NO_CONTENT, response);

		} catch (InputValidationException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public ClientDriverDto findDriver(Long driverId) throws InstanceNotFoundException {
		try {

			HttpResponse response = Request.Get(getEndpointAddress() + "drivers/" + driverId.toString()).execute()
					.returnResponse();

			validateStatusCode(HttpStatus.SC_OK, response);

			return XmlClientDriverDtoConversor.toClientDriverDto(response.getEntity().getContent());

		} catch (InstanceNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<ClientDriverDto> findDriverByCity(String city) {
		try {
			HttpResponse response = Request
					.Get(getEndpointAddress() + "drivers?byCity=" + URLEncoder.encode(city, "UTF-8")).execute()
					.returnResponse();

			validateStatusCode(HttpStatus.SC_OK, response);

			return XmlClientDriverDtoConversor.toClientDriverDtos(response.getEntity().getContent());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public ClientTripDto addTrip(ClientTripDto trip) throws InputValidationException, InstanceNotFoundException {
		try {
			HttpResponse response = Request.Post(getEndpointAddress() + "trips")
					.bodyStream(toInputStream(trip), ContentType.create("application/xml")).execute().returnResponse();

			validateStatusCode(HttpStatus.SC_CREATED, response);

			return XmlClientTripDtoConversor.toClientTripDto(response.getEntity().getContent());
		} catch (InputValidationException | InstanceNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void rateTrip(Long tripId, Short score, String userLogin) throws InputValidationException,
			InstanceNotFoundException, AlreadyRatedTripException, InvalidUserLoginException {
		// trips/tripId?score=score&userLogin=userLogin
		try {
			HttpResponse response = Request
					.Put(getEndpointAddress() + "trips/" + Long.valueOf(tripId)).bodyForm(Form.form()
							.add("userLogin", userLogin).add("score", Short.valueOf(score).toString()).build())
					.execute().returnResponse();
			validateStatusCode(HttpStatus.SC_NO_CONTENT, response);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<ClientTripDto> findTripsByDriver(Long driverId) throws InputValidationException {
		try {
			// GET a /trips?driverId=driverId
			HttpResponse response = Request.Get(getEndpointAddress() + "trips?driverId=" + driverId).execute()
					.returnResponse();

			validateStatusCode(HttpStatus.SC_OK, response);

			return XmlClientTripDtoConversor.toClientTripDtos(response.getEntity().getContent());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<ClientTripDto> findTripsByUser(String userLogin) throws InputValidationException {
		// GET a /trips=userLogin=userLogin
		try {
			HttpResponse response = Request.Get(getEndpointAddress() + "trips?userLogin=" + userLogin).execute()
					.returnResponse();

			validateStatusCode(HttpStatus.SC_OK, response);

			return XmlClientTripDtoConversor.toClientTripDtos(response.getEntity().getContent());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<ClientDriverDto> findDriversByUser(String userLogin) throws InputValidationException {
		// GET a drivers?userLogin=userLogin
		try {
			HttpResponse response = Request.Get(getEndpointAddress() + "drivers?userLogin=" + userLogin).execute()
					.returnResponse();
			
			validateStatusCode(HttpStatus.SC_OK, response);

			return XmlClientDriverDtoConversor.toClientDriverDtos(response.getEntity().getContent());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private synchronized String getEndpointAddress() {
		if (endpointAddress == null) {
			endpointAddress = ConfigurationParametersManager.getParameter(ENDPOINT_ADDRESS_PARAMETER);
		}
		return endpointAddress;
	}

	private InputStream toInputStream(ClientTripDto trip) {

		try {

			ByteArrayOutputStream xmlOutputStream = new ByteArrayOutputStream();
			XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());

			outputter.output(XmlClientTripDtoConversor.toXml(trip), xmlOutputStream);

			return new ByteArrayInputStream(xmlOutputStream.toByteArray());

		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	private InputStream toInputStream(ClientDriverDto trip) {

		try {

			ByteArrayOutputStream xmlOutputStream = new ByteArrayOutputStream();
			XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());

			outputter.output(XmlClientDriverDtoConversor.toXml(trip), xmlOutputStream);

			return new ByteArrayInputStream(xmlOutputStream.toByteArray());

		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	private void validateStatusCode(int successCode, HttpResponse response)
			throws InstanceNotFoundException, InputValidationException, ParsingException {

		try {

			int statusCode = response.getStatusLine().getStatusCode();

			/* Success? */
			if (statusCode == successCode) {
				return;
			}

			/* Handler error. */
			switch (statusCode) {

			case HttpStatus.SC_NOT_FOUND:
				throw XmlClientExceptionConversor.fromInstanceNotFoundExceptionXml(response.getEntity().getContent());

			case HttpStatus.SC_BAD_REQUEST:
				throw XmlClientExceptionConversor.fromInputValidationExceptionXml(response.getEntity().getContent());

			case HttpStatus.SC_FORBIDDEN:
				throw XmlClientExceptionConversor.fromAlreadyRatedTripExceptionXml(response.getEntity().getContent());

			case HttpStatus.SC_CONFLICT:
				throw XmlClientExceptionConversor.fromInvalidUserLoginExceptionXml(response.getEntity().getContent());

			default:
				throw new RuntimeException("HTTP error; status code = " + statusCode);
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

}
