package com.senacor.hackingdays.serializer;

import java.util.Date;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.senacor.hackingdays.serialization.data.Activity;

/**
 * @author Alasdair Collinson, Senacor Technologies AG
 */
public class ActivitySerializer extends Serializer<Activity> {
    @Override
    public void write(Kryo kryo, Output output, Activity object) {
        kryo.writeObject(output, object.getLastLogin());
        output.writeInt(object.getLoginCount());
    }

    @Override
    public Activity read(Kryo kryo, Input input, Class type) {
        return new Activity(kryo.readObject(input, Date.class), input.readInt());
    }
}
