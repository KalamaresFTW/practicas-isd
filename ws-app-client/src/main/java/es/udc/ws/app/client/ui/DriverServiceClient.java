package es.udc.ws.app.client.ui;

import java.util.List;

import es.udc.ws.app.client.service.ClientMiniUberService;
import es.udc.ws.app.client.service.ClientMiniUberServiceFactory;
import es.udc.ws.app.client.service.dto.ClientDriverDto;
import es.udc.ws.app.client.service.dto.ClientTripDto;
import es.udc.ws.util.exceptions.InputValidationException;

public class DriverServiceClient {

	public static void main(String[] args) {
		if (args.length == 0) {
			printUsageAndExit();
		}
		ClientMiniUberService clientMiniUberService = ClientMiniUberServiceFactory.getService();
		if ("-a".equalsIgnoreCase(args[0])) {
			validateArgs(args, 6, new int[] { 4, 5 });
			// [add] -a <name> <city> <vehicleModel> <startHour> <endHour>
			try {
				Long driverId = clientMiniUberService.addDriver(
						new ClientDriverDto(args[1], args[2], args[3], Short.valueOf(args[4]), Short.valueOf(args[5])));
				System.out.println("Driver " + driverId + " created sucessfully");
			} catch (NumberFormatException | InputValidationException ex) {
				ex.printStackTrace(System.err);
			} catch (Exception ex) {
				ex.printStackTrace(System.err);
			}

		} else if ("-u".equalsIgnoreCase(args[0])) {
			validateArgs(args, 7, new int[] { 1, 5, 6 });
			// [update] -u <driverId> <name> <city> <vehicleModel> <startHour>
			// <endHour>
			try {
				clientMiniUberService.updateDriver(new ClientDriverDto(Long.valueOf(args[1]), args[2], args[3], args[4],
						Short.valueOf(args[5]), Short.valueOf(args[6])));
				System.out.println("Driver " + args[1] + " updated sucessfully");
			} catch (NumberFormatException | InputValidationException ex) {
				ex.printStackTrace(System.err);
			} catch (Exception ex) {
				ex.printStackTrace(System.err);
			}
		} else if ("-f".equalsIgnoreCase(args[0])) {
			validateArgs(args, 2, new int[] { 1 });
			try {
				// [find] DriverServiceClient -f <driverId>
				ClientDriverDto driver = clientMiniUberService.findDriver(Long.valueOf(args[1]));
				System.out.println(driver);
			} catch (NumberFormatException ex) {
				ex.printStackTrace(System.err);
			} catch (Exception ex) {
				ex.printStackTrace(System.err);
			}
		} else if ("-t".equalsIgnoreCase(args[0])) {
			validateArgs(args, 2, new int[] { 1 });
			try {
				// [find] -f <driverId>
				List<ClientTripDto> trips = clientMiniUberService.findTripsByDriver(Long.valueOf(args[1]));

				System.out.println("Found " + trips.size() + " with driverId=" + Long.valueOf(args[1]));
				for (ClientTripDto trip : trips) {
					System.out.println(trip);
				}
			} catch (NumberFormatException ex) {
				ex.printStackTrace(System.err);
			} catch (Exception ex) {
				ex.printStackTrace(System.err);
			}
		}
	}

	public static void validateArgs(String[] args, int expectedArgs, int[] numericArguments) {
		if (expectedArgs != args.length) {
			printUsageAndExit();
		}
		for (int i = 0; i < numericArguments.length; i++) {
			int position = numericArguments[i];
			try {
				Double.parseDouble(args[position]);
			} catch (NumberFormatException n) {
				printUsageAndExit();
			}
		}
	}

	public static void printUsageAndExit() {
		printUsage();
		System.exit(-1);
	}

	public static void printUsage() {
		System.err.println("Usage:\n"
				+ "  [add]               DriverServiceClient -a <name> <city> <vehicleModel> <startHour> <endHour>\n"
				+ "  [update]            DriverServiceClient -u <driverId> <name> <city> <vehicleModel> <startHour> <endHour>\n"
				+ "  [find]              DriverServiceClient -f <driverId>\n"
				+ "  [findTripsByDriver] DriverServiceClient -t <driverId>\n");
	}
}
