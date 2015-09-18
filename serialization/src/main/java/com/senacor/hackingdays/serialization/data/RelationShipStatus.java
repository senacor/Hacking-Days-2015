package com.senacor.hackingdays.serialization.data;

import com.senacor.hackingdays.serialization.data.unsafe.BufferTooSmallException;
import com.senacor.hackingdays.serialization.data.unsafe.UnsafeMemory;
import com.senacor.hackingdays.serialization.data.unsafe.UnsafeSerializable;

public enum RelationShipStatus implements UnsafeSerializable {

    Divorced, Maried, Single;

    @Override
    public void serializeUnsafe(final UnsafeMemory memory) throws BufferTooSmallException {
        memory.putInt(this.ordinal());
    }

    public static RelationShipStatus deserializeUnsafe(final UnsafeMemory memory) {
        return RelationShipStatus.values()[memory.getInt()];
    }
}
