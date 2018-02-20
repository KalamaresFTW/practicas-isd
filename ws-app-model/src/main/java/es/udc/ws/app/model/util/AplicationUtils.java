package es.udc.ws.app.model.util;

import es.udc.ws.util.exceptions.InputValidationException;

public class AplicationUtils {

	public static void validateShort(String propertyName, Short value, int lowerValidLimit, int upperValidLimit)
			throws InputValidationException {
		if ((value == null) || (value < lowerValidLimit) || (value > upperValidLimit)) {
			throw new InputValidationException("Invalid " + propertyName + " value (it must be greater than "
					+ lowerValidLimit + " and lower than " + upperValidLimit + "): " + value);
		}
	}

}
