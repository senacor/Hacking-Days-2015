package com.senacor.hackingdays.distributedcache.generate.model2;

import java.io.Serializable;
import java.util.Objects;

public class Location2 implements Serializable {

    private static final long serialVersionUID = 1;

    private final String state;
    private final String city;
    private final String zip;

    public Location2(String state, String city, String zip) {
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
    public int hashCode() {
        return Objects.hash(state, city, zip);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Location other = (Location) obj;
        return Objects.equals(this.state, other.state)
                && Objects.equals(this.city, other.city)
                && Objects.equals(this.zip, other.zip);
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
