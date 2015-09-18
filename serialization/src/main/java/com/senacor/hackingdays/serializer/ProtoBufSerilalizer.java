package com.senacor.hackingdays.serializer;

import akka.serialization.JSerializer;
import com.google.protobuf.InvalidProtocolBufferException;
import com.senacor.hackingdays.serialization.data.proto.ProfileProtos;

public class ProtoBufSerilalizer extends JSerializer {

    @Override
    public Object fromBinaryJava(byte[] bytes, Class<?> manifest) {
        try {
            return ProfileProtos.Profile.parseFrom(bytes);
        } catch (InvalidProtocolBufferException e) {
            throw new IllegalStateException("Error on deserialize ProfileProtos.Profile", e);
        }
    }

    @Override
    public byte[] toBinary(Object o) {
        ProfileProtos.Profile profile = (ProfileProtos.Profile) o;
        return profile.toByteArray();
    }

    @Override
    public int identifier() {
        return 4711;
    }

    @Override
    public boolean includeManifest() {
        return false;
    }
}
