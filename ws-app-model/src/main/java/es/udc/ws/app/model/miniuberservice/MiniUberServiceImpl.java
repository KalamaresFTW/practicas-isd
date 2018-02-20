package es.udc.ws.app.model.miniuberservice;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import javax.sql.DataSource;

import es.udc.ws.app.model.driver.Driver;
import es.udc.ws.app.model.driver.SqlDriverDao;
import es.udc.ws.app.model.driver.SqlDriverDaoFactory;
import es.udc.ws.app.model.miniuberservice.exceptions.AlreadyRatedTripException;
import es.udc.ws.app.model.miniuberservice.exceptions.InvalidUserLoginException;
import es.udc.ws.app.model.trip.Trip;
import es.udc.ws.app.model.util.AplicationUtils;
import es.udc.ws.app.model.trip.SqlTripDao;
import es.udc.ws.app.model.trip.SqlTripDaoFactory;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.sql.DataSourceLocator;
import es.udc.ws.util.validation.PropertyValidator;
import static es.udc.ws.app.model.util.ModelConstants.*;

public class MiniUberServiceImpl implements MiniUberService {

	private final DataSource dataSource;
	private SqlDriverDao driverDao = null;
	private SqlTripDao tripDao = null;

	public MiniUberServiceImpl() {
		dataSource = DataSourceLocator.getDataSource(MINIUBER_DATA_SOURCE);
		driverDao = SqlDriverDaoFactory.getDao();
		tripDao = SqlTripDaoFactory.getDao();
	}

	private void validateDriver(Driver driver) throws InputValidationException {
		PropertyValidator.validateMandatoryString("Driver name", driver.getName());
		PropertyValidator.validateMandatoryString("Driver city", driver.getCity());
		PropertyValidator.validateMandatoryString("Driver vehicleModel", driver.getVehicleModel());
		AplicationUtils.validateShort("Driver StartHours", driver.getStartHour(), 0, 23);
		AplicationUtils.validateShort("Driver EndHours", driver.getEndHour(), 0, 23);
	}

	private void validateTrip(Trip trip) throws InputValidationException {
		PropertyValidator.validateLong("driverId", trip.getDriverId(), 0, 99999);
		PropertyValidator.validateMandatoryString("Trip userLogin", trip.getUserLogin());
		PropertyValidator.validateCreditCard(trip.getCardNumber());
		PropertyValidator.validateMandatoryString("Trip originLocation", trip.getOriginLocation());
		PropertyValidator.validateMandatoryString("Trip destinationLocation", trip.getDestinationLocation());
	}

	@Override
	public Driver addDriver(Driver driver) throws InputValidationException {
		validateDriver(driver);
		driver.setRegistrationDate(Calendar.getInstance());
		try (Connection connection = dataSource.getConnection()) {
			try {
				connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				connection.setAutoCommit(false);
				driver.setScoreCount(0);
				driver.setTotalScore(0);

				Driver createdDriver = driverDao.create(connection, driver);

				connection.commit();

				return createdDriver;
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

	@Override
	public void updateDriver(Driver driver) throws InputValidationException, InstanceNotFoundException {
		validateDriver(driver);
		try (Connection connection = dataSource.getConnection()) {
			try {
				/* Prepare connection. */
				connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				connection.setAutoCommit(false);
				/* Do work. */
				Driver foundDriver = driverDao.find(connection, driver.getDriverId());
				foundDriver.setCity(driver.getCity());
				foundDriver.setVehicleModel(driver.getVehicleModel());
				foundDriver.setStartHour(driver.getStartHour());
				foundDriver.setEndHour(driver.getEndHour());
				foundDriver.setScoreCount(driver.getScoreCount());
				foundDriver.setTotalScore(driver.getTotalScore());
				driverDao.update(connection, foundDriver);
				/* Commit. */
				connection.commit();
			} catch (InstanceNotFoundException e) {
				connection.rollback();
				throw e;
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

	/*
	 * Returns a list of active drivers. We consider that a driver is active if
	 * its average score is above the MINIMAL_AVG_SCORE, which is 5.0 and its
	 * startHour it's the same as the actual system hour.
	 * 
	 */
	@Override
	public List<Driver> findDriverByCity(String city) {
		try (Connection connection = dataSource.getConnection()) {
			return driverDao.findByCity(connection, city);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Driver findDriver(Long driverId) throws InstanceNotFoundException {
		try (Connection connection = dataSource.getConnection()) {
			return driverDao.find(connection, driverId);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Trip addTrip(Trip trip) throws InputValidationException, InstanceNotFoundException{
		validateTrip(trip);
		try (Connection connection = dataSource.getConnection()) {
			try {
				/* Prepare connection. */
				connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				connection.setAutoCommit(false);

				Calendar now = Calendar.getInstance();

				driverDao.find(connection, trip.getDriverId());
				
				/*
				if ((driver.getStartHour() > now.get(Calendar.HOUR_OF_DAY))
						|| (driver.getEndHour() < now.get(Calendar.HOUR_OF_DAY))) {
					throw new DriverNotAvailableException(driver.getDriverId());
				}*/
				trip.setBookingDate(now);
				Trip createdTrip = tripDao.create(connection, trip);
				connection.commit();
				return createdTrip;
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

	@Override
	public void rateTrip(Long tripId, Short score, String userLogin) throws InputValidationException,
			InstanceNotFoundException, AlreadyRatedTripException, InvalidUserLoginException {
		AplicationUtils.validateShort("score", score, MINIMUN_SCORE, MAXIMUM_SCORE);
		try (Connection connection = dataSource.getConnection()) {
			try {
				/* Prepare connection. */
				connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				connection.setAutoCommit(false);
				/* Do work. */
				Trip foundTrip = tripDao.find(connection, tripId);
				if (!foundTrip.getUserLogin().equals(userLogin)) {
					throw new InvalidUserLoginException(tripId, userLogin);
				}
				if (foundTrip.getScore() != null) {
					throw new AlreadyRatedTripException(tripId);
				}
				foundTrip.setScore(score);
				tripDao.update(connection, foundTrip);
				Driver tripDriver = driverDao.find(connection, foundTrip.getDriverId());
				tripDriver.increaseScoreCount();
				tripDriver.increaseTotalScore(score);
				driverDao.update(connection, tripDriver);
				/* Commit. */
				connection.commit();
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

	@Override
	public List<Trip> findTripsByUser(String userLogin) throws InputValidationException {
		PropertyValidator.validateMandatoryString("userLogin", userLogin);
		try (Connection connection = dataSource.getConnection()) {
			try {
				/* Prepare connection. */
				connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				connection.setAutoCommit(false);
				/* Do work. */
				connection.commit();
				return tripDao.findByUser(connection, userLogin);
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

	@Override
	public List<Trip> findTripsByDriver(Long driverId) throws InputValidationException {
		PropertyValidator.validateLong("driverId", driverId, 0, 999999);
		try (Connection connection = dataSource.getConnection()) {
			try {
				/* Prepare connection. */
				connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				connection.setAutoCommit(false);
				/* Do work. */
				connection.commit();
				return tripDao.findByDriver(connection, driverId);
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
}