package com.senacor.hackingdays.distributedcache;

import static com.senacor.hackingdays.distributedcache.Profile2MapStore.DEFAULT_DB_URL;

import java.util.Properties;

import com.hazelcast.core.MapLoader;
import com.hazelcast.core.MapStoreFactory;
import com.senacor.hackingdays.distributedcache.generate.model2.Profile2;

/**
 * User: André Goliath, Senacor Technologies AG
 * Date: 20.09.2015
 *
 * @author andre.goliath@senacor.com
 */
public class Profile2MapStoreFactory implements MapStoreFactory<String, Profile2> {
    @Override
    public MapLoader<String, Profile2> newMapStore(String mapname, Properties properties) {
        return new Profile2MapStore(mapname, DEFAULT_DB_URL);
    }
}
