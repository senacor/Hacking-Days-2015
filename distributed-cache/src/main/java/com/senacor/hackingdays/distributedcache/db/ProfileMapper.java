package com.senacor.hackingdays.distributedcache.db;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.IOUtils;

import com.senacor.hackingdays.distributedcache.generate.model.Activity;
import com.senacor.hackingdays.distributedcache.generate.model.Gender;
import com.senacor.hackingdays.distributedcache.generate.model.Location;
import com.senacor.hackingdays.distributedcache.generate.model.Profile;
import com.senacor.hackingdays.distributedcache.generate.model.Range;
import com.senacor.hackingdays.distributedcache.generate.model.RelationShipStatus;
import com.senacor.hackingdays.distributedcache.generate.model.Seeking;

public class ProfileMapper {

	private final Connection connection;
	private final String tableName;

	public ProfileMapper(Connection connection) {
		this(connection, "person");
	}

	public ProfileMapper(Connection connection, String tableName) {
		this.connection = connection;
		this.tableName = tableName;
		initializeSchema(connection, tableName);
	}
	private static String getResourceAsString(final String resourceName) {
		InputStream inputStream = null;
		try {
			inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream(resourceName);
			StringWriter stringWriter = new StringWriter();
			IOUtils.copy(inputStream, stringWriter);
			return stringWriter.toString();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		} finally {
			IOUtils.closeQuietly(inputStream);
		}
	}
	
	private static void initializeSchema(Connection connection, String tableName) {
		final String resource = "db/initializeprofiletable.sql";
		try {
			final String sql = getResourceAsString(resource);
			for (String statement : sql.split(";")) {
				statement = statement.replaceAll("\\$PROFILETABLE\\$", tableName);
				PreparedStatement stmt = connection.prepareStatement(statement);
				stmt.execute();
			}
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}

	private Profile mapProfileFromResultSet(ResultSet resultSet) throws SQLException {

		final UUID uuid = UUID.fromString(resultSet.getString("uuid"));
		final String name = resultSet.getString("name");
		final Gender gender = Gender.values()[resultSet.getInt("gender")];
		final int age = resultSet.getInt("age");
		final RelationShipStatus relationShipStatus = RelationShipStatus.values()[resultSet.getInt("relationship")];
		final boolean isSmoker = resultSet.getBoolean("smoker");
		final int loginCount = resultSet.getInt("activity_logincount");
		final long lastLoginTime = resultSet.getLong("activity_lastlogin");
		final String city = resultSet.getString("location_city");
		final String state = resultSet.getString("location_state");
		final String zip = resultSet.getString("location_zip");
		final Gender seekingGender = Gender.values()[resultSet.getInt("seeking_gender")];
		final int lowerAge = resultSet.getInt("seeking_age_min");
		final int upperAge = resultSet.getInt("seeking_age_max");

		Profile result = new Profile(name, gender, uuid);
		result.setAge(age);
		result.setRelationShip(relationShipStatus);
		result.setSmoker(isSmoker);
		result.setActivity(new Activity(new Date(lastLoginTime), loginCount));
		result.setLocation(new Location(state, city, zip));
		result.setSeeking(new Seeking(seekingGender, new Range(lowerAge, upperAge)));
		return result;

	}

	private String createSelectAllStatement() {
		return "select * from " + tableName;
	}

	private String createSelectAllUUIDsStatement() {
		return "select uuid from " + tableName;
	}
	
	private String createSelectByIdStatement() {
		return "select * from " + tableName + " where uuid = ?";
	}

	private String createInsertStatement() {
		return "insert into " //
				+ this.tableName //
				+ "(uuid, name, gender, age, location_state, location_city, location_zip, relationship, smoker, seeking_gender, seeking_age_min, seeking_age_max, activity_logincount, activity_lastlogin) "
				+ "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	}

	private String createUpdateStatement() {
		return "update " //
				+ this.tableName //
				+ " set " //
				+ "uuid=?, name=?, gender=?, age=?, location_state=?, location_city=?, location_zip=?, relationship=?, smoker=?, seeking_gender=?, seeking_age_min=?, seeking_age_max=?, activity_logincount=?, activity_lastlogin=? "
				+ "where uuid=?";
	}

	private String createDeleteStatement() {
		return "delete from " + tableName + " where uuid = ?";
	}

	private void mapProfileToStatement(PreparedStatement statement, Profile profile) throws SQLException {
		int i = 1;
		statement.setString(i++, profile.getId().toString());
		statement.setString(i++, profile.getName());
		statement.setInt(i++, profile.getGender().ordinal());
		statement.setInt(i++, profile.getAge());
		statement.setString(i++, profile.getLocation().getState());
		statement.setString(i++, profile.getLocation().getCity());
		statement.setString(i++, profile.getLocation().getZip());
		statement.setInt(i++, profile.getRelationShip().ordinal());
		statement.setBoolean(i++, profile.isSmoker());
		statement.setInt(i++, profile.getSeeking().getGender().ordinal());
		statement.setInt(i++, profile.getSeeking().getAgeRange().getLower());
		statement.setInt(i++, profile.getSeeking().getAgeRange().getUpper());
		statement.setInt(i++, profile.getActivity().getLoginCount());
		statement.setLong(i++, profile.getActivity().getLastLogin().getTime());
		if (statement.getParameterMetaData().getParameterCount() >= i) {
			// for update
			statement.setString(i++, profile.getId().toString());
		}
		assert(i == statement.getParameterMetaData().getParameterCount() + 1);
	}

	public List<Profile> getAllProfiles() {
		try (PreparedStatement statement = connection.prepareStatement(createSelectAllStatement())) {
			ResultSet resultset = statement.executeQuery();
			List<Profile> result = new ArrayList<>();
			while (resultset.next()) {
				result.add(mapProfileFromResultSet(resultset));
			}
			return result;
		} catch (SQLException ex) {
			throw new RuntimeException("SQLException when fetching all profiles.", ex);
		}
	}

	public List<UUID> getAllIds() {
		try (PreparedStatement statement = connection.prepareStatement(createSelectAllUUIDsStatement())) {
			ResultSet resultset = statement.executeQuery();
			List<UUID> result = new ArrayList<>();
			while (resultset.next()) {
				result.add(UUID.fromString(resultset.getString("uuid")));
			}
			return result;
		} catch (SQLException ex) {
			throw new RuntimeException("SQLException when fetching all profiles.", ex);
		}
	}

	public Profile getProfileById(UUID id) {
		try (PreparedStatement statement = connection
				.prepareStatement(createSelectByIdStatement())) {
			statement.setString(1, id.toString());
			ResultSet resultset = statement.executeQuery();
			if (resultset.next()) {
				return mapProfileFromResultSet(resultset);
			}
			return null;
		} catch (SQLException ex) {
			throw new RuntimeException("SQLException when fetching Profile by Id.", ex);
		}
	}

	public boolean insertProfile(Profile profile) {
		try (PreparedStatement statement = connection.prepareStatement(createInsertStatement())) {
			mapProfileToStatement(statement, profile);
			return statement.execute();

		} catch (SQLException ex) {
			throw new RuntimeException("SQLException when inserting profile.", ex);
		}

	}

	public boolean updateProfile(Profile profile) {
		try (PreparedStatement statement = connection.prepareStatement(createUpdateStatement())) {
			mapProfileToStatement(statement, profile);
			return statement.executeUpdate() == 1;

		} catch (SQLException ex) {
			throw new RuntimeException("SQLException when updating profile.", ex);
		}
	}

	public boolean deleteProfile(UUID id) {
		try (PreparedStatement statement = connection.prepareStatement(createDeleteStatement())) {
			statement.setString(1, id.toString());
			return statement.executeUpdate() == 1;

		} catch (SQLException ex) {
			throw new RuntimeException("SQLException when updating profile.", ex);
		}

	}

}
