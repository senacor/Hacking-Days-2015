package com.senacor.hackingdays.distributedcache.executor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.IMap;
import com.senacor.hackingdays.distributedcache.generate.model.Profile;

public class MatchFinderCallable implements Callable<Collection<Profile>>, Serializable, HazelcastInstanceAware {

	private static final long serialVersionUID = 8043869010850083334L;
	
	private transient HazelcastInstance hazelcastInstance;
	private final Profile referenceProfile;
	
	public MatchFinderCallable(Profile referenceProfile) {
		this.referenceProfile = referenceProfile;
	}
	
	@Override
	public void setHazelcastInstance(HazelcastInstance instance) {
		this.hazelcastInstance = instance;
	}

	@Override
	public Collection<Profile> call() throws Exception {
		IMap<String, Profile> map = hazelcastInstance.getMap("profiles");
		Collection<Profile> matchingProfiles = new ArrayList<>();
		for (String checkProfileKey : map.localKeySet()) {
			Profile checkProfile = map.get(checkProfileKey);
			if (ProfileMatcher.matching(checkProfile, referenceProfile)) {
				matchingProfiles.add(checkProfile);
			}
		}
		return matchingProfiles;
	}
	
}
