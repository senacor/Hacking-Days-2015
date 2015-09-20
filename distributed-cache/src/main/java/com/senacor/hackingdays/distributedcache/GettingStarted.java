package com.senacor.hackingdays.distributedcache;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.senacor.hackingdays.distributedcache.generate.ProfileGenerator;
import com.senacor.hackingdays.distributedcache.generate.model.Profile;

public class GettingStarted {
    public static void main(String[] args) {
        HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();
//        IMap<String, Profile> profiles = hazelcastInstance.getMap("profiles");
//        ProfileGenerator.newInstance(500).stream()
//                .forEach(profile -> profiles.set(profile.getId().toString(), profile));

        // System.out.println("Profile with key 1: " + profiles.get(1));
        // System.out.println("Map Size:" + hazelcastInstance.getMap("profiles").size());

        // Map<String, Profile> datingProfiles = hazelcastInstance.getMap("datingProfiles");
        // ProfileGenerator.newInstance(5).stream()
        //         .forEach(profile -> datingProfiles.put(profile.getId().toString(), profile));

        IMap<String, Profile> profileMap = hazelcastInstance.getMap("profileMap");
        System.out.println("Profile Map Size:" + profileMap.size());

   //     ProfileGenerator.newInstance(50000).stream().forEach(profile -> profileMap.put(profile.getId().toString(), profile));
        System.out.println("Profile Map Size:" + profileMap.size());

        for (IMap.Entry<String, Profile> profileEntry : profileMap.entrySet()) {
//            System.out.println(profileEntry.getKey() + " | " + profileEntry.getValue());
        }
    }
}