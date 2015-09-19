package com.senacor.hackingdays.lmax.lmax.fraudrule;

import com.senacor.hackingdays.lmax.generate.model.Profile;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Created with IntelliJ IDEA.
 * User: lrscholz
 * Date: 19/09/15
 * Time: 19:13
 * To change this template use File | Settings | File Templates.
 */
public class FraudRule {
  public List<Profile> getDetected() {
    return detected;
  }

  Predicate<Profile> rule;
  String message;
  int count;
  List<Profile> detected = new ArrayList<>();

  public void addDetected(Profile p) {
    detected.add(p);
  }

  public Predicate<Profile> getRule() {
    return rule;
  }

  public String getMessage() {
    return message;
  }

  public int getCount() {
    return detected.size();
  }

  public FraudRule(Predicate<Profile> rule, String message) {
    this.message = message;
    this.rule = rule;
  }
}
