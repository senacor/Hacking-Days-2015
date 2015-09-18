package com.senacor.hackingdays.serializer;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.senacor.hackingdays.serialization.data.Activity;
import com.senacor.hackingdays.serialization.data.Gender;
import com.senacor.hackingdays.serialization.data.Location;
import com.senacor.hackingdays.serialization.data.Profile;
import com.senacor.hackingdays.serialization.data.Range;
import com.senacor.hackingdays.serialization.data.RelationShipStatus;
import com.senacor.hackingdays.serialization.data.Seeking;

public class JsonIoSerializerTest {

	private static final String NAME = "Hans Mueller";

	private Profile profile;

	private JsonIoSerializer jsonIoSerializer;


	@Before
	public void setup() {
		profile = new Profile(NAME, Gender.Male);
		profile.setAge(38);
		profile.setActivity(new Activity(new Date(), 3));
		profile.setSmoker(true);
		profile.setLocation(new Location("Germany", "Nuremberg", "90402"));
		profile.setRelationShip(RelationShipStatus.Maried);
		profile.setSeeking(new Seeking(Gender.Female, new Range(21, 30)));

		jsonIoSerializer = new JsonIoSerializer();
	}

    @Test
    public void inputAndOutputAreSame() {
        final byte[] binaryProfile = jsonIoSerializer.toBinary(profile);

        Profile deserialized = (Profile) jsonIoSerializer.fromBinary(binaryProfile, Profile.class);

        assertThat(deserialized, is(not(nullValue())));
        assertThat(deserialized.getName(), is(profile.getName()));
        assertThat(deserialized.getGender(), is(profile.getGender()));
        assertThat(deserialized.getAge(), is(profile.getAge()));
    }

}
