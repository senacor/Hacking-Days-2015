package com.senacor.hackingdays.lmax.lmax;

import com.senacor.hackingdays.lmax.generate.model.Gender;
import com.senacor.hackingdays.lmax.generate.model.Profile;
import com.senacor.hackingdays.lmax.lmax.matchmaking.GenderSeekingType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mhaunolder on 19.09.15.
 */
public class LocationConsumer extends CompletableConsumer {

    private final Map<GenderSeekingType, CompletableConsumer> consumerMap;

    public LocationConsumer(int expectedMessages, Runnable onComplete) {
        super(expectedMessages, onComplete);
        consumerMap = new HashMap<>();
    }

    @Override
    protected void onComplete() {
    }

    @Override
    protected void processEvent(Profile profile, long sequence, boolean endOfBatch) {
        GenderSeekingType type = GenderSeekingType.MF;

        switch (profile.getGender()) {
            case Male:
                if (Gender.Male.equals(profile.getSeeking().getGender())) {
                    type = GenderSeekingType.MM;
                }
                break;
            case Female:
                if (Gender.Female.equals(profile.getSeeking().getGender())) {
                    type = GenderSeekingType.FF;
                }
                break;
            case Disambiguous:
            default:
                break;
        }

        if (!consumerMap.containsKey(type)) {
            consumerMap.put(type, new GenderConsumer(maxSequence, onComplete, type));
        }

        CompletableConsumer nextConsumer = consumerMap.get(type);
        nextConsumer.processEvent(profile, sequence, endOfBatch);

        if (maxSequence == sequence) {
            System.out.println(String.format("MatchMaking found %d Male:Female matches.",
                    consumerMap.get(GenderSeekingType.MF).getMatchList().size()));
            System.out.println(String.format("MatchMaking found %d Male:Male matches.",
                    consumerMap.get(GenderSeekingType.MM).getMatchList().size()));
            System.out.println(String.format("MatchMaking found %d Female:Female matches.",
                    consumerMap.get(GenderSeekingType.FF).getMatchList().size()));
        }
    }

}
