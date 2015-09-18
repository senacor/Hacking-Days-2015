package com.senacor.hackingdays.serialization.data;

public enum RelationShipStatus implements UnsafeSerializable {

    Divorced, Maried, Single;

    @Override
    public void serializeUnsafe(final UnsafeMemory memory) {
        memory.putInt(this.ordinal());
    }

    public static RelationShipStatus deserializeUnsafe(final UnsafeMemory memory) {
        return RelationShipStatus.values()[memory.getInt()];
    }
}
