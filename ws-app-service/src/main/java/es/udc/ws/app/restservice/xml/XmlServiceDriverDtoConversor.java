package es.udc.ws.app.restservice.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.jdom2.DataConversionException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;

import es.udc.ws.app.dto.ServiceDriverDto;
import es.udc.ws.util.xml.exceptions.ParsingException;

public class XmlServiceDriverDtoConversor {

	public final static Namespace XML_NS = Namespace.getNamespace("http://ws.udc.es/miniuber/xml");

	public static Document toXml(ServiceDriverDto driver) throws IOException {

		Element driverElement = toJDOMElement(driver);

		return new Document(driverElement);
	}
	
    public static Document toXml(List<ServiceDriverDto> drivers) throws IOException {

        Element driversElement = new Element("drivers", XML_NS);
        for (int i = 0; i < drivers.size(); i++) {
            ServiceDriverDto xmlDriverDto = drivers.get(i);
            Element driverElement = toJDOMElement(xmlDriverDto);
            driversElement.addContent(driverElement);
        }

        return new Document(driversElement);
    }

	public static Element toJDOMElement(ServiceDriverDto driver) {

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

		if (driver.getVehicleModel() != null) {
			Element vehicleModelElement = new Element("vehicleModel", XML_NS);
			vehicleModelElement.setText(driver.getVehicleModel());
			driverElement.addContent(vehicleModelElement);
		}

		if (driver.getStartHour() != null) {
			Element startHourElement = new Element("startHour", XML_NS);
			startHourElement.setText(Short.toString(driver.getStartHour()));
			driverElement.addContent(startHourElement);
		}

		if (driver.getEndHour() != null) {
			Element endHourElement = new Element("endHour", XML_NS);
			endHourElement.setText(Short.toString(driver.getEndHour()));
			driverElement.addContent(endHourElement);
		}

		Element scoreElement = new Element("score", XML_NS);
		scoreElement.setText(Double.toString(driver.getScore())); 
		driverElement.addContent(scoreElement);

		return driverElement;  
	}

	public static ServiceDriverDto toServiceDriverDto(InputStream driverXml) throws ParsingException {
		try {
			SAXBuilder builder = new SAXBuilder();
			Document document = builder.build(driverXml);
			Element rootElement = document.getRootElement();
			return toServiceDriverDto(rootElement);
		} catch (ParsingException ex) {
			throw ex;
		} catch (Exception e) {
			throw new ParsingException(e);
		}
	}

	private static ServiceDriverDto toServiceDriverDto(Element driverElement)
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
		String vehicleModel = driverElement.getChildTextNormalize("vehicleModel", XML_NS);
		Short startHour = Short.valueOf(driverElement.getChildTextTrim("startHour", XML_NS));
		Short endHour = Short.valueOf(driverElement.getChildTextTrim("endHour", XML_NS));

		return new ServiceDriverDto(identifier, name, city, vehicleModel, startHour, endHour);
	}

}
