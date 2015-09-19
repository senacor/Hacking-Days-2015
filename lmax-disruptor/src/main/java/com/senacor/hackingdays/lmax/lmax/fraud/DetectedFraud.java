package com.senacor.hackingdays.lmax.lmax.fraud;

import com.senacor.hackingdays.lmax.generate.model.Profile;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: lrscholz
 * Date: 19/09/15
 * Time: 18:46
 * To change this template use File | Settings | File Templates.
 */
public class DetectedFraud {
  Profile profile;

  public Profile getProfile() {
    return profile;
  }

  public void setProfile(Profile profile) {
    this.profile = profile;
  }

  public List<FraudEntry> getMatchingEntries() {

    return matchingEntries;
  }

  public void setMatchingEntries(List<FraudEntry> matchingEntries) {
    this.matchingEntries = matchingEntries;
  }

  List<FraudEntry> matchingEntries;
}
