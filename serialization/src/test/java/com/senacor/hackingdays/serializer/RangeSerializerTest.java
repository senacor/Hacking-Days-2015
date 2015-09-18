package com.senacor.hackingdays.serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.senacor.hackingdays.serialization.data.Range;
import com.senacor.hackingdays.serializer.kryo.RangeSerializer;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Alasdair Collinson, Senacor Technologies AG
 */
public class RangeSerializerTest {

    private final static int LOWER = 31;
    private final static int UPPER = 60;

    @Test
    public void inputAndOutputAreSame() {
        Range inputElement = new Range(LOWER, UPPER);
        Kryo kryo = new Kryo();

        RangeSerializer serializer = new RangeSerializer();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Output output = new Output(outputStream);
        serializer.write(kryo, output, inputElement);
        output.flush();

        Input input = new Input(new ByteArrayInputStream(outputStream.toByteArray()));
        Range outputElement = serializer.read(kryo, input, Range.class);

        Assert.assertEquals(inputElement.getLower(), outputElement.getLower());
        Assert.assertEquals(inputElement.getUpper(), outputElement.getUpper());
    }

}