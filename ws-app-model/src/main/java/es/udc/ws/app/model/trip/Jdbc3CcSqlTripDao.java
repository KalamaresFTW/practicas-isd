package es.udc.ws.app.model.trip;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class Jdbc3CcSqlTripDao extends AbstractSqlTripDao {

	@Override
	public Trip create(Connection connection, Trip trip) {

		String query = "INSERT INTO Trip (driverId, userLogin, originLocation, destinationLocation, "
				+ "cardNumber, bookingdate) VALUES (?, ?, ?, ?, ?, ?)";

		try (PreparedStatement preparedStatement = connection.prepareStatement(query,
				Statement.RETURN_GENERATED_KEYS)) {
			int i = 1;
			preparedStatement.setLong(i++, trip.getDriverId());
			preparedStatement.setString(i++, trip.getUserLogin());
			preparedStatement.setString(i++, trip.getOriginLocation());
			preparedStatement.setString(i++, trip.getDestinationLocation());
			preparedStatement.setString(i++, trip.getCardNumber());
			Timestamp ts = (trip.getBookingDate() != null) ? new Timestamp(trip.getBookingDate().getTime().getTime())
					: null;
			preparedStatement.setTimestamp(i++, ts);

			/* Execute query. */
			preparedStatement.executeUpdate();

			/* Get generated identifier. */
			ResultSet resultSet = preparedStatement.getGeneratedKeys();

			if (!resultSet.next()) {
				throw new SQLException("JDBC driver did not return generated key.");
			}
			Long tripId = resultSet.getLong(1);

			/* Return Trip. */
			return new Trip(tripId, trip.getDriverId(), trip.getUserLogin(), trip.getOriginLocation(),
					trip.getDestinationLocation(), trip.getCardNumber(), trip.getBookingDate());
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
