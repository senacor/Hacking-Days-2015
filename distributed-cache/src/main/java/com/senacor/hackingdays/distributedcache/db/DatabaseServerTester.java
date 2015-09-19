package com.senacor.hackingdays.distributedcache.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.h2.jdbcx.JdbcConnectionPool;
import org.h2.jdbcx.JdbcDataSource;

import com.senacor.hackingdays.distributedcache.generate.ProfileGenerator;
import com.senacor.hackingdays.distributedcache.generate.model.Profile;

public class DatabaseServerTester {

	private static DataSource createDataSource() {
		JdbcDataSource dataSource = new JdbcDataSource();
		// dataSource.setURL("jdbc:h2:tcp://192.168.220.124/~/test");
		dataSource.setURL("jdbc:h2:tcp://172.16.13.152/~/test");

		JdbcConnectionPool pool = JdbcConnectionPool.create(dataSource);
		pool.setMaxConnections(10);
		return pool;
	}

	public static void main(String[] args) throws SQLException {
		DataSource dataSource = createDataSource();
		try (Connection connection = dataSource.getConnection()) {
			ProfileMapper mapper = new ProfileMapper(dataSource, "profileMap");
			Profile profile = ProfileGenerator.newProfile();
			mapper.insertProfile(profile);

//			System.out.println(mapper.getAllProfiles());
//			System.out.println(mapper.getAllIds().size());

			PreparedStatement statement = connection.prepareStatement("select * from profilemap");
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
