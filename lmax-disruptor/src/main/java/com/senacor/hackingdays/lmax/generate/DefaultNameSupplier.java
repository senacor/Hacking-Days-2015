package com.senacor.hackingdays.lmax.generate;


import com.google.common.collect.ArrayListMultimap;
import com.senacor.hackingdays.lmax.generate.model.Gender;

import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

class DefaultNameSupplier implements NameSupplier {
    private final Random rnd = new Random();
    private final ArrayListMultimap<Gender, String> names = ArrayListMultimap.create();

    DefaultNameSupplier() {
        Stream.of(Gender.values()).forEach(g -> names.putAll(g, forGender(g)));
    }

    private List<String> forGender(Gender gender) {
        switch (gender) {
            case Male:
                return parse("male-firstname.txt");
            case Female:
                return parse("female-firstname.txt");
            case Disambiguous:
                return parse("transgender-name.txt");
            default:
                throw new AssertionError();
        }
    }

    private static List<String> parse(String fileName) {
        return LineReader.readLines(fileName);
    }

    @Override
    public String apply(Gender gender) {
        List<String> namesForGender = names.get(gender);
        return namesForGender.get(rnd.nextInt(namesForGender.size()));
    }
}
