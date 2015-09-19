package com.senacor.hackingdays.distributedcache;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.senacor.hackingdays.distributedcache.generate.ProfileGenerator;
import com.senacor.hackingdays.distributedcache.generate.model.Profile;

import java.util.Map;
import java.util.Queue;
import java.util.UUID;

public class GettingStarted {
    public static void main(String[] args) {
        HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();
        Map<String, Profile> profiles = hazelcastInstance.getMap("profiles");
        ProfileGenerator.newInstance(500).stream()
                .forEach(profile -> profiles.put(profile.getId().toString(), profile));

        // System.out.println("Profile with key 1: " + profiles.get(1));
        System.out.println("Map Size:" + hazelcastInstance.getMap("profiles").size());

        Queue<Profile> queueCustomers = hazelcastInstance.getQueue("profiles");
        // queueCustomers.offer(ProfileGenerator.newProfile());
        // queueCustomers.offer(ProfileGenerator.newProfile());
        // queueCustomers.offer(ProfileGenerator.newProfile());
        System.out.println("First profile: " + queueCustomers.poll());
        System.out.println("Second profile: " + queueCustomers.peek());
        System.out.println("Queue size: " + queueCustomers.size());

        Map<String, Profile> datingProfiles = hazelcastInstance.getMap("datingProfiles");
        // ProfileGenerator.newInstance(5).stream()
        //         .forEach(profile -> datingProfiles.put(profile.getId().toString(), profile));

        IMap<UUID, Profile> profileMap = hazelcastInstance.getMap("profileMap");
        System.out.println("Profile Map Size:" + profileMap.size());

        // ProfileGenerator.newInstance(5).stream().forEach(profile -> profileMap.put(profile.getId(), profile));
        // System.out.println("Profile Map Size:" + profileMap.size());

        for (IMap.Entry<UUID, Profile> profileEntry : profileMap.entrySet()) {
            System.out.println(profileEntry.getKey() + " | " + profileEntry.getValue());
        }
    }
}