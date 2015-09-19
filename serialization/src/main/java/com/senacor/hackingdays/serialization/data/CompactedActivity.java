package com.senacor.hackingdays.serialization.data;

public class CompactedActivity {
    private CompactedProfile profile;

    public CompactedActivity(CompactedProfile profile) {
        this.profile = profile;
    }

    public int getLoginCount() {
        return profile.getActivityLoginCount();
    }

    public void setLoginCount(int loginCount) {
        profile.setActivityLoginCount(loginCount);
    }
}
