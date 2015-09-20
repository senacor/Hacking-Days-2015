package com.senacor.hackingdays.lmax.lmax.kryo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.senacor.hackingdays.lmax.generate.model.Profile;

public class KryoProfileSerializer {

	private final Kryo kryo = new Kryo();

	{
		kryo.addDefaultSerializer(Profile.class, new ProfileSerializer());
		kryo.setReferences(false);
	}

	public Profile fromBinaryJava(byte[] bytes) {
		try (final Input input = new Input(new ByteArrayInputStream(bytes))) {
			return kryo.readObject(input, Profile.class);
		}
	}

	public byte[] toBinary(Profile profile) {
		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try (final Output output = new Output(outputStream)) {
			kryo.writeObject(output, profile);
		}
		return outputStream.toByteArray();
	}
}