package com.senacor.hackingdays.distributedcache.generate.model;

import java.io.Serializable;

public class Seeking implements Serializable {

    private final Gender gender;
    private final Range ageRange;

    public Seeking(
            Gender gender,
            Range ageRange) {
        this.gender = gender;
        this.ageRange = ageRange;
    }

    public Gender getGender() {
        return gender;
    }

    public Range getAgeRange() {
        return ageRange;
    }

    @Override
    public String toString() {
        return "Seeking{" +
                "gender=" + gender +
                ", ageRange=" + ageRange +
                '}';
    }

}
