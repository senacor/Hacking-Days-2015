package com.senacor.hackingdays.lmax.lmax;

import com.senacor.hackingdays.lmax.generate.model.Profile;
import com.senacor.hackingdays.lmax.generate.model.Range;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by mhaunolder on 19.09.15.
 */
public class GenderConsumer extends CompletableConsumer {

    private static final int LIST_SIZE = 100;

    private final Collection<Profile> profileList;
    private static final int MAXIMUM = 10000;

    public GenderConsumer(int expectedMessages, Runnable onComplete) {
        super(expectedMessages, onComplete);
        profileList = mostRecentList();
    }

    private Set<Profile> mostRecentList() {
        return Collections.newSetFromMap(new LinkedHashMap<Profile, Boolean>(32, 0.7f, true) {
            protected boolean removeEldestEntry(
                    Map.Entry<Profile, Boolean> eldest) {
                return size() > MAXIMUM;
            }
        });
    }

    @Override
    protected void onComplete() {

    }

    @Override
    protected void processEvent(Profile profile, long sequence, boolean endOfBatch) {

        profileList.add(profile);

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

                    /*System.out.println("    Age MATCH:" +
                            " C: " + currentProfile.getAge()+ " seeking" + currentProfile.getSeeking().getAgeRange().toString() +
                            " P: " + matchingProfile.getAge()+ " seeking" + matchingProfile.getSeeking().getAgeRange().toString());
                    */
                    System.out.println("Gender MATCH:" + currentProfile.getName() + " " + matchingProfile.getName() +
                            " C: " + currentProfile.getLocation().getZip() + " P: " + matchingProfile.getLocation().getZip()
                            + " C: " + currentProfile.getGender() + " seeking " + currentProfile.getSeeking().getGender() +
                            " P: " + matchingProfile.getGender() + " seeking " + matchingProfile.getSeeking().getGender() + " END MATCH");

                    match = true;
                }

                if (match) {
                    iterator.remove();
                }

            }
            // System.out.println(profileList.size() + " entries left after matching.");
        }

    }

    private boolean isInRange(int value, Range range) {
        return (value >= range.getLower() && value <= range.getLower());
    }

}
