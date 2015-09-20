package com.senacor.hackingdays.distributedcache;

import java.io.Closeable;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.h2.jdbcx.JdbcConnectionPool;
import org.h2.jdbcx.JdbcDataSource;

import com.google.common.collect.Maps;
import com.hazelcast.core.MapStore;
import com.senacor.hackingdays.distributedcache.db.ProfileMapper;
import com.senacor.hackingdays.distributedcache.generate.model.Profile;

/**
 * @author Alasdair Collinson, Senacor Technologies AG
 */
public class ProfileMapStore implements MapStore<String, Profile>, Closeable {

	private static final String DEFAULT_MAP_ID = "profile";
	private static final String DEFAULT_DB_URL = "jdbc:h2:tcp://192.168.220.124/~/test";
	// private static final String DEFAULT_DB_URL =
	// "jdbc:h2:tcp://172.16.13.152/~/test";
	public static final String PROPERTY_FILE = "maps.properties.xml";

	private final String mapId;
	private final String dbUrl;
	private final ProfileMapper mapper;
	private final DataSource dataSource;

	public ProfileMapStore() {
		Properties properties = getProperties();
		mapId = properties.getProperty("mapName", DEFAULT_MAP_ID);
		dbUrl = properties.getProperty("db-url", DEFAULT_DB_URL);
		dataSource = createDataSource();
		mapper = new ProfileMapper(dataSource, mapId);
	}

	private Properties getProperties() {
		Properties properties = new Properties();
		try {
			properties.loadFromXML(getClass().getClassLoader().getResourceAsStream(PROPERTY_FILE));
		} catch (IOException e) {
			System.err.println("Failed to load properties from file, using defaults");
		}
		return properties;
	}

	@Override
	public void store(String key, Profile value) {
		if (mapper.getProfileById(UUID.fromString(key)) == null) {
			mapper.insertProfile(value);
		} else {
			mapper.updateProfile(value);
		}
	}

	@Override
	public void storeAll(Map<String, Profile> map) {
		for (Map.Entry<String, Profile> entry : map.entrySet()) {
			store(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public void delete(String key) {
		mapper.deleteProfile(UUID.fromString(key));
	}

	@Override
	public void deleteAll(Collection<String> keys) {
		for (String key : keys) {
			delete(key);
		}
	}

	@Override
	public Profile load(String key) {
		return mapper.getProfileById(UUID.fromString(key));
	}

	@Override
	public Map<String, Profile> loadAll(Collection<String> keys) {
		Map<String, Profile> result = Maps.newHashMap();
		keys.stream().forEach(key -> {
			Profile profile = load(key);
			if (profile != null) {
				result.put(profile.getId().toString(), profile);
			}
		});
		return result;
	}

	@Override
	public Iterable<String> loadAllKeys() {
		return mapper.getAllIds().stream().map(UUID::toString).collect(Collectors.toList());
	}

	private DataSource createDataSource() {
		JdbcDataSource dataSource = new JdbcDataSource();
		dataSource.setURL(dbUrl);
		JdbcConnectionPool pool = JdbcConnectionPool.create(dataSource);
		pool.setMaxConnections(10);
		return pool;
	}

	@Override
	public void close() throws IOException {
		// noop
	}
}
