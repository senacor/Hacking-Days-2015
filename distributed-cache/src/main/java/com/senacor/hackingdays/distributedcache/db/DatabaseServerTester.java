package com.senacor.hackingdays.distributedcache.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

import com.senacor.hackingdays.distributedcache.generate.ProfileGenerator;
import com.senacor.hackingdays.distributedcache.generate.model.Profile;

public class DatabaseServerTester {

	private static DataSource createDataSource() {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName("org.h2.Driver");
		dataSource.setUrl(com.senacor.hackingdays.distributedcache.ProfileMapStore.DEFAULT_DB_URL);
		dataSource.setInitialSize(10);
		return dataSource;
	}

	public static void main(String[] args) throws SQLException {
		DataSource dataSource = createDataSource();
		try (Connection connection = dataSource.getConnection()) {
			ProfileMapper mapper = new ProfileMapper(dataSource, "profileMap");
			Profile profile = ProfileGenerator.newProfile();
			mapper.insertProfile(profile);

//			System.out.println(mapper.getAllProfiles());
//			System.out.println(mapper.getAllIds().size());

			PreparedStatement statement = connection.prepareStatement("select count(*) from profilemap");
			// statement.execute();
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
