package com.senacor.hackingdays.serialization.data.generate;


import com.senacor.hackingdays.serialization.thirftdata.Gender;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

class NameSupplierThrift implements Supplier<String> {
    private final List<String> names;
    private final Random rnd = new Random();

    private NameSupplierThrift(List<String> names) {
        this.names = names;
    }

    static NameSupplierThrift forGender(Gender gender) {
        switch (gender.getValue()) {
            case 0:
                return new NameSupplierThrift(parse("male-firstname.txt"));
            case 1:
                return new NameSupplierThrift(parse("female-firstname.txt"));
            case 2:
                return new NameSupplierThrift(parse("transgender-name.txt"));
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
