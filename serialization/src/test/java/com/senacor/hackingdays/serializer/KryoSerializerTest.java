package com.senacor.hackingdays.serializer;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.esotericsoftware.minlog.Log;
import com.senacor.hackingdays.serialization.data.Activity;
import com.senacor.hackingdays.serialization.data.Gender;
import com.senacor.hackingdays.serialization.data.Location;
import com.senacor.hackingdays.serialization.data.Profile;
import com.senacor.hackingdays.serialization.data.Range;
import com.senacor.hackingdays.serialization.data.RelationShipStatus;
import com.senacor.hackingdays.serialization.data.Seeking;

/**
 * @author Alasdair Collinson, Senacor Technologies AG
 */
public class KryoSerializerTest {

	private static final String NAME = "Hans Mueller";

	private Profile profile;

	private KryoSerializer kryoSerializer;


	@Before
	public void setup() {
		Log.set(Log.LEVEL_TRACE);

//		System.out.println("NAME (chars): " + Arrays.toString(NAME.toCharArray()));
//		System.out.println("NAME (bytes): " + Arrays.toString(NAME.getBytes(StandardCharsets.UTF_8)));

		profile = new Profile(NAME, Gender.Male);
		profile.setAge(38);
		profile.setActivity(new Activity(new Date(), 3));
		profile.setSmoker(true);
		profile.setLocation(new Location("Germany", "Nuremberg", "90402"));
		profile.setRelationShip(RelationShipStatus.Maried);
		profile.setSeeking(new Seeking(Gender.Female, new Range(21, 30)));

		kryoSerializer = new KryoSerializer();
	}

    @Test
    public void inputAndOutputAreSame() {
        final byte[] binaryProfile = kryoSerializer.toBinary(profile);

//        System.out.println("Profile binary: " + Arrays.toString(binaryProfile));

        Profile outputElement = (Profile) kryoSerializer.fromBinary(binaryProfile, Profile.class);

        assertThat(outputElement.getName(), is(profile.getName()));
        assertThat(outputElement.getGender(), is(profile.getGender()));
        assertThat(outputElement.getAge(), is(profile.getAge()));
    }

}