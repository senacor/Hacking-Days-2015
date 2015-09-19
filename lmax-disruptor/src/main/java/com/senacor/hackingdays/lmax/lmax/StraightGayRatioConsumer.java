package com.senacor.hackingdays.lmax.lmax;

import com.senacor.hackingdays.lmax.generate.model.Gender;
import com.senacor.hackingdays.lmax.generate.model.Profile;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mhaunolder on 19.09.15.
 */
public class StraightGayRatioConsumer extends CompletableConsumer {
    public StraightGayRatioConsumer(int expectedMessages, Runnable onComplete) {
        super(expectedMessages, onComplete);
    }

    @Override
    protected void onComplete() {

    }

    @Override
    protected void processEvent(Profile profile, long sequence, boolean endOfBatch) {

    }

//    private int straight;
//    private int gay;
//
//    public StraightGayRatioConsumer(int expectedMessages, Runnable onComplete) {
//        super(expectedMessages, onComplete);
//        consumerMap = new HashMap<>();
//    }
//
//    @Override
//    protected void onComplete() {
//
//    }
//
//    @Override
//    protected void processEvent(Profile profile, long sequence, boolean endOfBatch) {
//        if (profile.getGender().equals(Gender.Disambiguous) || profile.getSeeking())
//        if (profile.getGender().equals(profile.))
//    }

}
