package com.senacor.hackingdays.serializer;

import org.junit.Test;
import org.junit.runner.RunWith;

import akka.serialization.Serializer;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static junitparams.JUnitParamsRunner.$;

@RunWith(JUnitParamsRunner.class)
public class SerializerTest {

	@Test
	@Parameters(method = "serializers")
	public <T extends Serializer> void testSerialization(Class<T> clazz)
			throws InstantiationException, IllegalAccessException {
		Serializer serializer = clazz.newInstance();
		SerializableTestObject zeug = new SerializableTestObject("a", "b", false);
		byte[] serialized = serializer.toBinary(zeug);
		assertThat(serialized, is(notNullValue()));
		SerializableTestObject deserialized = (SerializableTestObject) serializer.fromBinary(serialized,
				SerializableTestObject.class);
		assertThat(deserialized, is(equalTo(zeug)));
	}

	public static Object[] serializers() {
		return $($(GsonSerializer.class), $(GsonSerializer2.class), $(XStreamXMLSerializer.class));
	}

}