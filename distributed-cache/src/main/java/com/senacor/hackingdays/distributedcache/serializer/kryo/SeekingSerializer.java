package com.senacor.hackingdays.distributedcache.serializer.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.senacor.hackingdays.distributedcache.generate.model.Gender;
import com.senacor.hackingdays.distributedcache.generate.model.Range;
import com.senacor.hackingdays.distributedcache.generate.model.Seeking;

/**
 * @author tschapitz
 */
public class SeekingSerializer extends Serializer<Seeking> {

    public final static SeekingSerializer INSTANCE = new SeekingSerializer();

//    {
//        setAcceptsNull(true);
//    }

    public void write(Kryo kryo, Output output, Seeking seeking) {
        kryo.writeObjectOrNull(output, seeking.getGender(), GenderSerializer.INSTANCE);
        kryo.writeObjectOrNull(output, seeking.getAgeRange(), RangeSerializer.INSTANCE);
    }

    public Seeking read(Kryo kryo, Input input, Class<Seeking> type) {
        return new Seeking(kryo.readObjectOrNull(input, Gender.class, GenderSerializer.INSTANCE),
                kryo.readObjectOrNull(input, Range.class, RangeSerializer.INSTANCE)
        );
    }
}
