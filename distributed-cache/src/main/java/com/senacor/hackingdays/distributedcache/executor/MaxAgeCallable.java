package com.senacor.hackingdays.distributedcache.executor;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.IMap;
import com.senacor.hackingdays.distributedcache.generate.model.Profile;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.Callable;

/**
 * @author Andreas Keefer
 */
public class MaxAgeCallable implements Callable<MaxAgeCallable.MaxAgeRes>, Serializable, HazelcastInstanceAware {

    private static final long serialVersionUID = 1;

    private transient HazelcastInstance hazelcastInstance;

    @Override
    public MaxAgeRes call() throws Exception {
        Profile res = null;
        long start = System.currentTimeMillis();
        IMap<String, Profile> profiles = hazelcastInstance.getMap("profiles");
        Set<String> localKeySet = profiles.localKeySet();
        for (String key : localKeySet) {
            Profile profile = profiles.get(key);
            if (null == res || res.getAge() < profile.getAge()) {
                res = profile;
            }
        }

        String info = "MaxAgeCallable: " + localKeySet.size() + " durchsucht. Found: "
                + res + " runtime=" + (System.currentTimeMillis() - start);
        System.out.println(info);

        return new MaxAgeRes(res, info);
    }

    public static final class MaxAgeRes implements Serializable {
        private Profile profile;
        private String info;

        public MaxAgeRes(Profile profile, String info) {
            this.profile = profile;
            this.info = info;
        }

        public Profile getProfile() {
            return profile;
        }

        public void setProfile(Profile profile) {
            this.profile = profile;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }


        @Override
        public String toString() {
            return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                    .append("profile", profile)
                    .append("info", info)
                    .toString();
        }
    }

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }
}
