package com.senacor.hackingdays.distributedcache.executor;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.ExecutionCallback;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IExecutorService;

/**
 * @author Andreas Keefer
 */
public class HazelcastExecutor {
    public static void main(String[] args) {
        HazelcastInstance client = HazelcastClient.newHazelcastClient();
        IExecutorService executor = client.getExecutorService("exec");
        executor.submit(new MaxAgeCallable(), new ExecutionCallback<MaxAgeCallable.MaxAgeRes>() {
            @Override
            public void onResponse(MaxAgeCallable.MaxAgeRes response) {
                System.out.println("maxAge profile = " + response);
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
