package es.udc.ws.app.restservice.xml;

import java.io.IOException;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;

import es.udc.ws.app.model.miniuberservice.exceptions.AlreadyRatedTripException;
import es.udc.ws.app.model.miniuberservice.exceptions.InvalidUserLoginException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public class XmlServiceExceptionConversor {

	public final static Namespace XML_NS = Namespace.getNamespace("http://ws.udc.es/miniuber/xml");

	public static Document toInputValidationExceptionXml(InputValidationException ex) throws IOException {

		Element exceptionElement = new Element("InputValidationException", XML_NS);

		Element messageElement = new Element("message", XML_NS);
		messageElement.setText(ex.getMessage());
		exceptionElement.addContent(messageElement);

		return new Document(exceptionElement);
	}

	public static Document toInstanceNotFoundException(InstanceNotFoundException ex) throws IOException {

		Element exceptionElement = new Element("InstanceNotFoundException", XML_NS);

		if (ex.getInstanceId() != null) {
			Element instanceIdElement = new Element("instanceId", XML_NS);
			instanceIdElement.setText(ex.getInstanceId().toString());

			exceptionElement.addContent(instanceIdElement);
		}

		if (ex.getInstanceType() != null) {
			Element instanceTypeElement = new Element("instanceType", XML_NS);
			instanceTypeElement.setText(ex.getInstanceType());

			exceptionElement.addContent(instanceTypeElement);
		}
		return new Document(exceptionElement);
	}
	
	/*
	public static Document toDriverNotAvailable(DriverNotAvaliableException ex) throws IOException {

		Element exceptionElement = new Element("DriverNotAvaliableException", XML_NS);

		if (ex.getDriverId() != null) {
			Element instanceIdElement = new Element("driverId", XML_NS);
			instanceIdElement.setText(ex.getDriverId().toString());

			exceptionElement.addContent(instanceIdElement);
		}

		return new Document(exceptionElement);
	}*/

	public static Document toAlreadyRatedTripExceptionXml(AlreadyRatedTripException ex) {
		Element exceptionElement = new Element("AlreadyRatedTripException", XML_NS);
		if (ex.getTripId() != null) {
			Element tripIdElement = new Element("tripId", XML_NS);
			tripIdElement.setText(ex.getTripId().toString());
			exceptionElement.addContent(tripIdElement);
		}
		return new Document(exceptionElement);
	}

	public static Document toInvalidUserLoginExceptionXml(InvalidUserLoginException ex) {
		Element exceptionElement = new Element("InvalidUserLogin", XML_NS);
		if (ex.getTripId() != null) {
			Element tripIdElement = new Element("tripId", XML_NS);
			tripIdElement.setText(ex.getTripId().toString());
			exceptionElement.addContent(tripIdElement);
		}
		if (ex.getUserLogin() != null) {
			Element userLoginElement = new Element("userLogin", XML_NS);
			userLoginElement.setText(ex.getUserLogin());
			exceptionElement.addContent(userLoginElement);
		}
		return new Document(exceptionElement);
	}
	
}
