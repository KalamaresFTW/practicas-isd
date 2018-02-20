package es.udc.ws.app.model.driver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class Jdbc3CcSqlDriverDao extends AbstractSqlDriverDao {

	@Override
	public Driver create(Connection connection, Driver driver) {
		String queryString = "INSERT INTO Driver"
				+ "(name, city, vehicleModel, startHour, endHour, scoreCount, totalScore, registrationDate)"
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		try (PreparedStatement preparedStatement = connection.prepareStatement(queryString,
				Statement.RETURN_GENERATED_KEYS)) {
			int i = 1;
			preparedStatement.setString(i++, driver.getName());
			preparedStatement.setString(i++, driver.getCity());
			preparedStatement.setString(i++, driver.getVehicleModel());
			preparedStatement.setShort(i++, driver.getStartHour());
			preparedStatement.setShort(i++, driver.getEndHour());
			preparedStatement.setInt(i++, driver.getScoreCount());
			preparedStatement.setInt(i++, driver.getTotalScore());
			Timestamp ts = (driver.getRegistrationDate() != null)
					? new Timestamp(driver.getRegistrationDate().getTime().getTime()) : null;
			preparedStatement.setTimestamp(i++, ts);

			preparedStatement.executeUpdate();

			ResultSet resultSet = preparedStatement.getGeneratedKeys();

			if (!resultSet.next())
				throw new SQLException("The JDBC driver did non return generated key.");

			Long driverId = resultSet.getLong(1);
			return new Driver(driverId, driver.getName(), driver.getCity(), driver.getVehicleModel(),
					driver.getStartHour(), driver.getEndHour(), driver.getScoreCount(), driver.getTotalScore(),
					driver.getRegistrationDate());
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}
