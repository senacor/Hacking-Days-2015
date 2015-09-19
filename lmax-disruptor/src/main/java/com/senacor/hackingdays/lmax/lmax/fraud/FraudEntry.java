package com.senacor.hackingdays.lmax.lmax.fraud;

import com.senacor.hackingdays.lmax.generate.model.Gender;
import com.senacor.hackingdays.lmax.generate.model.Profile;

public class FraudEntry {
  private String name;
  private String gender;
  private String relationship;
  private int age;
  private String city;
  private String state;
  private String uuid;

  public String getUuid() {
    return uuid;
  }

  public void setFrom(Profile profile) {
    this.city = profile.getLocation().getCity();
    this.state = profile.getLocation().getState();
    this.age = profile.getAge();
    this.gender = profile.getGender().toString();
    this.name = profile.getName();
    this.relationship = profile.getRelationShip().toString();
    this.uuid = profile.getId().toString();
  }

  public String getName() {
    return name;
  }

  public String getGender() {
    return gender;
  }

  public String getRelationship() {
    return relationship;
  }

  public int getAge() {
    return age;
  }

  public String getCity() {
    return city;
  }

  public String getState() {
    return state;
  }

  public float similarness(Profile other) {
    int similar = 0;

    if (city.equals(other.getLocation().getCity())) {
      similar++;
    }

    if (state.equals(other.getLocation().getState())) {
      similar++;
    }

    if (age == other.getAge()) {
      similar++;
    }

    if (gender.equals(other.getGender().toString())) {
      similar++;
    }

    if (name.equals(other.getName())) {
      similar++;
    }

    if (gender.equals(other.getGender().toString())) {
      similar++;
    }

    if (relationship.equals(other.getRelationShip().toString())) {
      similar++;
    }

    if (similar == 6) {
      int i = 1;
      i = 3;
    }

    return (float) similar / 7;
  }
}
