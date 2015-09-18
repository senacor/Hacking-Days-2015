package com.senacor.hackingdays.serializer;

import java.nio.charset.Charset;

import com.google.gson.Gson;

import akka.serialization.JSerializer;

public class GsonSerializer extends JSerializer {

	private final Gson gson = new Gson();
	private static final Charset UTF8 = Charset.forName("UTF-8");

	@Override
	public int identifier() {
		return 1501;
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
