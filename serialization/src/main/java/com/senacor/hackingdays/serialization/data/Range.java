package com.senacor.hackingdays.serialization.data;

import java.io.Serializable;

public class Range implements Serializable {

    private static final long serialVersionUID = 1;
    private final int upper;
    private final int lower;

    public Range(int upper, int lower) {
        this.upper = upper;
        this.lower = lower;
    }

    public int getUpper() {
        return upper;
    }

    public int getLower() {
        return lower;
    }
}
