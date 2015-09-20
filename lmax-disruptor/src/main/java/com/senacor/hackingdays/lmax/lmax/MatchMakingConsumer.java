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
        final String parsedZip = profile.getLocation().getZip().substring(0, 2);

        if (!consumerMap.containsKey(parsedZip)) {
            consumerMap.put(parsedZip, new LocationConsumer(maxSequence, onComplete));
        }

        CompletableConsumer nextConsumer = consumerMap.get(parsedZip);
        nextConsumer.processEvent(profile, sequence, endOfBatch);
    }
}
