package com.senacor.hackingdays.lmax.generate;


import com.senacor.hackingdays.lmax.generate.model.Gender;
import com.senacor.hackingdays.lmax.generate.model.Range;

import java.util.Random;
import java.util.function.Function;

public interface AgeSupplier extends Function<Gender, Integer> {


    static AgeSupplier randomAge() {

        return randomAge(Range.MIN_AGE, Range.MAX_AGE);
    }
    static AgeSupplier randomAge(int min, int max) {

        final Random random = new Random();

        return g -> random.nextInt(max - min) + min;
    }
}
