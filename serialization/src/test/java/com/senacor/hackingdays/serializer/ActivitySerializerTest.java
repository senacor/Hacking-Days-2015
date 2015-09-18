package com.senacor.hackingdays.serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Date;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.senacor.hackingdays.serialization.data.Activity;
import com.senacor.hackingdays.serialization.data.Location;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Alasdair Collinson, Senacor Technologies AG
 */
public class ActivitySerializerTest {

    private final static Date LAST_LOGIN = new Date();
    private final static int LOGIN_COUNT = 5;

    @Test
    public void inputAndOutputAreSame() {
        Activity inputElement = new Activity(LAST_LOGIN, LOGIN_COUNT);
        Kryo kryo = new Kryo();

        ActivitySerializer serializer = new ActivitySerializer();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Output output = new Output(outputStream);
        serializer.write(kryo, output, inputElement);
        output.flush();

        Input input = new Input(new ByteArrayInputStream(outputStream.toByteArray()));
        Activity outputElement = serializer.read(kryo, input, Location.class);

        Assert.assertEquals(inputElement.getLastLogin(), outputElement.getLastLogin());
        Assert.assertEquals(inputElement.getLoginCount(), outputElement.getLoginCount());
    }
}