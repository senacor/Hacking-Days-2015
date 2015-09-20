package com.senacor.hackingdays.distributedcache;

import static com.senacor.hackingdays.distributedcache.ProfileMapStore.DEFAULT_DB_URL;

import java.util.Properties;

import com.hazelcast.core.MapLoader;
import com.hazelcast.core.MapStoreFactory;
import com.senacor.hackingdays.distributedcache.generate.model.Profile;

/**
 * User: André Goliath, Senacor Technologies AG
 * Date: 20.09.2015
 *
 * @author andre.goliath@senacor.com
 */
public class ProfileMapStoreFactory implements MapStoreFactory<String, Profile> {
    @Override
    public MapLoader<String, Profile> newMapStore(String mapname, Properties properties) {
        return new ProfileMapStore(mapname, DEFAULT_DB_URL);
    }
}
