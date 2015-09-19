package com.senacor.hackingdays.distributedcache.db;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;

import com.google.common.io.Resources;
import com.senacor.hackingdays.distributedcache.generate.ProfileGenerator;

public class DatabaseServer {

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

	private static Connection createConnection() {
		try {
			Class.forName("org.h2.Driver");
			Connection conn = DriverManager.getConnection("jdbc:h2:tcp://172.16.13.152/~/test");
			return conn;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static void initializeSchema(Connection connection) {
		final String resource = "db/initialize.sql";
		try {
			final String sql = getResourceAsString(resource);
			for (String statement : sql.split(";")) {
				PreparedStatement stmt = connection.prepareStatement(statement);
				stmt.execute();
			}
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}

	public static void main(String[] args) throws SQLException {

		try (Connection connection = createConnection()) {
			initializeSchema(connection);
			ProfileMapper profileMapper = new ProfileMapper(connection);
			profileMapper.insertProfile(ProfileGenerator.newProfile());
			profileMapper.insertProfile(ProfileGenerator.newProfile());
			profileMapper.insertProfile(ProfileGenerator.newProfile());
			profileMapper.insertProfile(ProfileGenerator.newProfile());
			
			System.out.println(profileMapper.getAllProfiles());
		}

	}

}
