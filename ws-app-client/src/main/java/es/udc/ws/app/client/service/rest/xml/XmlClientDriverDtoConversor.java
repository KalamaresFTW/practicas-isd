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

import es.udc.ws.app.client.service.dto.ClientDriverDto;
import es.udc.ws.util.xml.exceptions.ParsingException;

public class XmlClientDriverDtoConversor {

	public final static Namespace XML_NS = Namespace.getNamespace("http://ws.udc.es/miniuber/xml");

	public static Document toXml(ClientDriverDto driver) throws IOException {

		Element driverElement = toJDOMElement(driver);

		return new Document(driverElement);
	}

	public static Element toJDOMElement(ClientDriverDto driver) {

		Element driverElement = new Element("driver", XML_NS);

		if (driver.getDriverId() != null) {
			Element identifierElement = new Element("driverId", XML_NS);
			identifierElement.setText(driver.getDriverId().toString());
			driverElement.addContent(identifierElement);
		}

		Element nameElement = new Element("name", XML_NS);
		nameElement.setText(driver.getName());
		driverElement.addContent(nameElement);

		Element cityElement = new Element("city", XML_NS);
		cityElement.setText(driver.getCity());
		driverElement.addContent(cityElement);

		Element vehicleModelElement = new Element("vehicleModel", XML_NS);
		vehicleModelElement.setText(driver.getVehicleModel());
		driverElement.addContent(vehicleModelElement);

		Element startHourElement = new Element("startHour", XML_NS);
		startHourElement.setText(driver.getStartHour().toString());
		driverElement.addContent(startHourElement);

		Element endHourElement = new Element("endHour", XML_NS);
		endHourElement.setText(driver.getEndHour().toString());
		driverElement.addContent(endHourElement);

		return driverElement;
	}

	public static List<ClientDriverDto> toClientDriverDtos(InputStream driversXml) throws ParsingException {
		try {

			SAXBuilder builder = new SAXBuilder();
			Document document = builder.build(driversXml);
			Element rootElement = document.getRootElement();

			if (!"drivers".equalsIgnoreCase(rootElement.getName())) {
				throw new ParsingException("Unrecognized element '" + rootElement.getName() + "' ('drivers' expected)");
			}
			List<Element> children = rootElement.getChildren();
			List<ClientDriverDto> driverDtos = new ArrayList<>(children.size());
			
			for (int i = 0; i < children.size(); i++) {
				Element element = children.get(i);
				driverDtos.add(toClientDriverDto(element));
			}

			return driverDtos;
		} catch (ParsingException ex) {
			throw ex;
		} catch (Exception e) {
			throw new ParsingException(e);
		}
	}

	public static ClientDriverDto toClientDriverDto(InputStream driverXML) throws ParsingException {
		try {

			SAXBuilder builder = new SAXBuilder();
			Document document = builder.build(driverXML);
			Element rootElement = document.getRootElement();

			return toClientDriverDto(rootElement);
		} catch (ParsingException ex) {
			throw ex;
		} catch (Exception e) {
			throw new ParsingException(e);
		}
	}

	private static ClientDriverDto toClientDriverDto(Element driverElement)
			throws ParsingException, DataConversionException {
		if (!"driver".equals(driverElement.getName())) {
			throw new ParsingException("Unrecognized element '" + driverElement.getName() + "' ('driver' expected)");
		}
		Element identifierElement = driverElement.getChild("driverId", XML_NS);
		Long identifier = null;

		if (identifierElement != null) {
			identifier = Long.valueOf(identifierElement.getTextTrim());
		}

		String name = driverElement.getChildTextNormalize("name", XML_NS);

		String city = driverElement.getChildTextNormalize("city", XML_NS);

		Element vehicleModelElement = driverElement.getChild("vehicleModel", XML_NS);
		String vehicleModel = null;
		if (vehicleModelElement != null) {
			vehicleModel = vehicleModelElement.getTextNormalize();
		}
		
		Element startHourElement = driverElement.getChild("startHour", XML_NS);
		Short startHour = null;
		if (startHourElement != null) {
			startHour = Short.valueOf(startHourElement.getTextNormalize());
		}
		
		Element endHourElement = driverElement.getChild("endHour", XML_NS);
		Short endHour = null;
		if (endHourElement != null) {
			endHour = Short.valueOf(endHourElement.getTextNormalize());
		}

		Double score = Double.valueOf(driverElement.getChildTextTrim("score", XML_NS));

		return new ClientDriverDto(identifier, name, city, vehicleModel, startHour, endHour, score);
	}

}
