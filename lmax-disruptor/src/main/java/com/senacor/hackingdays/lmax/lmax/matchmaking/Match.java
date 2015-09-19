package com.senacor.hackingdays.lmax.lmax.matchmaking;

import com.senacor.hackingdays.lmax.generate.model.Profile;

/**
 * Created by mhaunolder on 19.09.15.
 */
public class Match {

    private final Profile profile1;
    private final Profile profile2;

    public Match(final Profile profile1, final Profile profile2) {
        this.profile1 = profile1;
        this.profile2 = profile2;
    }

    public Profile getProfile1() {
        return profile1;
    }

    public Profile getProfile2() {
        return profile2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Match match = (Match) o;

        if (profile1 != null ? !profile1.getId().equals(match.profile1.getId()) : match.profile1 != null) return false;
        return !(profile2 != null ? !profile2.getId().equals(match.profile2.getId()) : match.profile2 != null);
    }

    @Override
    public String toString() {
        return "Match{" +
                "profile1=" + profile1 +
                ", profile2=" + profile2 +
                '}';
    }
}
