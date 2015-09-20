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
import com.senacor.hackingdays.distributedcache.db.Profile2Mapper;
import com.senacor.hackingdays.distributedcache.generate.model2.Profile2;
import org.apache.commons.dbcp2.BasicDataSource;

/**
 * @author Alasdair Collinson, Senacor Technologies AG
 */
public class Profile2MapStore implements MapStore<String, Profile2>, Closeable {

    // private static final String DEFAULT_MAP_ID = "profile";
    public static final String DEFAULT_MAP_ID = "profile2Map";
    public static final String DEFAULT_DB_URL = "jdbc:h2:tcp://192.168.220.124/~/test";
    // public static final String DEFAULT_DB_URL = "jdbc:h2:tcp://172.16.13.152/~/test";

    private final String dbUrl;
    private final Profile2Mapper mapper;
    private final DataSource dataSource;

    public Profile2MapStore(String mapname, String dbUrl) {
        this.dbUrl = dbUrl;
        this.dataSource = createDataSource();
        mapper = new Profile2Mapper(dataSource, mapname);
    }

    public Profile2MapStore() {
        this(DEFAULT_MAP_ID, DEFAULT_DB_URL);
    }

    @Override
    public void store(String key, Profile2 value) {
            mapper.mergeProfile(value);
    }

    @Override
    public void storeAll(Map<String, Profile2> map) {
        for (Map.Entry<String, Profile2> entry : map.entrySet()) {
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
    public Profile2 load(String key) {
        return mapper.getProfileById(UUID.fromString(key));
    }

    @Override
    public Map<String, Profile2> loadAll(Collection<String> keys) {
        Map<String, Profile2> result = Maps.newHashMap();
        keys.stream().forEach(key -> {
            Profile2 profile = load(key);
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
