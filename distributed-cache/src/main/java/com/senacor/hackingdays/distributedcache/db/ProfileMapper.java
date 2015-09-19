package com.senacor.hackingdays.distributedcache.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.senacor.hackingdays.distributedcache.generate.model.Activity;
import com.senacor.hackingdays.distributedcache.generate.model.Gender;
import com.senacor.hackingdays.distributedcache.generate.model.Location;
import com.senacor.hackingdays.distributedcache.generate.model.Profile;
import com.senacor.hackingdays.distributedcache.generate.model.Range;
import com.senacor.hackingdays.distributedcache.generate.model.RelationShipStatus;
import com.senacor.hackingdays.distributedcache.generate.model.Seeking;

public class ProfileMapper {

	private final Connection connection;

	public ProfileMapper(Connection connection) {
		this.connection = connection;
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

	private final String INSERT_PROFILE = "insert into profile "
			+ "(uuid, name, gender, age, location_state, location_city, location_zip, relationship, smoker, seeking_gender, seeking_age_min, seeking_age_max, activity_logincount, activity_lastlogin) "
			+ "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private final String UPDATE_PROFILE = "update profile set "
			+ "uuid=?, name=?, gender=?, age=?, location_state=?, location_city=?, location_zip=?, relationship=?, smoker=?, seeking_gender=?, seeking_age_min=?, seeking_age_max=?, activity_logincount=?, activity_lastlogin=?) "
			+ "where uuid=?";

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
		// statement.setLong(i++, 25l);
		if (statement.getParameterMetaData().getParameterCount() >= i) {
			// for update
			statement.setString(i++, profile.getId().toString());
		}
		assert(i == statement.getParameterMetaData().getParameterCount() + 1);
	}

	public List<Profile> getAllProfiles() {
		try (PreparedStatement statement = connection.prepareStatement("select * from profile")) {
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
		try (PreparedStatement statement = connection.prepareStatement("select uuid from profile")) {
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
		try (PreparedStatement statement = connection.prepareStatement("select * from profile where uuid = ?")) {
			statement.setString(1, id.toString());
			ResultSet resultset = statement.executeQuery();
			if (resultset.next()) {
				return mapProfileFromResultSet(resultset);
			}
			return null;
		} catch (SQLException ex) {
			throw new RuntimeException("SQLException when fetching all profiles.", ex);
		}
	}

	public boolean insertProfile(Profile profile) {
		try (PreparedStatement statement = connection.prepareStatement(INSERT_PROFILE)) {
			mapProfileToStatement(statement, profile);
			return statement.execute();

		} catch (SQLException ex) {
			throw new RuntimeException("SQLException when inserting profile.", ex);
		}

	}

	public boolean updateProfile(Profile profile) {
		try (PreparedStatement statement = connection.prepareStatement(UPDATE_PROFILE)) {
			mapProfileToStatement(statement, profile);
			return statement.executeUpdate() == 1;

		} catch (SQLException ex) {
			throw new RuntimeException("SQLException when updating profile.", ex);
		}
	}

	public boolean deleteProfile(UUID id) {
		try (PreparedStatement statement = connection.prepareStatement("delete from profile where uuid = ?")) {
			statement.setString(1, id.toString());
			return statement.executeUpdate() == 1;

		} catch (SQLException ex) {
			throw new RuntimeException("SQLException when updating profile.", ex);
		}

	}

}
