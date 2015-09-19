package com.senacor.hackingdays.lmax.lmax;

import static org.junit.Assert.*;

import org.hamcrest.BaseMatcher;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

import com.senacor.hackingdays.lmax.generate.model.Gender;
import com.senacor.hackingdays.lmax.generate.model.Profile;

public class AverageAgeEventHandlerTest {

  AverageAgeEventHandler h;
  private DisruptorEnvelope envl;

  public void setUp(int msgCount) {
    h = new AverageAgeEventHandler(msgCount, () -> System.out.println("Fertig!"));
    envl = new DisruptorEnvelope();
  }

  @Test
  public void testOneProfile() throws Exception {
    setUp(1);
    Profile profile = new Profile("John", Gender.Male);
    profile.setAge(45);
    envl.setProfile(profile);

    h.onEvent(envl, 0, false);

    assertThat(h.getAgeOcurrenceMales().size(), CoreMatchers.is(1));
    assertThat(h.getAgeOcurrenceMales().get(45), CoreMatchers.is(1L));
    
    assertEquals(45.0, h.determineAverage(h.getAgeOcurrenceMales()), 0.001);
  }

  @Test
  public void testTwoProfiles() throws Exception {
    setUp(2);
    Profile profile = new Profile("John", Gender.Male);
    profile.setAge(45);
    envl.setProfile(profile);
    h.onEvent(envl, 0, false);
    
    profile.setAge(35);
    h.onEvent(envl, 1, false);
    
    assertThat(h.getAgeOcurrenceMales().size(), CoreMatchers.is(2));
    assertThat(h.getAgeOcurrenceMales().get(45), CoreMatchers.is(1L));
    assertThat(h.getAgeOcurrenceMales().get(35), CoreMatchers.is(1L));

    assertEquals(40.0, h.determineAverage(h.getAgeOcurrenceMales()), 0.001);
  }
  
  @Test
  public void testThreeProfiles() throws Exception {
    setUp(3);
    Profile profile = new Profile("John", Gender.Male);
    profile.setAge(45);
    envl.setProfile(profile);
    h.onEvent(envl, 0, false);
    
    profile.setAge(35);
    h.onEvent(envl, 1, false);
    h.onEvent(envl, 2, false);
    
    assertThat(h.getAgeOcurrenceMales().size(), CoreMatchers.is(2));
    assertThat(h.getAgeOcurrenceMales().get(45), CoreMatchers.is(1L));
    assertThat(h.getAgeOcurrenceMales().get(35), CoreMatchers.is(2L));
    
    assertEquals(115/3.0, h.determineAverage(h.getAgeOcurrenceMales()), 0.001);
  }
  
}
