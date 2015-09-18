package com.senacor.hackingdays.serializer;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.senacor.hackingdays.serialization.data.Gender;
import com.senacor.hackingdays.serialization.data.Profile;

/**
 * @author Alasdair Collinson, Senacor Technologies AG
 */
public class ProfileSerializerTest {

	private static final String NAME = "Hans Mueller";
	private Profile profile;
	private Kryo kryo;
	private ProfileSerializer profileSerializer;

	@Before
	public void setup() {
		System.out.println(Arrays.toString(NAME.getBytes()));

		profile = new Profile(NAME, Gender.Male);
		profile.setAge(38);

		kryo = new Kryo();
		profileSerializer = new ProfileSerializer(kryo);

		kryo.register(Profile.class, profileSerializer);
		kryo.register(Gender.class, new GenderSerializer());
	}

    @Test
    public void inputAndOutputAreSame() {
    	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Output output = new Output(outputStream);

        profileSerializer.write(kryo, output, profile);

        output.flush();
        output.close();

        System.out.println(Arrays.toString(outputStream.toByteArray()));

        Input input = new Input(new ByteArrayInputStream(outputStream.toByteArray()));

        Profile outputElement = profileSerializer.read(kryo, input, Profile.class);

        assertThat(outputElement.getName(), is(profile.getName()));
        assertThat(outputElement.getGender(), is(profile.getGender()));
        assertThat(outputElement.getAge(), is(profile.getAge()));
    }

}