package com.senacor.hackingdays.serializer.kryo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Collection;
import java.util.Date;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.google.common.collect.Lists;
import com.senacor.hackingdays.serialization.data.Activity;
import com.senacor.hackingdays.serialization.data.Location;
import com.senacor.hackingdays.serializer.kryo.ActivitySerializer;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Alasdair Collinson, Senacor Technologies AG
 */
@RunWith(JUnitParamsRunner.class)
public class ActivitySerializerTest {

    private final static int LOGIN_COUNT = 5;

    @Test
    @Parameters(method = "loginDates")
    public void inputAndOutputAreSame(Date lastLogin) {
        Activity inputElement = new Activity(lastLogin, LOGIN_COUNT);
        Kryo kryo = new Kryo();

        ActivitySerializer serializer = new ActivitySerializer();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Output output = new Output(outputStream);
        serializer.write(kryo, output, inputElement);
        output.flush();

        Input input = new Input(new ByteArrayInputStream(outputStream.toByteArray()));
        Activity outputElement = serializer.read(kryo, input, Activity.class);

        Assert.assertEquals(inputElement.getLastLogin(), outputElement.getLastLogin());
        Assert.assertEquals(inputElement.getLoginCount(), outputElement.getLoginCount());
    }

    private Collection<Date> loginDates() {
        return Lists.newArrayList(new Date(), null);
    }
}