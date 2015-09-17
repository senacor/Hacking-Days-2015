package com.senacor.hackingdays.actor;

import java.io.Serializable;

public final class GenerateMessages implements Serializable {

    private final int count;

    public GenerateMessages(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }
}
