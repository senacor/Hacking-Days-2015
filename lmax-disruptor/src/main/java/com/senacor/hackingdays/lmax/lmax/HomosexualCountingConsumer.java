package com.senacor.hackingdays.lmax.lmax;

import com.senacor.hackingdays.lmax.generate.model.Profile;

import java.util.Calendar;
import java.util.Date;

public class HomosexualCountingConsumer extends CompletableConsumer {

    private int count;

    public HomosexualCountingConsumer(int expectedMessages, Runnable onComplete) {
        super(expectedMessages, onComplete);
    }

    @Override
    protected void onComplete() {
        System.out.println((double)count/(double)maxSequence + "% homosexual users");
    }

    @Override
    protected void processEvent(Profile profile, long sequence, boolean endOfBatch) {

        if (profile.getGender().equals(profile.getSeeking().getGender())) {
            count++;
        }
    }
}
