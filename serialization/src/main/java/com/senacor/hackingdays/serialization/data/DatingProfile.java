package com.senacor.hackingdays.serialization.data;

import java.io.Serializable;

public class DatingProfile implements Serializable {

    private static final long serialVersionUID = 1;

    private String firstName;
    private String lastName;
    private Gender gender;
    private int age;
    private Location location;
    private RelationShipStatus relationShip;
    private boolean smoker;
    private Seeking seeking;
    private Activity activity;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public RelationShipStatus getRelationShip() {
        return relationShip;
    }

    public void setRelationShip(RelationShipStatus relationShip) {
        this.relationShip = relationShip;
    }

    public boolean isSmoker() {
        return smoker;
    }

    public void setSmoker(boolean smoker) {
        this.smoker = smoker;
    }

    public Seeking getSeeking() {
        return seeking;
    }

    public void setSeeking(Seeking seeking) {
        this.seeking = seeking;
    }
}
