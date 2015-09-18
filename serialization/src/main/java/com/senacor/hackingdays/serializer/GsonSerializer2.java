package com.senacor.hackingdays.serializer;

import akka.serialization.JSerializer;
import com.google.gson.Gson;
import com.senacor.hackingdays.serialization.data.Profile;

import java.nio.charset.Charset;

/**
 * Created by mhaunolder on 18.09.15.
 */
public class GsonSerializer2 extends JSerializer {

    private static final Charset UTF8 = Charset.forName("UTF-8");
    private final Gson gson = new Gson();

    @Override
    public Object fromBinaryJava(byte[] bytes, Class<?> aClass) {
        return gson.fromJson(new String(bytes, UTF8), Profile.class);
    }

    @Override
    public byte[] toBinary(Object o) {
        return gson.toJson(o).getBytes(UTF8);
    }

    @Override
    public boolean includeManifest() {
        return false;
    }

    @Override
    public int identifier() {
        return 42;
    }
}