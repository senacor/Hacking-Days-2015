package com.senacor.hackingdays.actor;

import com.senacor.hackingdays.serialization.data.generate.DataGenerator;

import java.io.Serializable;

public final class GenerateMessages implements Serializable {

    private final int count;
    private Class<DataGenerator> generator = null;

    public GenerateMessages(int count) {
        this.count = count;
    }

    public GenerateMessages(int count, Class generator) {
      this.count = count;
      this.generator = generator;
    }

    public int getCount() {
        return count;
    }

    public Class<DataGenerator> getGenerator() {
      return generator;
    }
}
