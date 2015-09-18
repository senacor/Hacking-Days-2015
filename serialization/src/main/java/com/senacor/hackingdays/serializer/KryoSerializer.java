package com.senacor.hackingdays.serializer;

import akka.serialization.JSerializer;

public class KryoSerializer extends JSerializer {

    @Override
    public Object fromBinaryJava(byte[] bytes, Class<?> manifest) {
        return null;
    }

    @Override
    public byte[] toBinary(Object o) {
        return new byte[0];
    }

    @Override
    public int identifier() {
        return 0;
    }

    @Override
    public boolean includeManifest() {
        return false;
    }

}
