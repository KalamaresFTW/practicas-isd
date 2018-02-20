package es.udc.ws.app.restservice.xml;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.List;

import org.jdom2.DataConversionException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;
import es.udc.ws.app.dto.ServiceTripDto;
import es.udc.ws.util.xml.exceptions.ParsingException;

public class XmlServiceTripDtoConversor {

	public final static Namespace XML_NS = Namespace.getNamespace("http://ws.udc.es/miniuber/xml");

	public static Document toXml(ServiceTripDto trip) throws IOException {

		Element tripElement = toJDOMElement(trip);

		return new Document(tripElement);
	}
	
    public static Document toXml(List<ServiceTripDto> trips) throws IOException {

        Element tripsElement = new Element("trips", XML_NS);
        for (int i = 0; i < trips.size(); i++) {
            ServiceTripDto xmlTripDto = trips.get(i);
            Element tripElement = toJDOMElement(xmlTripDto);
            tripsElement.addContent(tripElement);
        }
        return new Document(tripsElement);
    }


	private static Element toJDOMElement(ServiceTripDto trip) {
		Element tripElement = new Element("trip", XML_NS);
		
		if (trip.getTripId() != null) {
			Element tripIdElement = new Element("tripId", XML_NS);
			tripIdElement.setText(trip.getTripId().toString());
			tripElement.addContent(tripIdElement);
		}

		if (trip.getDriverId() != null) {
			Element driverIdElement = new Element("driverId", XML_NS);
			driverIdElement.setText(trip.getDriverId().toString());
			tripElement.addContent(driverIdElement);
		}
		
		if (trip.getUserLogin() != null) {
			Element userLoginElement = new Element("userLogin", XML_NS);
			userLoginElement.setText(trip.getUserLogin());
			tripElement.addContent(userLoginElement);
		}
		
		if (trip.getOriginLocation() != null) {
			Element originLocationElement = new Element("originLocation", XML_NS);
			originLocationElement.setText(trip.getOriginLocation());
			tripElement.addContent(originLocationElement);
		}
		
		if (trip.getDestinationLocation() != null) {
			Element destinationLocationElement = new Element("destinationLocation", XML_NS);
			destinationLocationElement.setText(trip.getDestinationLocation());
			tripElement.addContent(destinationLocationElement);
		}
		
		if (trip.getBookingDate() != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Element bookingDateElement = new Element("bookingDate", XML_NS);
			bookingDateElement.setText(sdf.format(trip.getBookingDate().getTime().getTime()));
			tripElement.addContent(bookingDateElement);
		}
		return tripElement;
	}
	

	public static ServiceTripDto toServiceTripDto(InputStream tripXml) throws ParsingException {
		try {
			SAXBuilder builder = new SAXBuilder();
			Document document = builder.build(tripXml);
			Element rootElement = document.getRootElement();
			return toServiceTripDto(rootElement);
		} catch (ParsingException ex) {
			throw ex;
		} catch (Exception e) {
			throw new ParsingException(e);
		}
	}

	private static ServiceTripDto toServiceTripDto(Element tripElement)
			throws ParsingException, DataConversionException {
		if (!"trip".equals(tripElement.getName())) {
			throw new ParsingException("Unrecognized element '" + tripElement.getName() + "' ('trip' expected)");
		}
		
		Element driverIdElement = tripElement.getChild("driverId", XML_NS);
		Long driverId = null;
		if (driverIdElement != null) {
			driverId = Long.valueOf(driverIdElement.getTextTrim());
		}
		
		String userLogin = tripElement.getChildTextNormalize("userLogin", XML_NS);
		String originLocation = tripElement.getChildTextNormalize("originLocation", XML_NS);
		String destinationLocation = tripElement.getChildTextNormalize("destinationLocation", XML_NS);
		String creditCardNumber = tripElement.getChildTextNormalize("creditCardNumber", XML_NS);

		return new ServiceTripDto(driverId, userLogin, originLocation, destinationLocation, creditCardNumber);
	}

	
}
