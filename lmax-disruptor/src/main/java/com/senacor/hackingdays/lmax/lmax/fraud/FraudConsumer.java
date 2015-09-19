package com.senacor.hackingdays.lmax.lmax.fraud;

import com.lmax.disruptor.EventHandler;
import com.senacor.hackingdays.lmax.generate.model.Profile;
import com.senacor.hackingdays.lmax.lmax.DisruptorEnvelope;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Created with IntelliJ IDEA.
 * User: lrscholz
 * Date: 19/09/15
 * Time: 16:25
 * To change this template use File | Settings | File Templates.
 */
public class FraudConsumer implements EventHandler<DisruptorEnvelope> {

  // Fraud structure

  // state -> city
  // gender
  // name + age

  private final int maxSequence;
  private final Runnable onComplete;


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

  public FraudConsumer(int expectedMessages, Runnable onComplete) {
    this.maxSequence = expectedMessages -1;
    this.onComplete = onComplete;
  }

  @Override
  public void onEvent(DisruptorEnvelope event, long sequence, boolean endOfBatch) throws Exception {
    if (endOfBatch) {
            // System.out.println("End of batch fraud");
    }
    // System.out.println(sequence);
    if (maxSequence == sequence)
      onComplete.run();

    List<FraudEntry> fraudEntries = locationDirectory.findEntries(event.getProfile());

    if (fraudEntries.size() > 0) {
      Profile p = event.getProfile();

      List<FraudEntry> trueFraud = new ArrayList<>();
      fraudEntries.stream().filter(q -> q.similarness(p) < 0.99).forEach(q -> trueFraud.add(q));

      long duplicates = fraudEntries.stream().filter(q -> q.similarness(p) > 0.99).count();

      if (duplicates == 0 && trueFraud.size() == 0) {
        locationDirectory.addEntry(p);
      }

      if (trueFraud.size() > 0) {
        String text = MessageFormat.format("data  : name {0}, age {1}, gender {2}, state {3}, city {4}, gender {5}, relationship {6}", p.getName(), p.getAge(), p.getGender().toString(), p.getLocation().getState(), p.getLocation().getCity(), p.getGender().toString(), p.getRelationShip().toString());
        System.out.println(text);
      }

      for(FraudEntry q : trueFraud) {
        String text2 = MessageFormat.format("fraud : name {0}, age {1}, gender {2}, state {3}, city {4}, gender {5}, relationship {6}", q.getName(), q.getAge(), q.getGender().toString(), q.getState(), q.getCity(), q.getGender(), q.getRelationship());
        System.out.println(text2);
      }
    }
  }
}
