package com.senacor.hackingdays.lmax.lmax;

import com.senacor.hackingdays.lmax.generate.model.Profile;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mhaunolder on 19.09.15.
 */
public class MatchMakingConsumer extends CompletableConsumer {

    private final Map<String, CompletableConsumer> consumerMap;

    public MatchMakingConsumer(int expectedMessages, Runnable onComplete) {
        super(expectedMessages, onComplete);
        consumerMap = new HashMap<>();
    }

    @Override
    protected void onComplete() {

    }

    @Override
    protected void processEvent(Profile profile, long sequence, boolean endOfBatch) {

    }

    @Override
    protected void processEvent(DisruptorEnvelope event, long sequence, boolean endOfBatch) {

        final String parsedZip = event.getProfile().getLocation().getZip().substring(0,2);

        CompletableConsumer nextConsumer = consumerMap.get(parsedZip);
        if (nextConsumer == null) {
            nextConsumer = consumerMap.put(parsedZip, new LocationConsumer(maxSequence, onComplete));
        }

        nextConsumer.processEvent(event, sequence, endOfBatch);
    }
}
