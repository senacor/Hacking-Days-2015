package com.senacor.hackingdays.serialization.data;

import static com.google.common.base.Preconditions.checkArgument;

import java.io.Serializable;
import java.util.Objects;

import org.codehaus.jackson.annotate.JsonProperty;

import com.senacor.hackingdays.serialization.data.unsafe.BufferTooSmallException;
import com.senacor.hackingdays.serialization.data.unsafe.UnsafeMemory;
import com.senacor.hackingdays.serialization.data.unsafe.UnsafeSerializable;

public class Range implements Serializable, UnsafeSerializable {
    private static final long serialVersionUID = 1;

    public static final int MAX_AGE = 75;
    public static final int MIN_AGE = 21;

    private final int lower;
    private final int upper;

    public Range(
            @JsonProperty("lower") int lower,
            @JsonProperty("upper") int upper) {

        checkArgument(upper <= MAX_AGE, "upper must be < " + MAX_AGE + " but is " + upper);
        checkArgument(lower >= MIN_AGE, "lower must be > " + MIN_AGE + " but is " + lower);
        checkArgument(lower <= upper);

        this.lower = lower;
        this.upper = upper;
    }

    public int getUpper() {
        return upper;
    }

    public int getLower() {
        return lower;
    }

    @Override
    public int hashCode() {
        return Objects.hash(lower, upper);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Range other = (Range) obj;
        return Objects.equals(this.lower, other.lower)
                && Objects.equals(this.upper, other.upper);
    }

    @Override
    public String toString() {
        return "Range{" +
                "lower=" + lower +
                ", upper=" + upper +
                '}';
    }

    @Override
    public void serializeUnsafe(UnsafeMemory memory) throws BufferTooSmallException {
        memory.putInt(lower);
        memory.putInt(upper);
    }

    public static Range deserializeUnsafe(UnsafeMemory memory) {
        final int lower = memory.getInt();
        final int upper = memory.getInt();
        return new Range(lower, upper);
    }
}
