package com.senacor.hackingdays.distributedcache.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.senacor.hackingdays.distributedcache.generate.ProfileGenerator;

public class DatabaseServerTester {

	private static Connection createConnection() {
		try {
			Class.forName("org.h2.Driver");
			Connection conn = DriverManager.getConnection("jdbc:h2:tcp://192.168.220.124/~/test");
			return conn;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) throws SQLException {
		try (Connection connection = createConnection()) {
			ProfileMapper mapper = new ProfileMapper(connection);
			mapper.insertProfile(ProfileGenerator.newProfile());
			System.out.println(mapper.getAllProfiles());
			System.out.println(mapper.getAllIds().size());
			
			PreparedStatement statement = connection.prepareStatement("select * from profilemap");
			ResultSet rs = statement.executeQuery();
			int i = 0;
			while (rs.next()) {
				System.out.println(rs.getString(1));
				i++;
			}
			System.out.println(i);
		}
		
		
		
	}

}
