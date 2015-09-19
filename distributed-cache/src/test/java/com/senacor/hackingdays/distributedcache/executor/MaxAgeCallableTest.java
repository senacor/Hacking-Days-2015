package com.senacor.hackingdays.distributedcache.executor;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.senacor.hackingdays.distributedcache.generate.ProfileGenerator;
import com.senacor.hackingdays.distributedcache.generate.model.Gender;
import com.senacor.hackingdays.distributedcache.generate.model.Profile;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.*;

/**
 * @author Andreas Keefer
 */
public class MaxAgeCallableTest {

    @Test
    public void testCall() throws Exception {
        final Map<String, Profile> testData = newHashMap();
        ProfileGenerator.newInstance(10).stream()
                .forEach(profile -> {
                    testData.put(profile.getId().toString(), profile);
                    System.out.println("profile.age=" + profile.getAge());
                });
        Profile max = new Profile("max", Gender.Disambiguous);
        max.setAge(999);
        testData.put(max.getId().toString(), max);

        MaxAgeCallable maxAgeCallable = new MaxAgeCallable();
        HazelcastInstance hazelcastInstanceMock = mock(HazelcastInstance.class);
        IMap<String, Profile> profilesMapMock = mock(IMap.class);
        doReturn(profilesMapMock).when(hazelcastInstanceMock).getMap("profiles");
        doReturn(testData.keySet()).when(profilesMapMock).localKeySet();
        doAnswer(invocation -> {
            Object key = invocation.getArguments()[0];
            return testData.get(key);
        }).when(profilesMapMock).get(anyObject());

        maxAgeCallable.setHazelcastInstance(hazelcastInstanceMock);
        MaxAgeCallable.MaxAgeRes maxAgeCallableRes = maxAgeCallable.call();
        System.out.println("maxAge=" + maxAgeCallableRes);
        Assert.assertThat("some profile with max age",
                maxAgeCallableRes.getProfile().getId(), is(max.getId()));
    }
}