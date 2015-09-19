package com.senacor.hackingdays.lmax.generate;


import com.senacor.hackingdays.lmax.generate.model.Gender;
import com.senacor.hackingdays.lmax.generate.model.Range;

import java.util.Random;
import java.util.function.Function;

interface AgeSupplier extends Function<Gender, Integer> {


    static AgeSupplier randomAge() {

        final Random random = new Random();

        return g -> random.nextInt(Range.MAX_AGE - Range.MIN_AGE) + Range.MIN_AGE;
    }
}
