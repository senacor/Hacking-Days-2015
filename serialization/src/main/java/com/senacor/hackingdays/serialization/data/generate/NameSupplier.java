package com.senacor.hackingdays.serialization.data.generate;


import com.senacor.hackingdays.serialization.thirftdata.Gender;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

class NameSupplier implements Supplier<String> {
    private final List<String> names;
    private final Random rnd = new Random();

    private NameSupplier(List<String> names) {
        this.names = names;
    }

    static NameSupplier forGender(int gender) {
        switch (gender) {
            case Gender.Male:
                return new NameSupplier(parse("male-firstname.txt"));
            case Gender.Female:
                return new NameSupplier(parse("female-firstname.txt"));
            case Gender.Disambiguous:
                return new NameSupplier(parse("transgender-name.txt"));
            default:
                throw new AssertionError();
        }
    }

    private static List<String> parse(String fileName) {
            return LineReader.readLines(fileName);
    }

    public String get() {
        return names.get(rnd.nextInt(names.size()));
    }
}
