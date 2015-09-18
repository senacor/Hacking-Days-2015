package com.senacor.hackingdays.serialization.data;

import com.senacor.hackingdays.serialization.data.unsafe.BufferTooSmallException;
import com.senacor.hackingdays.serialization.data.unsafe.UnsafeMemory;
import com.senacor.hackingdays.serialization.data.unsafe.UnsafeSerializable;

public enum Gender implements UnsafeSerializable {

    Male, Female, Disambiguous;

    @Override
    public void serializeUnsafe(final UnsafeMemory memory) throws BufferTooSmallException {
        memory.putInt(this.ordinal());
    }

    public static Gender deserializeUnsafe(final UnsafeMemory memory) {
        return Gender.values()[memory.getInt()];
    }
}

