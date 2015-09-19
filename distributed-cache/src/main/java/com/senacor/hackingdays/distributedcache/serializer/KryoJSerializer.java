package com.senacor.hackingdays.distributedcache.serializer;

import akka.serialization.JSerializer;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.senacor.hackingdays.distributedcache.generate.model.Profile;
import com.senacor.hackingdays.distributedcache.serializer.kryo.sryll.ProfileSerializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class KryoJSerializer extends JSerializer {

    private Kryo kryo = new Kryo();

    public KryoJSerializer() {
        kryo.addDefaultSerializer(Profile.class, new ProfileSerializer());
        kryo.setReferences(false);
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

    public Kryo getKryo() {
        return kryo;
    }
}
