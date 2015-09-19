package com.senacor.hackingdays.lmax.generate;


import com.senacor.hackingdays.lmax.generate.model.Gender;

import java.util.Random;
import java.util.function.Supplier;

import static com.senacor.hackingdays.lmax.generate.model.Gender.Female;

public interface GenderSupplier extends Supplier<Gender> {


    static GenderSupplier randomGender() {
        Random random = new Random();
        return () -> {
            int i = random.nextInt(100);
            return i < 85 ? Gender.Male : i > 95 ? Gender.Disambiguous : Female;
        };

    }
}
