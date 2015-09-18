package com.senacor.hackingdays.serializer.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.senacor.hackingdays.serialization.data.Location;

/**
 * @author Alasdair Collinson, Senacor Technologies AG
 */
public class LocationSerializer extends Serializer<Location> {
    @Override
    public void write(Kryo kryo, Output output, Location object) {
        output.writeString(object.getState());
        output.writeString(object.getCity());
        output.writeString(object.getZip());
    }

    @Override
    public Location read(Kryo kryo, Input input, Class<Location> type) {
        return new Location(input.readString(), input.readString(), input.readString());
    }
}
