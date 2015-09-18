package com.senacor.hackingdays.serialization.data;

public enum Gender implements UnsafeSerializable {

    Male, Female, Disambiguous;

    @Override
    public void serializeUnsafe(final UnsafeMemory memory) {
        memory.putInt(this.ordinal());
    }

    public static Gender deserializeUnsafe(final UnsafeMemory memory) {
        return Gender.values()[memory.getInt()];
    }
}

