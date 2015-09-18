package com.senacor.hackingdays.serializer.kryo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Collection;
import java.util.List;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.google.common.collect.Lists;
import com.senacor.hackingdays.serialization.data.Gender;
import com.senacor.hackingdays.serializer.kryo.GenderSerializer;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Alasdair Collinson, Senacor Technologies AG
 */
@RunWith(JUnitParamsRunner.class)
public class GenderSerializerTest {

    @Test
    @Parameters(method = "genders")
    public void inputAndOutputAreSame(Gender inputElement) {
        Kryo kryo = new Kryo();

        GenderSerializer serializer = new GenderSerializer();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Output output = new Output(outputStream);
        serializer.write(kryo, output, inputElement);
        output.flush();

        Input input = new Input(new ByteArrayInputStream(outputStream.toByteArray()));
        Gender outputElement = serializer.read(kryo, input, Gender.class);

        Assert.assertEquals(inputElement, outputElement);
    }

    private Collection<Gender> genders() {
        List<Gender> genders = Lists.newArrayList(Gender.values());
        genders.add(null);
        return genders;
    }

}