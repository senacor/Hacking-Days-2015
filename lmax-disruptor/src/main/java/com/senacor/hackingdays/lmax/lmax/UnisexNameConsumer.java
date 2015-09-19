package com.senacor.hackingdays.lmax.lmax;

import com.senacor.hackingdays.lmax.generate.model.Profile;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class UnisexNameConsumer extends CompletableConsumer {


    private final Set<String> maleNames = new HashSet<>(1000000);
    private final Set<String> femaleNames = new HashSet<>(1000000);

    public UnisexNameConsumer(int expectedMessages, Runnable onComplete) {
        super(expectedMessages, onComplete);
    }

    @Override
    protected void onComplete() {
        int totalNames = maleNames.size() + femaleNames.size();
        maleNames.retainAll(femaleNames);
        System.out.println("A total of " + maleNames.size() + " names can be applied to both men and women. Total names: " + totalNames);
    }

    @Override
    protected void processEvent(Profile profile, long sequence, boolean endOfBatch) {
        String name = profile.getName();
        switch (profile.getGender()) {
            case Male:
                maleNames.add(name);
                break;
            case Female:
                femaleNames.add(name);
                break;
        }
    }
}
