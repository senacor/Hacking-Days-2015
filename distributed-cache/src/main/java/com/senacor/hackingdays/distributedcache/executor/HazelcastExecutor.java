package com.senacor.hackingdays.distributedcache.executor;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IExecutorService;
import com.hazelcast.core.Member;
import com.hazelcast.core.MultiExecutionCallback;

import java.util.Map;

/**
 * @author Andreas Keefer
 */
public class HazelcastExecutor {
    public static void main(String[] args) {
        final HazelcastInstance client = HazelcastClient.newHazelcastClient();
        IExecutorService executor = client.getExecutorService("execMaxAge");
        executor.submitToAllMembers(new MaxAgeCallable(), new MultiExecutionCallback() {
            @Override
            public void onResponse(Member member, Object value) {
                System.out.println("onResponse Member:" + member + " = " + value);
            }

            @Override
            public void onComplete(Map<Member, Object> values) {
                System.out.println("onComplete:");
                values.values().stream()
                        .forEach(System.out::println);
                client.shutdown();
            }
        });
//        executor.submit(new MaxAgeCallable(), new ExecutionCallback<MaxAgeCallable.MaxAgeRes>() {
//            @Override
//            public void onResponse(MaxAgeCallable.MaxAgeRes response) {
//                System.out.println("maxAge profile = " + response);
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                t.printStackTrace();
//            }
//        });
    }
}
