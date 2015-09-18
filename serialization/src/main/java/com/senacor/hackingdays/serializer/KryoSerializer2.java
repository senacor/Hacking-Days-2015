package com.senacor.hackingdays.serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.senacor.hackingdays.serialization.data.Activity;
import com.senacor.hackingdays.serialization.data.Gender;
import com.senacor.hackingdays.serialization.data.Location;
import com.senacor.hackingdays.serialization.data.Profile;
import com.senacor.hackingdays.serialization.data.Range;
import com.senacor.hackingdays.serialization.data.Seeking;
import com.senacor.hackingdays.serializer.kryo.ActivitySerializer;
import com.senacor.hackingdays.serializer.kryo.GenderSerializer;
import com.senacor.hackingdays.serializer.kryo.LocationSerializer;
import com.senacor.hackingdays.serializer.kryo.RangeSerializer;
import com.senacor.hackingdays.serializer.kryo.SeekingSerializer;

import akka.serialization.JSerializer;

public class KryoSerializer2 extends JSerializer {

    private Kryo kryo = new Kryo();

    public KryoSerializer2() {
        kryo.addDefaultSerializer(Range.class, new RangeSerializer());
        kryo.addDefaultSerializer(Location.class, new LocationSerializer());
        kryo.addDefaultSerializer(Gender.class, new GenderSerializer());
        kryo.addDefaultSerializer(Seeking.class, new SeekingSerializer());
        kryo.addDefaultSerializer(Activity.class, new ActivitySerializer());
//        kryo.addDefaultSerializer(Profile.class, new com.senacor.hackingdays.serializer.kryo.sryll.ProfileSerializer());
        kryo.addDefaultSerializer(Profile.class, new com.senacor.hackingdays.serializer.kryo.ProfileSerializer(kryo));
    }

    @Override
    public Object fromBinaryJava(byte[] bytes, Class<?> manifest) {
        Input input = new Input(new ByteArrayInputStream(bytes));
        Object object = kryo.readObject(input, manifest);
        input.close();
        return object;
    }

    @Override
    public byte[] toBinary(Object o) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Output output = new Output(outputStream);
        kryo.writeObject(output, o);
        output.close();
        return outputStream.toByteArray();
    }

    @Override
    public int identifier() {
        return 0;
    }

    @Override
    public boolean includeManifest() {
        return true;
    }

}
