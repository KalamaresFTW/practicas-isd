package es.udc.ws.app.model.driver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

public abstract class AbstractSqlDriverDao implements SqlDriverDao {

	@Override
	public void remove(Connection connection, Long driverId) throws InstanceNotFoundException {
		/* Create "queryString". */
		String queryString = "DELETE FROM Driver WHERE driverId = ?";

		try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
			/* Fill "preparedStatement". */
			int i = 1;
			preparedStatement.setLong(i++, driverId);
			/* Execute query. */
			int removedRow = preparedStatement.executeUpdate();
			if (removedRow == 0) {
				throw new InstanceNotFoundException(driverId, Driver.class.getName());
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void update(Connection connection, Driver driver) throws InstanceNotFoundException {
		/* Create "queryString". */
		String queryString = "UPDATE Driver SET city = ?, vehicleModel = ?, startHour = ?, "
				+ "endHour = ?, scoreCount = ?, totalScore = ? WHERE driverId = ?";
		try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
			/* Fill "preparedStatement". */
			int i = 1;
			preparedStatement.setString(i++, driver.getCity());
			preparedStatement.setString(i++, driver.getVehicleModel());
			preparedStatement.setShort(i++, driver.getStartHour());
			preparedStatement.setShort(i++, driver.getEndHour());
			preparedStatement.setInt(i++, driver.getScoreCount());
			preparedStatement.setInt(i++, driver.getTotalScore());
			preparedStatement.setLong(i++, driver.getDriverId());
			/* Execute query. */
			int updatedRows = preparedStatement.executeUpdate();

			if (updatedRows == 0) {
				throw new InstanceNotFoundException(driver.getDriverId(), Driver.class.getName());
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Driver find(Connection connection, Long driverId) throws InstanceNotFoundException {
		String queryString = "SELECT name, city, vehicleModel, startHour,"
				+ "endHour, scoreCount, totalScore, registrationDate FROM Driver WHERE driverId = ?";
		try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
			/* Fill "preparedStatement". */
			int i = 1;
			preparedStatement.setLong(i++, driverId.longValue());
			/* Execute query. */
			ResultSet resultSet = preparedStatement.executeQuery();
			if (!resultSet.next()) {
				throw new InstanceNotFoundException(driverId, Driver.class.getName());
			}
			/* Get results. */
			i = 1;
			String name = resultSet.getString(i++);
			String city = resultSet.getString(i++);
			String vehicleModel = resultSet.getString(i++);
			Short startHour = resultSet.getShort(i++);
			Short endHour = resultSet.getShort(i++);
			Integer scoreCount = resultSet.getInt(i++);
			Integer totalScore = resultSet.getInt(i++);
			Calendar registrationDate = Calendar.getInstance();
			registrationDate.setTime(resultSet.getTimestamp(i++));

			/* Return driver. */
			return new Driver(driverId, name, city, vehicleModel, startHour, endHour, scoreCount, totalScore,
					registrationDate);

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<Driver> findByCity(Connection connection, String city) {
		String queryString = "SELECT driverId, name, vehicleModel, startHour,"
				+ "endHour, scoreCount, totalScore, registrationDate FROM Driver "
				+ "WHERE city LIKE(?) AND (totalScore/scoreCount>=5) AND startHour<=? AND endHour>=?";
		try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
			ArrayList<Driver> drivers = new ArrayList<>();
			short currentHour = (short) Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
			int i = 1;
			preparedStatement.setString(i++, city);
			preparedStatement.setShort(i++, currentHour);
			preparedStatement.setShort(i++, currentHour);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				i = 1;
				Long driverId = resultSet.getLong(i++);
				String name = resultSet.getString(i++);
				String vehicleModel = resultSet.getString(i++);
				Short startHour = resultSet.getShort(i++);
				Short endHour = resultSet.getShort(i++);
				Integer scoreCount = resultSet.getInt(i++);
				Integer totalScore = resultSet.getInt(i++);
				Calendar registrationDate = Calendar.getInstance();
				registrationDate.setTime(resultSet.getTimestamp(i++));
				Driver driver = new Driver(driverId, name, city, vehicleModel, startHour, endHour, scoreCount,
						totalScore, registrationDate);
				drivers.add(driver);
			}
			return drivers;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}
