package com.senacor.hackingdays.serialization.data;

import java.io.Serializable;

public class Location implements Serializable {

    private static final long serialVersionUID = 1;

    private final String country;
    private final String city;

    public Location(String country, String city) {
        this.country = country;
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }
}
