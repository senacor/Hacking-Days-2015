package com.senacor.hackingdays.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.FieldSerializer;
import com.senacor.hackingdays.serialization.data.Gender;
import com.senacor.hackingdays.serialization.data.Profile;

public class ReflectionProfileSerializer extends FieldSerializer<Profile> {

	public ReflectionProfileSerializer(Kryo kryo) {
		super(kryo, Profile.class);
		removeField("name");
		removeField("gender");
	}

	@Override
	protected Profile create(Kryo kryo, Input input, Class<Profile> type) {
		final String name = kryo.readObject(input, String.class);
		final Gender gender = kryo.readObject(input, Gender.class);
		return new Profile(name, gender);
	}

	@Override
	public void write(Kryo kryo, Output output, Profile object) {
		kryo.writeObjectOrNull(output, object.getName(), String.class);
		kryo.writeObjectOrNull(output, object.getGender(), Gender.class);
		super.write(kryo, output, object);
	}

}