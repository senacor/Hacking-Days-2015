package com.senacor.hackingdays.serialization.data;

public class CompactedRange {
    private CompactedProfile profile;

    public CompactedRange(CompactedProfile profile) {
        this.profile = profile;
    }

    public int getLower() {
        return profile.getSeekingAgeLower();
    }

    public void setLower(int lower) {
        profile.setSeekingAgeLower(lower);
    }

    public int getUpper() {
        return profile.getSeekingAgeUpper();
    }

    public void setUpper(int upper) {
        profile.setSeekingAgeUpper(upper);
    }
}
