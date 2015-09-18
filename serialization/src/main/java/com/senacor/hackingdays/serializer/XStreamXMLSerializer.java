package com.senacor.hackingdays.serializer;

import java.nio.charset.Charset;

import com.google.common.base.Charsets;
import com.thoughtworks.xstream.XStream;

import akka.serialization.JSerializer;

public class XStreamXMLSerializer extends JSerializer {

	private static final XStream xStream = new XStream();
	private static final Charset CHARSET = Charsets.UTF_8;

	@Override
	public int identifier() {
		return XStreamXMLSerializer.class.hashCode();
	}

	@Override
	public boolean includeManifest() {
		return false;
	}

	@Override
	public byte[] toBinary(Object arg0) {
		return xStream.toXML(arg0).getBytes(CHARSET);
	}

	@Override
	public Object fromBinaryJava(byte[] arg0, Class<?> arg1) {
		return xStream.fromXML(new String(arg0, CHARSET));
	}

}
