package com.senacor.hackingdays.serialization.data;

import com.senacor.hackingdays.serialization.data.unsafe.BufferTooSmallException;
import com.senacor.hackingdays.serialization.data.unsafe.UnsafeMemory;
import com.senacor.hackingdays.serialization.data.unsafe.UnsafeSerializable;
import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;

public class Location implements Serializable, UnsafeSerializable {

    private static final long serialVersionUID = 1;

    private final String state;
    private final String city;
    private final String zip;

    public Location(
            @JsonProperty("state") String state,
            @JsonProperty("city") String city,
            @JsonProperty("zip") String zip) {
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

  @Override
  public void serializeUnsafe(UnsafeMemory memory) throws BufferTooSmallException {
    memory.putByteArray(state.getBytes());
    memory.putByteArray(city.getBytes());
    memory.putByteArray(zip.getBytes());
  }

  public static Location deserializeUnsafe(final UnsafeMemory memory) {
    final String state = new String(memory.getByteArray());
    final String city = new String(memory.getByteArray());
    final String zip = new String(memory.getByteArray());
    return new Location(state, city, zip);
  }
}
