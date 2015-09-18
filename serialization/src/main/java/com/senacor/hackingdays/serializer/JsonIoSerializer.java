package com.senacor.hackingdays.serializer;

import java.nio.charset.Charset;

import com.cedarsoftware.util.io.JsonReader;
import com.cedarsoftware.util.io.JsonWriter;

import akka.serialization.JSerializer;

public class JsonIoSerializer extends JSerializer {
	
	public static Charset UTF8 = Charset.forName("UTF-8");

    @Override
    public Object fromBinaryJava(byte[] bytes, Class<?> manifest) {
    	return JsonReader.toJava(new String(bytes, UTF8));
    }

    @Override
    public byte[] toBinary(Object o) {
    	return JsonWriter.toJson(o).getBytes(UTF8);
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
