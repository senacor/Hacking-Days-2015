package com.senacor.hackingdays.distributedcache.serializer;

import com.senacor.hackingdays.distributedcache.generate.model.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Andreas Keefer
 */
public class KryoJSerializerTest {
    private static final String NAME = "Hans Mueller";

    private Profile profile;

    private KryoJSerializer kryoJSerializer;


    @Before
    public void setup() {
//		Log.set(Log.LEVEL_TRACE);

//		System.out.println("NAME (chars): " + Arrays.toString(NAME.toCharArray()));
//		System.out.println("NAME (bytes): " + Arrays.toString(NAME.getBytes(StandardCharsets.UTF_8)));

        profile = new Profile(NAME, Gender.Male);
        profile.setAge(38);
        profile.setActivity(new Activity(new Date(), 3));
        profile.setSmoker(true);
        profile.setLocation(new Location("Germany", "Nuremberg", "90402"));
        profile.setRelationShip(RelationShipStatus.Maried);
        profile.setSeeking(new Seeking(Gender.Female, new Range(21, 30)));

        kryoJSerializer = new KryoJSerializer();
    }

    @Test
    public void inputAndOutputAreSame() {
        final byte[] binaryProfile = kryoJSerializer.toBinary(profile);

        System.out.println(binaryProfile.length + "Profile binary: " + Arrays.toString(binaryProfile));

        Profile outputElement = (Profile) kryoJSerializer.fromBinary(binaryProfile, Profile.class);

        assertThat(outputElement.getName(), is(profile.getName()));
        assertThat(outputElement.getGender(), is(profile.getGender()));
        assertThat(outputElement.getAge(), is(profile.getAge()));
    }

}