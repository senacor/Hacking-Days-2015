package com.senacor.hackingdays.serialization.data;

import com.senacor.hackingdays.serialization.data.unsafe.BufferTooSmallException;
import com.senacor.hackingdays.serialization.data.unsafe.SizeAware;
import com.senacor.hackingdays.serialization.data.unsafe.UnsafeMemory;
import com.senacor.hackingdays.serialization.data.unsafe.UnsafeSerializable;
import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;

public class Seeking implements Serializable, UnsafeSerializable, SizeAware {

    private final Gender gender;
    private final Range ageRange;

    public Seeking(
            @JsonProperty("gender") Gender gender,
            @JsonProperty("ageRange") Range ageRange) {
        this.gender = gender;
        this.ageRange = ageRange;
    }

    public Gender getGender() {
        return gender;
    }

    public Range getAgeRange() {
        return ageRange;
    }

    @Override
    public String toString() {
        return "Seeking{" +
                "gender=" + gender +
                ", ageRange=" + ageRange +
                '}';
    }

  @Override
  public void serializeUnsafe(UnsafeMemory memory) throws BufferTooSmallException {
    gender.serializeUnsafe(memory);
    ageRange.serializeUnsafe(memory);
  }

  public static Seeking deserializeUnsafe(final UnsafeMemory memory) {
    final Gender gender = Gender.deserializeUnsafe(memory);
    final Range ageRange = Range.deserializeUnsafe(memory);
    return new Seeking(gender, ageRange);
  }

  @Override
  public long getDeepSize() {
    final long seekingShallowSize = getShallowSize();
    // gender is enum, already accounted for
    final long seekingRangeSize = ageRange.getDeepSize();
    return seekingShallowSize + seekingRangeSize;
  }

  @Override
  public long getShallowSize() {
    return UnsafeMemory.sizeOf(this);
  }
}
