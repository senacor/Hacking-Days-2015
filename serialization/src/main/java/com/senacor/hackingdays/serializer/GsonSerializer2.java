package com.senacor.hackingdays.serializer;

import akka.serialization.JSerializer;
import com.google.gson.Gson;

/**
 * Created by mhaunolder on 18.09.15.
 */
public class GsonSerializer2 extends JSerializer {


    @Override
    public Object fromBinaryJava(byte[] bytes, Class<?> aClass) {
        return new Gson().fromJson(new String(bytes), aClass);
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