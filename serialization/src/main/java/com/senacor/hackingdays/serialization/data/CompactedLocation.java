package com.senacor.hackingdays.serialization.data;

public class CompactedLocation {
    private CompactedProfile profile;

    public CompactedLocation(CompactedProfile profile) {
        this.profile = profile;
    }

    public String getState() {
        return profile.getLocationState();
    }

    public String getZip() {
        return profile.getLocationZip();
    }

    public String getCity() {
        return profile.getLocationCity();
    }
}
