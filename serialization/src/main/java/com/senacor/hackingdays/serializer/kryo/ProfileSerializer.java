package com.senacor.hackingdays.serializer.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.serializers.FieldSerializer;
import com.senacor.hackingdays.serialization.data.Gender;
import com.senacor.hackingdays.serialization.data.Profile;

public class ProfileSerializer extends FieldSerializer<Profile> {

	public ProfileSerializer(Kryo kryo) {
		super(kryo, Profile.class);
	}

	@Override
	protected Profile create(Kryo kryo, Input input, Class<Profile> type) {
		final String name = kryo.readObject(input, String.class);
		final Gender gender = kryo.readObject(input, Gender.class);
		removeField("name");
		removeField("gender");
		return new Profile(name, gender);
	}

	@Override
	@SuppressWarnings("rawtypes")
	public int compare(CachedField field1, CachedField field2) {
		if ("name".equals(field1.getField().getName()) && "gender".equals(field2.getField().getName())) {
			return -1;
		}

		if ("gender".equals(field1.getField().getName()) && "name".equals(field2.getField().getName())) {
			return 1;
		}

		if ("name".equals(field1.getField().getName()) || "gender".equals(field1.getField().getName())) {
			return -1;
		}
		if ("name".equals(field2.getField().getName()) || "gender".equals(field2.getField().getName())) {
			return 1;
		}

		return super.compare(field1, field2);
	}

}