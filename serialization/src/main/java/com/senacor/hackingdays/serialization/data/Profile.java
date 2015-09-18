package com.senacor.hackingdays.serialization.data;

import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;

public class Profile implements Serializable {

    private static final long serialVersionUID = 1;

    private final String name;
    private final Gender gender;
    private int age;
    private Location location;
    private RelationShipStatus relationShip;
    private boolean smoker;
    private Seeking seeking;
    private Activity activity;

    public Profile(
            @JsonProperty("name") String name,
            @JsonProperty("gender") Gender gender) {
        this.name = name;
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public Gender getGender() {
        return gender;
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

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    @Override
    public String toString() {
        return "DatingProfile{" +
                "name='" + name + '\'' +
                ", gender=" + gender +
                ", age=" + age +
                ", location=" + location +
                ", relationShip=" + relationShip +
                ", smoker=" + smoker +
                ", seeking=" + seeking +
                ", activity=" + activity +
                '}';
    }
}
