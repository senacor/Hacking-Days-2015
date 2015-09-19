package com.senacor.hackingdays.lmax.generate;

import com.senacor.hackingdays.lmax.generate.model.Gender;
import com.senacor.hackingdays.lmax.generate.model.Range;
import com.senacor.hackingdays.lmax.generate.model.Seeking;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class DefaultSeekingSupplier implements SeekingSupplier {

    private final AgeSupplier ageSupplier = AgeSupplier.randomAge();

    private Seeking randomSeeking(Gender gender) {
        int bound1 = ageSupplier.apply(gender);
        int bound2 = ageSupplier.apply(gender);
        return new Seeking(gender, new Range(min(bound1, bound2), max(bound1, bound2)));
    }

    @Override
    public Seeking apply(Gender gender) {
        return randomSeeking(gender);
    }
}
