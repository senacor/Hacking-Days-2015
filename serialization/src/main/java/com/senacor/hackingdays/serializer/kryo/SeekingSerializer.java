package com.senacor.hackingdays.serializer.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.senacor.hackingdays.serialization.data.Gender;
import com.senacor.hackingdays.serialization.data.Range;
import com.senacor.hackingdays.serialization.data.Seeking;

/**
 *
 * @author tschapitz
 */
public class SeekingSerializer extends Serializer<Seeking> {

        public void write (Kryo kryo, Output output, Seeking seeking) {
            kryo.writeObjectOrNull(output, seeking.getGender(), Gender.class);
            kryo.writeObjectOrNull(output, seeking.getAgeRange(), Range.class);
        }

        public Seeking read (Kryo kryo, Input input, Class<Seeking> type) {
            return new Seeking(kryo.readObjectOrNull(input, Gender.class),
                               kryo.readObjectOrNull(input, Range.class)
            );
        }
}
