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
import com.senacor.hackingdays.serialization.data.Range;
import com.senacor.hackingdays.serialization.data.Seeking;
import com.senacor.hackingdays.serializer.kryo.RangeSerializer;
import com.senacor.hackingdays.serializer.kryo.SeekingSerializer;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Alasdair Collinson, Senacor Technologies AG
 */
@RunWith(JUnitParamsRunner.class)
public class SeekingSerializerTest {

    @Test
    @Parameters(method = "parameters")
    public void inputAndOutputAreSame(Gender gender, Range range) {
        Seeking inputElement = new Seeking(gender, range);
        Kryo kryo = new Kryo();
        kryo.register(Range.class, new RangeSerializer());

        SeekingSerializer serializer = new SeekingSerializer();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Output output = new Output(outputStream);
        serializer.write(kryo, output, inputElement);
        output.flush();

        Input input = new Input(new ByteArrayInputStream(outputStream.toByteArray()));
        Seeking outputElement = serializer.read(kryo, input, Seeking.class);

        Assert.assertEquals(inputElement.getAgeRange(), outputElement.getAgeRange());
        Assert.assertEquals(inputElement.getGender(), outputElement.getGender());
    }

    private Collection<Object[]> parameters() {
        List<Range> ranges = Lists.newArrayList(new Range(25, 35), new Range(30, 49), new Range(50, 75));
        List<Object[]> parameters = Lists.newArrayList();
        for (Gender gender : Gender.values()) {
            for (Range range : ranges) {
                parameters.add(new Object[]{gender, range});
            }
        }
        parameters.add(new Object[]{null, null});
        return parameters;
    }
}