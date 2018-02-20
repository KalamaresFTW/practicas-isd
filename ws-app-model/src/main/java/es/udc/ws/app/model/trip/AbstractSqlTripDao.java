package es.udc.ws.app.model.trip;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

public abstract class AbstractSqlTripDao implements SqlTripDao {

	@Override
	public void remove(Connection connection, Long tripId) throws InstanceNotFoundException {
		/* Create "queryString". */
		String queryString = "DELETE FROM Trip WHERE tripId = ?";
		try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
			/* Fill "preparedStatement". */
			int i = 1;
			preparedStatement.setLong(i++, tripId);
			/* Execute query. */
			int removedRow = preparedStatement.executeUpdate();
			if (removedRow == 0) {
				throw new InstanceNotFoundException(tripId, Trip.class.getName());
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void update(Connection connection, Trip trip) throws InstanceNotFoundException {
		/* Create "queryString". */
		String queryString = "UPDATE Trip SET driverId = ?, userLogin = ?, "
				+ "originLocation = ?, destinationLocation = ?, score = ?,"
				+ "cardNumber = ?, bookingDate = ? WHERE tripId = ?";

		try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

			/* Fill "preparedStatement". */
			int i = 1;
			preparedStatement.setLong(i++, trip.getDriverId());
			preparedStatement.setString(i++, trip.getUserLogin());
			preparedStatement.setString(i++, trip.getOriginLocation());
			preparedStatement.setString(i++, trip.getDestinationLocation());
			preparedStatement.setObject(i++, (Short) trip.getScore());
			preparedStatement.setString(i++, trip.getCardNumber());
			preparedStatement.setTimestamp(i++, new Timestamp(trip.getBookingDate().getTime().getTime()));
			preparedStatement.setLong(i++, trip.getTripId());
			/* Execute query. */
			int updatedRows = preparedStatement.executeUpdate();

			if (updatedRows == 0) {
				throw new InstanceNotFoundException(trip.getDriverId(), Trip.class.getName());
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Trip find(Connection connection, Long tripId) throws InstanceNotFoundException {
		String queryString = "SELECT driverId, userLogin, originLocation, destinationLocation,"
				+ "score, cardNumber, bookingDate FROM Trip WHERE tripId = ?";
		try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
			/* Fill "preparedStatement". */
			int i = 1;
			preparedStatement.setLong(i++, tripId.longValue());
			/* Execute query. */
			ResultSet resultSet = preparedStatement.executeQuery();

			if (!resultSet.next()) {
				throw new InstanceNotFoundException(tripId, Trip.class.getName());
			}
			/* Get results. */
			i = 1;
			Long driverId = resultSet.getLong(i++);
			String userLogin = resultSet.getString(i++);
			String originLocation = resultSet.getString(i++);
			String destinationLocation = resultSet.getString(i++);
			Short score = resultSet.getShort(i++);
			score = resultSet.wasNull() ? null : score;
			String cardNumber = resultSet.getString(i++);
			Calendar bookingDate = Calendar.getInstance();
			bookingDate.setTime(resultSet.getTimestamp(i++));
			/* Return trip. */
			return new Trip(tripId, driverId, userLogin, originLocation, destinationLocation, score, cardNumber,
					bookingDate);

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<Trip> findByDriver(Connection connection, Long driverId) {
		String queryString = "SELECT tripId, userLogin, originLocation, destinationLocation"
				+ ", score, cardNumber, bookingDate FROM Trip WHERE driverId = ?";
		try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
			/* Fill "preparedStatement". */
			int l = 1;
			preparedStatement.setLong(l, driverId.longValue());
			/* Execute query. */
			ResultSet resultSet = preparedStatement.executeQuery();
			List<Trip> tripList = new ArrayList<Trip>();
			/* Get results. */
			while (resultSet.next()) {
				int i = 1;
				long tripId = resultSet.getLong(i++);
				String userLogin = resultSet.getString(i++);
				String originLocation = resultSet.getString(i++);
				String destinationLocation = resultSet.getString(i++);
				short score = resultSet.getShort(i++);
				score = resultSet.wasNull() ? -1 : score;
				String cardNumber = resultSet.getString(i++);
				Calendar bookingDate = Calendar.getInstance();
				bookingDate.setTime(resultSet.getTimestamp(i++));

				tripList.add(new Trip(tripId, driverId, userLogin, originLocation, destinationLocation, score, cardNumber,
						bookingDate));
			}
			return tripList;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<Trip> findByUser(Connection connection, String userLogin) {
		String queryString = "SELECT tripId, driverId, originLocation, destinationLocation,"
				+ "score, cardNumber, bookingDate FROM Trip WHERE userLogin LIKE(?)";
		try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
			/* Fill "preparedStatement". */
			int i = 1;
			preparedStatement.setString(1, userLogin);
			/* Execute query. */
			ResultSet resultSet = preparedStatement.executeQuery();
			List<Trip> tripList = new ArrayList<>();
			/* Get results. */
			while (resultSet.next()) {
				i = 1;
				Long tripId = resultSet.getLong(i++);
				Long driverId = resultSet.getLong(i++);
				String originLocation = resultSet.getString(i++);
				String destinationLocation = resultSet.getString(i++);
				short score = (short) resultSet.getShort(i++);
				String cardNumber = resultSet.getString(i++);
				Calendar bookingDate = Calendar.getInstance();
				bookingDate.setTime(resultSet.getTimestamp(i++));
				tripList.add(new Trip(tripId, driverId, userLogin, originLocation, destinationLocation, score,
						cardNumber, bookingDate));
			}
			return tripList;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
