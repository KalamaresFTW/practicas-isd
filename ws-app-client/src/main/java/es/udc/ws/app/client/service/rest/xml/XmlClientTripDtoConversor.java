package es.udc.ws.app.client.service.rest.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.DataConversionException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;

import es.udc.ws.app.client.service.dto.ClientTripDto;
import es.udc.ws.util.xml.exceptions.ParsingException;

public class XmlClientTripDtoConversor {

	public final static Namespace XML_NS = Namespace.getNamespace("http://ws.udc.es/miniuber/xml");

	public static Document toXml(ClientTripDto trip) throws IOException {

		Element tripElement = toJDOMElement(trip);

		return new Document(tripElement);
	}

	private static Element toJDOMElement(ClientTripDto trip) {

		Element tripElement = new Element("trip", XML_NS);

		if (trip.getTripId() != null) {
			Element identifierElement = new Element("tripId", XML_NS);
			identifierElement.setText(trip.getTripId().toString());
			tripElement.addContent(identifierElement);
		}

		Element driverIdElement = new Element("driverId", XML_NS);
		driverIdElement.setText(trip.getDriverId().toString());
		tripElement.addContent(driverIdElement);

		Element userLoginElement = new Element("userLogin", XML_NS);
		userLoginElement.setText(trip.getUserLogin());
		tripElement.addContent(userLoginElement);

		Element originLocationElement = new Element("originLocation", XML_NS);
		originLocationElement.setText(trip.getOriginLocation());
		tripElement.addContent(originLocationElement);

		Element destinationLocationElement = new Element("destinationLocation", XML_NS);
		destinationLocationElement.setText(trip.getDestinationLocation());
		tripElement.addContent(destinationLocationElement);

		Element creditCardNumberElement = new Element("creditCardNumber", XML_NS);
		creditCardNumberElement.setText(trip.getCreditCardNumber());
		tripElement.addContent(creditCardNumberElement);

		return tripElement;
	}
	
	public static List<ClientTripDto> toClientTripDtos(InputStream tripsXML) {
        try {

            SAXBuilder builder = new SAXBuilder();
            Document document = builder.build(tripsXML);
            Element rootElement = document.getRootElement();

            if (!"trips".equalsIgnoreCase(rootElement.getName())) {
                throw new ParsingException("Unrecognized element '"
                        + rootElement.getName() + "' ('trips' expected)");
            }
            List<Element> children = rootElement.getChildren();
            List<ClientTripDto> tripDtos = new ArrayList<>(children.size());
            for (int i = 0; i < children.size(); i++) {
                Element element = children.get(i);
                tripDtos.add(toClientTripDto(element));
            }

            return tripDtos;
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
	}

	public static ClientTripDto toClientTripDto(InputStream tripXML) throws ParsingException {
		try {

			SAXBuilder builder = new SAXBuilder();
			Document document = builder.build(tripXML);
			Element rootElement = document.getRootElement();

			return toClientTripDto(rootElement);
		} catch (ParsingException ex) {
			throw ex;
		} catch (Exception e) {
			throw new ParsingException(e);
		}
	}

	private static ClientTripDto toClientTripDto(Element tripElement) throws ParsingException, DataConversionException {
		if (!"trip".equals(tripElement.getName())) {
			throw new ParsingException("Unrecognized element '" + tripElement.getName() + "' ('trip' expected)");
		}
		Element identifierElement = tripElement.getChild("tripId", XML_NS);
		Long identifier = null;

		if (identifierElement != null) {
			identifier = Long.valueOf(identifierElement.getTextTrim());
		}

		Element driverIdElement = tripElement.getChild("driverId", XML_NS);
		Long driverId = null;
		if (driverIdElement != null) {
			driverId = Long.valueOf(driverIdElement.getTextTrim());
		}
		String userLogin = tripElement.getChildTextNormalize("userLogin", XML_NS);
		String originLocation = tripElement.getChildTextNormalize("originLocation", XML_NS);
		String destinationLocation = tripElement.getChildTextNormalize("destinationLocation", XML_NS);
		String bookingDate = tripElement.getChildTextNormalize("bookingDate", XML_NS);

		return new ClientTripDto(identifier, driverId, userLogin, originLocation, destinationLocation, bookingDate);
	}

}
