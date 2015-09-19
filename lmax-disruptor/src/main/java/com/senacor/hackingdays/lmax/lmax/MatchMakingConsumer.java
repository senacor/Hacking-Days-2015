package com.senacor.hackingdays.lmax.lmax;

import com.senacor.hackingdays.lmax.generate.model.Gender;
import com.senacor.hackingdays.lmax.generate.model.Profile;
import com.senacor.hackingdays.lmax.generate.model.RelationShipStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mhaunolder on 19.09.15.
 */
public class MatchMakingConsumer extends CompletableConsumer {

    private final Map<String, CompletableConsumer> consumerMap;
    private volatile int allWomen = 0 ;
    private volatile int expelledWomen= 0;
    private volatile int expelledMen= 0;

    public MatchMakingConsumer(int expectedMessages, Runnable onComplete) {
        super(expectedMessages, onComplete);
        consumerMap = new HashMap<>();
    }

    @Override
    protected void onComplete() {
        System.out.println("COMPLETE!!!");
        System.out.println("All women " + allWomen);
        System.out.println("Evicted women " + expelledWomen);
        System.out.println("Evicted men " + expelledMen);
    }

    @Override
    protected void processEvent(Profile profile, long sequence, boolean endOfBatch) {
        final String parsedZip = profile.getLocation().getZip().substring(0, 2);

        if( profile.getGender().equals(Gender.Female)) allWomen++;

        if (checkExitConditions(profile)){
            if( profile.getGender().equals(Gender.Female)) expelledWomen++;
            if( profile.getGender().equals(Gender.Male)) expelledMen++;
            return;
        }

        if (!consumerMap.containsKey(parsedZip)) {
            consumerMap.put(parsedZip, new LocationConsumer(maxSequence, onComplete));
        }

        CompletableConsumer nextConsumer = consumerMap.get(parsedZip);
        nextConsumer.processEvent(profile, sequence, endOfBatch);
    }

    private boolean checkExitConditions(Profile profile) {
        if (RelationShipStatus.Maried.equals(profile.getRelationShip())) {
            // we are serious
            return true;
        }

        if(profile.getAge() < 21) return true;


        return false;
    }

}
