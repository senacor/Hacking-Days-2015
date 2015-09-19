package com.senacor.hackingdays.serialization.data;

public class CompactedSeeking {
    private CompactedProfile profile;

    public CompactedSeeking(CompactedProfile profile) {
        this.profile = profile;
    }

    public Gender getGender() {
        return profile.getSeekingGender();
    }

    public void setGender(Gender gender) {
        profile.setSeekingGender(gender);
    }

    public CompactedRange getCompactedRange() {
        return new CompactedRange(profile);
    }

}
