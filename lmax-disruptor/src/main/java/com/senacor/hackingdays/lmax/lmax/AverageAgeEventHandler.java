package com.senacor.hackingdays.lmax.lmax;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.StreamSupport;

import com.google.common.base.Functions;
import com.lmax.disruptor.EventHandler;
import com.senacor.hackingdays.lmax.generate.model.Profile;

public class AverageAgeEventHandler implements EventHandler<DisruptorEnvelope> {

  private final int maxSequence;
  private final Runnable onComplete;
  
  private Map<Integer, Long> ageOcurrenceMales = new HashMap<>();
  private Map<Integer, Long> ageOcurrenceFemales = new HashMap<>();
  private Map<Integer, Long> ageOcurrenceDisambiguous = new HashMap<>();

  public AverageAgeEventHandler(int expectedMessages, Runnable onComplete) {
      this.maxSequence = expectedMessages -1;
      this.onComplete = onComplete;
  }

  @Override
  public void onEvent(DisruptorEnvelope envl, long sequence, boolean endOfBatch) throws Exception {
    Map<Integer, Long> forGender = null;
    Profile profile = envl.getProfile();
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
    
    // are we done?
    if (maxSequence == sequence) {
      System.out.println("Males have an average age of " + determineAverage(ageOcurrenceMales));
      System.out.println("Females have an average age of " + determineAverage(ageOcurrenceFemales));
      System.out.println("Transgenders have an average age of " + determineAverage(ageOcurrenceDisambiguous));
      onComplete.run();
    }
  }

  protected float determineAverage(Map<Integer, Long> ageOcurrence) {
    long sumOfAges = ageOcurrence.entrySet().stream().mapToLong(entry -> entry.getKey()*entry.getValue()).sum();
    long numPersons = ageOcurrence.values().stream().mapToLong(e -> e).sum();
    
    if (numPersons <= 0) { // prevent division by zero
      return -1;
    }
    return sumOfAges / numPersons;
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

}
