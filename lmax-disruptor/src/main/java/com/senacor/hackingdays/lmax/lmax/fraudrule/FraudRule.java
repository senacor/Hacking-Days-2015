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
  Predicate<Profile> rule;
  String message;
  int count;
  List<RuleViolation> detected = new ArrayList<>();

  public List<RuleViolation> getDetected() {
    return detected;
  }

  public void setDetected(List<RuleViolation> detected) {
    this.detected = detected;
  }

  public Predicate<Profile> getRule() {
    return rule;
  }

  public String getMessage() {
    return message;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public FraudRule(Predicate<Profile> rule, String message) {
    this.message = message;
    this.rule = rule;
  }
}
