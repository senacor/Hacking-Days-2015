package com.senacor.hackingdays.serializer;

import akka.serialization.JSerializer;
import com.google.protobuf.Message;
import com.senacor.hackingdays.serialization.data.proto.ProfileProtos;

import java.lang.reflect.Method;

public class ProtoBufSerilalizer extends JSerializer {

    private static final Class[] CLASSES = new Class[]{byte[].class};

    @Override
    public Object fromBinaryJava(byte[] bytes, Class<?> clazz) {
        try {
            return clazz.getDeclaredMethod("parseFrom", CLASSES)
                    .invoke(null, new Object[]{bytes});
        } catch (Exception e) {
            throw new IllegalStateException("Error on deserialize ProfileProtos.Profile", e);
        }
    }

    @Override
    public byte[] toBinary(Object o) {
        if (o instanceof Message) {
            Message var3 = (Message) o;
            byte[] var4 = var3.toByteArray();
            return var4;
        } else {
            throw new IllegalArgumentException((new StringBuilder())
                    .append("Can\'t serialize a non-protobuf message using protobuf [").
                            append(o).append("]").toString());
        }
    }

    @Override
    public int identifier() {
        return 4711;
    }

    @Override
    public boolean includeManifest() {
        return true;
    }
}
