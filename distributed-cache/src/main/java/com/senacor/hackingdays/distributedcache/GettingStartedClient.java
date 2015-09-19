package com.senacor.hackingdays.distributedcache;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IQueue;
import com.hazelcast.security.UsernamePasswordCredentials;
import com.senacor.hackingdays.distributedcache.generate.model.Gender;
import com.senacor.hackingdays.distributedcache.generate.model.Profile;
import com.senacor.hackingdays.distributedcache.generate.model.Seeking;

public class GettingStartedClient {
    public static void main(String[] args) {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setCredentials(new UsernamePasswordCredentials("sinalco", "sinalco"));
        HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);
        IQueue<Profile> queue = client.getQueue("profiles");
        for(Profile profile : queue) {
            System.out.printf("%s, %d, %s aus %s, %s sucht: ", profile.getName(), profile.getAge(), toString(profile.getGender()), profile.getLocation().getCity(), profile.getLocation().getState());
            Seeking seeking = profile.getSeeking();
            System.out.printf("%s zwischen %d und %d%n", toString(seeking.getGender()), seeking.getAgeRange().getLower(), seeking.getAgeRange().getUpper());
        }

    }

    private static String toString(Gender gender) {
        switch (gender) {
            case Male: return "männlich";
            case Female: return "weiblich";
            case Disambiguous: return "uneindeutig";
            default: return "unbekannt";
        }
    }
}
