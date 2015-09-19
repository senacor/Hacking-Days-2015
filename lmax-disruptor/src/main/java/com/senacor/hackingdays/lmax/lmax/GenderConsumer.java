package com.senacor.hackingdays.lmax.lmax;

import com.senacor.hackingdays.lmax.generate.model.Gender;
import com.senacor.hackingdays.lmax.generate.model.Profile;
import com.senacor.hackingdays.lmax.generate.model.Range;
import com.senacor.hackingdays.lmax.generate.model.RelationShipStatus;
import com.senacor.hackingdays.lmax.lmax.matchmaking.GenderSeekingType;
import com.senacor.hackingdays.lmax.lmax.matchmaking.Match;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by mhaunolder on 19.09.15.
 */
public class GenderConsumer extends CompletableConsumer {

    private static final int LIST_SIZE = 100;

    private final GenderSeekingType type;
    private final List<Profile> profileList;

    public GenderConsumer(int expectedMessages, Runnable onComplete, GenderSeekingType type) {
        super(expectedMessages, onComplete);
        this.type = type;
        profileList = new ArrayList<>();
        matchList = new ArrayList<>();
    }

    @Override
    protected void onComplete() {
    }

    @Override
    protected void processEvent(Profile profile, long sequence, boolean endOfBatch) {

        if (checkConditions(profile)) {
            profileList.add(profile);
        }

        if (profileList.size() == LIST_SIZE) {
            List<Profile> matchingProfileList = new ArrayList<>(profileList);
            Iterator<Profile> iterator = profileList.iterator();
            while (iterator.hasNext()) {
                Profile currentProfile = iterator.next();

                boolean match = false;
                for (Profile matchingProfile : matchingProfileList) {

                    if (currentProfile.getId().equals(matchingProfile.getId()))
                        continue;

                    if (!(currentProfile.getGender().equals(matchingProfile.getSeeking().getGender())
                            && matchingProfile.getGender().equals(currentProfile.getSeeking().getGender())))
                        continue;

                    if (!(isInRange(currentProfile.getAge(), matchingProfile.getSeeking().getAgeRange())
                            && isInRange(matchingProfile.getAge(), currentProfile.getSeeking().getAgeRange()))) {
                        continue;
                    }

                    match = true;
                    Match matchPairs = new Match(currentProfile, matchingProfile);
                    if (!matchList.contains(matchPairs)) {
                        matchList.add(matchPairs);
                    }
                }
            }
            profileList.clear();
            // System.out.println(profileList.size() + " entries left after matching.");
        }

    }

    private boolean checkConditions(Profile profile) {
        if (RelationShipStatus.Maried.equals(profile.getRelationShip())) return false;

        if (Gender.Disambiguous.equals(profile.getGender())) return false;

        return  (profile.getAge() > 21);
    }

    private boolean isInRange(int value, Range range) {
        return (value >= range.getLower() && value <= range.getLower());
    }

}
