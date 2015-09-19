package com.senacor.hackingdays.lmax.lmax;

import com.senacor.hackingdays.lmax.generate.model.Gender;
import com.senacor.hackingdays.lmax.generate.model.Profile;
import com.senacor.hackingdays.lmax.generate.model.Range;

public class CreepyOldMenConsumer extends CompletableConsumer {


    public static final int OLD = 65;

    private int creepCount;

    public CreepyOldMenConsumer(int expectedMessages, Runnable onComplete) {
        super(expectedMessages, onComplete);
    }

    @Override
    protected void onComplete() {
        System.out.println("Dating Platform has " + creepCount + " creepy old men.");
    }

    @Override
    protected void processEvent(Profile profile, long sequence, boolean endOfBatch) {

        if (isNotMale(profile)) {
            return;
        }
        if (profile.getAge() < OLD) {
            return;
        }
        Range ageRange = profile.getSeeking().getAgeRange();
        if (isTeen(ageRange)) {
            creepCount++;
        }

    }

    private boolean isTeen(Range ageRange) {
        return ageRange.getLower() <= 19;
    }

    private boolean isNotMale(Profile profile) {
        return !profile.getGender().equals(Gender.Male);
    }


}
