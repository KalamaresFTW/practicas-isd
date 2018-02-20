package es.udc.ws.app.client.ui;

import java.util.List;

import es.udc.ws.app.client.service.ClientMiniUberService;
import es.udc.ws.app.client.service.ClientMiniUberServiceFactory;
import es.udc.ws.app.client.service.dto.ClientDriverDto;
import es.udc.ws.app.client.service.dto.ClientTripDto;
import es.udc.ws.util.exceptions.InputValidationException;

public class UserServiceClient {

	public static void main(String[] args) {
		if (args.length == 0) {
			printUsageAndExit();
		}
		ClientMiniUberService clientMiniUberService = ClientMiniUberServiceFactory.getService();
		if ("-a".equalsIgnoreCase(args[0])) {
			validateArgs(args, 6, new int[] { 1 });
			// [addTrip] <driverId> <userLogin> <originLocation>
			// <destinationLocation> <creditCardNumber>
			try {
				ClientTripDto trip = clientMiniUberService
						.addTrip(new ClientTripDto(Long.valueOf(args[1]), args[2], args[3], args[4], args[5]));
				System.out.println("Trip " + trip.getTripId() + " created sucessfully");
			} catch (NumberFormatException | InputValidationException ex) {
				ex.printStackTrace(System.err);
			} catch (Exception ex) {
				ex.printStackTrace(System.err);
			}

		} else if ("-f".equalsIgnoreCase(args[0])) {
			// [findAvailableDrivers] UserServiceClient -f <city>
			validateArgs(args, 2, new int[] {});
			if (args[1].isEmpty()) {
				printUsageAndExit();
			}
			try {
				List<ClientDriverDto> drivers = clientMiniUberService.findDriverByCity(args[1]);
				if (drivers.isEmpty()) {
					System.out.println("No available drivers found in " + args[1]);
				} else {
					for (ClientDriverDto driver : drivers) {
						System.out.println(driver);
					}
				}
			} catch (NumberFormatException ex) {
				ex.printStackTrace(System.err);
			} catch (Exception ex) {
				ex.printStackTrace(System.err);
			}
		} else if ("-r".equalsIgnoreCase(args[0])) {
			// [rateTrip] -r <tripId> <score> <userLogin>
			validateArgs(args, 4, new int[] { 1, 2 });
			try {
				clientMiniUberService.rateTrip(Long.valueOf(args[1]), Short.valueOf(args[2]), args[3]);
				System.out.println("Trip " + args[1] + " succesfully rated.");
			} catch (NumberFormatException ex) {
				ex.printStackTrace(System.err);
			} catch (Exception ex) {
				ex.printStackTrace(System.err);
			}
		} else if ("-u".equalsIgnoreCase(args[0])) {
			// [rateTrip] -r <tripId> <score> <userLogin>
			validateArgs(args, 2, new int[] {});
			if (args[1].isEmpty()) {
				printUsageAndExit();
			}
			try {
				List<ClientTripDto> trips = clientMiniUberService.findTripsByUser(args[1]);
				System.out.println("Found " + trips.size() + " trips with userLogin: " + args[1]);
				for (ClientTripDto trip : trips) {
					System.out.println(trip);
				}
			} catch (NumberFormatException ex) {
				ex.printStackTrace(System.err);
			} catch (Exception ex) {
				ex.printStackTrace(System.err);
			}
		} else if ("-d".equalsIgnoreCase(args[0])) {
			// [findDriversByUser] -d <driverId>
			validateArgs(args, 2, new int[] {});
			if (args[1].isEmpty()) {
				printUsageAndExit();
			}
			try {
				List<ClientDriverDto> drivers = clientMiniUberService.findDriversByUser(args[1]);
				System.out.println("Found " + drivers.size() + " drivers that made trips for user: " + args[1]);
				for (ClientDriverDto driver : drivers) {
					System.out.println("DriverId: " + driver.getDriverId() + " - Name: " + driver.getName()
							+ " - City: " + driver.getCity() + " - Score: " + driver.getScore());
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
				+ "  [addTrip]     UserServiceClient -a <driverId> <userLogin> <originLocation> <destinationLocation> <creditCardNumber> \n"
				+ "  [rateTrip]    UserServiceClient -r <tripId> <score> <userLogin> \n"
				+ "  [findAvailableDrivers] UserServiceClient -f <city>\n"
				+ "  [findTripsByUser] UserServiceClient -u <userLogin>\n"
				+ "  [findDriversByUser] UserServiceClient -d <driverId>\n");
	}

}
