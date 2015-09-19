package com.senacor.hackingdays.lmax.lmax;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.StreamSupport;

import com.google.common.base.Functions;
import com.lmax.disruptor.EventHandler;
import com.senacor.hackingdays.lmax.generate.model.Profile;

public class AverageAgeConsumer extends CompletableConsumer {

  private Map<Integer, Long> ageOcurrenceMales = new HashMap<>();
  private Map<Integer, Long> ageOcurrenceFemales = new HashMap<>();
  private Map<Integer, Long> ageOcurrenceDisambiguous = new HashMap<>();

  public AverageAgeConsumer(int expectedMessages, Runnable onComplete) {
      super(expectedMessages, onComplete);
  }

  @Override
  protected void processEvent(Profile profile, long sequence, boolean endOfBatch) {
    Map<Integer, Long> forGender = null;
    switch (profile.getGender()) {
    case Male:
      forGender = ageOcurrenceMales;
      break;

    case Female:
      forGender = ageOcurrenceFemales;
      break;
      
    case Disambiguous:
      forGender = ageOcurrenceDisambiguous;
      break;
      
    default:
      throw new IllegalStateException("unknown gender");
    }
    Long ocurrenceOfAge = forGender.getOrDefault(profile.getAge(), 0L);
    forGender.put(profile.getAge(), ocurrenceOfAge+1);
    
  }

  protected float determineAverage(Map<Integer, Long> ageOcurrence) {
    long sumOfAges = ageOcurrence.entrySet().stream().mapToLong(entry -> entry.getKey()*entry.getValue()).sum();
    long numPersons = ageOcurrence.values().stream().mapToLong(e -> e).sum();
    
    if (numPersons <= 0) { // prevent division by zero
      return -1;
    }
    return (float) sumOfAges / numPersons;
  }

  public Map<Integer, Long> getAgeOcurrenceMales() {
    return ageOcurrenceMales;
  }

  public Map<Integer, Long> getAgeOcurrenceFemales() {
    return ageOcurrenceFemales;
  }

  public Map<Integer, Long> getAgeOcurrenceDisambiguous() {
    return ageOcurrenceDisambiguous;
  }

  @Override
  protected void onComplete() {
    System.out.println("Males have an average age of " + determineAverage(ageOcurrenceMales));
    System.out.println("Females have an average age of " + determineAverage(ageOcurrenceFemales));
    System.out.println("Transgenders have an average age of " + determineAverage(ageOcurrenceDisambiguous));
    
  }

}
