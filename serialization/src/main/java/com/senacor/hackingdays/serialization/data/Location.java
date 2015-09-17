package com.senacor.hackingdays.serialization.data;

import java.io.Serializable;

public class Location implements Serializable {

    private static final long serialVersionUID = 1;

    private final String state;
    private final String city;
    private final String zip;

    public Location(String state, String city, String zip) {
        this.state = state;
        this.city = city;
        this.zip = zip;
    }

    public String getState() {
        return state;
    }

    public String getCity() {
        return city;
    }

    public String getZip() {
        return zip;
    }

    @Override
    public String toString() {
        return "Location{" +
                "state='" + state + '\'' +
                ", city='" + city + '\'' +
                ", zip='" + zip + '\'' +
                '}';
    }
}
