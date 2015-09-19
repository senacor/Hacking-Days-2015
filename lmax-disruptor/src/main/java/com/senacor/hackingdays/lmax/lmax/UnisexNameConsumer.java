package com.senacor.hackingdays.lmax.lmax;

import com.senacor.hackingdays.lmax.generate.model.Profile;

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
        maleNames.retainAll(femaleNames);
        System.out.println("A total of " + maleNames.size() + " names can be applied to both men and women");
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
