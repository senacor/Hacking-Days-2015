package com.senacor.hackingdays.serializer.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.senacor.hackingdays.serialization.data.Range;

/**
 *
 * @author tschapitz
 */
public class RangeSerializer extends Serializer<Range> {

        public void write (Kryo kryo, Output output, Range range) {
            output.writeByte(range.getLower());
            output.writeByte(range.getUpper());
        }

        public Range read (Kryo kryo, Input input, Class<Range> type) {
            return new Range(input.readByte(), input.readByte());
        }
}
