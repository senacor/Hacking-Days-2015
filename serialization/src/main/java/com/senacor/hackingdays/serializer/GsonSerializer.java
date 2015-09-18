package com.senacor.hackingdays.serializer;

import akka.serialization.JSerializer;
import com.google.gson.Gson;

import java.nio.charset.Charset;

public class GsonSerializer extends JSerializer {

	private static final Gson gson = new Gson();
	private static final Charset UTF8 = Charset.forName("UTF-8");
	private static final int IDENTIFIER =  GsonSerializer.class.hashCode();

	@Override
	public int identifier() {
		return IDENTIFIER;
	}

	@Override
	public boolean includeManifest() {
		return true;
	}

	@Override
	public byte[] toBinary(Object o) {
		return gson.toJson(o).getBytes(UTF8);
	}

	@Override
	public Object fromBinaryJava(byte[] payload, Class<?> clazz) {
		return gson.fromJson(new String(payload, UTF8), clazz);
	}

}
