package com.senacor.hackingdays.distributedcache;

import com.google.common.collect.Maps;
import com.hazelcast.core.MapStore;
import com.senacor.hackingdays.distributedcache.db.ProfileMapper;
import com.senacor.hackingdays.distributedcache.generate.model.Profile;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Alasdair Collinson, Senacor Technologies AG
 */
public class ProfileMapStore implements MapStore<String, Profile>, Closeable {

    private static final String DEFAULT_MAP_ID = "profile";
    private static final String DEFAULT_DB_URL = "jdbc:h2:tcp://192.168.220.124/~/test";
    public static final String PROPERTY_FILE = "maps.properties.xml";

    private final String mapId;
    private final String dbUrl;
    private final ProfileMapper mapper;
    private final Connection connection;

    public ProfileMapStore() {
        Properties properties = getProperties();
        mapId = properties.getProperty("mapName", DEFAULT_MAP_ID);
        dbUrl = properties.getProperty("db-url", DEFAULT_DB_URL);
        connection = createConnection();
        mapper = new ProfileMapper(connection, mapId);
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
    public synchronized void store(String key, Profile value) {
        if (mapper.getProfileById(UUID.fromString(key)) == null) {
            mapper.insertProfile(value);
        } else {
            mapper.updateProfile(value);
        }
    }

    @Override
    public synchronized void storeAll(Map<String, Profile> map) {
        for (Map.Entry<String, Profile> entry : map.entrySet()) {
            store(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public synchronized void delete(String key) {
        mapper.deleteProfile(UUID.fromString(key));
    }

    @Override
    public synchronized void deleteAll(Collection<String> keys) {
        for (String key : keys) {
            delete(key);
        }
    }

    @Override
    public synchronized Profile load(String key) {
        return mapper.getProfileById(UUID.fromString(key));
    }

    @Override
    public synchronized Map<String, Profile> loadAll(Collection<String> keys) {
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
    public synchronized Iterable<String> loadAllKeys() {
        return mapper.getAllIds().stream()
                .map(UUID::toString)
                .collect(Collectors.toList());
    }

    private Connection createConnection() {
        try {
            Class.forName("org.h2.Driver");
            return DriverManager.getConnection(dbUrl);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws IOException {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new IOException("Fehler beim Schließen der Verbindung zur Datenbank", e);
        }
    }
}
