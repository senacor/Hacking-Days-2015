package com.senacor.hackingdays.serializer;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.senacor.hackingdays.serialization.data.Activity;
import com.senacor.hackingdays.serialization.data.Gender;
import com.senacor.hackingdays.serialization.data.Location;
import com.senacor.hackingdays.serialization.data.Profile;
import com.senacor.hackingdays.serialization.data.Range;
import com.senacor.hackingdays.serialization.data.RelationShipStatus;
import com.senacor.hackingdays.serialization.data.Seeking;

import akka.serialization.JSerializer;

public class GsonSerializer extends JSerializer {

	private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS Z";
	private static final Gson gson = new GsonBuilder().setDateFormat(DATE_FORMAT)
			.registerTypeAdapter(Profile.class, new TypeAdapter<Profile>() {

				@Override
				public void write(JsonWriter out, Profile profile) throws IOException {
					out //
							.beginArray() //
							.value(profile.getName()) //
							.value(profile.getGender().ordinal())//
							.value(profile.getAge())//
							.value(profile.getRelationShip().ordinal()) //
							.value(profile.isSmoker()) //
							.value(profile.getActivity().getLoginCount()) //
							.value(profile.getActivity().getLastLogin().getTime()) //
							.value(profile.getLocation().getCity()) //
							.value(profile.getLocation().getState()) //
							.value(profile.getLocation().getZip())//
							.value(profile.getSeeking().getGender().ordinal()) //
							.value(profile.getSeeking().getAgeRange().getLower()) //
							.value(profile.getSeeking().getAgeRange().getUpper()) //
							.endArray(); //
				}

				@Override
				public Profile read(JsonReader in) throws IOException {
					in.beginArray();
					final String name = in.nextString();
					final Gender gender = Gender.values()[in.nextInt()];
					final int age = in.nextInt();
					final RelationShipStatus relationShipStatus = RelationShipStatus.values()[in.nextInt()];
					final boolean isSmoker = in.nextBoolean();
					final int loginCount = in.nextInt();
					final long lastLoginTime = in.nextLong();
					final String city = in.nextString();
					final String state = in.nextString();
					final String zip = in.nextString();
					final Gender seekingGender = Gender.values()[in.nextInt()];
					final int lowerAge = in.nextInt();
					final int upperAge = in.nextInt();
					in.endArray();
					Profile result = new Profile(name, gender);
					result.setAge(age);
					result.setRelationShip(relationShipStatus);
					result.setSmoker(isSmoker);
					result.setActivity(new Activity(new Date(lastLoginTime), loginCount));
					result.setLocation(new Location(state, city, zip));
					result.setSeeking(new Seeking(seekingGender, new Range(lowerAge, upperAge)));

					return result;
				}

			}).create();
	private static final Charset UTF8 = Charset.forName("UTF-8");
	private static final int IDENTIFIER = 13;

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
