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
    protected void processEvent(Profile event, long sequence, boolean endOfBatch) {
        String name = event.getProfile().getName();
        if(event.getProfile().getGender())
    }
}
