package com.senacor.hackingdays.lmax.lmax;


import com.senacor.hackingdays.lmax.generate.model.Profile;

public class DisruptorEnvelope {

    private Profile profile;

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public static DisruptorEnvelope wrap(Profile profile) {
    	final DisruptorEnvelope envelope = new DisruptorEnvelope();
    	envelope.setProfile(profile);
    	return envelope;
	}
}
