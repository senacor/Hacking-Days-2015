package com.senacor.hackingdays.serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.senacor.hackingdays.serialization.data.Location;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * @author Alasdair Collinson, Senacor Technologies AG
 */
public class LocationSerializerTest {

    private static final String STATE = "State";
    private static final String CITY = "City";
    private static final String ZIP = "Zip";

    @Test
    public void inputAndOutputAreSame() {
        Location inputElement = new Location(STATE, CITY, ZIP);
        Kryo kryo = Mockito.mock(Kryo.class);

        LocationSerializer serializer = new LocationSerializer();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Output output = new Output(outputStream);
        serializer.write(kryo, output, inputElement);
        output.flush();

        Input input = new Input(new ByteArrayInputStream(outputStream.toByteArray()));
        Location outputElement = serializer.read(kryo, input, Location.class);

        Assert.assertEquals(inputElement.getState(), outputElement.getState());
        Assert.assertEquals(inputElement.getCity(), outputElement.getCity());
        Assert.assertEquals(inputElement.getZip(), outputElement.getZip());
    }
}