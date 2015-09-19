package com.senacor.hackingdays.distributedcache;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import com.google.common.collect.Maps;
import com.hazelcast.core.MapStore;
import com.senacor.hackingdays.distributedcache.db.ProfileMapper;
import com.senacor.hackingdays.distributedcache.generate.model.Profile;

/**
 * @author Alasdair Collinson, Senacor Technologies AG
 */
public class ProfileMapStore implements MapStore<UUID, Profile>, Closeable {

    private static final String URL = "jdbc:hsqldb:mydatabase";
    private static final String USER = "SA";
    private static final String PASSWORD = "";

    private final ProfileMapper mapper;
    private final Connection connection;

    public ProfileMapStore() {
        connection = createConnection();
        mapper = new ProfileMapper(connection);
    }

    @Override
    public synchronized void store(UUID key, Profile value) {
        if (mapper.getProfileById(key) == null) {
            mapper.insertProfile(value);
        } else {
            mapper.updateProfile(value);
        }
    }

    @Override
    public synchronized void storeAll(Map<UUID, Profile> map) {
        for (Map.Entry<UUID, Profile> entry : map.entrySet()) {
            store(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public synchronized void delete(UUID key) {
        mapper.deleteProfile(key);
    }

    @Override
    public synchronized void deleteAll(Collection<UUID> keys) {
        for (UUID key : keys) {
            delete(key);
        }
    }

    @Override
    public synchronized Profile load(UUID key) {
        return mapper.getProfileById(key);
    }

    @Override
    public synchronized Map<UUID, Profile> loadAll(Collection<UUID> keys) {
        Map<UUID, Profile> result = Maps.newHashMap();
        keys.stream().forEach(key -> {
            Profile profile = load(key);
            if (profile != null) {
                result.put(profile.getId(), profile);
            }
        });
        return result;
    }

    @Override
    public synchronized Iterable<UUID> loadAllKeys() {
        return mapper.getAllIds();
    }

    private static Connection createConnection() {
        try {
            Class.forName("org.h2.Driver");
            return DriverManager.getConnection("jdbc:h2:tcp://172.16.13.152/~/test");
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
