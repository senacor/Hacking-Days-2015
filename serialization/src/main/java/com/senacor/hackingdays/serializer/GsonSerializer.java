package com.senacor.hackingdays.serializer;

import akka.serialization.JSerializer;
import com.google.gson.Gson;

/**
 * Created by mhaunolder on 18.09.15.
 */
public class GsonSerializer extends JSerializer {


    @Override
    public Object fromBinaryJava(byte[] bytes, Class<?> aClass) {
        return new Gson().fromJson(new String(bytes), Object.class);
    }

    @Override
    public byte[] toBinary(Object o) {
        return new Gson().toJson(o).getBytes();
    }

    @Override
    public boolean includeManifest() {
        return true;
    }

    @Override
    public int identifier() {
        return 42;
    }
}
