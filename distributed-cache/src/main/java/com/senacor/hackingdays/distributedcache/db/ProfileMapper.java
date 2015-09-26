package com.senacor.hackingdays.distributedcache.db;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;

import com.senacor.hackingdays.distributedcache.generate.model.Activity;
import com.senacor.hackingdays.distributedcache.generate.model.Gender;
import com.senacor.hackingdays.distributedcache.generate.model.Location;
import com.senacor.hackingdays.distributedcache.generate.model.Profile;
import com.senacor.hackingdays.distributedcache.generate.model.Range;
import com.senacor.hackingdays.distributedcache.generate.model.RelationShipStatus;
import com.senacor.hackingdays.distributedcache.generate.model.Seeking;

public class ProfileMapper {

	private final DataSource dataSource;
	// private final Connection connection;
	private final String tableName;

	public ProfileMapper(DataSource dataSource) {
		this(dataSource, "person");
	}

	public ProfileMapper(DataSource dataSource, String tableName) {
		this.dataSource = dataSource;
		this.tableName = tableName;
		initializeSchema(dataSource, tableName);
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

	private static void initializeSchema(DataSource dataSource, String tableName) {
		final String resource = "db/initializeprofiletable.sql";
		try (Connection connection = dataSource.getConnection()) {
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

	private String createMultiMergeStatement(final int count) {
		if (count < 1) {
			throw new IllegalArgumentException("count too small");
		}
		final String prefix = "merge into " //
				+ this.tableName //
				+ "(uuid, name, gender, age, location_state, location_city, location_zip, relationship, smoker, seeking_gender, seeking_age_min, seeking_age_max, activity_logincount, activity_lastlogin) values ";
		final String values ="(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		final int builderSize = prefix.length() + count * (values.length()+1);
		final StringBuilder builder = new StringBuilder(builderSize);
		builder.append(prefix);
		for (int i = 0; i < count-1; i++)
			builder.append(values).append(',');
		builder.append(values);
		return builder.toString();
	}

	private String createMergeStatement() {
		return "merge into " //
				+ this.tableName //
				+ "(uuid, name, gender, age, location_state, location_city, location_zip, relationship, smoker, seeking_gender, seeking_age_min, seeking_age_max, activity_logincount, activity_lastlogin) "
				+ "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	}

	private String createDeleteStatement() {
		return "delete from " + tableName + " where uuid = ?";
	}
	
	private int mapProfileToStatement(PreparedStatement statement, Profile profile, int startIndex, boolean isUpdate) throws SQLException {
		int i = startIndex;
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
		if (isUpdate) {
			// set where for update
			statement.setString(i++, profile.getId().toString());
		}
		return i;
//		assert(i == statement.getParameterMetaData().getParameterCount() + 1);
	}

	private int mapProfileToStatement(PreparedStatement statement, Profile profile, boolean isUpdate) throws SQLException {
		return mapProfileToStatement(statement, profile, 1, isUpdate);
	}
	
//	private 

	public List<Profile> getAllProfiles() {
		try (Connection connection = dataSource.getConnection();
				PreparedStatement statement = connection.prepareStatement(createSelectAllStatement())) {
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
		try (Connection connection = dataSource.getConnection();
				PreparedStatement statement = connection.prepareStatement(createSelectAllUUIDsStatement())) {
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
		try (Connection connection = dataSource.getConnection();
				PreparedStatement statement = connection.prepareStatement(createSelectByIdStatement())) {
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
		try (Connection connection = dataSource.getConnection();
				PreparedStatement statement = connection.prepareStatement(createInsertStatement())) {
			mapProfileToStatement(statement, profile, false);
			return statement.execute();

		} catch (SQLException ex) {
			throw new RuntimeException("SQLException when inserting profile.", ex);
		}

	}

	public boolean mergeProfile(Profile profile) {
		try (Connection connection = dataSource.getConnection();
				PreparedStatement statement = connection.prepareStatement(createMergeStatement())) {
			mapProfileToStatement(statement, profile, false);
			return statement.executeUpdate() == 1;

		} catch (SQLException ex) {
			throw new RuntimeException("SQLException when updating profile.", ex);
		}
	}
	
	public boolean mergeProfiles(Collection<Profile> profiles) {
		int count = profiles.size();
		try (Connection connection = dataSource.getConnection();
				PreparedStatement statement = connection.prepareStatement(createMultiMergeStatement(count))) {
			int offset = 1;
			for (Profile profile: profiles) {
				offset = mapProfileToStatement(statement, profile, offset, false);
			}
			return statement.executeUpdate() == 1;
		} catch (SQLException ex) {
			throw new RuntimeException("SQLException when updating profiles.", ex);
		}
		
	}

	public boolean deleteProfile(UUID id) {
		try (Connection connection = dataSource.getConnection();
				PreparedStatement statement = connection.prepareStatement(createDeleteStatement())) {
			statement.setString(1, id.toString());
			return statement.executeUpdate() == 1;
		} catch (SQLException ex) {
			throw new RuntimeException("SQLException when updating profile.", ex);
		}

	}

}
