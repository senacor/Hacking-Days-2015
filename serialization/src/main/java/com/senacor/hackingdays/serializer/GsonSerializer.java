package com.senacor.hackingdays.serializer;

import java.nio.charset.Charset;

import akka.serialization.JSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonSerializer extends JSerializer {

	private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS Z";
	private static final Gson gson = new GsonBuilder().setDateFormat(DATE_FORMAT).create();
	private static final Charset UTF8 = Charset.forName("UTF-8");
	private static final int IDENTIFIER =  13;

	@Override
	public int identifier() {
		return IDENTIFIER;
	}

	@Override
	public boolean includeManifest() {
		return false;
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
