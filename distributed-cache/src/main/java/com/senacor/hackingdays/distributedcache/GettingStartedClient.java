package com.senacor.hackingdays.distributedcache;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IList;
import com.hazelcast.core.IMap;
import com.hazelcast.core.IQueue;
import com.senacor.hackingdays.distributedcache.generate.model.Gender;
import com.senacor.hackingdays.distributedcache.generate.model.Profile;
import com.senacor.hackingdays.distributedcache.generate.model.Seeking;

public class GettingStartedClient {
    public static void main(String[] args) {

        HazelcastInstance client = HazelcastClient.newHazelcastClient();
        IQueue<Profile> queue = client.getQueue("profiles");

        IList<Object> fooListe = client.getList("foo");
                                   System.out.println("Fooliste item 0:"+fooListe.get(0));
        for (Profile profile : queue) {
            System.out.printf("Q1 (%s): %s, %d, %s aus %s, %s sucht: ", profile.getId().toString(), profile.getName(), profile.getAge(), toString(profile.getGender()), profile.getLocation().getCity(), profile.getLocation().getState());
            Seeking seeking = profile.getSeeking();
            System.out.printf("%s zwischen %d und %d%n", toString(seeking.getGender()), seeking.getAgeRange().getLower(), seeking.getAgeRange().getUpper());
        }

        IMap<String, Profile> map = client.getMap("profiles");
        for (Profile profile : map.values()) {
            System.out.printf("M1 (%s): %s, %d, %s aus %s, %s sucht: ", profile.getId().toString(), profile.getName(), profile.getAge(), toString(profile.getGender()), profile.getLocation().getCity(), profile.getLocation().getState());
            Seeking seeking = profile.getSeeking();
            System.out.printf("%s zwischen %d und %d%n", toString(seeking.getGender()), seeking.getAgeRange().getLower(), seeking.getAgeRange().getUpper());
        }

        IMap<String, Profile> map2 = client.getMap("datingProfiles");
        for (Profile profile : map2.values()) {
            System.out.printf("M2 (%s): %s, %d, %s aus %s, %s sucht: ", profile.getId().toString(), profile.getName(), profile.getAge(), toString(profile.getGender()), profile.getLocation().getCity(), profile.getLocation().getState());
            Seeking seeking = profile.getSeeking();
            System.out.printf("%s zwischen %d und %d%n", toString(seeking.getGender()), seeking.getAgeRange().getLower(), seeking.getAgeRange().getUpper());
        }
    }

    private static String toString(Gender gender) {
        switch (gender) {
            case Male:
                return "männlich";
            case Female:
                return "weiblich";
            case Disambiguous:
                return "uneindeutig";
            default:
                return "unbekannt";
        }
    }
}
