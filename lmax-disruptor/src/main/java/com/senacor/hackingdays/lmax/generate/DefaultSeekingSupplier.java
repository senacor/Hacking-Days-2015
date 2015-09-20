package com.senacor.hackingdays.lmax.generate;

import com.senacor.hackingdays.lmax.generate.model.Gender;
import com.senacor.hackingdays.lmax.generate.model.Range;
import com.senacor.hackingdays.lmax.generate.model.Seeking;

import java.util.Random;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class DefaultSeekingSupplier implements SeekingSupplier {

    public static final int GAY_RATIO = 10;
    private final Random rnd = new Random();

    private final AgeSupplier ageSupplier = AgeSupplier.randomAge();
    private final GenderSupplier genderSupplier = GenderSupplier.randomGender();

    private Seeking randomSeeking(Gender gender) {
        int bound1 = ageSupplier.apply(gender);
        int bound2 = ageSupplier.apply(gender);


        Gender seekingGender = null;
        if (gender.equals(Gender.Male)) {
            seekingGender = rnd.nextInt(10) % GAY_RATIO == 0 ? Gender.Female : Gender.Male;
        } else if(gender.equals(Gender.Female)) {
            seekingGender = rnd.nextInt(10) % GAY_RATIO == 0 ? Gender.Male : Gender.Female;
        } else {
            genderSupplier.get();
        }

        return new Seeking(seekingGender, new Range(min(bound1, bound2), max(bound1, bound2)));
    }

    @Override
    public Seeking apply(Gender gender) {
        return randomSeeking(gender);
    }
}
