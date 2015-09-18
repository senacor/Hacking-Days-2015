package com.senacor.hackingdays.serializer;

import java.nio.charset.Charset;

import com.cedarsoftware.util.io.JsonReader;
import com.cedarsoftware.util.io.JsonWriter;

import akka.serialization.JSerializer;

public class KyroSerilalizer extends JSerializer {
	
	JsonReader reader = new JsonReader();
	JsonWriter writer = new JsonWriter(System.out);
	public static Charset UTF8 = Charset.forName("UTF-8");

    @Override
    public Object fromBinaryJava(byte[] bytes, Class<?> manifest) {
    	return JsonReader.jsonToJava(new String(bytes, UTF8));
    }

    @Override
    public byte[] toBinary(Object o) {
    	return JsonWriter.objectToJson(o).getBytes(UTF8);
    }

    @Override
    public int identifier() {
        return 101;
    }

    @Override
    public boolean includeManifest() {
        return false;
    }
}
