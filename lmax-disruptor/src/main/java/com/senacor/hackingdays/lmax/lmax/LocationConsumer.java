package com.senacor.hackingdays.lmax.lmax;

import com.senacor.hackingdays.lmax.generate.model.Gender;
import com.senacor.hackingdays.lmax.generate.model.Profile;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mhaunolder on 19.09.15.
 */
public class LocationConsumer extends CompletableConsumer {

    private final Map<String, CompletableConsumer> consumerMap;

    public LocationConsumer(int expectedMessages, Runnable onComplete) {
        super(expectedMessages, onComplete);
        consumerMap = new HashMap<>();
    }

    @Override
    protected void processEvent(DisruptorEnvelope event, long sequence, boolean endOfBatch) {

        final Profile profile = event.getProfile();
        String genderConsumerString = "MF";

        switch (profile.getGender()) {
            case Male:
                if (Gender.Male.equals(profile.getSeeking().getGender())) {
                    genderConsumerString = "MM";
                }
                break;
            case Female:
                if (Gender.Female.equals(profile.getSeeking().getGender())) {
                    genderConsumerString = "FF";
                }
                break;
            case Disambiguous:
                // not yet implemented - only for premium users
                break;
            default:
                break;
        }


        CompletableConsumer nextConsumer = consumerMap.get(genderConsumerString);
        if (nextConsumer == null) {
            nextConsumer = consumerMap.put(genderConsumerString, new GenderConsumer(maxSequence, onComplete));
        }

        nextConsumer.processEvent(event, sequence, endOfBatch);
    }
}
