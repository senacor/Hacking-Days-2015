package com.senacor.hackingdays.distributedcache;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.security.UsernamePasswordCredentials;

public class GettingStartedClient {
    public static void main(String[] args) {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setCredentials(new UsernamePasswordCredentials("sinalco", "sinalco"));
        HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);
        IMap map = client.getMap("profiles");
        System.out.println("Map Size:" + map.size());
    }
}
