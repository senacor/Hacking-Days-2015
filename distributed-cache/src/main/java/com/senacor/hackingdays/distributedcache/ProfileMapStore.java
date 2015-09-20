package com.senacor.hackingdays.distributedcache;

import java.io.Closeable;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.sql.DataSource;

import com.google.common.collect.Maps;
import com.hazelcast.core.MapStore;
import com.senacor.hackingdays.distributedcache.db.ProfileMapper;
import com.senacor.hackingdays.distributedcache.generate.model.Profile;
import org.apache.commons.dbcp2.BasicDataSource;

/**
 * @author Alasdair Collinson, Senacor Technologies AG
 */
public class ProfileMapStore implements MapStore<String, Profile>, Closeable {

    // private static final String DEFAULT_MAP_ID = "profile";
    public static final String DEFAULT_MAP_ID = "unittestTable";
    public static final String DEFAULT_DB_URL = "jdbc:h2:tcp://192.168.220.124/~/test";
    // public static final String DEFAULT_DB_URL = "jdbc:h2:tcp://172.16.13.152/~/test";

    private final String dbUrl;
    private final ProfileMapper mapper;
    private final DataSource dataSource;

    public ProfileMapStore(String mapname, String dbUrl) {
        this.dbUrl = dbUrl;
        this.dataSource = createDataSource();
        mapper = new ProfileMapper(dataSource, mapname);
    }

    public ProfileMapStore() {
        this(DEFAULT_MAP_ID, DEFAULT_DB_URL);
    }

    @Override
    public void store(String key, Profile value) {
            mapper.mergeProfile(value);
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
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl(dbUrl);
        dataSource.setInitialSize(10);
        return dataSource;
    }

    @Override
    public void close() throws IOException {
        try {
            dataSource.getConnection().close();
        } catch (SQLException e) {
            throw new IOException("Fehler beim Schließen der Datenbankverbindung", e);
        }
    }
}
