package com.senacor.hackingdays.distributedcache.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.StreamSerializer;
import com.senacor.hackingdays.distributedcache.generate.model.Profile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Andreas Keefer
 */
public class KryoProfileStreamSerializer implements StreamSerializer<Profile> {

    private Kryo kryo = new KryoJSerializer().getKryo();

    @Override
    public void write(ObjectDataOutput out, Profile object) throws IOException {
        Output output = new Output((OutputStream) out);
        kryo.writeObject(output, object);
        output.flush();
    }

    @Override
    public Profile read(ObjectDataInput objectDataInput) throws IOException {
        InputStream in = (InputStream) objectDataInput;
        Input input = new Input(in);
        return kryo.readObject(input, Profile.class);
    }

    @Override
    public int getTypeId() {
        return 17;
    }

    @Override
    public void destroy() {

    }
}
