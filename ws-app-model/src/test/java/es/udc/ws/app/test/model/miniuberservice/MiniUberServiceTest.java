package es.udc.ws.app.test.model.miniuberservice;

import static es.udc.ws.app.model.util.ModelConstants.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.sql.DataSource;

import org.junit.BeforeClass;
import org.junit.Test;

import es.udc.ws.app.model.driver.Driver;
import es.udc.ws.app.model.driver.SqlDriverDao;
import es.udc.ws.app.model.driver.SqlDriverDaoFactory;
import es.udc.ws.app.model.miniuberservice.MiniUberService;
import es.udc.ws.app.model.miniuberservice.MiniUberServiceFactory;
import es.udc.ws.app.model.miniuberservice.exceptions.AlreadyRatedTripException;
import es.udc.ws.app.model.miniuberservice.exceptions.InvalidUserLoginException;
import es.udc.ws.app.model.trip.SqlTripDao;
import es.udc.ws.app.model.trip.SqlTripDaoFactory;
import es.udc.ws.app.model.trip.Trip;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.sql.DataSourceLocator;
import es.udc.ws.util.sql.SimpleDataSource;

public class MiniUberServiceTest {

	private static final String VALID_DRIVER_NAME = "Driver";
	private static final String INVALID_DRIVER_NAME = "";
	private static final String VALID_CITY_NAME = "A CoruÃ±a";
	private static final String OTHER_CITY_NAME = "Pontevedra";
	private static final long INVALID_DRIVER_ID = -1;

	private static final String VALID_CREDIT_CARD_NUMBER = "1234567890123456";
	@SuppressWarnings("unused")
	private static final String INVALID_CREDIT_CARD_NUMBER = "";

	private static MiniUberService miniUberService = null;

	private static SqlDriverDao driverDao = null;

	private static SqlTripDao tripDao = null;

	private static int driverIndex = 1; // Used to generate dummy data

	@BeforeClass
	public static void init() {
		DataSource dataSource = new SimpleDataSource();

		DataSourceLocator.addDataSource(MINIUBER_DATA_SOURCE, dataSource);

		miniUberService = MiniUberServiceFactory.getService();

		driverDao = SqlDriverDaoFactory.getDao();

		tripDao = SqlTripDaoFactory.getDao();
	}

	// Used to test the findByCity call to the service
	private Driver getValidDriver(String city, Short startHour, Short endHour) {
		return new Driver("Driver " + driverIndex, city, "Vehicle Model " + driverIndex++, (short) startHour,
				(short) endHour);
	}

	private Driver getValidDriver(String name, String city) {
		return new Driver(name, city, "Vehicle model", (short) 20, (short) 22);
	}

	private Driver getValidDriver(String name) {
		return getValidDriver(name, "city");
	}

	private Driver getValidDriver() {
		return getValidDriver("Driver name");
	}

	private Trip getValidTrip(Long driverId, String userLogin, String originLocation, String destinationLocation,
			String cardNumber) {
		return new Trip(driverId, userLogin, originLocation, destinationLocation, cardNumber);

	}

	private void updateDriver(Driver driver) {
		DataSource dataSource = DataSourceLocator.getDataSource(MINIUBER_DATA_SOURCE);
		try (Connection connection = dataSource.getConnection()) {
			try {
				/* Prepare connection. */
				connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				connection.setAutoCommit(false);
				driverDao.update(connection, driver);
				/* Commit. */
				connection.commit();
			} catch (InstanceNotFoundException e) {
				connection.commit();
				throw new RuntimeException(e);
			} catch (SQLException e) {
				connection.rollback();
				throw new RuntimeException(e);
			} catch (RuntimeException | Error e) {
				connection.rollback();
				throw e;
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private Driver createDriver(Driver driver) {
		Driver addedDriver = null;
		try {
			addedDriver = miniUberService.addDriver(driver);
		} catch (InputValidationException e) {
			throw new RuntimeException(e);
		}
		return addedDriver;
	}

	@SuppressWarnings("unused")
	private Trip createTrip(Trip trip) {
		Trip addedTrip = null;
		try {
			addedTrip = miniUberService.addTrip(trip);
		} catch (InputValidationException e) {
			throw new RuntimeException(e);
		} catch (InstanceNotFoundException e) {
			throw new RuntimeException(e);
		}
		return addedTrip;
	}

	private Trip findTrip(Trip trip) {
		DataSource dataSource = DataSourceLocator.getDataSource(TRIP_DATA_SOURCE);
		try (Connection connection = dataSource.getConnection()) {
			try {
				/* Prepare connection. */
				connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				connection.setAutoCommit(false);
				Trip foundTrip = tripDao.find(connection, trip.getTripId());
				/* Commit. */
				connection.commit();
				return foundTrip;
			} catch (InstanceNotFoundException e) {
				connection.commit();
				throw new RuntimeException(e);
			} catch (SQLException e) {
				connection.rollback();
				throw new RuntimeException(e);
			} catch (RuntimeException | Error e) {
				connection.rollback();
				throw e;
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unused")
	private void removeTrip(Long tripId) {
		DataSource dataSource = DataSourceLocator.getDataSource(TRIP_DATA_SOURCE);
		try (Connection connection = dataSource.getConnection()) {
			try {
				/* Prepare connection. */
				connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				connection.setAutoCommit(false);
				/* Do work. */
				tripDao.remove(connection, tripId);
				/* Commit. */
				connection.commit();
			} catch (InstanceNotFoundException e) {
				connection.commit();
				throw new RuntimeException(e);
			} catch (SQLException e) {
				connection.rollback();
				throw new RuntimeException(e);
			} catch (RuntimeException | Error e) {
				connection.rollback();
				throw e;
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private void removeDriver(Long driverId) {
		DataSource dataSource = DataSourceLocator.getDataSource(MINIUBER_DATA_SOURCE);
		try (Connection connection = dataSource.getConnection()) {
			try {
				/* Prepare connection. */
				connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				connection.setAutoCommit(false);
				/* Do work. */
				driverDao.remove(connection, driverId);
				/* Commit. */
				connection.commit();
			} catch (InstanceNotFoundException e) {
				connection.commit();
				throw new RuntimeException(e);
			} catch (SQLException e) {
				connection.rollback();
				throw new RuntimeException(e);
			} catch (RuntimeException | Error e) {
				connection.rollback();
				throw e;
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Test(expected = InputValidationException.class)
	public void testAddAndFindDriver() throws InputValidationException, InstanceNotFoundException {
		Driver driver = getValidDriver(VALID_DRIVER_NAME, VALID_CITY_NAME);

		Driver addedDriver = miniUberService.addDriver(driver);
		Driver foundDriver = miniUberService.findDriver(addedDriver.getDriverId());

		assertEquals(addedDriver, foundDriver);
		removeDriver(addedDriver.getDriverId());

		driver = getValidDriver(INVALID_DRIVER_NAME, VALID_CITY_NAME);
		miniUberService.addDriver(driver);
	}

	@Test
	public void testAddInvalidDriver() {
		Driver driver = getValidDriver();
		Driver addedDriver = null;
		boolean exceptionCatched = false;

		try {
			// Check driver name not null
			driver.setName(null);
			try {
				addedDriver = miniUberService.addDriver(driver);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);

			// Check driver name not empty
			exceptionCatched = false;
			driver = getValidDriver();
			driver.setName("");
			try {
				addedDriver = miniUberService.addDriver(driver);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);

			// Check city not null
			exceptionCatched = false;
			driver = getValidDriver();
			driver.setCity(null);
			try {
				addedDriver = miniUberService.addDriver(driver);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);

			// Check city not empty
			exceptionCatched = false;
			driver = getValidDriver();
			driver.setCity("");
			try {
				addedDriver = miniUberService.addDriver(driver);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);

			// Check start hour not null
			exceptionCatched = false;
			driver = getValidDriver();
			driver.setStartHour(null);
			try {
				addedDriver = miniUberService.addDriver(driver);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);

			// Check start hour not > 24
			exceptionCatched = false;
			driver = getValidDriver();
			driver.setStartHour((short) 25);
			try {
				addedDriver = miniUberService.addDriver(driver);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);

			// Check start hour not < 0
			exceptionCatched = false;
			driver = getValidDriver();
			driver.setStartHour((short) -3);
			try {
				addedDriver = miniUberService.addDriver(driver);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);

			// Check end hour not null
			exceptionCatched = false;
			driver = getValidDriver();
			driver.setEndHour(null);
			try {
				addedDriver = miniUberService.addDriver(driver);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);

			// Check end hour not > 24
			exceptionCatched = false;
			driver = getValidDriver();
			driver.setEndHour((short) 30);
			try {
				addedDriver = miniUberService.addDriver(driver);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);

			// Check end hour not < 0
			exceptionCatched = false;
			driver = getValidDriver();
			driver.setEndHour((short) -1);
			try {
				addedDriver = miniUberService.addDriver(driver);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);

			// Check VehicleModel not null
			exceptionCatched = false;
			driver = getValidDriver();
			driver.setVehicleModel(null);
			try {
				addedDriver = miniUberService.addDriver(driver);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);

			// Check VehicleModel not empty
			exceptionCatched = false;
			driver = getValidDriver();
			driver.setVehicleModel("");
			try {
				addedDriver = miniUberService.addDriver(driver);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);

		} finally {
			if (!exceptionCatched) {
				// Clear Database
				removeDriver(addedDriver.getDriverId());
			}
		}

	}

	@Test
	public void testUpdateDriver() throws InputValidationException, InstanceNotFoundException {
		Driver driver = getValidDriver();

		Driver addedDriver = miniUberService.addDriver(driver);

		addedDriver.setCity("New city");
		addedDriver.setVehicleModel("New vehicle Model");
		addedDriver.setStartHour((short) 10);
		addedDriver.setEndHour((short) 12);

		miniUberService.updateDriver(addedDriver);

		Driver foundDriver = miniUberService.findDriver(addedDriver.getDriverId());

		assertEquals(addedDriver, foundDriver);

		removeDriver(foundDriver.getDriverId());
	}

	@Test
	public void testFindByCity() throws InputValidationException {
		short currentHour = (short) Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

		ArrayList<Driver> driverList = new ArrayList<>(); // Dummy data

		Driver driver = getValidDriver(VALID_CITY_NAME, currentHour, (short) (currentHour + 1));
		driver = createDriver(driver);
		driver.setTotalScore(50);
		driver.setScoreCount(5); // Average score is 10 > 5.0
		updateDriver(driver);
		driverList.add(driver);

		driver = getValidDriver(VALID_CITY_NAME, (short) (currentHour - 1), (short) (currentHour));
		driver = createDriver(driver);
		driver.setTotalScore(25);
		driver.setScoreCount(5); // Average score is 5.0 >= 5.0
		updateDriver(driver);
		driverList.add(driver);

		driver = getValidDriver(VALID_CITY_NAME, currentHour, (short) (currentHour + 1));
		driver = createDriver(driver);
		driver.setTotalScore(24);
		driver.setScoreCount(5); // Average score is 4.8 < 5.0
		updateDriver(driver);
		driverList.add(driver);

		ArrayList<Driver> foundDrivers = null;
		foundDrivers = (ArrayList<Driver>) miniUberService.findDriverByCity(VALID_CITY_NAME);
		for (Driver fd : foundDrivers) {
			assertEquals(fd.getCity(), VALID_CITY_NAME);
			assertTrue(fd.getTotalScore() / fd.getScoreCount() >= MINIMUN_AVG_SCORE);
		}
		foundDrivers = (ArrayList<Driver>) miniUberService.findDriverByCity(OTHER_CITY_NAME);
		assertTrue(foundDrivers.isEmpty());
		for (Driver dl : driverList) {
			removeDriver(dl.getDriverId());
		}
	}

	@Test(expected = InputValidationException.class)
	public void testAddTrip() throws InputValidationException, InstanceNotFoundException {
		Driver driver = getValidDriver(VALID_DRIVER_NAME, VALID_CITY_NAME);
		driver = miniUberService.addDriver(driver);

		Trip trip = getValidTrip(driver.getDriverId(), "user", VALID_CITY_NAME, OTHER_CITY_NAME,
				VALID_CREDIT_CARD_NUMBER);
		trip = miniUberService.addTrip(trip);

		assertEquals(trip, findTrip(trip));

		removeDriver(trip.getDriverId()); // This also removes the trips of that
											// driver
		trip = getValidTrip(INVALID_DRIVER_ID, "user", VALID_CITY_NAME, OTHER_CITY_NAME, VALID_CREDIT_CARD_NUMBER);
		trip = miniUberService.addTrip(trip);
	}

	@Test
	public void testRateTrip()
			throws InputValidationException, InstanceNotFoundException, AlreadyRatedTripException, InvalidUserLoginException {
		Driver driver = getValidDriver(VALID_DRIVER_NAME, VALID_CITY_NAME);
		driver = miniUberService.addDriver(driver);
		String user = "user";
		Trip trip = getValidTrip(driver.getDriverId(), user, VALID_CITY_NAME, OTHER_CITY_NAME,
				VALID_CREDIT_CARD_NUMBER);
		trip = miniUberService.addTrip(trip);
		Short score = 5;
		miniUberService.rateTrip(trip.getTripId(), score, user);
		trip = findTrip(trip);
		assertEquals(trip.getScore(), score);

		driver = miniUberService.findDriver(driver.getDriverId());
		assertEquals(driver.getTotalScore(), new Integer(score));
		assertEquals(driver.getScoreCount(), new Integer(1));
		
		boolean exceptionCatched = false;
		try {
			miniUberService.rateTrip(trip.getTripId(), score, user); 
		} catch (AlreadyRatedTripException e) {
			exceptionCatched = true;
		}
		assertTrue(exceptionCatched);
		exceptionCatched = false;
		
		try {
			miniUberService.rateTrip(trip.getTripId(), score, "otherUser"); 
		} catch (InvalidUserLoginException e) {
			exceptionCatched = true;
		}
		assertTrue(exceptionCatched);
		exceptionCatched = false;
		
		try {
			miniUberService.rateTrip(300l, score, user); 
		} catch (InstanceNotFoundException e) {
			exceptionCatched = true;
		}
		assertTrue(exceptionCatched);
		
		removeDriver(driver.getDriverId());
	}

	@Test(expected = InputValidationException.class)
	public void testFindTripsByUser()
			throws InputValidationException, InstanceNotFoundException, AlreadyRatedTripException {
		Driver driver = getValidDriver(VALID_DRIVER_NAME, VALID_CITY_NAME);
		driver = miniUberService.addDriver(driver);
		String user = "user";

		Trip trip = getValidTrip(driver.getDriverId(), user, VALID_CITY_NAME, OTHER_CITY_NAME,
				VALID_CREDIT_CARD_NUMBER);
		trip = miniUberService.addTrip(trip);

		trip = getValidTrip(driver.getDriverId(), "Pepe", VALID_CITY_NAME, OTHER_CITY_NAME, VALID_CREDIT_CARD_NUMBER);
		trip = miniUberService.addTrip(trip);

		List<Trip> TripList = miniUberService.findTripsByUser(user);
		assertEquals(TripList.get(0).getUserLogin(), user);

		trip = getValidTrip(driver.getDriverId(), "", VALID_CITY_NAME, OTHER_CITY_NAME, VALID_CREDIT_CARD_NUMBER);

		try {
			miniUberService.findTripsByUser(trip.getUserLogin());
		} catch (InputValidationException e) {
			throw new InputValidationException("UserLogin can not be empty");
		} finally {
			removeDriver(driver.getDriverId());
		}
	}

	@Test(expected = InputValidationException.class)
	public void testFindByDriver()
			throws InputValidationException, InstanceNotFoundException, AlreadyRatedTripException{

		Driver driver1 = getValidDriver(VALID_DRIVER_NAME, VALID_CITY_NAME);
		driver1 = miniUberService.addDriver(driver1);

		Driver driver2 = getValidDriver(VALID_DRIVER_NAME, VALID_CITY_NAME);
		driver2 = miniUberService.addDriver(driver2);

		String user = "user";
		Trip trip = getValidTrip(driver1.getDriverId(), user, VALID_CITY_NAME, OTHER_CITY_NAME,
				VALID_CREDIT_CARD_NUMBER);
		trip = miniUberService.addTrip(trip);
		trip = getValidTrip(driver1.getDriverId(), user, VALID_CITY_NAME, OTHER_CITY_NAME, VALID_CREDIT_CARD_NUMBER);
		trip = miniUberService.addTrip(trip);

		Trip trip2 = getValidTrip(driver2.getDriverId(), user, VALID_CITY_NAME, OTHER_CITY_NAME,
				VALID_CREDIT_CARD_NUMBER);
		trip2 = miniUberService.addTrip(trip2);

		List<Trip> TripList = miniUberService.findTripsByDriver(driver1.getDriverId());
		for (Trip t : TripList) {
			assertEquals(t.getDriverId(), driver1.getDriverId());
		}

		trip = getValidTrip(INVALID_DRIVER_ID, user, VALID_CITY_NAME, OTHER_CITY_NAME, VALID_CREDIT_CARD_NUMBER);

		try {
			miniUberService.findTripsByDriver(trip.getDriverId());
		} catch (InputValidationException e) {
			throw new InputValidationException(e.getMessage());
		} finally {
			removeDriver(driver1.getDriverId());
			removeDriver(driver2.getDriverId());
		}
	}

}