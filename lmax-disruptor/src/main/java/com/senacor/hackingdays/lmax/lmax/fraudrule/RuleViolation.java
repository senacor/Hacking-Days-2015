package com.senacor.hackingdays.lmax.lmax.fraudrule;

import com.senacor.hackingdays.lmax.generate.model.Profile;

/**
 * Created with IntelliJ IDEA.
 * User: lrscholz
 * Date: 19/09/15
 * Time: 19:22
 * To change this template use File | Settings | File Templates.
 */
public class RuleViolation {
  private Profile profile;
  private String message;

  public Profile getProfile() {
    return profile;
  }

  public String getMessage() {
    return message;
  }

  public RuleViolation(Profile profile, String message) {

    this.profile = profile;
    this.message = message;
  }
}
