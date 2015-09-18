/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.senacor.hackingdays.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.senacor.hackingdays.serialization.data.Gender;

/**
 *
 * @author tschapitz
 */
public class GenderSerializer extends Serializer<Gender> {

    public final static GenderSerializer INSTANCE = new GenderSerializer();

    {
        setImmutable(true);
        setAcceptsNull(true);
    }

    public void write(Kryo kryo, Output output, Gender object) {
        if (object != null) {
            output.writeByte(object.ordinal());
        } else {
            output.writeByte(-1);
        }
    }

    public Gender read(Kryo kryo, Input input, Class<Gender> type) {
        byte ordinal = input.readByte();
        if (ordinal >= 0) {
            return Gender.values()[ordinal];
        }
        return null;
    }
}
