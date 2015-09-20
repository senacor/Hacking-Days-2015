package com.senacor.hackingdays.distributedcache;

import com.hazelcast.core.MapLoader;
import com.hazelcast.core.MapStoreFactory;
import com.senacor.hackingdays.distributedcache.ProfileMapStore;
import com.senacor.hackingdays.distributedcache.generate.model.Profile;

import java.util.Properties;

/**
 * User: André Goliath, Senacor Technologies AG
 * Date: 20.09.2015
 *
 * @author andre.goliath@senacor.com
 */
public class ProfileMapStoreFactory implements MapStoreFactory<String,Profile> {
    @Override
    public MapLoader<String, Profile> newMapStore(String mapname, Properties properties) {
        return new ProfileMapStore(mapname,"jdbc:h2:tcp://192.168.220.124/~/test");
    }
}
