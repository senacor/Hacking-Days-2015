package com.senacor.hackingdays.lmax.lmax;

import com.senacor.hackingdays.lmax.generate.model.Profile;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by mhaunolder on 19.09.15.
 */
public class GenderConsumer extends CompletableConsumer {

    private static final int LIST_SIZE = 1000;

    private final List<Profile> profileList;

    public GenderConsumer(int expectedMessages, Runnable onComplete) {
        super(expectedMessages, onComplete);
        profileList = new ArrayList<>();
    }

    @Override
    protected void processEvent(DisruptorEnvelope event, long sequence, boolean endOfBatch) {

        profileList.add(event.getProfile());

        if (profileList.size() == LIST_SIZE) {
            Iterator<Profile> iterator = profileList.iterator();
            while (iterator.hasNext()) {
                Profile currentProfile = iterator.next();

                for (Profile profile : profileList) {

                    if (currentProfile.getGender().equals(profile.getSeeking().getGender())
                            && profile.getGender().equals(currentProfile.getSeeking().getGender())) {
                        System.out.println(String.format("MATCH!!!! CURRENT: %s, PROFILE: %s",
                                currentProfile.toString(), profile.toString()));
                    }

                }
            }

        }

    }
}
