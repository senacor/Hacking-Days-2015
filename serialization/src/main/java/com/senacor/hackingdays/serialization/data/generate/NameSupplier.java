package com.senacor.hackingdays.serialization.data.generate;

import com.senacor.hackingdays.serialization.data.Gender;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class NameSupplier implements Supplier<String> {
    private final List<String> names;
    private final Random rnd = new Random();

    private NameSupplier(List<String> names) {
        this.names = names;
    }

    public static NameSupplier forGender(Gender gender) {
        switch (gender) {
            case Male:
                return new NameSupplier(parse("male-firstname.txt"));
            case Female:
                return new NameSupplier(parse("female-firstname.txt"));
            case Disambiguous:
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
