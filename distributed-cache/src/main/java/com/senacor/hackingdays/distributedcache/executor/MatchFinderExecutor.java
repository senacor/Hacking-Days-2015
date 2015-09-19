package com.senacor.hackingdays.distributedcache.executor;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.SerializerConfig;
import com.hazelcast.core.ExecutionCallback;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.IExecutorService;
import com.hazelcast.core.IMap;
import com.hazelcast.core.Member;
import com.hazelcast.core.MemberSelector;
import com.hazelcast.core.MultiExecutionCallback;
import com.hazelcast.security.UsernamePasswordCredentials;
import com.senacor.hackingdays.distributedcache.generate.ProfileGenerator;
import com.senacor.hackingdays.distributedcache.generate.model.Gender;
import com.senacor.hackingdays.distributedcache.generate.model.Profile;
import com.senacor.hackingdays.distributedcache.generate.model.Range;
import com.senacor.hackingdays.distributedcache.generate.model.Seeking;
import com.senacor.hackingdays.distributedcache.serializer.KryoProfileStreamSerializer;

public class MatchFinderExecutor {
    public static void main(String[] args) {
        ClientConfig clientConfig = new ClientConfig();
//        clientConfig.getSerializationConfig().getSerializerConfigs().add(
//                new SerializerConfig().
//                        setTypeClass(Profile.class).
//                        setImplementation(new KryoProfileStreamSerializer()));
        clientConfig.setCredentials(new UsernamePasswordCredentials("sinalco", "sinalco"));
        HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);
        IExecutorService ex = client.getExecutorService("my-distributed-executor");
        
        IMap<String, Profile> map = client.getMap("datingProfiles");
//        for (Entry<String, Profile> entry : map.entrySet()) {
//        	ex.executeOnKeyOwner(new ProfilePrinter(entry.getValue()), entry.getKey());
//        }
        
        ExecutionCallback<Collection<Profile>> executionCallback = new ExecutionCallback<Collection<Profile>>() {
			
			@Override
			public void onResponse(Collection<Profile> matchingProfiles) {
				for (Profile profile : matchingProfiles) {
					System.out.println(profile);
				}
			}
			
			@Override
			public void onFailure(Throwable t) {
				t.printStackTrace();
			}
		};
        
        Profile newCustomer = ProfileGenerator.newProfile();
        Seeking seeking = new Seeking(Gender.Female, new Range(23, 60));
        newCustomer.setSeeking(seeking);
        System.out.println("MatchingProfiles for " + newCustomer);
		ex.submitToAllMembers(new MatchFinderCallable(newCustomer), new MultiExecutionCallback() {
			
			@Override
			public void onResponse(Member member, Object value) {
				System.out.println("Von Member " + member + " erhaltene Matches: " + ((Collection<Profile>)value).size());
			}
			
			@Override
			public void onComplete(Map<Member, Object> values) {
				for (Object object : values.values()) {
					Collection<Profile> profiles = (Collection<Profile>) object;
					for (Profile profile : profiles) {
						System.out.println(profile);
					}
				}
			}
		});
    }
    
}
