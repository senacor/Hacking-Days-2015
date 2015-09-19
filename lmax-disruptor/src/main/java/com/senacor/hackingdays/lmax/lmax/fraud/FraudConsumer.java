package com.senacor.hackingdays.lmax.lmax.fraud;

import com.senacor.hackingdays.lmax.generate.model.Profile;
import com.senacor.hackingdays.lmax.lmax.CompletableConsumer;
import com.senacor.hackingdays.lmax.lmax.DisruptorEnvelope;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: lrscholz
 * Date: 19/09/15
 * Time: 16:25
 * To change this template use File | Settings | File Templates.
 */
public class FraudConsumer extends CompletableConsumer {

    // Fraud structure

    // state -> city
    // gender
    // name + age

    public FraudConsumer(int expectedMessages, Runnable onComplete) {
        super(expectedMessages, onComplete);
    }


    private static class NameAgeFraudDirectory extends FraudDirectoryWithMap<ListFraudDirectory> {
        public NameAgeFraudDirectory() {
            super(ListFraudDirectory.class, p -> p.getName() + ":" + p.getAge());
        }
    }

    private static class CityFraudDirectory extends FraudDirectoryWithMap<NameAgeFraudDirectory> {
        public CityFraudDirectory() {
            super(NameAgeFraudDirectory.class, p -> p.getLocation().getCity());
        }
    }

    private static class StateFraudDirectory extends FraudDirectoryWithMap<CityFraudDirectory> {
        public StateFraudDirectory() {
            super(CityFraudDirectory.class, p -> p.getLocation().getState());
        }
    }

    private FraudDirectory locationDirectory = new StateFraudDirectory();

    @Override
    protected void onComplete() {
        
    }

    @Override
    protected void processEvent(Profile profile, long sequence, boolean endOfBatch) {
        if (endOfBatch) {
            // System.out.println("End of batch fraud");
        }

        List<FraudEntry> fraudEntries = locationDirectory.findEntries(profile);

        if (fraudEntries.size() > 0) {

            List<FraudEntry> trueFraud = new ArrayList<>();
            fraudEntries.stream().filter(q -> q.similarness(profile) < 0.99).forEach(q -> trueFraud.add(q));

            long duplicates = fraudEntries.stream().filter(q -> q.similarness(profile) > 0.99).count();

            if (duplicates == 0 && trueFraud.size() == 0) {
                locationDirectory.addEntry(profile);
            }

            if (trueFraud.size() > 0) {
                String text = MessageFormat.format("data  : name {0}, age {1}, gender {2}, state {3}, city {4}, gender {5}, relationship {6}", profile.getName(), profile.getAge(), profile.getGender().toString(), profile.getLocation().getState(), profile.getLocation().getCity(), profile.getGender().toString(), profile.getRelationShip().toString());
                System.out.println(text);
            }

            for (FraudEntry q : trueFraud) {
                String text2 = MessageFormat.format("fraud : name {0}, age {1}, gender {2}, state {3}, city {4}, gender {5}, relationship {6}", q.getName(), q.getAge(), q.getGender().toString(), q.getState(), q.getCity(), q.getGender(), q.getRelationship());
                System.out.println(text2);
            }
        }
    }
}
