package com.senacor.hackingdays.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.senacor.hackingdays.serialization.data.Activity;
import java.util.Date;

/**
 * @author Alasdair Collinson, Senacor Technologies AG
 */
public class ActivitySerializer extends Serializer<Activity> {

    public final static ActivitySerializer INSTANCE = new ActivitySerializer();
    {
        setAcceptsNull(true);
    }

    @Override
    public void write(Kryo kryo, Output output, Activity object) {
        kryo.writeObjectOrNull(output, object.getLastLogin(), Date.class);
        output.writeInt(object.getLoginCount());
    }

    @Override
    public Activity read(Kryo kryo, Input input, Class type) {
        return new Activity(kryo.readObjectOrNull(input, Date.class), input.readInt());
    }
}
