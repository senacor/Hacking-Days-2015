package com.senacor.hackingdays.actor;

import com.senacor.hackingdays.serialization.data.Profile;

import java.io.Serializable;

public final class GenerateMessages implements Serializable {

    private final int count;
    private final Class<?> profileClass;

    public GenerateMessages(int count) {
        this(count, Profile.class);
    }
    public GenerateMessages(int count, Class<?> profileClass) {
        this.count = count;
        this.profileClass = profileClass;
    }

    public int getCount() {
        return count;
    }

    public Class<?> getProfileClass() {
        return profileClass;
    }
}
