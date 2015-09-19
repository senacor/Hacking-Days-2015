package com.senacor.hackingdays.distributedcache.db;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
//import org.h2.util.IOUtils;

import com.senacor.hackingdays.distributedcache.domain.Stuff;

public class DatabaseServer {

	private static String getResourceAsString(final String resourceName) {
		InputStream inputStream = null;
		try {
			inputStream = Resources.getResourceAsStream(resourceName);
			StringWriter stringWriter = new StringWriter();
			IOUtils.copy(inputStream, stringWriter);
			return stringWriter.toString();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		} finally {
			IOUtils.closeQuietly(inputStream);
		}
	}

	private static SqlSessionFactory createSqlSessionFactory() {
		InputStream inputStream = null;
		try {
			final String resource = "db/mybatis-config.xml";
			inputStream = Resources.getResourceAsStream(resource);
			return new SqlSessionFactoryBuilder().build(inputStream);
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		} finally {
			IOUtils.closeQuietly(inputStream);
		}

	}

	private static void initializeSchema(DataSource ds) {
		final String resource = "db/initialize.sql";
		try (Connection connection = ds.getConnection()) {
			final String sql = getResourceAsString(resource);
			for (String statement : sql.split(";")) {
				PreparedStatement stmt = connection.prepareStatement(statement);
				stmt.execute();
			}
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}

	public static void main(String[] args) throws IOException {

		SqlSessionFactory sessionFactory = createSqlSessionFactory();
		DataSource ds = sessionFactory.getConfiguration().getEnvironment().getDataSource();
		initializeSchema(ds);
		SqlSession session = sessionFactory.openSession();
		StuffMapper mapper = session.getMapper(StuffMapper.class);
		mapper.createStuff(new Stuff("Hans"));
		mapper.createStuff(new Stuff("Wurst"));
		mapper.createStuff(new Stuff("Zeug"));
		mapper.createStuff(new Stuff("Dings"));
		List<Stuff> allStuff = mapper.getAllStuff();
		for (Stuff stuff : allStuff) {
			System.out.println(stuff);
		}

	}

}
