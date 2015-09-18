package com.senacor.hackingdays.lmax.lmax;

import com.senacor.hackingdays.serialization.data.Profile;

public class DisruptorEnvelope {

    private Profile profile;

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}
